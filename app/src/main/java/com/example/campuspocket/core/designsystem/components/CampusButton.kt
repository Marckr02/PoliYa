package com.example.campuspocket.core.designsystem.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.campuspocket.core.designsystem.ShapeControl
import com.example.campuspocket.core.designsystem.extended

enum class CampusButtonStyle { PRIMARY, SECONDARY, DESTRUCTIVE, INK }

/**
 * Botón único de la app (v8 §6.3): 52 dp de alto, radio 7 y etiqueta de 14 en
 * peso 800. El primario va en ámbar con borde propio y texto `OnLime`; el
 * secundario y el de peligro son transparentes con borde. El deshabilitado usa
 * `Track`/`InkMuted` sin opacidad: el motivo se comunica en el texto contiguo.
 */
@Composable
fun CampusButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: CampusButtonStyle = CampusButtonStyle.PRIMARY,
    enabled: Boolean = true,
    loading: Boolean = false,
    leadingIcon: ImageVector? = null
) {
    val ext = MaterialTheme.extended
    val disabled = !enabled
    val container = when (style) {
        CampusButtonStyle.PRIMARY -> ext.accent
        CampusButtonStyle.SECONDARY -> Color.Transparent
        CampusButtonStyle.DESTRUCTIVE -> Color.Transparent
        // Relleno del botón INK: el token de tinta se queda (texto `onInk`).
        CampusButtonStyle.INK -> ext.ink
    }
    val content = when (style) {
        CampusButtonStyle.PRIMARY -> ext.onLime
        CampusButtonStyle.SECONDARY -> MaterialTheme.colorScheme.onSurface
        CampusButtonStyle.DESTRUCTIVE -> ext.danger
        CampusButtonStyle.INK -> ext.onInk
    }
    val border = when {
        disabled -> ext.controlLine
        style == CampusButtonStyle.PRIMARY -> ext.accentBorder
        style == CampusButtonStyle.SECONDARY -> ext.controlLine
        style == CampusButtonStyle.DESTRUCTIVE -> ext.danger
            .copy(alpha = 0.5f)
            .compositeOver(MaterialTheme.colorScheme.outlineVariant)
        // Borde del botón INK: mismo relleno, no es un indicador sobre Surface.
        else -> ext.ink
    }
    // Sin opacidad: los colores deshabilitados son tokens opacos.
    val resolvedContainer = if (disabled) ext.track else container
    val resolvedContent = if (disabled) ext.inkMuted else content
    Button(
        onClick = onClick,
        modifier = modifier.heightIn(min = 52.dp),
        enabled = enabled && !loading,
        shape = ShapeControl,
        contentPadding = PaddingValues(horizontal = 20.dp),
        border = BorderStroke(1.dp, border),
        colors = ButtonDefaults.buttonColors(
            containerColor = resolvedContainer,
            contentColor = resolvedContent,
            disabledContainerColor = resolvedContainer,
            disabledContentColor = resolvedContent
        )
    ) {
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                strokeWidth = 2.dp,
                color = content
            )
        } else {
            if (leadingIcon != null) {
                androidx.compose.material3.Icon(
                    imageVector = leadingIcon,
                    contentDescription = null, // decorativo: la etiqueta contigua describe la accion
                    modifier = Modifier.size(18.dp)
                )
                androidx.compose.foundation.layout.Spacer(Modifier.size(8.dp))
            }
            Text(text = text, style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Composable
fun CampusButtonRowScope(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: CampusButtonStyle = CampusButtonStyle.PRIMARY
) {
    CampusButton(text = text, onClick = onClick, modifier = modifier, style = style)
}

/** Enlace de acción textual con área táctil mínima de 48 dp. */
@Composable
fun CampusTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentColor: Color = MaterialTheme.colorScheme.primary
) {
    TextButton(
        onClick = onClick,
        modifier = modifier.heightIn(min = 48.dp),
        enabled = enabled,
        colors = ButtonDefaults.textButtonColors(contentColor = contentColor)
    ) {
        Text(text = text, style = MaterialTheme.typography.labelLarge)
    }
}
