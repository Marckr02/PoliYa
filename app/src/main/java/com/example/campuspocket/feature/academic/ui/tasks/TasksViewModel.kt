package com.example.campuspocket.feature.academic.ui.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campuspocket.core.di.AppScope
import com.example.campuspocket.core.designsystem.components.TaskFilter
import com.example.campuspocket.feature.academic.domain.model.Course
import com.example.campuspocket.feature.academic.domain.model.Task
import com.example.campuspocket.feature.academic.domain.repository.CourseRepository
import com.example.campuspocket.feature.academic.domain.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

/**
 * Lista de tareas del semestre: pendientes/completadas, filtrables por materia y
 * ordenadas por fecha de entrega. El borrado es diferido (5 s) para permitir deshacer.
 */
data class TasksUiState(
    val filter: TaskFilter = TaskFilter.ALL,
    val tasks: List<Task> = emptyList(),
    val courses: List<Course> = emptyList(),
    val isLoading: Boolean = true
)

sealed interface TasksEvent {
    data class Deleted(val task: Task) : TasksEvent
    data class Completed(val task: Task, val nextOccurrenceId: Long?) : TasksEvent
}

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class TasksViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val courseRepository: CourseRepository,
    private val appScope: AppScope
) : ViewModel() {

    // FIX-01: "Todas" reúne pendientes y completadas; el filtro se aplica en el estado.
    private val filter = MutableStateFlow(TaskFilter.ALL)
    private val selectedCourseId = MutableStateFlow<Long?>(null)
    private val hiddenTaskIds = MutableStateFlow<Set<Long>>(emptySet())

    private val allTasksFlow = combine(
        taskRepository.observePending(),
        taskRepository.observeCompleted()
    ) { pending, completed -> pending + completed }

    val uiState: StateFlow<TasksUiState> = combine(
        allTasksFlow,
        courseRepository.observeCourses(),
        filter,
        selectedCourseId,
        hiddenTaskIds
    ) { tasks, courses, selectedFilter, courseId, hiddenIds ->
        TasksUiState(
            filter = selectedFilter,
            courses = courses,
            tasks = filterTasks(
                tasks = tasks.filterNot { task -> task.id != null && task.id in hiddenIds },
                filter = selectedFilter,
                courseId = courseId
            ).sortedBy { it.dueAt },
            isLoading = false
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        TasksUiState()
    )

    private val _events = MutableSharedFlow<TasksEvent>(extraBufferCapacity = 8)
    val events: SharedFlow<TasksEvent> = _events.asSharedFlow()

    private var pendingDeletion: Task? = null
    private var pendingDeletionJob: Job? = null

    fun setFilter(value: TaskFilter) {
        filter.value = value
    }

    fun setCourseFilter(courseId: Long?) {
        selectedCourseId.value = courseId
    }

    /** Completar cancela sus alarmas y crea la siguiente ocurrencia si se repite. */
    fun setCompleted(task: Task, completed: Boolean) {
        if (!completed) {
            appScope.scope.launch {
                taskRepository.undoCompletion(task, nextOccurrenceId = null)
            }
            return
        }

        appScope.scope.launch {
            // La tarjeta muestra el check y el tachado (~200 ms) antes de moverse.
            delay(360)
            val nextId = taskRepository.complete(task)
            if (isTodayOrOverdue(task)) {
                _events.emit(TasksEvent.Completed(task, nextId))
            }
        }
    }

    fun undoCompletion(task: Task, nextOccurrenceId: Long?) {
        appScope.scope.launch {
            taskRepository.undoCompletion(task, nextOccurrenceId)
        }
    }

    /** Oculta la tarea de inmediato y confirma el borrado si no se deshace en 5 s. */
    fun delete(task: Task) {
        val taskId = task.id ?: return
        pendingDeletionJob?.cancel()
        pendingDeletion = task
        hiddenTaskIds.update { it + taskId }
        _events.tryEmit(TasksEvent.Deleted(task))

        pendingDeletionJob = appScope.scope.launch {
            delay(5_000)
            pendingDeletion = null
            taskRepository.delete(taskId)
            hiddenTaskIds.update { it - taskId }
        }
    }

    fun undoDelete() {
        val task = pendingDeletion ?: return
        pendingDeletionJob?.cancel()
        pendingDeletionJob = null
        pendingDeletion = null
        task.id?.let { id -> hiddenTaskIds.update { it - id } }
    }

    /** Confirma el borrado pendiente de inmediato (al descartar el snackbar). */
    fun commitPendingDeletion() {
        val task = pendingDeletion ?: return
        pendingDeletionJob?.cancel()
        pendingDeletionJob = null
        pendingDeletion = null
        appScope.scope.launch {
            task.id?.let { id ->
                taskRepository.delete(id)
                hiddenTaskIds.update { it - id }
            }
        }
    }

    override fun onCleared() {
        // Si se sale de la pantalla, el borrado se confirma aunque la app siga viva.
        val task = pendingDeletion
        if (task?.id != null) {
            appScope.scope.launch { taskRepository.delete(task.id) }
            pendingDeletion = null
        }
        super.onCleared()
    }

    private fun isTodayOrOverdue(task: Task): Boolean {
        val zone = ZoneId.systemDefault()
        val dueDate = task.dueAt.atZone(zone).toLocalDate()
        return !dueDate.isAfter(LocalDate.now(zone))
    }
}

/**
 * Semántica de filtros (FIX-01): "Todas" incluye pendientes y completadas;
 * "Completadas" solo completadas; "Hoy", "Esta semana" y "Por materia" solo
 * pendientes del rango. El filtro por materia lo refina la UI con su selector.
 */
internal fun filterTasks(
    tasks: List<Task>,
    filter: TaskFilter,
    today: LocalDate = LocalDate.now(),
    courseId: Long? = null
): List<Task> {
    val base = if (courseId != null) tasks.filter { it.courseId == courseId } else tasks
    return when (filter) {
        TaskFilter.ALL -> base
        TaskFilter.COMPLETED -> base.filter { it.completedAt != null }
        TaskFilter.TODAY -> base.filter {
            it.completedAt == null &&
                it.dueAt.atZone(ZoneId.systemDefault()).toLocalDate() == today
        }
        TaskFilter.THIS_WEEK -> base.filter {
            val date = it.dueAt.atZone(ZoneId.systemDefault()).toLocalDate()
            it.completedAt == null && !date.isBefore(today) && !date.isAfter(today.plusDays(6))
        }
        TaskFilter.BY_COURSE -> base.filter { it.completedAt == null }
    }
}
