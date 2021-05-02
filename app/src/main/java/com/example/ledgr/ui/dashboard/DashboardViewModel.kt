package com.example.ledgr.ui.dashboard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ledgr.data.LedgrRepository
import com.example.ledgr.data.model.BudgetCategory
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.round

class DashboardViewModel() : ViewModel() {

    private val _spending = MutableLiveData<Any>()
    val spending: LiveData<Any> get() = _spending

    private val _budget = MutableLiveData<Any>()
    val budget: LiveData<Any> get() = _budget

    /**
     * Depreciated
     *
    val currentDate = zoneDateTime()
    val month = currentDate.month.toString().toLowerCase(Locale.ROOT).capitalize(Locale.ROOT)
    val year = currentDate.year.toString()
    */

    init {
        _spending.value = 0.0
    }

    fun getData(data: LedgrRepository) {

        val userData = data.get()

        var periodSpending = 0.0
        Log.i("acali-api", userData.toString())
        val budgetList : ArrayList<BudgetCategory> = ArrayList()
        for (budget in userData) {
            Log.i("acali-api", budget.toString())
            val b = budget
            periodSpending += b.asJsonObject.get("actual").asFloat
            budgetList.add(BudgetCategory(
                    b.asJsonObject.get("category").asString,
                    b.asJsonObject.get("planned").asFloat,
                    b.asJsonObject.get("actual").asFloat
            ))
        }

        Log.i("acali-api", periodSpending.toString())

        // Set LiveData values
        _spending.value = round(periodSpending)
        _budget.value = budgetList
    }

    /**
     * Depreciated

    private fun zoneDateTime(): ZonedDateTime {
        val zdt: ZonedDateTime =
                ZonedDateTime.ofInstant(Instant.now(), ZoneId.of("America/Montreal"))
        return zdt
    }

     */


    override fun onCleared() {
        super.onCleared()
        Log.i("acali-DashboardViewModel", "onCleared was called")
        Log.i("acali-DashboardViewModel", spending.toString())
    }
}