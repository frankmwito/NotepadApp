package com.example.note

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Note::class, TodoItem::class], version = 2, exportSchema = true)
@TypeConverters(UriConverter::class, Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
    abstract fun todoItemDao(): TodoItemDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }

        fun provideNoteRepository(context: Context): NoteRepository {
            val database = getInstance(context)
            return NoteRepositoryImpl(database.noteDao())
        }

        fun provideTodoItemRepository(context: Context): TodoItemRepository {
            val database = getInstance(context)
            return TodoItemRepositoryImpl(database.todoItemDao())
        }
    }
}

