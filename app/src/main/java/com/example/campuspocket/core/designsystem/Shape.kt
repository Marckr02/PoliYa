package com.example.campuspocket.core.designsystem

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/**
 * Escala de formas del rediseño editorial v8.
 *
 * Los tokens con nombre son la fuente de verdad para los componentes nuevos;
 * `CampusShapes` los mapea a los roles de Material según el uso real de la app:
 * - `extraSmall`: chips y etiquetas → 10 dp.
 * - `small`: campos y controles → 7 dp.
 * - `medium` / `large`: tarjetas → 12 dp.
 * - `extraLarge`: héroes, hojas y diálogos → 16 dp.
 */
val ShapeControl = RoundedCornerShape(7.dp)
val ShapeCard = RoundedCornerShape(12.dp)
val ShapeHero = RoundedCornerShape(16.dp)
val ShapeChip = RoundedCornerShape(10.dp)
val ShapeRoundButton = RoundedCornerShape(8.dp)
val ShapeDock = RoundedCornerShape(18.dp)
val ShapeBottomSheet = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
val ShapeSnackbar = RoundedCornerShape(12.dp)
val ShapeTaskCheckbox = RoundedCornerShape(5.dp)
val ShapeImpactCard = RoundedCornerShape(6.dp)
val ShapeNoteSheet = RoundedCornerShape(4.dp)

val CampusShapes = Shapes(
    extraSmall = ShapeChip,
    small = ShapeControl,
    medium = ShapeCard,
    large = ShapeCard,
    extraLarge = ShapeHero
)

/** Radio máximo: píldoras de cualquier altura (botones circulares y dock previo). */
val PillShape = RoundedCornerShape(percent = 50)

/** Hoja de notas: papel con esquinas casi rectas (4 dp). */
val NoteShape = ShapeNoteSheet

/** Tarjetas pequeñas tintadas: mismo radio de tarjeta que el resto (12 dp). */
val SmallCardShape = ShapeCard
