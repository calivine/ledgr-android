package com.example.ledgr

import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.selection.*
import com.example.ledgr.data.DataBuilder
import com.example.ledgr.ui.widget.date.Date
import com.example.ledgr.adapters.BudgetProgressListAdapter
import com.example.ledgr.adapters.PendingTransactionsAdapter
import com.example.ledgr.data.model.PendingTransaction
import com.example.ledgr.ui.dashboard.ExpandableItemAnimator
import com.example.ledgr.ui.widget.ApproveTransactionDialog
import com.example.ledgr.viewmodels.DashboardViewModel
import com.example.ledgr.viewmodels.DashboardViewModelFactory
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.budget_item.view.*
import kotlinx.android.synthetic.main.budget_list_container.*
import kotlinx.android.synthetic.main.budget_progress_bar.*
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.fragment_new_transaction.*
import kotlinx.android.synthetic.main.notification_card.*

import java.util.*
import kotlin.math.round

/**
 * A simple [Fragment] subclass.
 * Use the [DashboardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DashboardFragment : Fragment(), PendingTransactionsAdapter.PendingDialogListener {

    private lateinit var dashboardViewModel: DashboardViewModel
    private lateinit var dataRepository: DataBuilder
    private lateinit var pendingTransactionsAdapter: PendingTransactionsAdapter
    // private lateinit var tracker: SelectionTracker<Long>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("acaliDASHBOARD_FRAGMENT", "onCreate was called")
        pendingTransactionsAdapter = activity?.let { PendingTransactionsAdapter(it) }!!

        setFragmentResultListener("pendingTransactionApproved") { requestKey, bundle ->
            Log.d("acalifragmentResultListener", "fragmentResultListener called")

            Log.d("acalifragmentResultListener", "fragmentResultListener ${bundle.getString("index")}")

        }
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
        // pending_transaction_container.itemAnimator = ExpandableItemAnimator()
        // pending_transaction_container.adapter = pendingTransactionsAdapter
        pending_transaction_container. also {
            it.itemAnimator = ExpandableItemAnimator()
            it.adapter = pendingTransactionsAdapter
        }
        /**
        tracker = SelectionTracker.Builder<Long>("selectedPendingTransaction",
            pending_transaction_container,
            StableIdKeyProvider(pending_transaction_container),
            PendingItemLookup(pending_transaction_container),
            StorageStrategy.createLongStorage()
            ).withSelectionPredicate(
                SelectionPredicates.createSelectAnything()
            ).build()

        pendingTransactionsAdapter.tracker = tracker

        tracker.addObserver(
            object: SelectionTracker.SelectionObserver<Long>() {
                override fun onSelectionChanged() {
                    super.onSelectionChanged()

                    if (tracker.selection.size() > 0) {

                        val first = tracker.selection.first().toInt()
                        val trS = tracker.selection.last().toInt()
                        if (trS > 0) {
                            val selection = pendingTransactionsAdapter.pendingList.transactions[tracker.selection.last().toInt() - 1]
                            val selectedId = selection.id
                            val pendingTransaction = selection // PendingTransaction(date, desc, amount.toFloat())
                            val dialog = ApproveTransactionDialog(selection.description, pendingTransaction, selectedId)
                            // val dialog = ApproveTransactionDialog(selection.get("text").toString(), pendingTransaction, selectedId)
                            dialog.show(requireActivity().supportFragmentManager, "ApproveTransactionDialog")

                        }
                        else {
                            pendingTransactionsAdapter.isExpanded = !pendingTransactionsAdapter.isExpanded
                        }
                        /**
                        val date = selection
                            .date
                            // .toString()
                            .removeSurrounding("\"")
                            .substringBefore(" ")
                        val description = selection
                            .description
                            // .toString()
                            .removeSurrounding("\"")
                            .substring(19)
                            .substringBefore(" was approved.")
                            .split(" at ")
                        val amount = description[0]
                        val desc = description[1]
                        Log.d("acali PendingTransactionsAdapter", description.toString())
                        */

                    }

                }
            }

        )

        */



        dashboardViewModel.also {
            it.get(url)

            it.pendingTransactions.observe(viewLifecycleOwner, {
                val responseObj = it as JsonObject
                // val pendingTransactions = responseObj.getAsJsonArray("pending")
                val pendingTransactions = dataRepository.pendingTransactionsList(responseObj.getAsJsonArray("pending"))
                val budget = responseObj.getAsJsonArray("budget")
                val categoryList = dataRepository.convertToSet(budget)
                // val sharedPref = context.getSharedPreferences(context.getString(R.string.api_token), Context.MODE_PRIVATE)
                val categorySet = sharedPref.getStringSet(getString(R.string.categories), mutableSetOf())
                if (categorySet.isNullOrEmpty()) {
                    sharedPref.edit().putStringSet(context?.getString(R.string.categories),
                        categoryList.toMutableSet()
                    ).apply()
                }


                // pendingTransactionsAdapter.pendingList = pList
                pendingTransactionsAdapter.pendingList.transactions = pendingTransactions
                pendingTransactionsAdapter.notifyDataSetChanged()

            })
            it.budget.observe(viewLifecycleOwner, {
                val responseObj = it as JsonObject

                val budgetList: JsonArray = responseObj.getAsJsonArray("budget")
                val viewList = dataRepository.budgetListForView(budgetList)

                // Initialize Adapter with budget data.
                val budgetListAdapter = BudgetProgressListAdapter(viewList, requireActivity())

                // budget_list.adapter = budgetListAdapter

                budget_list_recycler.adapter = budgetListAdapter
            })

            it.spending.observe(viewLifecycleOwner, {
                var totalSpending = 0F
                var plannedSpending = 0F
                val responseObj = it as JsonObject
                val budgetList: JsonArray = responseObj.getAsJsonArray("budget")
                for (budgetItem in budgetList) {

                    totalSpending += budgetItem.asJsonObject.get("actual").asFloat
                    plannedSpending += budgetItem.asJsonObject.get("planned").asFloat
                }
                val roundedTotal = round(totalSpending)
                progress_bar_test.apply {
                    setActual(0F)
                    setPlanned(plannedSpending)
                }
                ObjectAnimator.ofFloat(progress_bar_test, "actual", 0F, totalSpending).apply {
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

        new_transaction_FAB.setOnClickListener {
            findNavController().navigate(R.id.action_dashboard_to_newTransactionFragment)
        }
        /**
        // Connect to Ledgr Database
        dashboardViewModel.get(url)


        dashboardViewModel.budget.observe(viewLifecycleOwner, Observer {
            val budgetList: JsonArray = it as JsonArray
            val viewList = dataRepository.budgetListForView(budgetList)

            // Initialize Adapter with budget data.
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
        */
    }

    override fun onCreateDialog() {
        Log.d("acaliDASHBOARD_FRAGMENT", "onCreateDialog")
    }

    override fun onItemClicked(transaction: PendingTransaction) {
        Log.d("acaliDASHBOARD_FRAGMENT", "$transaction")

    }

    override fun onResume() {
        super.onResume()
        Log.d("acaliDASHBOARD_FRAGMENT", "onResume was called")
    }

    override fun onStart() {
        super.onStart()
        Log.d("acaliDASHBOARD_FRAGMENT", "onStart was called")

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

    fun removeFromAdapter(position: Int) {
        Log.d("acali", "RemovedTest")
        pendingTransactionsAdapter.deleteItem(position)
    }


}