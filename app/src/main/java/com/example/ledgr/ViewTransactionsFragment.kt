package com.example.ledgr

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import java.util.*
import com.example.ledgr.data.LedgrDataSource
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonArray
import kotlinx.android.synthetic.main.fragment_view_transactions.*


/**
 * A simple [Fragment] subclass.
 * create an instance of this fragment.
 */
class ViewTransactionsFragment : Fragment() {
    // private val transactions = ArrayList<String>()
    var transactions: MutableLiveData<Any> = MutableLiveData()
    private val transactionList = ArrayList<Map<*, *>>()
    private lateinit var transactionListAdapter: ArrayAdapter<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_transactions, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d("acalibundle", arguments?.getString("api").toString().removeSurrounding("\""))
        val apiKey = arguments?.getString("api").toString().removeSurrounding("\"")
        // ListView
        val transactionsList = activity?.findViewById<ListView>(R.id.transaction_list)


        val ledgr =
            LedgrDataSource(apiKey, requireActivity())
        ledgr.transactions().getLegacy(transactions)

        transactions.observe(viewLifecycleOwner, androidx.lifecycle.Observer {

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
                val tMap = mapOf<String, String>(
                    "date" to item.asJsonObject.get("date").asString,
                    "category" to item.asJsonObject.get("category").asString,
                    "description" to item.asJsonObject.get("description").asString,
                    "amount" to amount
                )

                transactionList.add(tMap)

            }
            Log.d("acali.arrayMap", transactionsList.toString())

            val transactionListAdapterClass =
                TransactionListAdapter(requireActivity(), transactionList)

            // transactionListAdapter = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, transactions)

            transactionsList?.adapter = transactionListAdapterClass
        })

        transaction_list_refresh.setOnRefreshListener {
            Log.d("acaliONREFRESH", "onRefresh called from SwipeRefreshLayout")
            transactionList.clear()
            ledgr.transactions().getLegacy(transactions)
            transaction_list_refresh.isRefreshing = false
        }
    }


}