package com.example.ledgr.ui.newTransaction

import com.example.ledgr.data.LedgrDataSource
import com.google.gson.JsonObject

class NewTransaction(val dataSource: LedgrDataSource, val body: JsonObject, val pendingId: Int = -1) {
    val url = if (pendingId < 0) { "https://api.ledgr.site/transactions" }
    else { "https://api.ledgr.site/transactions?id=$pendingId" }

    fun create(): JsonObject {
        var returnBody = JsonObject()

        returnBody = dataSource.post(url, body)

        return returnBody
    }
}