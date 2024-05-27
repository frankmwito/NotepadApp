package com.example.note

import java.time.LocalDateTime

class TodoItemRepositoryImpl(private val todoItemDao: TodoItemDao) : TodoItemRepository {
    override suspend fun insert(todoItem: TodoItem) = todoItemDao.insert(todoItem)
    override suspend fun update(todoItem: TodoItem) = todoItemDao.update(todoItem)
    override suspend fun delete(todoItem: TodoItem) = todoItemDao.delete(todoItem)
    override suspend fun getTodoItemById(id: Int) = todoItemDao.getTodoItemById(id)
    override fun getAllTodoItems() = todoItemDao.getAllTodoItems()
    override fun getCompletedTodoItems() = todoItemDao.getCompletedTodoItems()
    override fun getOverdueTodoItems(now: LocalDateTime) = todoItemDao.getOverdueTodoItems(now)
    override fun getNoDateTodoItems() = todoItemDao.getNoDateTodoItems()
}