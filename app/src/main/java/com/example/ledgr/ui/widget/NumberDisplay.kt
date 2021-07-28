package com.example.ledgr.ui.widget

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import kotlinx.android.synthetic.main.fragment_new_transaction.*

open class NumberDisplay(context: Context, attrs: AttributeSet): androidx.appcompat.widget.AppCompatTextView(context, attrs) {

    // var state = "DOLLARS"
    // var cent1 = "0"
    // var cent2 = "0"


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }

    open fun onClick(number: String) {}

    open fun update(number: String) {}

    fun updateDisplay(text: String) {
        this.text = text
    }

    fun decimalClicked(number: String): Boolean {
        return number == "."
    }

    fun isThousands(number: String): Boolean {
        return number.substringBefore('.').length >= 4
    }
}