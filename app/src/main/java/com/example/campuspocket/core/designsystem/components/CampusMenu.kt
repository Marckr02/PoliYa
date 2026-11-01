package com.example.campuspocket.core.designsystem.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.example.campuspocket.core.designsystem.ShapeCard
import com.example.campuspocket.core.designsystem.extended

/**
 * Menú desplegable del sistema de diseño (v10 C7, creado para B3).
 *
 * Estética editorial en lugar de la de Material: superficie `Surface`, borde
 * `Line` de 1 dp, radio `ShapeCard`, sin tinte y sin sombra. Las filas son
 * [CampusMenuItem] de 48 dp.
 */
@Composable
fun CampusMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        shape = ShapeCard,
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        content = content
    )
}

/**
 * Fila de un [CampusMenu]: 48 dp de alto, texto `Body` y rol `Button`.
 * La variante [destructive] usa `Danger`; la separan de las demás acciones
 * con [CampusMenuDivider].
 */
@Composable
fun CampusMenuItem(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    destructive: Boolean = false,
    enabled: Boolean = true,
    leading: (@Composable () -> Unit)? = null
) {
    val ext = MaterialTheme.extended
    val contentColor = when {
        !enabled -> ext.inkMuted
        destructive -> ext.danger
        else -> MaterialTheme.colorScheme.onSurface
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 48.dp)
            .clickable(
                enabled = enabled,
                role = Role.Button,
                onClick = onClick
            )
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        leading?.invoke()
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = contentColor
        )
    }
}

/** Separador fino entre acciones normales y destructivas de un [CampusMenu]. */
@Composable
fun CampusMenuDivider(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 1.dp)
            .background(MaterialTheme.colorScheme.outlineVariant)
    )
}
