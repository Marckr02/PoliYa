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

/*
 * v10 C1: los radios que faltaban tienen ahora nombre semántico. La regla es
 * que ningún componente escriba un número suelto: si un radio no existe, se
 * añade aquí con el nombre de lo que forma.
 */

/** Barra de color (categoría, presupuesto): 2 dp. */
val ShapeBar = RoundedCornerShape(2.dp)

/** Badge de icono cuadrado (inicial de módulo, inicial de nota): 14 dp. */
val ShapeBadge = RoundedCornerShape(14.dp)

/** Fila de una hoja inferior (opción de menú, opción de navegación): 14 dp. */
val ShapeSheetOption = RoundedCornerShape(14.dp)

/** Tile de icono grande (44 dp) y fichas de selección grandes: 16 dp. */
val ShapeIconTile = RoundedCornerShape(16.dp)

/** Panel destacado dentro de una tarjeta (gráfico de ritmo): 24 dp. */
val ShapePanel = RoundedCornerShape(24.dp)

/** Tarjeta pequeña de leyenda (gráfico de ritmo): 18 dp. */
val ShapeLegend = RoundedCornerShape(18.dp)

/** Tarjeta ilustrada de estado vacío: 32 dp. */
val ShapeBlob = RoundedCornerShape(32.dp)

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
