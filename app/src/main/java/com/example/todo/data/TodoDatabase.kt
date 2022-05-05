package com.example.todo.data

import android.content.Context
import androidx.room.*
import androidx.room.migration.AutoMigrationSpec

@Database(
    version = 4,
    entities = [TodoItem::class],
    autoMigrations = [
        AutoMigration(
            from = 3,
            to = 4,
            spec = TodoDatabase.DeleteColumnDataDeleteMigration::class
        )
    ]
)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao

    @DeleteColumn(tableName = "TodoItem", columnName = "dataDelete")
    class DeleteColumnDataDeleteMigration : AutoMigrationSpec

    companion object {
        private var instance: TodoDatabase? = null

        @Synchronized
        fun getInstance(context: Context): TodoDatabase {
            instance?.let {
                return it
            }
            return Room.databaseBuilder(
                context.applicationContext,
                TodoDatabase::class.java,
                "todo_database"
            ).build().apply { instance = this }
        }

    }
}