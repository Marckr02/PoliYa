package com.example.campuspocket.feature.finance.ui.payments

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.example.campuspocket.R
import com.example.campuspocket.core.designsystem.MoneyText
import com.example.campuspocket.core.designsystem.components.BentoCard
import com.example.campuspocket.core.designsystem.components.UnderlinedTabRow
import com.example.campuspocket.core.designsystem.components.CampusButton
import com.example.campuspocket.core.designsystem.components.CampusButtonStyle
import com.example.campuspocket.core.designsystem.components.CampusChip
import com.example.campuspocket.core.designsystem.components.CampusDialog
import com.example.campuspocket.core.designsystem.components.CampusMenu
import com.example.campuspocket.core.designsystem.components.CampusMenuDivider
import com.example.campuspocket.core.designsystem.components.CampusMenuItem
import com.example.campuspocket.core.designsystem.components.CampusTextField
import com.example.campuspocket.core.designsystem.components.EmptyPaymentsState
import com.example.campuspocket.core.designsystem.components.ShimmerList
import com.example.campuspocket.core.designsystem.components.RoundIconButton
import com.example.campuspocket.core.navigation.LocalSnackbarHost
import com.example.campuspocket.core.navigation.showUndoSnackbar
import com.example.campuspocket.core.util.Money
import com.example.campuspocket.feature.finance.data.ScheduledPaymentEntity
import com.example.campuspocket.feature.finance.domain.repository.FinanceRepository
import com.example.campuspocket.feature.finance.ui.calendar.CalendarScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

private val DATE_FMT: DateTimeFormatter =
    DateTimeFormatter.ofPattern("d MMM yyyy", Locale.forLanguageTag("es"))

enum class PaymentsView { LIST, CALENDAR }

private fun freqLabelRes(f: String): Int = when (f) {
    "WEEKLY" -> R.string.scheduled_payment_frequency_weekly
    "YEARLY" -> R.string.scheduled_payment_frequency_yearly
    else -> R.string.scheduled_payment_frequency_monthly
}

/**
 * Pagos programados dentro de la pestaña Pagos: lista sin FAB, con calendario
 * y creación en la cabecera. La lógica del ViewModel se conserva intacta.
 *
 * En v5 el contenido se expone como [paymentItems]; esta pantalla queda como
 * envoltorio para previews y pruebas.
 */
@Composable
fun PaymentsScreen(
    onAddPayment: () -> Unit,
    onEditPayment: (Long) -> Unit,
    onOpenCalendar: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PaymentsViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var view by rememberSaveable { mutableStateOf(PaymentsView.LIST) }
    val onDeletePayment = rememberPaymentDeletionHandler(viewModel)

    LazyColumn(modifier = modifier.fillMaxWidth()) {
        paymentItems(
            state = state,
            viewModel = viewModel,
            view = view,
            onViewChange = { view = it },
            onAddPayment = onAddPayment,
            onEditPayment = onEditPayment,
            onOpenCalendar = onOpenCalendar,
            onDeletePayment = onDeletePayment
        )
    }
}

/**
 * Elimina el pago de inmediato y ofrece deshacer en el snackbar. Vive en la
 * pantalla, no en la tarjeta: al borrarse, el ítem sale de la lista y su scope
 * desaparecería antes de poder restaurarlo.
 */
@Composable
internal fun rememberPaymentDeletionHandler(
    viewModel: PaymentsViewModel,
    scope: CoroutineScope = rememberCoroutineScope()
): (ScheduledPaymentEntity) -> Unit {
    val snackbarHost = LocalSnackbarHost.current
    val deletedMessage = stringResource(R.string.scheduled_payment_deleted_snackbar)
    val undoLabel = stringResource(R.string.snackbar_undo)
    return { payment ->
        scope.launch {
            viewModel.deleteForUndo(payment)
            val undone = snackbarHost.showUndoSnackbar(
                message = deletedMessage,
                actionLabel = undoLabel,
                timeoutMillis = 5_000L
            )
            if (undone) viewModel.restore(payment)
        }
    }
}

/**
 * Ítems de la pestaña Pagos (Fase 2 v5): cabecera, selector lista/calendario y
 * una tarjeta perezosa por pago con clave estable.
 */
