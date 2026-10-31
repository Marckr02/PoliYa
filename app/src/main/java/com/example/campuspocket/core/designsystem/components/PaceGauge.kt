package com.example.campuspocket.core.designsystem.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.campuspocket.core.designsystem.CampusNumeralHero
import com.example.campuspocket.core.designsystem.LocalMotion
import com.example.campuspocket.core.designsystem.MotionEasing
import com.example.campuspocket.core.designsystem.extended
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * Medidor semicircular del ritmo de presupuesto (v8 §7).
 *
 * Geometría: 248 dp de ancho y trazo de 14 dp; arco de 180° que va de izquierda
 * a derecha por arriba. La pista es blanco al 15 % sobre `Ink`; el relleno usa
 * [fillColor] y la marca del esperado es una línea radial blanca de 3 dp.
 *
 * Expone una única descripción accesible (los hijos quedan fusionados) y anima
 * el barrido durante 600 ms solo cuando `LocalMotion` está activo.
 */
@Composable
fun PaceGauge(
    spentFraction: Float,
    expectedFraction: Float,
    percentLabel: String,
    contentDescription: String,
    modifier: Modifier = Modifier,
    fillColor: Color = MaterialTheme.extended.gaugeFillDark,
    diameter: Dp = 248.dp,
    strokeWidth: Dp = 14.dp,
    showCenterText: Boolean = true,
    caption: String? = null
) {
    val ext = MaterialTheme.extended
    val motion = LocalMotion.current
    val boundedSpent = spentFraction.coerceIn(0f, 1f)
    val boundedExpected = expectedFraction.coerceIn(0f, 1f)
    val progress = remember { Animatable(if (motion.enabled) 0f else boundedSpent) }

    LaunchedEffect(boundedSpent, motion.enabled) {
        if (motion.enabled) {
            progress.animateTo(
                targetValue = boundedSpent,
                animationSpec = tween(
                    durationMillis = motion.ms(600),
                    easing = MotionEasing.EmphasizedDecelerate
                )
            )
        } else {
            progress.snapTo(boundedSpent)
        }
    }

    val height = diameter / 2 + strokeWidth / 2 + 2.dp
    val textSize: TextUnit = if (diameter < 180.dp) 26.sp else 44.sp
    val supSize: TextUnit = if (diameter < 180.dp) 12.sp else 18.sp

    Box(
        modifier = modifier
            .width(diameter)
            .height(height)
            .clearAndSetSemantics { this.contentDescription = contentDescription },
        contentAlignment = Alignment.BottomCenter
    ) {
        Canvas(modifier = Modifier.width(diameter).height(height)) {
            val stroke = strokeWidth.toPx()
            val radius = (size.width - stroke) / 2f
            val center = Offset(size.width / 2f, radius + stroke / 2f)
            val topLeft = Offset(center.x - radius, center.y - radius)
            val arcSize = Size(radius * 2f, radius * 2f)

            drawArc(
                color = Color.White.copy(alpha = 0.15f),
                startAngle = 180f,
                sweepAngle = 180f,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = stroke, cap = StrokeCap.Round)
            )
            if (progress.value > 0f) {
                drawArc(
                    color = fillColor,
                    startAngle = 180f,
                    sweepAngle = 180f * progress.value,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(width = stroke, cap = StrokeCap.Round)
                )
            }

            val angle = PI * (1.0 - boundedExpected)
            val cosAngle = cos(angle).toFloat()
            val sinAngle = sin(angle).toFloat()
            val inner = radius - stroke / 2f - 5.dp.toPx()
            val outer = radius + stroke / 2f + 5.dp.toPx()
            drawLine(
                color = Color.White,
                start = Offset(center.x + inner * cosAngle, center.y - inner * sinAngle),
                end = Offset(center.x + outer * cosAngle, center.y - outer * sinAngle),
                strokeWidth = 3.dp.toPx(),
                cap = StrokeCap.Round
            )
        }

        if (showCenterText) {
            Column(
                modifier = Modifier.padding(bottom = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = buildAnnotatedString {
                        append(percentLabel)
                        withStyle(
                            SpanStyle(
                                fontSize = supSize,
                                baselineShift = BaselineShift.Superscript,
                                fontWeight = FontWeight.ExtraBold
                            )
                        ) { append("%") }
                    },
                    style = CampusNumeralHero.copy(fontSize = textSize),
                    color = ext.onInk,
                    maxLines = 1
                )
                if (caption != null) {
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = caption,
                        style = MaterialTheme.typography.labelSmall,
                        color = ext.onInk.copy(alpha = 0.72f),
                        maxLines = 1
                    )
                }
            }
        }
    }
}

/** Etiqueta de porcentaje con coma decimal y un decimal (p. ej. 68,5). */
fun formatGaugePercent(fraction: Float): String =
    String.format(java.util.Locale.getDefault(), "%.1f", fraction * 100f).replace('.', ',')
