package com.example.campuspocket.core.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.draw.clip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import com.example.campuspocket.core.designsystem.components.CampusTextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import kotlinx.coroutines.withTimeoutOrNull
import androidx.compose.ui.unit.dp
import com.example.campuspocket.core.designsystem.ShapeSnackbar
import com.example.campuspocket.core.designsystem.extended

/**
 * CompositionLocal para el SnackbarHost global (Sección 3.1 v5).
 *
 * Se crea un SnackbarHostState en CampusNavHost y se expone vía
 * LocalSnackbarHost para que cualquier pantalla pueda mostrar snackbars
 * (incluyendo "Deshacer") sin pasar el estado por parámetros.
 */
val LocalSnackbarHost = staticCompositionLocalOf<SnackbarHostState> {
    error("LocalSnackbarHost no proporcionado. Usa CampusNavHost que lo provee.")
}

/** Acceso ergonómico al SnackbarHostState global. */
@Composable
fun snackbarHostState(): SnackbarHostState = LocalSnackbarHost.current

/**
 * Snackbar con acción y duración de 5 s (v5 §3.1). Devuelve true si el usuario
 * pulsó la acción. Al vencer el tiempo se descarta y se devuelve false.
 */
suspend fun SnackbarHostState.showUndoSnackbar(
    message: String,
    actionLabel: String,
    timeoutMillis: Long = 5_000L
): Boolean {
    val result = withTimeoutOrNull(timeoutMillis) {
        showSnackbar(
            message = message,
            actionLabel = actionLabel,
            withDismissAction = false,
            duration = SnackbarDuration.Indefinite
        )
    }
    if (result == null) {
        currentSnackbarData?.dismiss()
        return false
    }
    return result == SnackbarResult.ActionPerformed
}

/**
 * Snackbar del rediseño v8: fondo `Ink`, texto blanco, borde `Lime` de 1 dp,
 * radio 12 y 54 dp mínimos; la acción va en `Lime` con 5 s de duración.
 */
@Composable
fun CampusSnackbar(data: SnackbarData) {
    val ext = MaterialTheme.extended
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .heightIn(min = 54.dp)
            .clip(ShapeSnackbar)
            .background(ext.ink)
            .border(1.dp, ext.lime, ShapeSnackbar)
            .padding(start = 16.dp, end = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = data.visuals.message,
            style = MaterialTheme.typography.bodyMedium,
            color = ext.onInk,
            modifier = Modifier.weight(1f)
        )
        data.visuals.actionLabel?.let { label ->
            CampusTextButton(
                text = label,
                onClick = { data.performAction() },
                contentColor = ext.lime
            )
        }
    }
}

/**
 * Host del Snackbar que usa el diseño personalizado.
 */
@Composable
fun CampusSnackbarHost(
    hostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    androidx.compose.material3.SnackbarHost(
        hostState = hostState,
        modifier = modifier,
        snackbar = { data -> CampusSnackbar(data) }
    )
}