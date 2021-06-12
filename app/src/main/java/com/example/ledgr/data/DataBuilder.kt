package com.example.ledgr.data


import com.example.ledgr.data.model.BudgetCategory
import com.google.gson.JsonArray
import java.util.*


/**
 * Class that requests user activity information from Ledgr server
 */
class DataBuilder() {

    fun budgetListForView(data: JsonArray) : ArrayList<BudgetCategory> {
        val viewList: ArrayList<BudgetCategory> = ArrayList()

        data.forEach {
            val budgetItem = it.asJsonObject
            if (budgetItem.get("planned").asFloat > 0F) {
                viewList.add(
                    BudgetCategory(
                        budgetItem.get("category").asString,
                        budgetItem.get("planned").asFloat,
                        budgetItem.get("actual").asFloat,
                        budgetItem.get("icon").asString,
                    )
                )
            }
        }

        return viewList
    }
}