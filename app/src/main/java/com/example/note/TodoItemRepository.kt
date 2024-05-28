package com.example.note

interface TodoItemRepository {
    suspend fun insert(todoItem: TodoItem)
    suspend fun update(todoItem: TodoItem)
    suspend fun delete(todoItem: TodoItem)
    suspend fun getAllTodoItems(): List<TodoItem>
}