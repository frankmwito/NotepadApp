package com.example.note

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime



class TodoListViewModel(application: Application) : AndroidViewModel(application) {
    private val todoItemRepository: TodoItemRepository = AppDatabase.provideTodoItemRepository(application)

    private val _allTodoItems = MutableLiveData<List<TodoItem>>()
    val allTodoItems: LiveData<List<TodoItem>> get() = _allTodoItems

    private val _achievedTodoItems = MutableLiveData<List<TodoItem>>()
    val achievedTodoItems: LiveData<List<TodoItem>> get() = _achievedTodoItems

    private val _overdueTodoItems = MutableLiveData<List<TodoItem>>()
    val overdueTodoItems: LiveData<List<TodoItem>> get() = _overdueTodoItems

    private val _noDateTodoItems = MutableLiveData<List<TodoItem>>()
    val noDateTodoItems: LiveData<List<TodoItem>> get() = _noDateTodoItems

    private val _searchResults = MutableLiveData<List<TodoItem>>()
    val searchResults: LiveData<List<TodoItem>> get() = _searchResults

    init {
        loadAllTodoItems()
    }

    private fun loadAllTodoItems() {
        viewModelScope.launch(Dispatchers.Main) {
            todoItemRepository.getAllTodoItems().observeForever { items ->
                _allTodoItems.postValue(items)
                _achievedTodoItems.postValue(items.filter { it.completed })
                _overdueTodoItems.postValue(items.filter { it.alertTime?.isBefore(LocalDateTime.now()) == true && !it.completed })
                _noDateTodoItems.postValue(items.filter { it.alertTime == null && !it.completed })
            }
        }
    }

    fun insert(todoItem: TodoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            todoItemRepository.insert(todoItem)
            loadAllTodoItems()
        }
    }

    fun update(todoItem: TodoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            todoItemRepository.update(todoItem)
            loadAllTodoItems()
        }
    }

    fun delete(todoItem: TodoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            todoItemRepository.delete(todoItem)
            loadAllTodoItems()
        }
    }

    fun searchTodoItems(query: String) {
        viewModelScope.launch(Dispatchers.Main) {
            val items = withContext(Dispatchers.IO) { todoItemRepository.searchTodoItems(query) }
            _searchResults.postValue(items)
        }
    }

    fun sortTodoItems(sortBy: String, sortOrder: TodoItemRepository.SortOrder) {
        viewModelScope.launch(Dispatchers.Main) {
            val items = withContext(Dispatchers.IO) { todoItemRepository.sortTodoItems(sortBy, sortOrder) }
            _allTodoItems.postValue(items)
            _achievedTodoItems.postValue(items.filter { it.completed })
            _overdueTodoItems.postValue(items.filter { it.alertTime?.isBefore(LocalDateTime.now()) == true && !it.completed })
            _noDateTodoItems.postValue(items.filter { it.alertTime == null && !it.completed })
        }
    }
}
