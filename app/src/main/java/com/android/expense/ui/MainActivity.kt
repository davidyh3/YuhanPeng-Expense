package com.android.expense.ui

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.DatePicker
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.android.expense.R
import com.android.expense.adapter.ContentFragmentStateAdapter
import com.android.expense.databinding.ActivityMainBinding
import com.android.expense.entity.ExpenseCategory
import com.android.expense.entity.MessageEvent
import com.android.expense.fragment.ContentFragment
import com.android.expense.viewmodel.ExpenseViewModel
import com.google.android.material.tabs.TabLayoutMediator
import com.gyf.immersionbar.ImmersionBar
import org.greenrobot.eventbus.EventBus
import java.util.Calendar


class MainActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    private val mBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val viewModel: ExpenseViewModel by viewModels()

    private val tabList = mutableListOf<ExpenseCategory>()
    private lateinit var tabLayoutMediator: TabLayoutMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        mBinding.apply {
            ImmersionBar.with(this@MainActivity).titleBar(toolbar).statusBarDarkFont(true).navigationBarColor(R.color.color_bg).init()

            tabLayoutMediator = TabLayoutMediator(tlTab, vpContent) { tab, position ->
                val category = tabList[position]
                tab.text = category.name
            }

            vpContent.offscreenPageLimit = 10

            btAdd.setOnClickListener {
                Intent(this@MainActivity, AddAndEditExpenseActivity::class.java).let {
                    it.putExtra("is_add", true)
                    startActivity(it)
                }
            }

            tvFilter.setOnClickListener {
                showFilterDialog()
            }
            viewModel.loadAllCategory {
                tabList.clear()
                tabList.addAll(it)

                val fragments = it.map { category ->
                    val fragment = ContentFragment()
                    val bundle = Bundle()
                    bundle.putLong("categoryId", category.id)
                    fragment.arguments = bundle
                    fragment
                }

                val fragmentStateAdapter = ContentFragmentStateAdapter(fragments, supportFragmentManager, lifecycle)
                vpContent.adapter = fragmentStateAdapter
                tabLayoutMediator.attach()
            }
        }
    }


    private fun showFilterDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.filter)
        val items = resources.getStringArray(R.array.filter_label)
        builder.setItems(items) { _, which ->
            when (which) {
                0 -> {
                    EventBus.getDefault().post(MessageEvent())
                }

                1 -> {
                    showDateDialog()
                }
            }
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
        val messageEvent = MessageEvent()
        messageEvent.date = dateStr
        EventBus.getDefault().post(messageEvent)
    }

    private fun formatNum(value: Int): String {
        return if (value < 10) {
            "0$value"
        } else {
            "$value"
        }
    }
}