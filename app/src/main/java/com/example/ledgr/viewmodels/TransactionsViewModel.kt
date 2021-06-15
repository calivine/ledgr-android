package com.example.ledgr.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ledgr.data.LedgrDataSource

class TransactionsViewModel(val dataSource: LedgrDataSource) : ViewModel() {

    private val _transactions = dataSource.connect()
    val transactions: LiveData<Any> get() = _transactions

    fun get(url: String) {
        dataSource.getData(url)
    }

}

class TransactionsViewModelFactory(private val context: Context, private val token: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransactionsViewModel::class.java)) {
            return TransactionsViewModel(dataSource = LedgrDataSource(context, token)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}