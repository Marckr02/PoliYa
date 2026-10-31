package com.example.campuspocket.core.designsystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.campuspocket.core.designsystem.CampusTheme
import com.example.campuspocket.core.designsystem.extended
import com.example.campuspocket.feature.academic.domain.model.Priority

/**
 * Catálogo de componentes base v8 (prototipo #32): reemplaza la pantalla de
 * producción por vistas previas para revisar botones, campos, chips, pestañas,
 * tarjetas, interruptor y teclado en claro y oscuro. Solo para desarrollo.
 */
@Preview(name = "Catálogo base · claro", showBackground = true, widthDp = 390)
@Composable
private fun DesignSystemCatalogLightPreview() {
    CampusTheme(darkTheme = false) {
        CatalogContent()
    }
}

@Preview(name = "Catálogo base · oscuro", showBackground = true, widthDp = 390)
@Composable
private fun DesignSystemCatalogDarkPreview() {
    CampusTheme(darkTheme = true) {
        CatalogContent()
    }
}

@Composable
private fun CatalogContent() {
    Surface(color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            SectionLabel("Botones")
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                CampusButton(text = "Guardar movimiento", onClick = {})
                CampusButton(text = "Cancelar", onClick = {}, style = CampusButtonStyle.SECONDARY)
                CampusButton(text = "Eliminar cuenta", onClick = {}, style = CampusButtonStyle.DESTRUCTIVE)
                CampusButton(text = "Acción en tinta", onClick = {}, style = CampusButtonStyle.INK)
                CampusButton(text = "Revisa el monto", onClick = {}, enabled = false)
            }

            SectionLabel("Campos")
            CampusTextField(
                value = "BusTransporte",
                onValueChange = {},
                label = "Concepto",
                placeholder = "Ej. Bus"
            )
            CampusTextField(
                value = "0",
                onValueChange = {},
                label = "Monto",
                isError = true,
                errorDescription = "Ingresa un monto para continuar.",
                supportingText = {
                    Text(
                        text = "Ingresa un monto para continuar.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            )

            SectionLabel("Chips y pestañas")
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                CampusChip(label = "Todas", selected = true, onClick = {})
                CampusChip(label = "Hoy", selected = false, onClick = {})
            }
            UnderlinedTabRow(
                options = listOf("Movimientos", "Presupuestos", "Pagos"),
                selected = "Movimientos",
                onSelect = {},
                label = { it }
            )
            PillSegmentedControl(
                options = listOf("Gasto", "Ingreso", "Transferencia"),
                selected = "Gasto",
                onSelect = {},
                label = { it }
            )

            SectionLabel("Tarjetas y controles")
            BentoCard(contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp)) {
                Text(
                    text = "Tarjeta base",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            BentoCard(
                containerColor = MaterialTheme.extended.ink,
                contentColor = MaterialTheme.extended.onInk,
                contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp)
            ) {
                Text(
                    text = "Tarjeta héroe",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.extended.onInk
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                CampusSwitch(checked = true, onCheckedChange = {}, contentDescription = "Interruptor")
                CampusSwitch(checked = false, onCheckedChange = {}, contentDescription = "Interruptor")
                CampusCheckbox(checked = true, onCheckedChange = {}, contentDescription = "Tarea")
                PriorityTag(priority = Priority.HIGH)
            }

            SectionLabel("Teclado")
            TransactionKeypadPanel(
                amountDigits = "1250",
                onAmountChange = {},
                onSave = {}
            )
        }
    }
}

@Composable
private fun SectionLabel(text: String) {
    Column {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(4.dp))
    }
}
