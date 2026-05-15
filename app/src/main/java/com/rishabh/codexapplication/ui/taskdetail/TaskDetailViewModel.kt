package com.rishabh.codexapplication.ui.taskdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rishabh.codexapplication.domain.model.Todo
import com.rishabh.codexapplication.domain.repository.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TaskDetailUiState(
    val id: Long = 0,
    val title: String = "",
    val description: String = "",
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val titleError: String? = null,
    val isEditMode: Boolean = false
) {
    val canSave: Boolean
        get() = title.isNotBlank()
}

@HiltViewModel
class TaskDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val todoRepository: TodoRepository
) : ViewModel() {
    private val taskId: Long = savedStateHandle["taskId"] ?: 0L

    private val _uiState = MutableStateFlow(TaskDetailUiState(isEditMode = taskId != 0L))
    val uiState: StateFlow<TaskDetailUiState> = _uiState.asStateFlow()

    private val navigationEvents = Channel<Unit>(Channel.BUFFERED)
    val navigateBackEvents = navigationEvents.receiveAsFlow()

    init {
        if (taskId != 0L) {
            viewModelScope.launch {
                todoRepository.observeTodo(taskId).collect { todo ->
                    if (todo != null) {
                        _uiState.value = TaskDetailUiState(
                            id = todo.id,
                            title = todo.title,
                            description = todo.description,
                            isCompleted = todo.isCompleted,
                            createdAt = todo.createdAt,
                            isEditMode = true
                        )
                    }
                }
            }
        }
    }

    fun onTitleChanged(title: String) {
        _uiState.update {
            it.copy(
                title = title,
                titleError = if (title.isBlank()) "Title is required" else null
            )
        }
    }

    fun onDescriptionChanged(description: String) {
        _uiState.update { it.copy(description = description) }
    }

    fun onCompletedChanged(isCompleted: Boolean) {
        _uiState.update { it.copy(isCompleted = isCompleted) }
    }

    fun saveTask() {
        val state = _uiState.value
        if (state.title.isBlank()) {
            _uiState.update { it.copy(titleError = "Title is required") }
            return
        }

        viewModelScope.launch {
            todoRepository.saveTodo(
                Todo(
                    id = state.id,
                    title = state.title.trim(),
                    description = state.description.trim(),
                    isCompleted = state.isCompleted,
                    createdAt = state.createdAt
                )
            )
            navigationEvents.send(Unit)
        }
    }

    fun deleteTask() {
        val id = _uiState.value.id
        if (id == 0L) return

        viewModelScope.launch {
            todoRepository.deleteTodo(id)
            navigationEvents.send(Unit)
        }
    }
}
