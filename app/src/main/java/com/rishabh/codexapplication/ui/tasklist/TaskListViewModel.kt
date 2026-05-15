package com.rishabh.codexapplication.ui.tasklist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rishabh.codexapplication.domain.model.Todo
import com.rishabh.codexapplication.domain.repository.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TaskListUiState(
    val tasks: List<Todo> = emptyList(),
    val searchQuery: String = ""
) {
    val visibleTasks: List<Todo>
        get() = if (searchQuery.isBlank()) {
            tasks
        } else {
            tasks.filter { task ->
                task.title.contains(searchQuery, ignoreCase = true) ||
                    task.description.contains(searchQuery, ignoreCase = true)
            }
        }

    val openCount: Int
        get() = tasks.count { !it.isCompleted }

    val completedCount: Int
        get() = tasks.count { it.isCompleted }
}

@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val todoRepository: TodoRepository
) : ViewModel() {
    private val searchQuery = MutableStateFlow("")

    val uiState: StateFlow<TaskListUiState> = combine(
        todoRepository.observeTodos(),
        searchQuery
    ) { tasks, query ->
        TaskListUiState(tasks = tasks, searchQuery = query)
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = TaskListUiState()
        )

    init {
        viewModelScope.launch {
            todoRepository.seedInitialTasksIfEmpty()
        }
    }

    fun onSearchQueryChanged(query: String) {
        searchQuery.value = query
    }

    fun setCompleted(id: Long, isCompleted: Boolean) {
        viewModelScope.launch {
            todoRepository.setCompleted(id, isCompleted)
        }
    }
}
