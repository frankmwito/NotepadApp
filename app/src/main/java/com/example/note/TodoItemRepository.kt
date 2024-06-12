package com.example.note

import androidx.lifecycle.LiveData

interface TodoItemRepository {

    fun getAllTodoItems(): LiveData<List<TodoItem>>

    suspend fun insert(todoItem: TodoItem)

    suspend fun update(todoItem: TodoItem)

    suspend fun delete(todoItem: TodoItem)
    enum class SortOrder { ASC, DESC }

    suspend fun searchTodoItems(query: String): List<TodoItem>

    suspend fun sortTodoItems(sortBy: String, sortOrder: SortOrder): List<TodoItem>
}

