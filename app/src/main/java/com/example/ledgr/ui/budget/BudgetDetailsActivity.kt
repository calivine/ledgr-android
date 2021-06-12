package com.example.ledgr.ui.budget

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.ledgr.R
import com.example.ledgr.TransactionListAdapter
import com.example.ledgr.data.model.Transaction
import com.example.ledgr.ui.widget.date.Date
import com.example.ledgr.ui.widget.LineChart
import com.github.aachartmodel.aainfographics.aachartcreator.*
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AATooltip
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_budget_details.*

class BudgetDetailsActivity : AppCompatActivity() {
    private lateinit var budgetDetailsViewModel: BudgetDetailsViewModel
    private lateinit var transactionListAdapter: TransactionListAdapter
    private lateinit var chartModel: AAChartModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_budget_details)
        val sharedPrefs = getSharedPreferences(getString(R.string.api_token), MODE_PRIVATE)

        val token = sharedPrefs.getString(getString(R.string.api_token), "")
        val target = intent.getStringExtra("category")

        val transactionsListView = this.budget_details_list
        val transactionList = ArrayList<Transaction>()
        /**
        val chartModel = AAChartModel()
            .chartType(AAChartType.Line)
            .series(arrayOf(AASeriesElement().name("Data").data(arrayOf(10,15,12,19,24,30,17))))

        */

        // details_chart.aa_drawChartWithChartModel(chartModel)


        budgetDetailsViewModel = ViewModelProvider(this, BudgetDetailsViewModelFactory(this, token!!.toString())).get(
            BudgetDetailsViewModel::class.java)

        val url = "https://api.ledgr.site/transactions?category=${target}"
        budgetDetailsViewModel.get(url)
        budget_details_category.text = target

        budgetDetailsViewModel.transactions.observe(this, {
            // val data = it
            val budgetPeriodSet: MutableMap<String, Float> = mutableMapOf()
            val transactionData: JsonArray = it as JsonArray
            transactionData.forEach {
                val transactionItem = it as JsonObject
                transactionList.add(
                    Transaction(date= Date().displayAsString(transactionItem.get("date").asString),
                        category=it.asJsonObject.get("category").asString,
                        description = it.asJsonObject.get("description").asString,
                        amount = it.asJsonObject.get("amount").asFloat)
                )

                val key = transactionItem.get("period").asString
                if (budgetPeriodSet.containsKey(key)) {
                    val tempValue = budgetPeriodSet.get(key)
                    if (tempValue != null) {
                        budgetPeriodSet[key] =
                            tempValue + transactionItem.get("actual").asFloat
                    }
                } else {
                    budgetPeriodSet[key] = transactionItem.get("actual").asFloat
                }

            }
            /**
            // Add Transactions to transactionList
            for (item in it as JsonArray) {
                val transaction = Transaction(date= Date().displayAsString(item.asJsonObject.get("date").asString), 
                    category=item.asJsonObject.get("category").asString,
                    description = item.asJsonObject.get("description").asString,
                    amount = item.asJsonObject.get("amount").asFloat)
                transactionList.add(transaction)
            }
            */

            // val values = budgetPeriodSet.values.toList()
            // val valueArray = Array(values.size) { i -> values[i] }
            // val keys = budgetPeriodSet.keys.toList()
            transactionListAdapter = TransactionListAdapter(this, transactionList)
            transactionsListView.adapter = transactionListAdapter
            /**
            chartModel = AAChartModel()
                .chartType(AAChartType.Line)
                .dataLabelsEnabled(true)
                .series(arrayOf(AASeriesElement()
                    .name("")
                    .data(Array(values.size - 1) { i -> values[i] })

                ))
            val chartOptions = AAOptionsConstructor.configureChartOptions(chartModel)
            chartOptions.xAxis(AAXAxis())
            chartOptions.xAxis?.categories(Array(keys.size - 1) { i -> keys[i] })
            */

            val lineChart = LineChart()

            lineChart.setDataAndLabels(budgetPeriodSet).build()


            // details_chart.aa_drawChartWithChartModel(chartModel)

            details_chart.aa_drawChartWithChartOptions(chartOptions = lineChart.getOptions())


        })
    }
}