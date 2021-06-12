package com.example.ledgr.ui.budget

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ledgr.data.LedgrDataSource

class BudgetDetailsViewModel(val dataSource: LedgrDataSource) : ViewModel() {
    private val _transactions = dataSource.connect()
    val transactions: LiveData<Any> get() = _transactions

    fun get(url: String) {
        dataSource.getData(url)
    }
}

class BudgetDetailsViewModelFactory(private val context: Context, private val token: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BudgetDetailsViewModel::class.java)) {
            return BudgetDetailsViewModel(dataSource = LedgrDataSource(context, token)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}