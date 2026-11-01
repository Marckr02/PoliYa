package com.example.campuspocket.feature.finance.support

import com.example.campuspocket.feature.finance.data.BudgetEntity
import com.example.campuspocket.feature.finance.data.MonthlyBudgetEntity
import com.example.campuspocket.feature.finance.data.ScheduledPaymentEntity
import com.example.campuspocket.feature.finance.data.TransactionDao
import com.example.campuspocket.feature.finance.data.TransactionType
import com.example.campuspocket.feature.finance.domain.WithdrawalResult
import com.example.campuspocket.feature.finance.domain.model.Account
import com.example.campuspocket.feature.finance.domain.model.Category
import com.example.campuspocket.feature.finance.domain.model.Transaction
import com.example.campuspocket.feature.finance.domain.repository.FinanceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

/**
 * Repositorio de finanzas en memoria para las pruebas instrumentadas.
 * Incluye los pagos programados: [paymentsFlow] es mutable para poder
 * comprobar el borrado y el "Deshacer" sin tocar la base de datos.
 */
class FakeFinanceRepositoryUi(
    initialAccounts: List<Account> = emptyList(),
    initialCategories: List<Category> = emptyList(),
    initialPayments: List<ScheduledPaymentEntity> = emptyList()
) : FinanceRepository {

    private var nextTxId = 0L
    val accountsFlow = MutableStateFlow(initialAccounts)
    val categoriesFlow = MutableStateFlow(initialCategories)
    val transactionsFlow = MutableStateFlow(emptyList<Transaction>())
    val monthlyBudgetsFlow = MutableStateFlow(emptyList<MonthlyBudgetEntity>())
    val paymentsFlow = MutableStateFlow(initialPayments)
    val insertedTransactions = mutableListOf<Transaction>()

    // ---- Cuentas ----
    override fun observeActiveAccounts(): Flow<List<Account>> = accountsFlow
    override fun observeAccount(accountId: Long): Flow<Account?> =
        accountsFlow.map { list -> list.firstOrNull { it.id == accountId } }
    override suspend fun getAccount(accountId: Long): Account? =
        accountsFlow.value.firstOrNull { it.id == accountId }
    override suspend fun insertAccount(account: Account): Long = 0L
    override suspend fun updateAccount(account: Account) = Unit
    override suspend fun archiveAccount(accountId: Long) = Unit

    // ---- Categorías ----
    override fun observeActiveCategories(): Flow<List<Category>> = categoriesFlow
    override suspend fun getCategory(categoryId: Long): Category? =
        categoriesFlow.value.firstOrNull { it.id == categoryId }
    override suspend fun insertCategory(category: Category): Long = 0L
    override suspend fun updateCategory(category: Category) = Unit
    override suspend fun archiveCategory(categoryId: Long) = Unit

    // ---- Transacciones ----
    override fun observeTransactions(): Flow<List<Transaction>> = transactionsFlow
    override fun observeTransactionsByDateRange(start: LocalDate, end: LocalDate): Flow<List<Transaction>> =
        transactionsFlow.map { list -> list.filter { it.date >= start && it.date <= end } }
    override fun observeTransactionsByAccount(accountId: Long): Flow<List<Transaction>> =
        transactionsFlow.map { list ->
            list.filter { it.accountId == accountId || it.targetAccountId == accountId }
        }
    override suspend fun getTransaction(id: Long): Transaction? =
        transactionsFlow.value.firstOrNull { it.id == id }
    override suspend fun insertTransaction(transaction: Transaction): Long {
        insertedTransactions += transaction
        val id = ++nextTxId
        transactionsFlow.value = transactionsFlow.value + transaction.copy(id = id)
        return id
    }
    override suspend fun updateTransaction(transaction: Transaction) = Unit
    override suspend fun deleteTransaction(transactionId: Long) = Unit

    // ---- Presupuestos ----
    override fun observeAllBudgets(): Flow<List<BudgetEntity>> = MutableStateFlow(emptyList())
    override fun observeBudgetsForMonth(yearMonth: String): Flow<List<BudgetEntity>> =
        MutableStateFlow(emptyList())
    override suspend fun getBudgetsForMonth(yearMonth: String): List<BudgetEntity> = emptyList()
    override suspend fun getBudget(yearMonth: String, categoryId: Long): BudgetEntity? = null
    override suspend fun upsertBudget(yearMonth: String, categoryId: Long, amountCents: Long) = Unit
    override suspend fun deleteBudget(yearMonth: String, categoryId: Long) = Unit

    override fun observeMonthlyBudgets(): Flow<List<MonthlyBudgetEntity>> = monthlyBudgetsFlow
    override suspend fun getMonthlyBudget(yearMonth: String): MonthlyBudgetEntity? =
        monthlyBudgetsFlow.value.firstOrNull { it.yearMonth == yearMonth }
    override suspend fun upsertMonthlyBudget(yearMonth: String, amountCents: Long) = Unit
    override suspend fun deleteMonthlyBudget(yearMonth: String) = Unit

    override suspend fun withdrawToCash(
        savingsAccountId: Long,
        amountCents: Long,
        date: LocalDate
    ): WithdrawalResult = WithdrawalResult.Success(++nextTxId)

    // ---- Pagos programados ----
    override fun observeActiveScheduledPayments(): Flow<List<ScheduledPaymentEntity>> =
        paymentsFlow.map { list -> list.filter { it.active } }
    override fun observeAllScheduledPayments(): Flow<List<ScheduledPaymentEntity>> = paymentsFlow
    override suspend fun getScheduledPayment(id: Long): ScheduledPaymentEntity? =
        paymentsFlow.value.firstOrNull { it.id == id }
    override suspend fun getPaymentsDueUntil(date: LocalDate): List<ScheduledPaymentEntity> =
        paymentsFlow.value.filter { it.active && it.nextDueDate <= date }
    override suspend fun insertScheduledPayment(payment: ScheduledPaymentEntity): Long {
        val id = payment.id
        paymentsFlow.value = paymentsFlow.value + payment
        return id
    }
    override suspend fun updateScheduledPayment(payment: ScheduledPaymentEntity) {
        paymentsFlow.value = paymentsFlow.value.map { if (it.id == payment.id) payment else it }
    }
    override suspend fun deleteScheduledPayment(id: Long) {
        paymentsFlow.value = paymentsFlow.value.filterNot { it.id == id }
    }
    override suspend fun markScheduledPaymentPaid(
        payment: ScheduledPaymentEntity,
        amountCents: Long,
        date: LocalDate
    ) = Unit

    // ---- Agregados ----
    override fun observeAccountDeltas(): Flow<List<TransactionDao.AccountDelta>> =
        transactionsFlow.map { list ->
            list.groupBy { it.accountId }.map { (accountId, txs) ->
                TransactionDao.AccountDelta(
                    accountId,
                    txs.sumOf {
                        when (it.type) {
                            TransactionType.INCOME, TransactionType.REFUND -> it.amountCents
                            else -> -it.amountCents
                        }
                    }
                )
            }
        }
    override fun observeTransferInDeltas(): Flow<List<TransactionDao.AccountDelta>> =
        transactionsFlow.map { emptyList() }
    override fun observeNetSpentByCategory(
        start: LocalDate,
        end: LocalDate
    ): Flow<List<TransactionDao.CategorySpent>> = transactionsFlow.map { emptyList() }
}
