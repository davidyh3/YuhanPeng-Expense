package com.android.expense.sqlite.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.android.expense.entity.ExpenseDetail


@Dao
interface ExpenseDetailDao {

    @Query("select * from expense_detail where category_id = :categoryId")
    fun queryExpenseListByCategoryId(categoryId: Long): List<ExpenseDetail>

    @Query("select * from expense_detail where category_id = :categoryId and date =:date")
    fun queryExpenseListByCategoryIdWithDate(categoryId: Long, date: String): List<ExpenseDetail>

    @Insert
    fun insert(expenseDetail: ExpenseDetail): Long

    @Update
    fun update(expenseDetail: ExpenseDetail): Int
}