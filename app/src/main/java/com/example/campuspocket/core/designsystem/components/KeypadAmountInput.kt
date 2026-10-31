package com.example.campuspocket.core.designsystem.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.example.campuspocket.R
import com.example.campuspocket.core.designsystem.CampusKeypadLabel
import com.example.campuspocket.core.designsystem.CampusTheme
import com.example.campuspocket.core.designsystem.LocalMotion
import com.example.campuspocket.core.designsystem.MotionTokens
import com.example.campuspocket.core.designsystem.ShapeBottomSheet
import com.example.campuspocket.core.designsystem.ShapeChip
import com.example.campuspocket.core.designsystem.extended
import com.example.campuspocket.core.util.Haptics

const val KEYPAD_BACKSPACE = "⌫"
const val KEYPAD_DOUBLE_ZERO = "00"
const val KEYPAD_TRIPLE_ZERO = "000"

private val KEYPAD_FIRST_ROW = listOf("1", "2", "3")
private val KEYPAD_OTHER_ROWS = listOf(
    listOf("4", "5", "6"),
    listOf("7", "8", "9"),
    listOf(KEYPAD_DOUBLE_ZERO, "0", KEYPAD_TRIPLE_ZERO)
)

/**
 * Calcula los dígitos que se envían al ViewModel tras pulsar una tecla.
 *
 * El ViewModel interpreta los dígitos como centavos (estilo cajero), por lo que
 * el texto visible se deriva de ahí y aquí solo se manipulan dígitos. Se quitan
 * los ceros a la izquierda y se limita el monto a 9 dígitos (9.999.999,99).
 * Esta función es pura para poder probarla sin Compose.
 */
fun keypadNextDigits(current: String, key: String): String {
    val digits = current.filter(Char::isDigit)
    if (key == KEYPAD_BACKSPACE) {
        return digits.dropLast(1).trimStart('0').take(9)
    }

    val appended = digits + key.filter(Char::isDigit)
    return appended.trimStart('0').take(9)
}

