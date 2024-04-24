package com.android.expense.sqlite.dao

import androidx.room.Dao
import androidx.room.Query
import com.android.expense.entity.ExpenseCategory


@Dao
interface CategoryDao {

    @Query("select * from expense_category")
    fun queryAllCategory(): List<ExpenseCategory>

    @Query("select * from expense_category where id =:id")
    fun queryCategoryById(id: Long): ExpenseCategory
}