package com.example.campuspocket

import android.app.Application
import android.content.SharedPreferences
import com.example.campuspocket.core.notifications.NotificationChannels
import com.example.campuspocket.core.preferences.PreferenceKeys
import com.example.campuspocket.feature.settings.security.LockInterceptor
import com.google.firebase.FirebaseApp
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.HiltAndroidApp
import java.util.Locale

@HiltAndroidApp
class CampusApp : Application() {
    override fun onCreate() {
        super.onCreate()
        val esLocale = Locale.forLanguageTag("es")
        Locale.setDefault(esLocale)
        NotificationChannels.ensureCreated(this)
        LockInterceptor.register(this)

        // Crashlytics: inicialización perezosa; solo se activa si el usuario dio consentimiento.
        // La preferencia se gestiona en Ajustes > Privacidad > "Enviar informes de fallos".
        initCrashlyticsIfOptedIn()
    }

    private fun initCrashlyticsIfOptedIn() {
        val prefs = getSharedPreferences(PreferenceKeys.CRASHLYTICS_PREFS, MODE_PRIVATE)
        val optedIn = prefs.getBoolean(PreferenceKeys.CRASHLYTICS_OPT_IN, false)

        // Inicializar FirebaseApp primero si no está inicializado
        if (FirebaseApp.getApps(this).isEmpty()) {
            FirebaseApp.initializeApp(this)
        }

        // Crashlytics se inicializa vía componente; si no está presente, no rompemos la app
        try {
            if (optedIn) {
                // Marcar versión en los informes para filtrar en consola
                FirebaseCrashlytics.getInstance().setCustomKey("app_version", BuildConfig.VERSION_NAME)
                FirebaseCrashlytics.getInstance().setCustomKey("app_version_code", BuildConfig.VERSION_CODE)
                FirebaseCrashlytics.getInstance().setCustomKey("git_sha", BuildConfig.GIT_SHA)
                FirebaseCrashlytics.getInstance().setCustomKey("build_date", BuildConfig.BUILD_DATE)
            } else {
                // Desactivar recolección si no hay consentimiento
                FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(false)
            }
        } catch (e: Exception) {
            // Crashlytics no disponible (p. ej. build sin google-services.json); no rompemos la app
            // Log para depuración
            e.printStackTrace()
        }
    }
}