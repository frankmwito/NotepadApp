package com.example.note

class TodoItemRepositoryImpl(private val todoItemDao: TodoItemDao) : TodoItemRepository {
    override suspend fun insert(todoItem: TodoItem) {
        todoItemDao.insert(todoItem)
    }

    override suspend fun update(todoItem: TodoItem) {
        todoItemDao.update(todoItem)
    }

    override suspend fun delete(todoItem: TodoItem) {
        todoItemDao.delete(todoItem)
    }

    override suspend fun getAllTodoItems(): List<TodoItem> {
        return todoItemDao.getAllTodoItems().value ?: emptyList()
    }
}