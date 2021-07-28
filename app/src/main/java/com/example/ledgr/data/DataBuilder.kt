package com.example.ledgr.data


import android.content.Context
import com.example.ledgr.R
import com.example.ledgr.data.model.BudgetCategory
import com.example.ledgr.data.model.PendingTransaction
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import java.util.*
import kotlin.collections.ArrayList


/**
 * Class that requests user activity information from Ledgr server
 */
class DataBuilder() {

    fun pendingTransactionsList(data: JsonArray): ArrayList<PendingTransaction> {
        val viewList: ArrayList<PendingTransaction> = ArrayList()
        data.forEach {
            val responseObject = it.asJsonObject
            if (responseObject.get("text").asString.length > 19) {
                viewList.add(
                    PendingTransaction(
                        it.asJsonObject.get("id").asInt,
                        it.asJsonObject.get("created_at").asString,
                        it.asJsonObject.get("text").asString.substring(19).substringBefore(" was approved.").split(" at ")[1],
                        it.asJsonObject.get("text").asString.substring(19).substringBefore(" was approved.").split(" at ")[0].toFloat()
                    )
                )

            }

        }
        return viewList

    }


    fun budgetPickerOptions(data: JsonArray) : ArrayList<BudgetCategory> {
        val filteredData = data.filter {
            it.asJsonObject.get("planned").asFloat > 0F
        }
        return buildList(filteredData)
    }

    fun budgetListForView(data: JsonArray) : ArrayList<BudgetCategory> {

        val filteredData = data.filter {
            it.asJsonObject.get("planned").asFloat > 0F //&& it.asJsonObject.get("actual").asFloat > 0F
        }

        return buildList(filteredData)
    }

    fun convertToSet(data: JsonArray): MutableSet<String> {
        val categoryList: ArrayList<String> = ArrayList()
        for (item in data) {
            val category = item.asJsonObject.get("category").toString().removeSurrounding("\"")
            categoryList.add(category)
        }

        return categoryList.toMutableSet()

    }

    private fun buildList(list: List<JsonElement>): ArrayList<BudgetCategory> {
        val viewList: ArrayList<BudgetCategory> = ArrayList()
        list.forEach {
            viewList.add(
                BudgetCategory(
                    it.asJsonObject.get("category").asString,
                    it.asJsonObject.get("planned").asFloat,
                    it.asJsonObject.get("actual").asFloat,
                    it.asJsonObject.get("icon").asString,
                )
            )
        }
        return viewList
    }


}