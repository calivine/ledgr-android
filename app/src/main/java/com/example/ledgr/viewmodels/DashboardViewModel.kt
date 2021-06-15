package com.example.ledgr.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ledgr.data.LedgrDataSource

class DashboardViewModel(val dataSource: LedgrDataSource) : ViewModel() {

    private val _spending = dataSource.connect()
    val spending: LiveData<Any> get() = _spending

    private val _budget = dataSource.connect()
    val budget: LiveData<Any> get() = _budget

    init {
        // _spending.value = 0.0
    }

    fun get(url: String) {
        dataSource.getData(url)
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("acali-DashboardViewModel", "onCleared was called")
    }
}

class DashboardViewModelFactory(private val context: Context, private val token: String?) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
            return DashboardViewModel(dataSource = LedgrDataSource(context, token)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")

    }
}