package com.rishabh.codexapplication.data.repository

import com.rishabh.codexapplication.data.local.TodoDao
import com.rishabh.codexapplication.data.local.TodoEntity
import com.rishabh.codexapplication.domain.model.Todo
import com.rishabh.codexapplication.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TodoRepositoryImpl @Inject constructor(
    private val todoDao: TodoDao
) : TodoRepository {
    override fun observeTodos(): Flow<List<Todo>> {
        return todoDao.observeTodos().map { todos -> todos.map(TodoEntity::toDomain) }
    }

    override fun observeTodo(id: Long): Flow<Todo?> {
        return todoDao.observeTodo(id).map { it?.toDomain() }
    }

    override suspend fun saveTodo(todo: Todo): Long {
        return todoDao.upsert(todo.toEntity())
    }

    override suspend fun setCompleted(id: Long, isCompleted: Boolean) {
        todoDao.setCompleted(id, isCompleted)
    }

    override suspend fun deleteTodo(id: Long) {
        todoDao.deleteTodo(id)
    }
}

private fun TodoEntity.toDomain(): Todo {
    return Todo(
        id = id,
        title = title,
        description = description,
        isCompleted = isCompleted,
        createdAt = createdAt
    )
}

private fun Todo.toEntity(): TodoEntity {
    return TodoEntity(
        id = id,
        title = title,
        description = description,
        isCompleted = isCompleted,
        createdAt = createdAt
    )
}
