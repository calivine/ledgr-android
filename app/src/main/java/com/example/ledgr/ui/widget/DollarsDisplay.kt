package com.example.ledgr.ui.widget

import android.content.Context
import android.util.AttributeSet
import kotlinx.android.synthetic.main.fragment_new_transaction.*

class DollarsDisplay(context: Context, attrs: AttributeSet): NumberDisplay(context, attrs) {

    override fun onClick(number: String) {

        super.onClick(number)
    }

    override fun update(number: String) {
        val old = this.text.toString()
        val new = if (old == "0") { number } else { "$old$number" }
        // val formattedText = "$new"
        this.text = new
    }

    fun backspace() {
        val displayText = this.text.toString()
        val withBackspace = displayText.dropLast(1)
        if (withBackspace == "") {
            this.text = "0"
        }
        else {
            this.text = withBackspace
        }

    }
}