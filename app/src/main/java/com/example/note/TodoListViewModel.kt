package com.example.note

import android.app.Application
import android.util.Log
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

    private val _allTodoItems = MutableLiveData<List<TodoItem>>()
    val allTodoItems: LiveData<List<TodoItem>> get() = _allTodoItems

    private val _completedTodoItems = MutableLiveData<List<TodoItem>>()
    val completedTodoItems: LiveData<List<TodoItem>> get() = _completedTodoItems

    private val _overdueTodoItems = MutableLiveData<List<TodoItem>>()
    val overdueTodoItems: LiveData<List<TodoItem>> get() = _overdueTodoItems

    private val _noDateTodoItems = MutableLiveData<List<TodoItem>>()
    val noDateTodoItems: LiveData<List<TodoItem>> get() = _noDateTodoItems

    init {
        loadAllTodoItems()
    }

    private fun loadAllTodoItems() {
        viewModelScope.launch(Dispatchers.Main) {
            todoItemRepository.getAllTodoItems().observeForever { items ->
                Log.d("TodoListViewModel", "Loaded items: $items")

                _allTodoItems.postValue(items)
                _completedTodoItems.postValue(items.filter { it.completed })
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
}
