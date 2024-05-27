package com.example.note

import androidx.lifecycle.LiveData
import java.time.LocalDateTime

interface TodoItemRepository {
    suspend fun insert(todoItem: TodoItem)
    suspend fun update(todoItem: TodoItem)
    suspend fun delete(todoItem: TodoItem)
    suspend fun getTodoItemById(id: Int): TodoItem?
    fun getAllTodoItems(): LiveData<List<TodoItem>>
    fun getCompletedTodoItems(): LiveData<List<TodoItem>>
    fun getOverdueTodoItems(now: LocalDateTime): LiveData<List<TodoItem>>
    fun getNoDateTodoItems(): LiveData<List<TodoItem>>
}