package com.example.ledgr.ui.widget.date

import android.os.Build
import android.util.Log
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*

class Date() {
    private val currentInstant = Calendar.getInstance()
    val year = currentInstant.get(Calendar.YEAR)
    val day = currentInstant.get(Calendar.DAY_OF_MONTH)
    val month = currentInstant.get(Calendar.MONTH)
    private val monthDisplayName = currentInstant.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ROOT)!!.toString()

    private val zdt = if (Build.VERSION.SDK_INT >= 26)
    {
        ZonedDateTime.ofInstant(Instant.now(), ZoneId.of("America/Montreal"))
    }
    else
    {
        TimeZone.getTimeZone("America/Montreal")
    }


    fun getCompleteDate(date: String):String {
        Log.d("acaligetCompleteDate:", date)
        val months = currentInstant.getDisplayNames(Calendar.MONTH, Calendar.LONG, Locale.ROOT)
        val dateArray = date.split(' ')
        Log.d("acaligetCompleteDate:", dateArray.toString())
        val monthAsInt: Int? = months!![dateArray[1]]

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

    fun getCurrentMonth(): String {
        return if (Build.VERSION.SDK_INT >= 26) {
            zdt as ZonedDateTime
            zdt.month.toString().toLowerCase(Locale.ROOT).capitalize(Locale.ROOT)
        } else {
            // zdt as TimeZone
            monthDisplayName
        }


    }

    fun getCurrentYear(): String {
        return if (Build.VERSION.SDK_INT >= 26) {
            zdt as ZonedDateTime
            zdt.year.toString()
        } else {
            year.toString()
        }
    }

    fun today(): String {
        val dMonth = if (month < 10) {
            "0$month"
        } else {
            "$month"
        }

        val dDay = if (day < 10) {
            "0$day"
        } else {
            "$day"
        }
        return "$year-$dMonth-$dDay"
    }







}