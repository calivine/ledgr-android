package com.example.ledgr.viewmodels

import android.content.Context
import android.util.StringBuilderPrinter
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ledgr.data.LedgrDataSource
import java.lang.IllegalArgumentException

class NewTransactionViewModel(val dataSource: LedgrDataSource) : ViewModel() {
    private val _categories = dataSource.connect()
    val categories: LiveData<Any> get() = _categories

    fun get(url: String) {
        dataSource.getDataFromUrl(url)
    }
}

class NewTransactionViewModelFactory(private val context: Context, private val token: String) : ViewModelProvider.Factory {

    override fun <T: ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewTransactionViewModel::class.java)) {
            return NewTransactionViewModel(dataSource = LedgrDataSource(context, token)) as T
        }
        throw IllegalArgumentException("Unkown ViewModel class")
    }
}