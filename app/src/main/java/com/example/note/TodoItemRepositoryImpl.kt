package com.example.note

import android.util.Log
import androidx.lifecycle.LiveData
import com.google.android.gms.drive.query.SortOrder
import java.time.LocalDateTime


class TodoItemRepositoryImpl(private val todoItemDao: TodoItemDao) : TodoItemRepository {

    override fun getAllTodoItems(): LiveData<List<TodoItem>> {
        return todoItemDao.getAllTodoItems()
    }

    override suspend fun insert(todoItem: TodoItem) {
        todoItemDao.insert(todoItem)
    }

    override suspend fun update(todoItem: TodoItem) {
        todoItemDao.update(todoItem)
    }

    override suspend fun delete(todoItem: TodoItem) {
        todoItemDao.delete(todoItem)
    }

    override suspend fun searchTodoItems(query: String): List<TodoItem> {
        return todoItemDao.searchTodoItems(query)
    }

    override suspend fun sortTodoItems(sortBy: String, sortOrder: TodoItemRepository.SortOrder): List<TodoItem> {
        return when (sortBy) {
            "title" -> {
                if (sortOrder == TodoItemRepository.SortOrder.ASC) {
                    todoItemDao.sortTodoItemsByTitleAsc()
                } else {
                    todoItemDao.sortTodoItemsByTitleDesc()
                }
            }
            "date" -> {
                if (sortOrder == TodoItemRepository.SortOrder.ASC) {
                    todoItemDao.sortTodoItemsByDateAsc()
                } else {
                    todoItemDao.sortTodoItemsByDateDesc()
                }
            }
            else -> throw IllegalArgumentException("Invalid sort by field")
        }
    }
}
