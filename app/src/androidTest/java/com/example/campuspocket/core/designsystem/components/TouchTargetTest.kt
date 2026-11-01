@file:SuppressLint("ViewModelConstructorInComposable")

package com.example.campuspocket.core.designsystem.components

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.unit.dp
import com.example.campuspocket.R
import com.example.campuspocket.core.designsystem.CampusTheme
import com.example.campuspocket.core.navigation.CampusSnackbarHost
import com.example.campuspocket.core.navigation.LocalSnackbarHost
import com.example.campuspocket.core.preferences.UserPreferences
import com.example.campuspocket.feature.finance.data.ScheduledPaymentEntity
import com.example.campuspocket.feature.finance.support.FakeFinanceRepositoryUi
import com.example.campuspocket.feature.finance.ui.payments.PaymentsScreen
import com.example.campuspocket.feature.finance.ui.payments.PaymentsViewModel
import com.example.campuspocket.feature.home.ui.HomeScreen
import com.example.campuspocket.feature.home.ui.HomeUiState
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate

/**
 * B1 (v10): todo elemento interactivo debe medir al menos 48 × 48 dp de
 * área táctil. La comprobación es genérica: recorre los nodos con acción de
 * clic de la composición y mide sus límites en raíz.
 *
 * Los elementos decorativos (puntos de color, iniciales) no tienen acción de
 * clic, así que no entran en la medición.
 */
class TouchTargetTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    private fun assertTouchTargetsAreAtLeast48Dp() {
        val density = composeRule.density
        val minPx = with(density) { 48.dp.toPx() }
        val tooSmall = composeRule.onAllNodes(hasClickAction()).fetchSemanticsNodes()
            .filter { node ->
                val bounds = node.boundsInRoot
                bounds.height < minPx || bounds.width < minPx
            }
        assertTrue(
            "Nodos accionables menores de 48 dp: ${tooSmall.size}",
            tooSmall.isEmpty()
        )
    }

    @Test
    fun losControlesDelSistemaDeDisenoCumplen48Dp() {
        composeRule.setContent {
            CampusTheme {
                Column(modifier = Modifier.fillMaxSize().padding(8.dp)) {
                    CampusButton(text = "Guardar", onClick = {})
                    CampusButton(
                        text = "Marcar como pagado",
                        onClick = {},
                        style = CampusButtonStyle.SECONDARY
                    )
                    CampusChip(label = "Todas", selected = true, onClick = {})
                    CampusChip(label = "Hoy", selected = false, onClick = {})
                    UnderlinedTabRow(
                        options = listOf("Lista", "Calendario"),
                        selected = "Lista",
                        onSelect = {},
                        label = { it }
                    )
                    RoundIconButton(
                        icon = Icons.Filled.MoreVert,
                        contentDescription = "Más acciones",
                        onClick = {}
                    )
                    CampusSwitch(
                        checked = true,
                        onCheckedChange = {},
                        contentDescription = "Interruptor"
                    )
                    CampusTextButton(text = "Editar", onClick = {})
                }
            }
        }

        assertTouchTargetsAreAtLeast48Dp()
    }

    @Test
    fun laPantallaDeInicioCumple48Dp() {
        composeRule.setContent {
            CampusTheme {
                HomeScreen(
                    preferences = UserPreferences(),
                    onOpenModule = {},
                    onOpenSettings = {},
                    state = HomeUiState(
                        monthlyBudgetCents = 16_000L,
                        monthlySpentCents = 5_000L,
                        monthlyRemainingCents = 11_000L
                    )
                )
            }
        }

        assertTouchTargetsAreAtLeast48Dp()
    }

    @Test
    fun laPantallaDePagosCumple48Dp() {
        val repo = FakeFinanceRepositoryUi(
            initialPayments = listOf(
                ScheduledPaymentEntity(
                    id = 1L,
                    name = "Arriendo",
                    amountCents = 45_000,
                    accountId = 1,
                    nextDueDate = LocalDate.now().plusDays(5)
                )
            )
        )

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

        assertTouchTargetsAreAtLeast48Dp()
    }

    @Test
    fun elDockCumple48Dp() {
        composeRule.setContent {
            CampusTheme {
                FloatingDock(
                    destinations = listOf(
                        DockDestination(
                            route = "home",
                            label = "Inicio",
                            outlineRes = R.drawable.ic_dock_home_outline,
                            filledRes = R.drawable.ic_dock_home_filled
                        ),
                        DockDestination(
                            route = "notes",
                            label = "Notas",
                            outlineRes = R.drawable.ic_dock_notes_outline,
                            filledRes = R.drawable.ic_dock_notes_filled
                        )
                    ),
                    currentRoute = "home",
                    onNavigate = {}
                )
            }
        }

        assertTouchTargetsAreAtLeast48Dp()
    }
}
