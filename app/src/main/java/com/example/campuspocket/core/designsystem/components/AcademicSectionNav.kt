package com.example.campuspocket.core.designsystem.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.campuspocket.R

/** Secciones de la pestaña Académico, en el orden de la sub-navegación v8. */
enum class AcademicSection(val labelRes: Int) {
    TODAY(R.string.academic_today),
    WEEK(R.string.academic_week),
    COURSES(R.string.academic_courses),
    TASKS(R.string.academic_tasks)
}

/**
 * Sub-navegación de Académico (v8 §5): pestañas subrayadas Hoy / Semana /
 * Materias / Tareas. Vive solo en Semana y Materias (decisión D3); Hoy
 * conserva sus acciones de cabecera y Tareas su botón de volver.
 */
@Composable
fun AcademicSectionNav(
    selected: AcademicSection,
    onSelect: (AcademicSection) -> Unit,
    modifier: Modifier = Modifier
) {
    UnderlinedTabRow(
        options = AcademicSection.entries,
        selected = selected,
        onSelect = onSelect,
        label = { stringResource(it.labelRes) },
        modifier = modifier
    )
}
