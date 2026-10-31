package com.example.campuspocket.core.designsystem

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.campuspocket.R

/** Manrope (OFL) empaquetada en res/font; escala con el ajuste del sistema. */
val CampusFontFamily = FontFamily(
    Font(R.font.manrope_medium, FontWeight.Medium),
    Font(R.font.manrope_semibold, FontWeight.SemiBold),
    Font(R.font.manrope_bold, FontWeight.Bold),
    Font(R.font.manrope_extrabold, FontWeight.ExtraBold)
)

private fun style(
    size: Int,
    lineHeight: Int,
    weight: FontWeight,
    letterSpacing: Float = 0f
) = TextStyle(
    fontFamily = CampusFontFamily,
    fontWeight = weight,
    fontSize = size.sp,
    lineHeight = lineHeight.sp,
    letterSpacing = letterSpacing.sp,
    // Manrope incluye tnum: montos, fechas y contadores no bailan al cambiar.
    fontFeatureSettings = "tnum"
)

/**
 * Jerarquía editorial v8:
 * - Hero 44 / Display 32 / Title 22 con tracking negativo.
 * - Onboarding 34; cuerpo 14 con interlineado 1,55 para lectura cómoda.
 * - Labels de 14 en peso 800 para botones; caption mínimo de 11.
 */
val CampusTypography = Typography(
    displayLarge = style(44, 48, FontWeight.ExtraBold, -2.42f),
    displayMedium = style(34, 36, FontWeight.ExtraBold, -0.7f),
    displaySmall = style(32, 33, FontWeight.ExtraBold, -1.76f),
    headlineLarge = style(32, 33, FontWeight.ExtraBold, -1.76f),
    headlineMedium = style(22, 24, FontWeight.ExtraBold, -0.44f),
    headlineSmall = style(22, 24, FontWeight.ExtraBold, -0.44f),
    titleLarge = style(22, 24, FontWeight.ExtraBold, -0.44f),
    titleMedium = style(16, 21, FontWeight.Bold, -0.1f),
    titleSmall = style(14, 20, FontWeight.Bold),
    bodyLarge = style(14, 22, FontWeight.Medium),
    bodyMedium = style(14, 22, FontWeight.Medium),
    bodySmall = style(13, 20, FontWeight.Medium),
    labelLarge = style(14, 17, FontWeight.ExtraBold),
    labelMedium = style(12, 16, FontWeight.Bold, 0.2f),
    labelSmall = style(11, 15, FontWeight.SemiBold, 0.4f)
)

/**
 * Estilos especiales fuera de la escala Material: cifras hero, numerales de
 * tarjeta, sobrelíneas, teclado y papel de notas. Evitan tamaños y alturas de
 * línea sueltos en las pantallas y escalan con el ajuste de fuente del sistema.
 */
val CampusNumeralHero = style(44, 48, FontWeight.ExtraBold, -2.42f)
val CampusNumeral = style(32, 36, FontWeight.ExtraBold, -1.76f)
val CampusNumeralSmall = style(24, 29, FontWeight.ExtraBold, -0.6f)
val CampusOverline = style(11, 16, FontWeight.ExtraBold, 0.88f)
val CampusKeypadLabel = style(18, 22, FontWeight.ExtraBold, -0.2f)
val CampusNoteTitle = style(28, 34, FontWeight.ExtraBold, -0.7f)
val CampusNoteHeading = style(18, 24, FontWeight.Bold, -0.2f)
val CampusNoteBody = style(14, 23, FontWeight.Medium)
