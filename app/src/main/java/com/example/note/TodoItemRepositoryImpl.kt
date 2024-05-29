package com.example.note

import android.util.Log
import androidx.lifecycle.LiveData
import java.time.LocalDateTime

class TodoItemRepositoryImpl(private val todoItemDao: TodoItemDao) : TodoItemRepository {

    override suspend fun insert(todoItem: TodoItem) {
        Log.d("TodoItemRepositoryImpl", "Inserting item: $todoItem")
        todoItemDao.insert(todoItem)
    }

    override suspend fun update(todoItem: TodoItem) {
        Log.d("TodoItemRepositoryImpl", "Updating item: $todoItem")
        todoItemDao.update(todoItem)
    }

    override suspend fun delete(todoItem: TodoItem) {
        Log.d("TodoItemRepositoryImpl", "Deleting item: $todoItem")
        todoItemDao.delete(todoItem)
    }

    override fun getAllTodoItems(): LiveData<List<TodoItem>> {
        val items = todoItemDao.getAllTodoItems()
        Log.d("TodoItemRepositoryImpl", "Retrieved all items")
        return items
    }

    override fun getCompletedTodoItems(): LiveData<List<TodoItem>> {
        val items = todoItemDao.getCompletedTodoItems()
        Log.d("TodoItemRepositoryImpl", "Retrieved completed items")
        return items
    }

    override fun getOverdueTodoItems(): LiveData<List<TodoItem>> {
        val now = LocalDateTime.now()
        val items = todoItemDao.getOverdueTodoItems(now)
        Log.d("TodoItemRepositoryImpl", "Retrieved overdue items")
        return items
    }

    override fun getNoDateTodoItems(): LiveData<List<TodoItem>> {
        val items = todoItemDao.getNoDateTodoItems()
        Log.d("TodoItemRepositoryImpl", "Retrieved no date items")
        return items
    }
}
