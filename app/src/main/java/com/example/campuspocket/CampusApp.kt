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

        // Crashlytics: inicialización manual (auto-inicialización desactivada en Manifest)
        initCrashlyticsIfOptedIn()
    }

    private fun initCrashlyticsIfOptedIn() {
        val prefs = getSharedPreferences(PreferenceKeys.CRASHLYTICS_PREFS, MODE_PRIVATE)
        val optedIn = prefs.getBoolean(PreferenceKeys.CRASHLYTICS_OPT_IN, false)

        try {
            // Inicializar FirebaseApp primero (requiere app/google-services.json)
            if (FirebaseApp.getApps(this).isEmpty()) {
                FirebaseApp.initializeApp(this)
            }

            // Crashlytics: solo habilitar si hay consentimiento
            val crashlytics = FirebaseCrashlytics.getInstance()
            if (optedIn) {
                crashlytics.setCrashlyticsCollectionEnabled(true)
                crashlytics.setCustomKey("app_version", BuildConfig.VERSION_NAME)
                crashlytics.setCustomKey("app_version_code", BuildConfig.VERSION_CODE)
                crashlytics.setCustomKey("git_sha", BuildConfig.GIT_SHA)
                crashlytics.setCustomKey("build_date", BuildConfig.BUILD_DATE)
            } else {
                crashlytics.setCrashlyticsCollectionEnabled(false)
            }
        } catch (e: Exception) {
            // Crashlytics/Firebase no configurado o google-services.json ausente
            e.printStackTrace()
        }
    }
}