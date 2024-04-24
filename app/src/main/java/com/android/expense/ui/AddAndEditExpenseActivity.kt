package com.android.expense.ui

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.android.expense.R
import com.android.expense.databinding.ActivityAddExpenseBinding
import com.android.expense.entity.ExpenseCategory
import com.android.expense.entity.ExpenseDetail
import com.android.expense.entity.MessageEvent
import com.android.expense.viewmodel.ExpenseViewModel
import com.gyf.immersionbar.ImmersionBar
import org.greenrobot.eventbus.EventBus
import java.util.Calendar


class AddAndEditExpenseActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    private val mBinding by lazy { ActivityAddExpenseBinding.inflate(layoutInflater) }

    private val viewModel: ExpenseViewModel by viewModels()

    private val isAdd by lazy { intent.getBooleanExtra("is_add", true) }

    private val updateExpenseDetail: ExpenseDetail? by lazy { intent.getParcelableExtra("expenseDetail") }

    private var selectCategory: ExpenseCategory? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        mBinding.apply {
            ImmersionBar.with(this@AddAndEditExpenseActivity).titleBar(toolbar).statusBarDarkFont(true).navigationBarColor(R.color.color_bg).init()

            tvTitle.text = if (isAdd) getString(R.string.add_expense) else getString(R.string.edit_expense)

            updateExpenseDetail?.let {
                tvDate.text = it.date
                etAmount.setText(it.amount)
                viewModel.queryCategory(it.categoryId) { category ->
                    selectCategory = category
                    tvCategory.text = category.name
                }
            }

            ivBack.setOnClickListener {
                finish()
            }

            tvCategory.setOnClickListener {
                viewModel.loadAllCategory {
                    showCategoryDialog(it)
                }
            }

            tvDate.setOnClickListener {
                showDateDialog()
            }

            btSure.setOnClickListener {
                submitDate()
            }
        }
    }

    private fun submitDate() {
        if (selectCategory == null) {
            Toast.makeText(this, R.string.hint_category, Toast.LENGTH_SHORT).show()
            return
        }

        val date = mBinding.tvDate.text.toString().trim()
        if (date.isEmpty()) {
            Toast.makeText(this, R.string.hint_date, Toast.LENGTH_SHORT).show()
            return
        }

        val amount = mBinding.etAmount.text.toString().trim()
        if (amount.isEmpty()) {
            Toast.makeText(this, R.string.hint_amount, Toast.LENGTH_SHORT).show()
            return
        }

        if (isAdd) {
            val expenseDetail = ExpenseDetail()
            expenseDetail.categoryId = selectCategory!!.id
            expenseDetail.date = date
            expenseDetail.amount = amount

            viewModel.addExpenseDetail(expenseDetail) {
                if (it) {
                    EventBus.getDefault().post(MessageEvent())
                    finish()
                } else {
                    Toast.makeText(this, R.string.fail, Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            updateExpenseDetail?.let {
                it.categoryId = selectCategory!!.id
                it.date = date
                it.amount = amount

                viewModel.updateExpenseDetail(it) { result ->
                    if (result) {
                        EventBus.getDefault().post(MessageEvent())
                        finish()
                    } else {
                        Toast.makeText(this, R.string.fail, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }


    private fun showCategoryDialog(list: List<ExpenseCategory>) {
        val items = list.map { it.name }.toTypedArray()
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.select_category)
        builder.setItems(items) { _, which ->
            selectCategory = list[which]
            mBinding.tvCategory.text = items[which]
        }

        builder.create().show()
    }

    private fun showDateDialog() {
        val calendar = Calendar.getInstance()

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)


        val datePickerDialog = DatePickerDialog(this, this, year, month, dayOfMonth)
        datePickerDialog.setTitle(R.string.hint_date)
        datePickerDialog.show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val dateStr = "${year}-${formatNum(month + 1)}-${formatNum(dayOfMonth)}"
        mBinding.tvDate.text = dateStr
    }

    private fun formatNum(value: Int): String {
        return if (value < 10) {
            "0$value"
        } else {
            "$value"
        }
    }
}