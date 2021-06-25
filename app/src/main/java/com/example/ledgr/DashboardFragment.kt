package com.example.ledgr

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ledgr.data.DataBuilder
import com.example.ledgr.ui.widget.date.Date
import com.example.ledgr.adapters.BudgetProgressListAdapter
import com.example.ledgr.viewmodels.DashboardViewModel
import com.example.ledgr.viewmodels.DashboardViewModelFactory
import com.google.gson.JsonArray
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.budget_item.view.*
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.fragment_new_transaction.*

import java.util.*
import kotlin.math.round

/**
 * A simple [Fragment] subclass.
 * Use the [DashboardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel
    private lateinit var dataRepository: DataBuilder

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("acaliDASHBOARD_FRAGMENT", "onViewCreated was called")

        // Spending by categories ListView
        // val budgetlist = activity?.findViewById<ListView>(R.id.budget_list)
        // val spendingDisplay = activity?.spending_display
        // val token = arguments?.getString("api").toString()
        // Get logged in user's api token.
        val sharedPref =
            activity?.getSharedPreferences(getString(R.string.api_token), Context.MODE_PRIVATE)
                ?: return
        val token = sharedPref.getString(getString(R.string.api_token), "")
        val url =
            "https://api.ledgr.site/budget?month=${Date().getCurrentMonth()}&year=${Date().getCurrentYear()}"


        dataRepository = DataBuilder()

        // Initialize Dashboard ViewModel
        dashboardViewModel =
            ViewModelProvider(this, DashboardViewModelFactory(requireActivity(), token!!.toString())).get(
                DashboardViewModel::class.java
            )

        // Connect to Ledgr Database
        dashboardViewModel.get(url)


        dashboardViewModel.budget.observe(viewLifecycleOwner, Observer {
            val budgetList: JsonArray = it as JsonArray
            val viewList = dataRepository.budgetListForView(budgetList)

            val budgetListAdapter = BudgetProgressListAdapter(viewList, requireActivity())
            // budget_list.adapter = budgetListAdapter
            budget_list_recycler.adapter = budgetListAdapter
            // budgetlist?.adapter = budgetListAdapter
        })

        dashboardViewModel.spending.observe(viewLifecycleOwner, Observer {
            var totalSpending = 0F
            var plannedSpending = 0F
            val budgetList: JsonArray = it as JsonArray
            for (budgetItem in budgetList) {
                totalSpending += budgetItem.asJsonObject.get("actual").asFloat
                plannedSpending += budgetItem.asJsonObject.get("planned").asFloat
            }
            val roundedTotal = round(totalSpending)
            progress_bar_test.apply {
                setActual(0F)
                setPlanned(plannedSpending)
            }
            ObjectAnimator.ofFloat(progress_bar_test, "actual", 0F, totalSpending.toFloat()).apply {
                duration = 2000
                interpolator = DecelerateInterpolator()
                start()
            }
            // progress_bar_test
            val display = "$${roundedTotal}"
            // spendingDisplay?.setFormattedText(display)
            spending_display.setFormattedText(display)
        })
    }

    override fun onResume() {
        super.onResume()
        Log.d("acaliDASHBOARD_FRAGMENT", "onResume was called")
    }

    override fun onStart() {
        super.onStart()
        Log.d("acaliDASHBOARD_FRAGMENT", "onStart was called")

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("acaliDASHBOARD_FRAGMENT", "onCreate was called")
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
        Log.d("acaliDASHBOARD_FRAGMENT", "onDestroyView called")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d("acaliDASHBOARD_FRAGMENT", "onDetach called")
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        Log.d("acaliDASHBOARD_FRAGMENT", "onViewStateRestored was called")

    }


}