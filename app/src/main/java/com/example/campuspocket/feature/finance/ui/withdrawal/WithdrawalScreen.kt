package com.example.campuspocket.feature.finance.ui.withdrawal

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.campuspocket.R
import com.example.campuspocket.core.designsystem.CampusOverline
import com.example.campuspocket.core.designsystem.MoneyText
import com.example.campuspocket.core.designsystem.ShapeCard
import com.example.campuspocket.core.designsystem.ShapeChip
import com.example.campuspocket.core.designsystem.components.BentoCard
import com.example.campuspocket.core.designsystem.components.CampusButton
import com.example.campuspocket.core.designsystem.components.CampusButtonStyle
import com.example.campuspocket.core.designsystem.components.CampusDialog
import com.example.campuspocket.core.designsystem.components.CampusSelectField
import com.example.campuspocket.core.designsystem.components.CampusSelectItem
import com.example.campuspocket.core.designsystem.components.CampusTextButton
import com.example.campuspocket.core.designsystem.components.CampusTextField
import com.example.campuspocket.core.designsystem.components.CampusTopBar
import com.example.campuspocket.core.designsystem.components.EmptyState
import com.example.campuspocket.core.designsystem.components.rememberDiscardChangesGuard
import com.example.campuspocket.core.designsystem.extended
import com.example.campuspocket.core.designsystem.formatMoneyOrHidden
import com.example.campuspocket.core.util.Money
import com.example.campuspocket.feature.finance.domain.AccountBalance
import com.example.campuspocket.feature.finance.domain.WithdrawalError
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WithdrawalScreen(
    onDone: () -> Unit,
    onCreateAccount: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: WithdrawalViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val requestBack = rememberDiscardChangesGuard(
        hasUnsavedChanges = state.hasUnsavedChanges && state.savedAmountCents == null,
        onDiscard = onDone
    )

    Column(modifier = modifier.fillMaxSize()) {
        CampusTopBar(
            title = stringResource(R.string.finance_withdrawal),
            onBack = requestBack
        )
        when {
            state.isLoading -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator() }

            state.savingsAccounts.isEmpty() -> Box(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                EmptyState(
                    title = stringResource(R.string.withdrawal_no_savings_title),
                    message = stringResource(R.string.withdrawal_no_savings_message),
                    iconRes = R.drawable.ic_wallet,
                    actionLabel = stringResource(R.string.withdrawal_create_savings),
                    onAction = onCreateAccount
                )
            }

            else -> {
                val source = state.selectedAccount
                val insufficientBalance = source != null && state.amountCents > source.balanceCents
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    SavingsAccountDropdown(
                        accounts = state.savingsAccounts,
                        selectedId = state.selectedAccountId,
                        onSelect = viewModel::selectAccount
                    )

                    CashDestinationCard(
                        cashName = state.cashAccount?.name,
                        showError = state.error == WithdrawalError.MISSING_CASH_ACCOUNT,
                        onCreateAccount = onCreateAccount
                    )

                    AmountSection(
                        state = state,
                        onPreset = viewModel::selectPreset,
                        onOther = viewModel::selectOtherAmount,
                        onAmountChange = viewModel::onAmountChange
                    )

                    if (source != null && state.cashAccount != null && state.amountCents > 0L) {
                        AfterWithdrawalCard(
                            savingsAfterCents = (source.balanceCents - state.amountCents)
                                .coerceAtLeast(0L),
                            cashAfterCents = state.cashBalanceCents + state.amountCents
                        )
                    }

                    state.error?.let { error ->
                        if (error == WithdrawalError.MISSING_SOURCE_ACCOUNT) {
                            InlineError(errorText(error = error, accountName = state.selectedAccount?.account?.name))
                        }
                    }
                    if (insufficientBalance) {
                        InlineError(
                            stringResource(
                                R.string.withdrawal_insufficient_reason,
                                source?.account?.name.orEmpty()
                            )
                        )
                    }
                }
                CampusButton(
                    text = stringResource(
                        if (!state.isSaving && !insufficientBalance) {
                            R.string.withdrawal_confirm
                        } else {
                            R.string.withdrawal_review_amount
                        }
                    ),
                    onClick = viewModel::save,
                    enabled = !state.isSaving && !insufficientBalance,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 12.dp)
                )
            }
        }
    }

    LaunchedEffect(state.savedAmountCents) {
        if (state.savedAmountCents != null) {
            onDone()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SavingsAccountDropdown(
    accounts: List<AccountBalance>,
    selectedId: Long?,
    onSelect: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val selected = accounts.firstOrNull { it.account.id == selectedId }

    BentoCard(
        modifier = modifier.fillMaxWidth(),
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        shape = ShapeCard
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = stringResource(R.string.withdrawal_from).uppercase(Locale.getDefault()),
                    style = CampusOverline,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = selected?.account?.name.orEmpty(),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                selected?.let {
                    Text(
                        text = stringResource(R.string.withdrawal_balance, formatMoneyOrHidden(it.balanceCents)),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Icon(
                imageVector = Icons.Filled.KeyboardArrowDown,
                contentDescription = null, // decorativo: el texto contiguo describe la acción
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun AfterWithdrawalCard(
    savingsAfterCents: Long,
    cashAfterCents: Long,
    modifier: Modifier = Modifier
) {
    BentoCard(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        shape = ShapeCard,
        contentPadding = PaddingValues(16.dp)
    ) {
        Text(
            text = stringResource(R.string.withdrawal_after_title),
            style = CampusOverline,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.withdrawal_savings_label),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            MoneyText(
                amountCents = savingsAfterCents,
                style = MaterialTheme.typography.titleSmall
            )
        }
        Spacer(Modifier.height(6.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.withdrawal_cash_label),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            MoneyText(
                amountCents = cashAfterCents,
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}

@Composable
private fun CashDestinationCard(
    cashName: String?,
    showError: Boolean,
    onCreateAccount: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(6.dp)) {
        BentoCard(
            modifier = Modifier.fillMaxWidth(),
            containerColor = if (showError) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.surface,
            contentColor = if (showError) MaterialTheme.colorScheme.onErrorContainer else MaterialTheme.colorScheme.onSurface,
            shape = ShapeCard
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(MaterialTheme.extended.lavender, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_wallet),
                        contentDescription = null, // decorativo: el texto contiguo describe la cuenta
                        tint = MaterialTheme.extended.onLavender
                    )
                }
                Column {
                    Text(
                        text = stringResource(R.string.withdrawal_to).uppercase(Locale.getDefault()),
                        style = CampusOverline,
                        color = if (showError) MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.82f) else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = cashName ?: stringResource(R.string.account_type_cash),
                        style = MaterialTheme.typography.titleMedium,
                        color = if (showError) MaterialTheme.colorScheme.onErrorContainer else MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
        if (showError) {
            BentoCard(
                modifier = Modifier.fillMaxWidth(),
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer,
                shape = ShapeCard,
                contentPadding = PaddingValues(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.withdrawal_error_cash),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.weight(1f)
                    )
                    CampusTextButton(
                        text = stringResource(R.string.finance_new_account),
                        onClick = onCreateAccount,
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
private fun AmountSection(
    state: WithdrawalUiState,
    onPreset: (Int) -> Unit,
    onOther: () -> Unit,
    onAmountChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = stringResource(R.string.transaction_amount).uppercase(Locale.getDefault()),
            style = CampusOverline,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf(5, 10, 15).forEach { dollars ->
                WithdrawalAmountChip(
                    label = "$$dollars",
                    selected = !state.isCustomAmount && state.presetDollars == dollars,
                    onClick = { onPreset(dollars) },
                    modifier = Modifier.weight(1f)
                )
            }
            WithdrawalAmountChip(
                label = stringResource(R.string.withdrawal_other),
                selected = state.isCustomAmount,
                onClick = onOther,
                modifier = Modifier.weight(1f)
            )
        }

        if (state.isCustomAmount) {
            val amountError = state.error?.takeIf {
                it == WithdrawalError.MISSING_AMOUNT ||
                    it == WithdrawalError.NOT_MULTIPLE_OF_5 ||
                    it == WithdrawalError.INSUFFICIENT_BALANCE
            }
            CampusTextField(
                value = Money.toLocalDecimalString(state.amountCents),
                onValueChange = onAmountChange,
                label = stringResource(R.string.withdrawal_other_amount),
                prefix = "$",
                singleLine = true,
                isError = amountError != null,
                supportingText = amountError?.let {
                    { InlineError(errorText(it, state.selectedAccount?.account?.name)) }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            state.error?.takeIf {
                it == WithdrawalError.MISSING_AMOUNT ||
                    it == WithdrawalError.NOT_MULTIPLE_OF_5 ||
                    it == WithdrawalError.INSUFFICIENT_BALANCE
            }?.let { error ->
                InlineError(errorText(error, state.selectedAccount?.account?.name))
            }
        }
    }
}

@Composable
private fun WithdrawalAmountChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val ext = MaterialTheme.extended
    val selectedDesc = stringResource(if (selected) R.string.chip_state_selected else R.string.chip_state_unselected)
    Box(
        modifier = modifier
            .heightIn(min = 48.dp)
            .clip(ShapeChip)
            .background(if (selected) ext.lime else androidx.compose.ui.graphics.Color.Transparent)
            .border(1.dp, if (selected) ext.inkIndicator else ext.controlLine, ShapeChip)
            .combinedClickable(
                onClick = onClick,
                onLongClick = null,
                role = Role.RadioButton
            )
            .semantics {
                this.selected = selected
                this.stateDescription = selectedDesc
            }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (selected) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null, // decorativo: el chip tiene etiqueta
                    tint = ext.onLime,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(4.dp))
            }
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.ExtraBold),
                color = if (selected) ext.onLime else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun InlineError(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.error,
        modifier = Modifier
            .padding(start = 4.dp)
            .semantics { liveRegion = androidx.compose.ui.semantics.LiveRegionMode.Polite }
    )
}

@Composable
private fun errorText(error: WithdrawalError, accountName: String?): String = when (error) {
    WithdrawalError.MISSING_AMOUNT -> stringResource(R.string.withdrawal_error_amount)
    WithdrawalError.NOT_MULTIPLE_OF_5 -> stringResource(R.string.withdrawal_error_multiple)
    WithdrawalError.INSUFFICIENT_BALANCE ->
        stringResource(R.string.withdrawal_error_balance, accountName.orEmpty())
    WithdrawalError.MISSING_CASH_ACCOUNT -> stringResource(R.string.withdrawal_error_cash)
    WithdrawalError.MISSING_SOURCE_ACCOUNT -> stringResource(R.string.withdrawal_error_savings)
}
