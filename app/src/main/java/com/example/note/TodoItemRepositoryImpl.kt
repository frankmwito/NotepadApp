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
        return todoItemDao.searchTodoItems(query).value ?: emptyList()
    }

    override suspend fun sortTodoItems(
        sortBy: String,
        sortOrder: TodoItemRepository.SortOrder
    ): List<TodoItem> {
        return when (sortBy) {
            "title" -> {
                if (sortOrder == TodoItemRepository.SortOrder.ASC) {
                    todoItemDao.sortTodoItemsByTitleAsc().value ?: emptyList()
                } else {
                    todoItemDao.sortTodoItemsByTitleDesc().value ?: emptyList()
                }
            }
            "date" -> {
                if (sortOrder == TodoItemRepository.SortOrder.DESC) {
                    todoItemDao.sortTodoItemsByDateAsc().value ?: emptyList()
                } else {
                    todoItemDao.sortTodoItemsByDateDesc().value ?: emptyList()
                }
            }
            else -> throw IllegalArgumentException("Invalid sort by field")
        }
    }
}
