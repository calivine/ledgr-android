package com.example.ledgr.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View

class CentsDisplay(context: Context, attrs: AttributeSet): NumberDisplay(context, attrs) {

    var cent1 = "0"
    var cent2 = "0"
    var state = "1"


    override fun onClick(number: String) {
        super.onClick(number)
    }

    override fun update(number: String) {
        when (state) {
            "1" -> {
                updateCent1(number)
            }
            "2" -> {
                updateCent2(number)
            }
        }
    }



    fun appear() {
        this.visibility = View.VISIBLE
    }

    fun disappear() {
        this.visibility = View.INVISIBLE
    }

    private fun resetCents() {
        cent1 = "0"
        cent2 = "0"
    }



    fun updateCent1(number: String) {
        cent1 = number
        updateCents()
        state = "2"
        // updateState("CENT2")
    }

    fun updateCent2(number: String) {
        cent2 = number
        updateCents()
        state = "1"
        // updateState("DOLLARS")
    }

    private fun updateCents() {
        val updatedText = ".$cent1$cent2"

        addCentsToDisplay(updatedText)
    }

    private fun addCentsToDisplay(text: String) {
        this.text = text
    }



}