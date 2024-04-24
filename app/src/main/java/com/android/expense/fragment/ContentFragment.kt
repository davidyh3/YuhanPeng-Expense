package com.android.expense.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.android.expense.adapter.ExpenseAdapter
import com.android.expense.databinding.FragmentContentBinding
import com.android.expense.entity.MessageEvent
import com.android.expense.viewmodel.ContentViewModel
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class ContentFragment : Fragment() {
    private val mBinding by lazy { FragmentContentBinding.inflate(layoutInflater) }
    private val viewModel: ContentViewModel by viewModels()

    private val expenseAdapter by lazy { ExpenseAdapter() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: MessageEvent?) {
        event?.let {
            loadData(it.date)
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.apply {
            rvContent.adapter = expenseAdapter

        }
        loadData("")
    }

    private fun loadData(date: String) {
        val categoryId = arguments?.getLong("categoryId") ?: 0

        viewModel.loadExpenseList(categoryId,date) {
            if (it.isEmpty()) {
                mBinding.rvContent.visibility = View.GONE
                mBinding.tvNoDate.visibility = View.VISIBLE
            } else {
                mBinding.rvContent.visibility = View.VISIBLE
                mBinding.tvNoDate.visibility = View.GONE
                expenseAdapter.submitList(it)
            }
        }
    }
}