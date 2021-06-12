package com.example.ledgr

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.util.Log
import android.widget.ListView
import android.widget.TextView
import java.util.*
import com.example.ledgr.ui.widget.date.Date

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.example.ledgr.data.model.Transaction
import com.example.ledgr.ui.transactions.TransactionsViewModel
import com.example.ledgr.ui.transactions.TransactionsViewModelFactory
import com.google.gson.JsonArray
import kotlinx.android.synthetic.main.fragment_view_transactions.*


/**
 * A simple [Fragment] subclass.
 * create an instance of this fragment.
 */
class ViewTransactionsFragment : Fragment() {
    // private val transactions = ArrayList<String>()
    var transactions: MutableLiveData<Any> = MutableLiveData()
    private val transactionList = ArrayList<Transaction>()

    private lateinit var transactionsViewModel: TransactionsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_transactions, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Log.d("acalibundle", arguments?.getString("api").toString().removeSurrounding("\""))
        // val token = arguments?.getString("api").toString().removeSurrounding("\"")
        // ListView
        val transactionsList = activity?.findViewById<ListView>(R.id.transaction_list)

        val sharedPref = activity?.getSharedPreferences(getString(R.string.api_token), Context.MODE_PRIVATE) ?: return
        val token = sharedPref.getString(getString(R.string.api_token), "")


        val url = "https://api.ledgr.site/transactions?month=${Date().getCurrentMonth()}&year=${Date().getCurrentYear()}"

        // Initialize Transactions ViewModel
        transactionsViewModel = ViewModelProvider(this, TransactionsViewModelFactory(requireActivity(), token!!.toString())).get(TransactionsViewModel::class.java)

        // Connect to Ledgr Database
        transactionsViewModel.get(url)

        /** Depreciated
        val ledgr =
        LedgrDataSource(requireActivity(), token)
        ledgr.transactions().getLegacy(transactions)
         */

        transactionsViewModel.transactions.observe(viewLifecycleOwner, androidx.lifecycle.Observer {

            Log.d("acaliObserveTransList", it.toString())
            val check = it as JsonArray
            if (check.size() == 0) {
                val placeholder = TextView(requireActivity())
            }
            for (item in it as JsonArray) {
                val amount = if (item.asJsonObject.get("amount").asString.substringAfter(
                        '.',
                        "${item.asJsonObject.get("amount")}.00"
                    ).count() == 1
                ) "$${item.asJsonObject.get("amount").asString}0" else "$${item.asJsonObject.get("amount").asString}"
                /**
                val tMap = mapOf<String, String>(
                "date" to item.asJsonObject.get("date").asString,
                "category" to item.asJsonObject.get("category").asString,
                "description" to item.asJsonObject.get("description").asString,
                "amount" to amount
                )

                val transaction = Transaction(
                date=item.asJsonObject.get("date").asString,
                category=item.asJsonObject.get("category").asString,
                description = item.asJsonObject.get("description").asString,
                amount = item.asJsonObject.get("amount").asFloat
                )

                transactionList.add(transaction)
                 */

                transactionList.add(Transaction(
                    date=Date().displayAsString(item.asJsonObject.get("date").asString),
                    category=item.asJsonObject.get("category").asString,
                    description = item.asJsonObject.get("description").asString,
                    amount = item.asJsonObject.get("amount").asFloat
                ))
                val dateTest = Date().displayAsString(item.asJsonObject.get("date").asString)
                Log.d("acaliDatetest", dateTest)
            }

            Log.i("acali.arrayMap", transactionsList.toString())

            val transactionListAdapterClass =
                TransactionListAdapter(requireActivity(), transactionList)

            // transactionListAdapter = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, transactions)

            transactionsList?.adapter = transactionListAdapterClass
        })

        transaction_list_refresh.setOnRefreshListener {
            Log.d("acaliONREFRESH", "onRefresh called from SwipeRefreshLayout")
            transactionList.clear()
            // ledgr.transactions().getLegacy(transactions)
            transactionsViewModel.get(url)
            transaction_list_refresh.isRefreshing = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("acali-ViewTransactionsFragment", "onCreate was called")
    }

    override fun onPause() {
        super.onPause()
        Log.d("acali-ViewTransactionsFragment", "onPause was called")
    }

    override fun onStop() {
        super.onStop()
        Log.d("acali-ViewTransactionsFragment", "onStop was called")
    }

    override fun onResume() {
        super.onResume()

        Log.d("acali-ViewTransactionsFragment", "onResume was called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("acali-ViewTransactionsFragment", "onDestroy was called")
    }

}