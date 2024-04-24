package com.android.expense.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "expense_category")
data class ExpenseCategory(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
) {
    @ColumnInfo(name = "name")
    var name:String=""
}