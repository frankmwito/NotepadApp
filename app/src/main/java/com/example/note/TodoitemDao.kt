package com.example.note

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import java.time.LocalDateTime

@Dao
interface TodoItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(todoItem: TodoItem)

    @Update
    suspend fun update(todoItem: TodoItem)

    @Delete
    suspend fun delete(todoItem: TodoItem)

    @Query("SELECT * FROM todoitems WHERE id = :id")
    suspend fun getTodoItemById(id: Int): TodoItem?

    @Query("SELECT * FROM todoitems")
    fun getAllTodoItems(): LiveData<List<TodoItem>>

    @Query("SELECT * FROM todoitems WHERE completed = 1")
    fun getAchievedTodoItems(): LiveData<List<TodoItem>>

    @Query("SELECT * FROM todoitems WHERE alertTime < :now AND completed = 0")
    fun getOverdueTodoItems(now: LocalDateTime): LiveData<List<TodoItem>>

    @Query("SELECT * FROM todoitems WHERE alertTime IS NULL AND completed = 0")
    fun getNoDateTodoItems(): LiveData<List<TodoItem>>

    @Query("SELECT * FROM todoitems WHERE title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%'")
    suspend fun searchTodoItems(query: String): List<TodoItem> // Change to List<TodoItem>

    @Query("SELECT * FROM todoitems ORDER BY title ASC")
    suspend fun sortTodoItemsByTitleAsc(): List<TodoItem> // Change to List<TodoItem>

    @Query("SELECT * FROM todoitems ORDER BY title DESC")
    suspend fun sortTodoItemsByTitleDesc(): List<TodoItem> // Change to List<TodoItem>

    @Query("SELECT * FROM todoitems ORDER BY alertTime ASC")
    suspend fun sortTodoItemsByDateAsc(): List<TodoItem> // Change to List<TodoItem>

    @Query("SELECT * FROM todoitems ORDER BY alertTime DESC")
    suspend fun sortTodoItemsByDateDesc(): List<TodoItem> // Change to List<TodoItem>
}

