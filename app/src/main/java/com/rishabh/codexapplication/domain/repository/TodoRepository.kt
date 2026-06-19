package com.rishabh.codexapplication.domain.repository

import com.rishabh.codexapplication.domain.model.Todo
import kotlinx.coroutines.flow.Flow

interface TodoRepository {
    fun observeTodos(): Flow<List<Todo>>
    fun observeTodo(id: Long): Flow<Todo?>
    suspend fun saveTodo(todo: Todo): Long
    suspend fun setCompleted(id: Long, isCompleted: Boolean)
    suspend fun deleteTodo(id: Long)
}
