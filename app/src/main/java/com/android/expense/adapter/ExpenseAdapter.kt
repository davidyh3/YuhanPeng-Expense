package com.android.expense.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.expense.R
import com.android.expense.databinding.ItemExpenseBinding
import com.android.expense.entity.ExpenseDetail
import com.android.expense.ui.AddAndEditExpenseActivity

class ExpenseAdapter : RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>() {

    private val showData = mutableListOf<ExpenseDetail>()

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: List<ExpenseDetail>) {
        showData.clear()
        showData.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val binding = ItemExpenseBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ExpenseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val expenseDetail = showData[position]
        setData(holder.binding, expenseDetail)
    }

    override fun getItemCount(): Int {
        return showData.size
    }


    private fun setData(binding: ItemExpenseBinding, expenseDetail: ExpenseDetail) {
        binding.apply {
            val context = root.context
            tvDate.text = context.getString(R.string.expense_date, expenseDetail.date)
            tvAmount.text = context.getString(R.string.expense_amount, expenseDetail.amount)
            root.setOnClickListener {
                Intent(context, AddAndEditExpenseActivity::class.java).let {
                    it.putExtra("is_add", false)
                    it.putExtra("expenseDetail", expenseDetail)
                    context.startActivity(it)
                }
            }
        }
    }

    inner class ExpenseViewHolder(val binding: ItemExpenseBinding) : RecyclerView.ViewHolder(binding.root)
}