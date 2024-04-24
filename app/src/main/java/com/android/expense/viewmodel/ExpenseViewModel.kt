package com.android.expense.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.android.expense.entity.ExpenseCategory
import com.android.expense.entity.ExpenseDetail
import com.android.expense.sqlite.ExpenseRoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ExpenseViewModel(private val application: Application) : AndroidViewModel(application) {

    fun loadAllCategory(action: (List<ExpenseCategory>) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val list = ExpenseRoomDatabase.getRoomDatabase(application).categoryDao().queryAllCategory()
            withContext(Dispatchers.Main) {
                action.invoke(list)
            }
        }
    }

    fun queryCategory(categoryId: Long, action: (ExpenseCategory) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val  expenseCategory = ExpenseRoomDatabase.getRoomDatabase(application).categoryDao().queryCategoryById(categoryId)
            withContext(Dispatchers.Main) {
                action.invoke(expenseCategory)
            }
        }
    }

    fun addExpenseDetail(expenseDetail: ExpenseDetail, action: (Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = ExpenseRoomDatabase.getRoomDatabase(application).expenseDetailDao().insert(expenseDetail)
            withContext(Dispatchers.Main) {
                action.invoke(result > 0)
            }
        }
    }

    fun updateExpenseDetail(expenseDetail: ExpenseDetail, action: (Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = ExpenseRoomDatabase.getRoomDatabase(application).expenseDetailDao().update(expenseDetail)
            withContext(Dispatchers.Main) {
                action.invoke(result > 0)
            }
        }
    }
}