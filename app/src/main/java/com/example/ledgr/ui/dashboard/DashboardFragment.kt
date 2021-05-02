package com.example.ledgr.ui.dashboard

import android.annotation.SuppressLint
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
import androidx.lifecycle.ViewModelProvider
import com.example.ledgr.R
import com.example.ledgr.data.LedgrRepository
import com.example.ledgr.data.model.BudgetCategory
import com.example.ledgr.ui.budget.BudgetProgressListAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.budget_row.view.*
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.fragment_new_transaction.*
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 * Use the [DashboardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DashboardFragment : Fragment() {
    // var spending: MutableLiveData<Any> = MutableLiveData()
    var budget: MutableLiveData<Any> = MutableLiveData()

    // private val budgetList = ArrayList<Map<String, String>>()
    private lateinit var budgetListAdapter : ArrayAdapter<String>

    private lateinit var dashboardViewModel : DashboardViewModel
    private lateinit var dashboardViewModelFactory: DashboardViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // Spending by categories ListView
        val budgetlist = activity?.findViewById<ListView>(R.id.budget_list)

        dashboardViewModelFactory = DashboardViewModelFactory()
        dashboardViewModel = ViewModelProvider(this, dashboardViewModelFactory).get(DashboardViewModel::class.java)

        dashboardViewModel.getData(LedgrRepository("LHWmiGNoaVbrgYTv7qETIVpoNkJ8H9IB1Y3Ze72voXY5Oei8Pyl7gp2Apfpw", requireActivity()))

        dashboardViewModel.budget.observe(viewLifecycleOwner, {
            /**
             * Depreciated
             *
            for (item in it as JsonArray) {

                // val outputLine = "${item.asJsonObject.get("category").asString} $${item.asJsonObject.get("planned").asString}"
                val budget = mapOf<String, String>(
                    "category" to item.asJsonObject.get("category").asString,
                    "planned" to item.asJsonObject.get("planned").asString,
                    "actual" to item.asJsonObject.get("actual").asString
                )
                budgetList.add(budget)
            }
            **/
            // budgetListAdapter = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, budgetList)
            val budgetListAdapter = BudgetProgressListAdapter(requireActivity(), it as ArrayList<BudgetCategory>)
            budgetlist?.adapter = budgetListAdapter
        })

        val spendingDisplay = activity?.findViewById<TextView>(R.id.spending_display)

        dashboardViewModel.spending.observe(viewLifecycleOwner, Observer {
            val display = "$${it}"
            spendingDisplay?.text = display
            // dashboardViewModel.spending.value = it.toString().toDouble()
            // spendingDisplay?.text = "$${dashboardViewModel.spending.toString()}"
            // progress_bar.progress = it.toString().substringAfter("$").toFloat().roundToInt()
        })
    }

    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()

        val spendingDisplay = activity?.findViewById<TextView>(R.id.spending_display)
        val budgetlist = activity?.findViewById<ListView>(R.id.budget_list)

        dashboardViewModel.budget.observe(viewLifecycleOwner, Observer {
            /**
             * Depreciated
            for (item in it as JsonArray) {
                // val outputLine = "${item.asJsonObject.get("category").asString} $${item.asJsonObject.get("planned").asString}"
                val budget = mapOf<String, String>(
                        "category" to item.asJsonObject.get("category").asString,
                        "planned" to item.asJsonObject.get("planned").asString,
                        "actual" to item.asJsonObject.get("actual").asString
                )
                // budgetList.add(budget)
            }
            */
            // budgetListAdapter = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, budgetList)
            val budgetListAdapter = BudgetProgressListAdapter(requireActivity(), it as ArrayList<BudgetCategory>)
            budgetlist?.adapter = budgetListAdapter
        })

        dashboardViewModel.spending.observe(viewLifecycleOwner, Observer {
            val display = "$${it}"
            spendingDisplay?.text = display
            // dashboardViewModel.spending.value = it.toString().toDouble()
            // spendingDisplay?.text = "$${dashboardViewModel.spending.toString()}"
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

    override fun onDestroyView() {
        super.onDestroyView()
        Log.i("acaliDASHBOARD_FRAGMENT", "onDestroyView called")
    }
    override fun onDetach() {
        super.onDetach()
        Log.i("acaliDASHBOARD_FRAGMENT", "onDetach called")
    }

}