package com.example.ledgr.data.model

import android.icu.util.Currency
import android.icu.util.CurrencyAmount
import java.util.*

data class BudgetCategory(val category: String, val planned: Float, val actual: Float, val icon: String) {
    fun formatActualForDisplay(): String {
        return formatZero(actual)
    }

    fun formatPlannedForDisplay(): String {
        return formatZero(planned)
    }

    private fun formatZero(value: Float): String {
        val text = value.toString()
        val curr = CurrencyAmount(2.20, Currency.getInstance("USD"))
        curr.number
        return if (text.substringAfter(".").length == 1) {
            "${text}0"
        } else {
            text
        }
    }
}
