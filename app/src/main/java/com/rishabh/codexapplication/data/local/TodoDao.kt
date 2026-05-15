package com.rishabh.codexapplication.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {
    @Query("SELECT * FROM todos ORDER BY createdAt ASC")
    fun observeTodos(): Flow<List<TodoEntity>>

    @Query("SELECT * FROM todos WHERE id = :id")
    fun observeTodo(id: Long): Flow<TodoEntity?>

    @Query("SELECT COUNT(*) FROM todos")
    suspend fun countTodos(): Int

    @Upsert
    suspend fun upsert(todo: TodoEntity): Long

    @Query("UPDATE todos SET isCompleted = :isCompleted WHERE id = :id")
    suspend fun setCompleted(id: Long, isCompleted: Boolean)

    @Query("DELETE FROM todos WHERE id = :id")
    suspend fun deleteTodo(id: Long)
}
