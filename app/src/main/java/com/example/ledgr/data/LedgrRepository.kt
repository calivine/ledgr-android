package com.example.ledgr.data

import android.content.Context
import com.google.gson.JsonArray
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*


/**
 * Class that requests user activity information from Ledgr server
 */
class LedgrRepository(token: String, activity: Context) {
    private val _activity = activity
    private val _token = token
    private val bearer = "Bearer $token"

    lateinit var data: JsonArray

    fun get() : JsonArray {
        val zdt: ZonedDateTime =
                ZonedDateTime.ofInstant(Instant.now(), ZoneId.of("America/Montreal"))
        val month = zdt.month.toString().toLowerCase(Locale.ROOT).capitalize(Locale.ROOT)
        val year = zdt.year.toString()

        val dataSource = LedgrDataSource(_token, _activity)

        val dataObject = dataSource.get("https://api.ledgr.site/budget?month=$month&year=$year")
        data = dataObject.getAsJsonArray("data")
        return data[0] as JsonArray
    }
}