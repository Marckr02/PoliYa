@file:SuppressLint("ViewModelConstructorInComposable")

package com.example.campuspocket.feature.finance

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertHasNoClickAction
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.dp
import com.example.campuspocket.core.designsystem.CampusTheme
import com.example.campuspocket.core.navigation.CampusSnackbarHost
import com.example.campuspocket.core.navigation.LocalSnackbarHost
import com.example.campuspocket.feature.finance.data.ScheduledPaymentEntity
import com.example.campuspocket.feature.finance.support.FakeFinanceRepositoryUi
import com.example.campuspocket.feature.finance.ui.payments.PaymentsScreen
import com.example.campuspocket.feature.finance.ui.payments.PaymentsViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate

/**
 * B3 (v10): la tarjeta de pago expone una sola acción principal y un menú de
 * overflow; "Eliminar" se confirma con el aviso y "Deshacer" lo restaura.
 */
class PaymentsUiTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    private fun payment(id: Long = 1L, name: String = "Arriendo") = ScheduledPaymentEntity(
        id = id,
        name = name,
        amountCents = 45_000,
        accountId = 1,
        nextDueDate = LocalDate.now().plusDays(5)
    )

    @Test
    fun laTarjetaExponeAccionPrincipalYMenuDeOverflow() {
        val repo = FakeFinanceRepositoryUi(initialPayments = listOf(payment()))

        composeRule.setContent {
            CampusTheme {
                val hostState = remember { SnackbarHostState() }
                CompositionLocalProvider(LocalSnackbarHost provides hostState) {
                    PaymentsScreen(
                        onAddPayment = {},
                        onEditPayment = {},
                        onOpenCalendar = {},
                        viewModel = PaymentsViewModel(repo)
                    )
                    CampusSnackbarHost(hostState)
                }
            }
        }

        // Acción principal visible.
        composeRule.onNodeWithText("Marcar como pagado").assertIsDisplayed()
        composeRule.onNodeWithText("Marcar como pagado").assertHasClickAction()

        // Botón de overflow con etiqueta descriptiva.
        composeRule.onNodeWithContentDescription("Más acciones de Arriendo").assertHasClickAction()

        // El estado (Activo) ya no es un control: la tarjeta tiene 2 acciones.
        composeRule.onNodeWithText("Activo").assertHasNoClickAction()

        // "Editar" y "Eliminar" viven en el menú, no en la tarjeta.
        composeRule.onAllNodes(hasText("Editar")).assertCountEquals(0)
        composeRule.onAllNodes(hasText("Eliminar")).assertCountEquals(0)

        composeRule.onNodeWithContentDescription("Más acciones de Arriendo").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithText("Editar").assertIsDisplayed()
        composeRule.onNodeWithText("Eliminar").assertIsDisplayed()
    }

    @Test
    fun eliminarMuestraElAvisoConDeshacer() {
        val repo = FakeFinanceRepositoryUi(initialPayments = listOf(payment()))

        composeRule.setContent {
            CampusTheme {
                val hostState = remember { SnackbarHostState() }
                CompositionLocalProvider(LocalSnackbarHost provides hostState) {
                    PaymentsScreen(
                        onAddPayment = {},
                        onEditPayment = {},
                        onOpenCalendar = {},
                        viewModel = PaymentsViewModel(repo)
                    )
                    CampusSnackbarHost(hostState)
                }
            }
        }

        composeRule.onNodeWithContentDescription("Más acciones de Arriendo").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithText("Eliminar").performClick()

        composeRule.waitUntil(5_000) {
            composeRule.onAllNodes(hasText("Pago programado eliminado")).fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithText("Pago programado eliminado").assertIsDisplayed()
        composeRule.onNodeWithText("DESHACER").assertIsDisplayed()

        // El pago se borra de inmediato y "Deshacer" lo restaura.
        composeRule.waitUntil(5_000) { repo.paymentsFlow.value.isEmpty() }
        composeRule.onNodeWithText("DESHACER").performClick()
        composeRule.waitUntil(5_000) { repo.paymentsFlow.value.size == 1 }
        assertEquals("Arriendo", repo.paymentsFlow.value.single().name)
    }

    @Test
    fun elMenuPermiteActivarYPausarElPago() {
        val repo = FakeFinanceRepositoryUi(initialPayments = listOf(payment()))

        composeRule.setContent {
            CampusTheme {
                val hostState = remember { SnackbarHostState() }
                CompositionLocalProvider(LocalSnackbarHost provides hostState) {
                    PaymentsScreen(
                        onAddPayment = {},
                        onEditPayment = {},
                        onOpenCalendar = {},
                        viewModel = PaymentsViewModel(repo)
                    )
                    CampusSnackbarHost(hostState)
                }
            }
        }

        composeRule.onNodeWithContentDescription("Más acciones de Arriendo").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithText("Pausar pago").performClick()

        composeRule.waitUntil(5_000) { !repo.paymentsFlow.value.single().active }
        composeRule.onNodeWithText("Pausado").assertIsDisplayed()
    }

    @Test
    fun laTarjetaNoDejaAccionesMenoresDe48Dp() {
        val repo = FakeFinanceRepositoryUi(initialPayments = listOf(payment()))

        composeRule.setContent {
            CampusTheme {
                val hostState = remember { SnackbarHostState() }
                CompositionLocalProvider(LocalSnackbarHost provides hostState) {
                    PaymentsScreen(
                        onAddPayment = {},
                        onEditPayment = {},
                        onOpenCalendar = {},
                        viewModel = PaymentsViewModel(repo)
                    )
                    CampusSnackbarHost(hostState)
                }
            }
        }

        val density = composeRule.density
        val small = composeRule.onAllNodes(hasClickAction()).fetchSemanticsNodes()
            .filter { node ->
                val bounds = node.boundsInRoot
                bounds.height < with(density) { 48.dp.toPx() } ||
                    bounds.width < with(density) { 48.dp.toPx() }
            }
        assertTrue(
            "Nodos accionables menores de 48 dp: ${small.size}",
            small.isEmpty()
        )
    }
}
