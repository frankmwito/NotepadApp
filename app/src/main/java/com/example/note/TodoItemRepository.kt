package com.example.note

import androidx.lifecycle.LiveData

interface TodoItemRepository {
    suspend fun insert(todoItem: TodoItem)
    suspend fun update(todoItem: TodoItem)
    suspend fun delete(todoItem: TodoItem)
    fun getAllTodoItems(): LiveData<List<TodoItem>>
    fun getAchiviedTodoItems(): LiveData<List<TodoItem>>
    fun getOverdueTodoItems(): LiveData<List<TodoItem>>
    fun getNoDateTodoItems(): LiveData<List<TodoItem>>
}
