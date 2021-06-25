package com.example.ledgr.data


import com.example.ledgr.data.model.BudgetCategory
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import java.util.*
import kotlin.collections.ArrayList


/**
 * Class that requests user activity information from Ledgr server
 */
class DataBuilder() {


    fun budgetPickerOptions(data: JsonArray) : ArrayList<BudgetCategory> {
        val filteredData = data.filter {
            it.asJsonObject.get("planned").asFloat > 0F
        }
        return buildList(filteredData)
    }

    fun budgetListForView(data: JsonArray) : ArrayList<BudgetCategory> {

        val filteredData = data.filter {
            it.asJsonObject.get("planned").asFloat > 0F && it.asJsonObject.get("actual").asFloat > 0F
        }

        return buildList(filteredData)
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