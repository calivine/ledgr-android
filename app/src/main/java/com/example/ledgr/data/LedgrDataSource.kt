package com.example.ledgr.data

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.koushikdutta.ion.Ion
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime


open class LedgrDataSource(activity: Context, token: String?) {

    private val _activity = activity
    private var bearer = "Bearer $token"

    private lateinit var _bearer: String

    private lateinit var auth: String
    private val routes = mapOf(
        "transactions" to "activity/transactions",
        "create" to "activity/transactions",
        "budget" to "budget/categories"
    )
    val root: String = "https://ledgr.site/api/"
    var method: String? = null
    var uri: String? = null

    private val _liveData = MutableLiveData<Any>()

    companion object {
        const val TAG = "acali LedgrDataSource"
    }


    init {
        Ion.getDefault(activity).conscryptMiddleware.enable(false)
    }


    fun connect(): MutableLiveData<Any> {
        return _liveData
    }

    fun budget(param: String? = null): LedgrDataSource {
        this.uri =
            if (param != null) "${this.root}${this.routes["budget"]}/${param}${this.auth}" else "${this.root}${this.routes["budget"]}${this.auth}"
        return this
    }

    private fun createDateString(): String {
        if (Build.VERSION.SDK_INT > 25) {
            val zdt: ZonedDateTime =
                ZonedDateTime.ofInstant(Instant.now(), ZoneId.of("America/Montreal"))
            return zdt.toString().substringBefore("T")
        }
        return ""


    }

    open fun post(url: String, json: JsonObject): JsonObject {
        return Ion.with(_activity)
            .load("POST", url)
            .setHeader("authorization", bearer)
            .setLogging(TAG, Log.INFO)
            .setJsonObjectBody(json)
            .asJsonObject()
            .get()
    }



    open fun postLegacy(
        json: JsonObject,
        callback: Unit? = null,
        results: MutableLiveData<Any>? = null
    ): JsonObject {
        this.method = "POST"
        /*
    .setCallback { ex, result ->
        if (ex != null) {
            Log.i("acali", ex.message)
        }
        Log.i("acali", result.toString())
        callback.run { this }
        results?.postValue(result.get("user"))
    }*/
        return Ion.with(_activity)
            .load(method, uri.toString())
            .setHeader("authorization", bearer)
            .setLogging(TAG, Log.INFO)
            .setJsonObjectBody(json)
            .asJsonObject()
            .get()
    }

    open fun get(route: String): JsonObject {
        return Ion.with(_activity)
            .load(route)
            .setHeader("authorization", bearer)
            .setLogging(TAG, Log.INFO)
            .asJsonObject()
            .get()
        /**
        .setCallback { ex, result ->
        if (ex != null) {
        Log.i("acali", ex.message)
        }
        Log.i("acali", result.toString())
        results.postValue(result.get("data"))
        }
         */
    }

    fun getDataFromUrl(url: String) {
        Ion.with(_activity)
            .load(url)
            .setHeader("authorization", bearer)
            .setLogging("acali-API", Log.INFO)
            .asJsonObject()
            .setCallback { ex, result ->
                if (ex != null) {
                    Log.i("acali", ex.message)
                } else if (result != null) {
                    Log.i(TAG, result.get("data").toString())
                    _liveData.value = result.get("data")
                }

            }
    }
}