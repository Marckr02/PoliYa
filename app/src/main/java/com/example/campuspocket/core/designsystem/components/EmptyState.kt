package com.example.campuspocket.core.designsystem.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.campuspocket.core.designsystem.PillShape
import com.example.campuspocket.core.designsystem.extended
import com.example.campuspocket.core.designsystem.ShapeBlob
import com.example.campuspocket.R

/**
 * Estado vacío reutilizable (Sección 8 v5). Única API de estados vacíos (FIX-20).
 *
 * Tarjeta `surface` de radio 32 dp, icono de 72 dp en caja tintada
 * (24 dp de radio), título 20 sp ExtraBold, texto 14 sp, botón primario de 48 dp.
 * Recibe textos ya resueltos; las variantes tipadas resuelven sus recursos.
 */
@Composable
fun EmptyState(
    modifier: Modifier = Modifier,
    @DrawableRes iconRes: Int,
    title: String,
    message: String,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null
) {
    val ext = MaterialTheme.extended

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        androidx.compose.material3.Card(
            modifier = Modifier.fillMaxWidth(),
            colors = androidx.compose.material3.CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            shape = ShapeBlob,
            elevation = androidx.compose.material3.CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Icono en caja tintada
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(ext.lavender),
                    contentAlignment = Alignment.Center
                ) {
                    androidx.compose.material3.Icon(
                        painter = painterResource(iconRes),
                        contentDescription = null, // decorativo: la etiqueta contigua describe la accion
                        tint = ext.onLavender,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                if (actionLabel != null && onAction != null) {
                    Spacer(Modifier.height(8.dp))
                    androidx.compose.material3.Button(
                        onClick = onAction,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 48.dp)
                            .clip(PillShape),
                        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Text(
                            text = actionLabel,
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.ExtraBold
                            )
                        )
                    }
                }
            }
        }
    }
}

/**
 * Estado vacío específico para tareas.
 */
@Composable
fun EmptyTasksState(
    modifier: Modifier = Modifier,
    onNewTask: (() -> Unit)? = null
) {
    EmptyState(
        modifier = modifier,
        iconRes = R.drawable.ic_dock_school_outline,
        title = stringResource(R.string.empty_tasks_title),
        message = stringResource(R.string.empty_tasks_message),
        actionLabel = stringResource(R.string.task_new),
        onAction = onNewTask
    )
}

/**
 * Estado vacío específico para movimientos/finanzas.
 */
@Composable
fun EmptyMovementsState(
    modifier: Modifier = Modifier,
    onNewMovement: (() -> Unit)? = null
) {
    EmptyState(
        modifier = modifier,
        // FIX-54: icono de gasto, coherente con la acción de crear un movimiento.
        iconRes = R.drawable.ic_move_expense,
        title = stringResource(R.string.empty_movements_title),
        message = stringResource(R.string.empty_movements_message),
        actionLabel = stringResource(R.string.finance_new_movement),
        onAction = onNewMovement
    )
}

/**
 * Estado vacío específico para notas.
 */
@Composable
fun EmptyNotesState(
    modifier: Modifier = Modifier,
    onNewNote: (() -> Unit)? = null
) {
    EmptyState(
        modifier = modifier,
        iconRes = R.drawable.ic_dock_notes_outline,
        title = stringResource(R.string.empty_notes_title),
        message = stringResource(R.string.empty_notes_message),
        actionLabel = stringResource(R.string.note_new),
        onAction = onNewNote
    )
}

/**
 * Estado vacío específico para pagos programados.
 */
@Composable
fun EmptyPaymentsState(
    modifier: Modifier = Modifier,
    onNewPayment: (() -> Unit)? = null
) {
    EmptyState(
        modifier = modifier,
        iconRes = R.drawable.ic_schedule,
        title = stringResource(R.string.empty_payments_title),
        message = stringResource(R.string.empty_payments_message),
        actionLabel = stringResource(R.string.scheduled_payment_new),
        onAction = onNewPayment
    )
}

/**
 * Estado vacío específico para materias.
 */
@Composable
fun EmptyCoursesState(
    modifier: Modifier = Modifier,
    onNewCourse: (() -> Unit)? = null
) {
    EmptyState(
        modifier = modifier,
        iconRes = R.drawable.ic_dock_school_outline,
        title = stringResource(R.string.empty_courses_title),
        message = stringResource(R.string.empty_courses_message),
        actionLabel = stringResource(R.string.course_new),
        onAction = onNewCourse
    )
}

/**
 * Patrón único de "registro no encontrado" para todos los formularios (FIX-07).
 * Se muestra cuando el ViewModel expone notFound = true y ofrece volver atrás.
 */
@Composable
fun FormNotFoundState(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    EmptyState(
        modifier = modifier,
        iconRes = R.drawable.ic_warning,
        title = stringResource(R.string.form_not_found_title),
        message = stringResource(R.string.form_not_found_message),
        actionLabel = stringResource(R.string.back),
        onAction = onBack
    )
}
