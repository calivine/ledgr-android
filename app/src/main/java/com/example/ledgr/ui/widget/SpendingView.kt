package com.example.ledgr.ui.widget

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet

class SpendingView(context: Context, attrs: AttributeSet) : androidx.appcompat.widget.AppCompatTextView(context, attrs) {

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }

    fun formatZero(text: String) : SpendingView {
        this.text = text

        if (this.text.toString().substringAfter(".").length == 1) {
            val formattedDisplay = "${this.text.toString()}0"
            this.text = formattedDisplay
            return this
        }
        return this
    }

    fun setFormattedText(text: String) {
        val formattedDisplay = if (text.substringAfter(".").length == 1) {
            "${text}0"
        } else {
            text
        }
        this.text = formattedDisplay
    }
}