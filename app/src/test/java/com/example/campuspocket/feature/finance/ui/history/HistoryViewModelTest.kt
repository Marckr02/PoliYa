package com.example.campuspocket.feature.finance.ui.history

import com.example.campuspocket.feature.finance.FakeFinanceRepository
import com.example.campuspocket.feature.finance.data.TransactionSubtype
import com.example.campuspocket.feature.finance.data.TransactionType
import com.example.campuspocket.feature.finance.domain.model.Transaction
import com.example.campuspocket.feature.finance.testAccount
import com.example.campuspocket.feature.finance.testCategory
import com.example.campuspocket.feature.finance.testExpense
import com.example.campuspocket.feature.finance.testIncome
import com.example.campuspocket.feature.finance.testTransfer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class HistoryViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // v10: fechas relativas al mes actual; el ViewModel carga por defecto el mes
    // en curso, así que fechas fijas caducaban al cambiar de mes.
    private val monthStart = LocalDate.now().withDayOfMonth(1)
    private val monthEnd = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth())

    @Test
    fun `sin filtros lista todo ordenado por fecha desc`() = runTest(testDispatcher) {
        val all = listOf(
            testExpense(id = 1, accountId = 1, categoryId = 1, amountCents = 500,
                description = "almuerzo", date = monthStart),
            testIncome(id = 2, accountId = 1, categoryId = 2, amountCents = 2_000, date = monthEnd),
            testTransfer(id = 3, accountId = 1, targetAccountId = 2, amountCents = 100, date = monthStart)
        )

        val vm = HistoryViewModel(newRepo(all))
        advanceUntilIdle()

        assertEquals(listOf(2L, 1L, 3L), vm.uiState.value.transactions.map { it.id })
    }

    @Test
    fun `applyFilters combina todos los criterios`() {
        val all = listOf(
            testExpense(id = 1, accountId = 1, categoryId = 1, description = "almuerzo", date = monthStart),
            testExpense(id = 2, accountId = 1, categoryId = 1, description = "taxi", date = monthEnd),
            testIncome(id = 3, accountId = 2, categoryId = 2, date = monthEnd)
        )

        val filtered = HistoryViewModel.applyFilters(
            all,
            HistoryFilters(type = TransactionType.EXPENSE, accountId = 1, categoryId = 1, query = "alm")
        )

        assertEquals(listOf(1L), filtered.map { it.id })
    }

    @Test
    fun `el filtro de subtipo aisla los retiros en cajero`() {
        val all = listOf(
            testTransfer(id = 1, accountId = 1, targetAccountId = 2),
            testTransfer(
                id = 2,
                accountId = 1,
                targetAccountId = 2,
                subtype = TransactionSubtype.RETIRO_CAJERO
            )
        )

        val withdrawals = HistoryViewModel.applyFilters(
            all,
            HistoryFilters(subtype = TransactionSubtype.RETIRO_CAJERO)
        )

        assertEquals(listOf(2L), withdrawals.map { it.id })
    }

    @Test
    fun `el filtro de cuenta tambien atrapa transferencias entrantes`() {
        val all = listOf(
            testTransfer(id = 1, accountId = 1, targetAccountId = 2, amountCents = 100)
        )

        val fromTarget = HistoryViewModel.applyFilters(all, HistoryFilters(accountId = 2))
        val fromOrigin = HistoryViewModel.applyFilters(all, HistoryFilters(accountId = 1))

        assertEquals(listOf(1L), fromTarget.map { it.id })
        assertEquals(listOf(1L), fromOrigin.map { it.id })
    }

    @Test
    fun `rango de fechas deja fuera lo que no toca`() = runTest(testDispatcher) {
        val all = listOf(
            testExpense(id = 1, date = monthStart),
            testExpense(id = 2, date = monthEnd)
        )
        val vm = HistoryViewModel(newRepo(all))
        advanceUntilIdle()

        vm.setDateRange(monthEnd, monthEnd)
        advanceUntilIdle()

        assertEquals(listOf(2L), vm.uiState.value.transactions.map { it.id })
    }

    private fun newRepo(transactions: List<Transaction>) = FakeFinanceRepository(
        accounts = listOf(testAccount(id = 1, name = "Efectivo"), testAccount(id = 2, name = "Banco")),
        categories = listOf(
            testCategory(id = 1, name = "Comida"),
            testCategory(id = 2, name = "Mesada", kind = com.example.campuspocket.feature.finance.data.CategoryKind.INCOME)
        ),
        transactions = transactions
    )
}
