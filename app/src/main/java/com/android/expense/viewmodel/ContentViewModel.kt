package com.android.expense.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.android.expense.entity.ExpenseDetail
import com.android.expense.sqlite.ExpenseRoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ContentViewModel(private val application: Application) : AndroidViewModel(application) {

    fun loadExpenseList(categoryId: Long, date: String, action: (List<ExpenseDetail>) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val list = if(date.isEmpty()){
                ExpenseRoomDatabase.getRoomDatabase(application).expenseDetailDao().queryExpenseListByCategoryId(categoryId)
            }else{
                ExpenseRoomDatabase.getRoomDatabase(application).expenseDetailDao().queryExpenseListByCategoryIdWithDate(categoryId,date)
            }
            withContext(Dispatchers.Main) {
                action.invoke(list)
            }
        }
    }
}