package com.example.note

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class TodoListViewModel(application: Application) : AndroidViewModel(application) {
    private var todoItemRepository: TodoItemRepository =
        AppDatabase.provideTodoItemRepository(application)
    val allTodoItems: LiveData<List<TodoItem>>
    val completedTodoItems: LiveData<List<TodoItem>>
    val overdueTodoItems: LiveData<List<TodoItem>>
    val noDateTodoItems: LiveData<List<TodoItem>>

    init {
        val todoItemDao = AppDatabase.getInstance(application).todoitemDao()
        todoItemRepository = TodoItemRepositoryImpl(todoItemDao)
        allTodoItems = todoItemRepository.getAllTodoItems()
        completedTodoItems = todoItemRepository.getCompletedTodoItems()
        overdueTodoItems = todoItemRepository.getOverdueTodoItems(LocalDateTime.now())
        noDateTodoItems = todoItemRepository.getNoDateTodoItems()
    }

    fun insert(todoItem: TodoItem) = viewModelScope.launch(Dispatchers.IO) {
        todoItemRepository.insert(todoItem)
        scheduleNotification(getApplication(), todoItem)
    }

    fun update(todoItem: TodoItem) = viewModelScope.launch(Dispatchers.IO) {
        todoItemRepository.update(todoItem)
    }

    fun delete(todoItem: TodoItem) = viewModelScope.launch(Dispatchers.IO) {
        todoItemRepository.delete(todoItem)
    }
}
/**
    fun getTodoItemById(id: Int): LiveData<TodoItem?> = liveData {
        emit(repository.getTodoItemById(id))
    }
}**/
