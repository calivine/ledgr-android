package com.example.ledgr

import android.util.Log
import java.util.*

class Date() {
    private val currentInstant = Calendar.getInstance()
    val year = currentInstant.get(Calendar.YEAR)
    val day = currentInstant.get(Calendar.DAY_OF_MONTH)
    val month = currentInstant.get(Calendar.MONTH)
    val monthDisplayName = currentInstant.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ROOT).toString()

    fun getCompleteDate(date: String):String {
        Log.d("acaligetCompleteDate:", date.toString())
        val months = currentInstant.getDisplayNames(Calendar.MONTH, Calendar.LONG, Locale.ROOT)
        val dateArray = date.split(' ')
        Log.d("acaligetCompleteDate:", dateArray.toString())
        val monthAsInt: Int? = months[dateArray[1]]

        if (monthAsInt != null) {
            return if (monthAsInt+1 >= 10) {
                "${dateArray[2]}-${monthAsInt+1}-${dateArray[0]}"
            } else {
                "${dateArray[2]}-0${monthAsInt+1}-${dateArray[0]}"
            }

        }
        return ""
    }

    fun display(): String {
        return "$day $monthDisplayName $year"
    }

    fun displayAsString(date:String): String {
        val months = mutableListOf<String>("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")
        val dateArray = date.split('-')
        val monthString = months[dateArray[1].toInt() -1]
        return "${dateArray[2]} $monthString ${dateArray[0]}"
    }







}