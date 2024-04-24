package com.android.expense.sqlite

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.android.expense.entity.ExpenseCategory
import com.android.expense.entity.ExpenseDetail
import com.android.expense.sqlite.dao.CategoryDao
import com.android.expense.sqlite.dao.ExpenseDetailDao


@Database(
    entities = [ExpenseCategory::class, ExpenseDetail::class],
    version = 1,
    exportSchema = false
)
abstract class ExpenseRoomDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun expenseDetailDao(): ExpenseDetailDao

    companion object {
        private const val DB_NAME = "expense_system"

        @Volatile
        private var INSTANCE: ExpenseRoomDatabase? = null
        fun getRoomDatabase(context: Context): ExpenseRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ExpenseRoomDatabase::class.java,
                    DB_NAME
                )
                    .createFromAsset("database/expense_system.db")
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

}