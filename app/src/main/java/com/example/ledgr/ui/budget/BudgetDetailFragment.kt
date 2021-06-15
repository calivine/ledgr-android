package com.example.ledgr.ui.budget

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.ledgr.R
import com.example.ledgr.adapters.TransactionListAdapter
import com.example.ledgr.data.model.Transaction
import com.example.ledgr.ui.widget.date.Date
import com.example.ledgr.ui.widget.LineChart
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_budget_details.*
import kotlinx.android.synthetic.main.activity_budget_details.details_chart
import kotlinx.android.synthetic.main.fragment_budget_detail.*


/**
 * A simple [Fragment] subclass.
 * Use the [BudgetDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BudgetDetailFragment : Fragment() {
    private lateinit var budgetDetailsViewModel: BudgetDetailsViewModel
    private lateinit var transactionListAdapter: TransactionListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_budget_detail, container, false)
    }



    override fun onResume() {
        super.onResume()

        val sharedPrefs = activity?.getSharedPreferences(getString(R.string.api_token),
            AppCompatActivity.MODE_PRIVATE
        )

        val token = sharedPrefs!!.getString(getString(R.string.api_token), "")

        //val target = requireActivity().intent.getStringExtra("category")



        val target = arguments?.getString("category")
        //val bundle = arguments


        val transactionsListView = requireActivity().findViewById<ListView>(R.id.budget_details_list)
        val transactionList = ArrayList<Transaction>()

        budgetDetailsViewModel = ViewModelProvider(this, BudgetDetailsViewModelFactory(requireActivity(), token!!.toString())).get(
            BudgetDetailsViewModel::class.java)

        val url = "https://api.ledgr.site/transactions?category=${target}"
        budgetDetailsViewModel.get(url)

        val budget_detail_label = requireActivity().findViewById<TextView>(R.id.budget_detail_category)

        budget_detail_label.text = target

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


            transactionListAdapter = TransactionListAdapter(requireActivity(), transactionList)
            transactionsListView.adapter = transactionListAdapter


            val lineChart = LineChart()
            lineChart.setDataAndLabels(budgetPeriodSet).build()

            // details_chart.aa_drawChartWithChartModel(chartModel)
            details_chart.aa_drawChartWithChartOptions(chartOptions = lineChart.getOptions())


        })

    }


}