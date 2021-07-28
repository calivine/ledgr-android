package com.example.ledgr.ui.widget

import android.content.Context
import android.util.AttributeSet
import com.example.ledgr.R

class CentDisplay(context: Context, attrs: AttributeSet): NumberDisplay(context, attrs) {

    var placeholder: Boolean = true

    override fun onClick(number: String) {
        if (number == ".") {
            return
        }
        else {
            update(number)
        }

    }

    override fun update(number: String) {
        updateCent(number)
    }

    private fun updateCent(number:String) {
        updateDisplay(number)
        placeholder = false
        this.setTextColor(context.getColor(R.color.ledgr_primary_dark))
    }

    fun backspace() {
        resetCent()
        placeholder = true
        this.setTextColor(context.getColor(R.color.light_blue_400))
    }

    private fun resetCent() {
        val zero = "0"
        updateCent(zero)
    }


}