fun LazyListScope.paymentItems(
    state: PaymentsUiState,
    viewModel: PaymentsViewModel,
    view: PaymentsView,
    onViewChange: (PaymentsView) -> Unit,
    onAddPayment: () -> Unit,
    onEditPayment: (Long) -> Unit,
    onOpenCalendar: () -> Unit,
    onDeletePayment: (ScheduledPaymentEntity) -> Unit
) {
    item(key = "payments_header", contentType = "header") {
        Box(Modifier.padding(horizontal = 20.dp)) {
            PaymentsHeader(
                state = state,
                onOpenCalendar = onOpenCalendar,
                onAddPayment = onAddPayment
            )
        }
    }

    item(key = "payments_selector", contentType = "selector") {
        Box(Modifier.padding(horizontal = 20.dp)) {
            PaymentsViewSelector(view = view, onSelect = onViewChange)
        }
    }

    if (view == PaymentsView.LIST) {
        when {
            state.isLoading -> item(key = "payments_loading", contentType = "loading") {
                ShimmerList(rows = 3)
            }

            state.payments.isEmpty() -> item(key = "payments_empty", contentType = "empty") {
                EmptyPaymentsState(onNewPayment = onAddPayment)
            }

            else -> items(
                items = state.payments,
                key = { payment -> "payment_${payment.id}" },
                contentType = { "payment" }
            ) { payment ->
                PaymentCardItem(
                    payment = payment,
                    viewModel = viewModel,
                    onEditPayment = onEditPayment,
                    onDeletePayment = onDeletePayment
                )
            }
        }
    } else {
        item(key = "payments_calendar", contentType = "calendar") {
            Box(Modifier.padding(horizontal = 20.dp)) {
                CalendarScreen(
                    onDaySelected = {},
                    embedded = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun PaymentCardItem(
    payment: ScheduledPaymentEntity,
    viewModel: PaymentsViewModel,
    onEditPayment: (Long) -> Unit,
    onDeletePayment: (ScheduledPaymentEntity) -> Unit
) {
    var payNow by remember(payment.id) { mutableStateOf(false) }

    Box(Modifier.padding(horizontal = 20.dp)) {
        PaymentCard(
            payment = payment,
            onToggleActive = { viewModel.toggleActive(payment) },
            onMarkPaid = { payNow = true },
            onEdit = { onEditPayment(payment.id) },
            onDelete = { onDeletePayment(payment) }
        )
    }

    if (payNow) {
        PayNowDialog(
            payment = payment,
            onDismiss = { payNow = false },
            onConfirm = { amountCents ->
                viewModel.markPaid(payment, amountCents, LocalDate.now())
                payNow = false
            }
        )
    }
}

@Composable
private fun PayNowDialog(
    payment: ScheduledPaymentEntity,
    onDismiss: () -> Unit,
    onConfirm: (Long) -> Unit
) {
    var amountCents by remember(payment.id) { mutableStateOf(payment.amountCents) }

    CampusDialog(
        onDismissRequest = onDismiss,
        title = payment.name,
        confirmText = stringResource(R.string.confirm),
        confirmEnabled = amountCents > 0,
        onConfirm = {
            if (amountCents > 0) onConfirm(amountCents)
        },
        content = {
            CampusTextField(
                value = Money.toLocalDecimalString(amountCents),
                onValueChange = { raw ->
                    amountCents = raw.filter(Char::isDigit).take(9).toLongOrNull() ?: 0L
                },
                label = stringResource(R.string.transaction_amount),
                prefix = "$",
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
        }
    )
}

@Composable
private fun PaymentsHeader(
    state: PaymentsUiState,
    onOpenCalendar: () -> Unit,
    onAddPayment: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            Text(
                text = stringResource(R.string.finance_scheduled),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = if (state.payments.size == 1) {
                    stringResource(R.string.payments_count_one)
                } else {
                    stringResource(R.string.payments_count, state.payments.size)
                },
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            RoundIconButton(
                icon = Icons.Filled.DateRange,
                contentDescription = stringResource(R.string.finance_open_calendar),
                onClick = onOpenCalendar,
                size = 44.dp,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            )
            RoundIconButton(
                icon = Icons.Filled.Add,
                contentDescription = stringResource(R.string.scheduled_payment_new),
                onClick = onAddPayment,
                size = 44.dp,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun PaymentsViewSelector(
    view: PaymentsView,
    onSelect: (PaymentsView) -> Unit
) {
    UnderlinedTabRow(
        options = PaymentsView.entries,
        selected = view,
        onSelect = onSelect,
        label = { option ->
            stringResource(
                if (option == PaymentsView.LIST) R.string.payments_view_list
                else R.string.finance_calendar
            )
        }
    )
}

@Composable
private fun PaymentCard(
    payment: ScheduledPaymentEntity,
    onToggleActive: () -> Unit,
    onMarkPaid: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    var menuExpanded by remember(payment.id) { mutableStateOf(false) }
    val moreActionsDescription = stringResource(
        R.string.scheduled_payment_more_actions,
        payment.name
    )
    BentoCard(
        shape = MaterialTheme.shapes.large,
        contentPadding = PaddingValues(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = payment.name,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.width(8.dp))
            MoneyText(
                amountCents = payment.amountCents,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Spacer(Modifier.height(4.dp))
        Text(
            text = stringResource(freqLabelRes(payment.frequency)) + " · " +
                stringResource(R.string.scheduled_payment_next_due) + ": " +
                DATE_FMT.format(payment.nextDueDate) + " · " +
                stringResource(R.string.scheduled_payment_remind_days, payment.remindDaysBefore),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Estado (activo/pausado): informativo, no accionable. La activación
            // vive en el menú de overflow para que la tarjeta tenga una sola
            // acción principal y un solo control secundario.
            CampusChip(
                label = stringResource(
                    if (payment.active) R.string.scheduled_payment_active
                    else R.string.scheduled_payment_paused
                ),
                selected = payment.active,
                onClick = null
            )
            Spacer(Modifier.weight(1f))
            Box {
                RoundIconButton(
                    icon = Icons.Filled.MoreVert,
                    contentDescription = moreActionsDescription,
                    onClick = { menuExpanded = true }
                )
                CampusMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false }
                ) {
                    CampusMenuItem(
                        text = stringResource(
                            if (payment.active) R.string.scheduled_payment_pause
                            else R.string.scheduled_payment_activate
                        ),
                        onClick = {
                            menuExpanded = false
                            onToggleActive()
                        }
                    )
                    CampusMenuItem(
                        text = stringResource(R.string.edit),
                        onClick = {
                            menuExpanded = false
                            onEdit()
                        }
                    )
                    CampusMenuDivider()
                    CampusMenuItem(
                        text = stringResource(R.string.delete),
                        destructive = true,
                        onClick = {
                            menuExpanded = false
                            onDelete()
                        }
                    )
                }
            }
        }
        Spacer(Modifier.height(12.dp))
        CampusButton(
            text = stringResource(R.string.scheduled_payment_mark_paid),
            onClick = onMarkPaid,
            style = CampusButtonStyle.SECONDARY,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

data class PaymentsUiState(val payments: List<ScheduledPaymentEntity> = emptyList(), val isLoading: Boolean = true)

@HiltViewModel
class PaymentsViewModel @Inject constructor(
    private val financeRepository: FinanceRepository
) : ViewModel() {

    val uiState: StateFlow<PaymentsUiState> = financeRepository.observeAllScheduledPayments()
        .map { list -> PaymentsUiState(list, false) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, PaymentsUiState())

    fun toggleActive(p: ScheduledPaymentEntity) {
        viewModelScope.launch {
            financeRepository.updateScheduledPayment(p.copy(active = !p.active))
        }
    }

    fun markPaid(p: ScheduledPaymentEntity, amountCents: Long, date: LocalDate) {
        viewModelScope.launch {
            financeRepository.markScheduledPaymentPaid(p, amountCents, date)
        }
    }

    /** Borra el pago inmediatamente para poder ofrecer "Deshacer" con la entidad completa. */
    suspend fun deleteForUndo(payment: ScheduledPaymentEntity) {
        financeRepository.deleteScheduledPayment(payment.id)
    }

    /** Reinserta el pago eliminado conservando su id original. */
    fun restore(payment: ScheduledPaymentEntity) {
        viewModelScope.launch {
            financeRepository.insertScheduledPayment(payment)
        }
    }
}