/**
 * Panel del teclado del formulario de movimiento (v8 §6.8).
 *
 * Rejilla 4 × 4 de 244 dp con separación de 7 dp: fila 1 con 1-3 y borrar;
 * filas 2-4 con los dígitos y, en la cuarta columna, un botón Guardar que ocupa
 * esas tres filas. Bordes `ControlLine`, radio 10, Guardar en `Lime` con borde
 * `Ink`. Vibra en cada tecla y en el guardado.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TransactionKeypadPanel(
    amountDigits: String,
    onAmountChange: (String) -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier,
    saveEnabled: Boolean = true
) {
    val ext = MaterialTheme.extended
    val view = LocalView.current
    val digits = amountDigits.filter(Char::isDigit)
    val digitWords = stringArrayResource(R.array.keypad_digit_words)
    val backspaceDescription = stringResource(R.string.keypad_backspace_desc)
    val doubleZeroDescription = stringResource(R.string.keypad_double_zero_desc)
    val tripleZeroDescription = stringResource(R.string.keypad_triple_zero_desc)
    val saveLabel = stringResource(R.string.keypad_save)
    val lineColor = MaterialTheme.colorScheme.outlineVariant
    val keyContainer = MaterialTheme.colorScheme.surface
    val keyContent = MaterialTheme.colorScheme.onSurface

    fun descriptionOf(key: String): String = when (key) {
        KEYPAD_BACKSPACE -> backspaceDescription
        KEYPAD_DOUBLE_ZERO -> doubleZeroDescription
        KEYPAD_TRIPLE_ZERO -> tripleZeroDescription
        else -> digitWords.getOrNull(key.toIntOrNull() ?: -1) ?: key
    }

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        shape = ShapeBottomSheet,
        tonalElevation = 0.dp
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .height(244.dp)
                .drawBehind {
                    drawLine(
                        color = lineColor,
                        start = Offset.Zero,
                        end = Offset(size.width, 0f),
                        strokeWidth = 1.dp.toPx()
                    )
                }
                .padding(horizontal = 14.dp, vertical = 8.dp)
        ) {
            val spacing = 7.dp
            val keyWidth = (maxWidth - spacing * 3) / 4f
            val keyHeight = (maxHeight - spacing * 3) / 4f
            val saveHeight = keyHeight * 3f + spacing * 2f

            Column(verticalArrangement = Arrangement.spacedBy(spacing)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(spacing)
                ) {
                    KEYPAD_FIRST_ROW.forEach { key ->
                        KeypadKey(
                            label = key,
                            description = descriptionOf(key),
                            modifier = Modifier.weight(1f).height(keyHeight),
                            containerColor = keyContainer,
                            contentColor = keyContent,
                            onClick = {
                                Haptics.tick(view)
                                onAmountChange(keypadNextDigits(digits, key))
                            }
                        )
                    }
                    KeypadKey(
                        label = KEYPAD_BACKSPACE,
                        description = descriptionOf(KEYPAD_BACKSPACE),
                        modifier = Modifier.weight(1f).height(keyHeight),
                        containerColor = keyContainer,
                        contentColor = keyContent,
                        isBackspace = true,
                        onLongClick = {
                            Haptics.reject(view)
                            onAmountChange("")
                        },
                        onClick = {
                            Haptics.tick(view)
                            onAmountChange(keypadNextDigits(digits, KEYPAD_BACKSPACE))
                        }
                    )
                }

                Box(modifier = Modifier.fillMaxWidth()) {
                    Column(verticalArrangement = Arrangement.spacedBy(spacing)) {
                        KEYPAD_OTHER_ROWS.forEach { row ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(spacing)
                            ) {
                                row.forEach { key ->
                                    KeypadKey(
                                        label = key,
                                        description = descriptionOf(key),
                                        modifier = Modifier.weight(1f).height(keyHeight),
                                        containerColor = keyContainer,
                                        contentColor = keyContent,
                                        onClick = {
                                            Haptics.tick(view)
                                            onAmountChange(keypadNextDigits(digits, key))
                                        }
                                    )
                                }
                                Spacer(Modifier.weight(1f))
                            }
                        }
                    }

                    // Guardar ocupa las tres filas de la cuarta columna.
                    SaveKeypadKey(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .width(keyWidth)
                            .height(saveHeight),
                        label = saveLabel,
                        enabled = saveEnabled,
                        onClick = {
                            Haptics.confirm(view)
                            onSave()
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun KeypadKey(
    label: String,
    description: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: androidx.compose.ui.graphics.Color,
    contentColor: androidx.compose.ui.graphics.Color,
    isBackspace: Boolean = false,
    onLongClick: (() -> Unit)? = null
) {
    val ext = MaterialTheme.extended
    val motion = LocalMotion.current
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.94f else 1f,
        animationSpec = tween(motion.ms(MotionTokens.Short)),
        label = "keypadScale"
    )
    val container by animateColorAsState(
        targetValue = if (pressed) ext.lime else containerColor,
        animationSpec = tween(motion.ms(MotionTokens.Short)),
        label = "keypadContainer"
    )
    val content by animateColorAsState(
        targetValue = if (pressed) ext.inkContent else contentColor,
        animationSpec = tween(motion.ms(MotionTokens.Short)),
        label = "keypadContent"
    )
    Box(
        modifier = modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clip(ShapeChip)
            .background(container)
            .border(1.dp, ext.controlLine, ShapeChip)
            .semantics { contentDescription = description }
            .combinedClickable(
                interactionSource = interaction,
                indication = LocalIndication.current,
                enabled = true,
                onClickLabel = null,
                role = Role.Button,
                onLongClickLabel = null,
                onLongClick = onLongClick,
                onDoubleClick = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        if (isBackspace) {
            Icon(
                painter = painterResource(R.drawable.ic_backspace),
                contentDescription = null, // decorativo: la etiqueta contigua describe la accion
                tint = content,
                modifier = Modifier.height(24.dp)
            )
        } else {
            Text(
                text = label,
                style = CampusKeypadLabel,
                color = content,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun SaveKeypadKey(
    label: String,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val ext = MaterialTheme.extended
    val motion = LocalMotion.current
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed && enabled) 0.94f else 1f,
        animationSpec = tween(motion.ms(MotionTokens.Short)),
        label = "saveKeyScale"
    )
    val container = if (enabled) ext.lime else ext.track
    val content = if (enabled) ext.inkContent else ext.inkMuted
    val border = if (enabled) ext.inkIndicator else ext.controlLine
    Box(
        modifier = modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clip(ShapeChip)
            .background(container)
            .border(1.dp, border, ShapeChip)
            .semantics { contentDescription = label }
            .combinedClickable(
                interactionSource = interaction,
                indication = LocalIndication.current,
                enabled = enabled,
                onClickLabel = null,
                role = Role.Button,
                onLongClickLabel = null,
                onLongClick = null,
                onDoubleClick = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = null, // decorativo: la etiqueta contigua describe la accion
                tint = content,
                modifier = Modifier.height(24.dp)
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = androidx.compose.ui.text.font.FontWeight.ExtraBold
                ),
                color = content,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
@Preview(name = "TransactionKeypadPanel", showBackground = true)
fun PreviewTransactionKeypadPanel() {
    CampusTheme {
        Surface {
            TransactionKeypadPanel(
                amountDigits = "12,50",
                onAmountChange = {},
                onSave = {}
            )
        }
    }
}
