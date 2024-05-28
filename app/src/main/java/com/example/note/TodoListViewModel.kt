package com.example.note

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class TodoListViewModel(application: Application) : AndroidViewModel(application) {
    private var todoItemRepository: TodoItemRepository =
        AppDatabase.provideTodoItemRepository(application)
    val allTodoItems: LiveData<List<TodoItem>> get() = _allTodoItems
    val completedTodoItems: LiveData<List<TodoItem>> get() = _completedTodoItems
    val overdueTodoItems: LiveData<List<TodoItem>> get() = _overdueTodoItems
    val noDateTodoItems: LiveData<List<TodoItem>> get() = _noDateTodoItems

    private val _allTodoItems = MutableLiveData<List<TodoItem>>()
    private val _completedTodoItems = MutableLiveData<List<TodoItem>>()
    private val _overdueTodoItems = MutableLiveData<List<TodoItem>>()
    private val _noDateTodoItems = MutableLiveData<List<TodoItem>>()

    init {
        loadAllTodoItems()
    }

    private fun loadAllTodoItems() = viewModelScope.launch {
        val items = todoItemRepository.getAllTodoItems()
        _allTodoItems.postValue(items.filter { it.alertTime?.isAfter(LocalDateTime.now()) ?: true && !it.completed })
        _completedTodoItems.postValue(items.filter { it.completed })
        _overdueTodoItems.postValue(items.filter { it.alertTime?.isBefore(LocalDateTime.now()) ?: false && !it.completed })
        _noDateTodoItems.postValue(items.filter { it.alertTime == null && !it.completed })
    }

    fun insert(todoItem: TodoItem) = viewModelScope.launch(Dispatchers.IO) {
        todoItemRepository.insert(todoItem)
        scheduleNotification(getApplication(), todoItem)
        loadAllTodoItems()
    }

    fun update(todoItem: TodoItem) = viewModelScope.launch(Dispatchers.IO) {
        todoItemRepository.update(todoItem)
        loadAllTodoItems()
    }

    fun delete(todoItem: TodoItem) = viewModelScope.launch(Dispatchers.IO) {
        todoItemRepository.delete(todoItem)
        loadAllTodoItems()
    }
}


/**
    fun getTodoItemById(id: Int): LiveData<TodoItem?> = liveData {
        emit(repository.getTodoItemById(id))
    }
}**/
