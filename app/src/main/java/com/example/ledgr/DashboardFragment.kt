package com.example.ledgr

import Ledgr
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.gson.JsonArray
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.fragment_new_transaction.*
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [DashboardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DashboardFragment : Fragment() {
    var spending: MutableLiveData<Any> = MutableLiveData()
    var budget: MutableLiveData<Any> = MutableLiveData()

    private val budgetList = ArrayList<Map<String, String>>()
    private lateinit var budgetListAdapter : ArrayAdapter<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val budgetlist = activity?.findViewById<ListView>(R.id.budget_list)

        getMonthlySpending()
        val it = Ledgr(requireActivity(), "LHWmiGNoaVbrgYTv7qETIVpoNkJ8H9IB1Y3Ze72voXY5Oei8Pyl7gp2Apfpw")
        it.budget().get(budget)

        budget.observe(viewLifecycleOwner, Observer {
            for (item in it as JsonArray) {
                val outputLine = "${item.asJsonObject.get("category").asString} $${item.asJsonObject.get("planned").asString}"
                val budget = mapOf<String, String>(
                    "category" to item.asJsonObject.get("category").asString,
                    "planned" to item.asJsonObject.get("planned").asString,
                    "actual" to item.asJsonObject.get("actual").asString
                )
                budgetList.add(budget)
            }
            // budgetListAdapter = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, budgetList)
            val budgetListAdapter = BudgetProgressListAdapter(requireActivity(), budgetList)
            budgetlist?.adapter = budgetListAdapter
        })


    }

    private fun getMonthlySpending() {
        val apiKey = arguments?.getString("api").toString().removeSurrounding("\"")
        val ledgr = Ledgr(requireActivity(), apiKey)
        ledgr.transactions("total").get(spending)
    }

    override fun onResume() {
        super.onResume()
        val spendingDisplay = activity?.findViewById<TextView>(R.id.spending_display)
        getMonthlySpending()
        spending.observe(this, Observer {
            val display = "$${it}"
            spendingDisplay?.text = display
            // progress_bar.progress = it.toString().substringAfter("$").toFloat().roundToInt()
        })

        Log.d("acaliDASHBOARD_FRAGMENT:", "onResume was called")
    }

    override fun onPause() {
        super.onPause()
        Log.d("acaliDASHBOARD_FRAGMENT", "onPause was called")
    }

    override fun onStop() {
        super.onStop()
        Log.d("acaliDASHBOARD_FRAGMENT", "onStop was called")
    }


}