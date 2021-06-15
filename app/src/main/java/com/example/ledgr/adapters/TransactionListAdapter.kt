package com.example.ledgr.adapters

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.ledgr.R
import com.example.ledgr.data.model.Transaction
import com.example.ledgr.ui.widget.SpendingView

class TransactionListAdapter(private val context: Activity, private val transactions: ArrayList<Transaction>)
    : BaseAdapter(){

        override fun getCount(): Int {
            return transactions.size
        }

        override fun getItem(position: Int): Any {
            return transactions[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, view:View?, parent: ViewGroup): View {
            val inflator = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val rowView = inflator.inflate(R.layout.transaction_item, parent, false)

            val descriptionText = rowView.findViewById(R.id.description) as TextView
            val dateText = rowView.findViewById(R.id.date) as TextView
            val amountText = rowView.findViewById(R.id.amount) as SpendingView
            val categoryText = rowView.findViewById(R.id.category) as TextView

            val transaction = getItem(position) as Transaction

            // descriptionText.text = transaction["description"].toString()
            descriptionText.text =transaction.description
            // dateText.text = transaction["date"].toString()
            dateText.text = transaction.date
            amountText.setFormattedText(transaction.amount.toString())
            categoryText.text = transaction.category
            /**
            rowView.setOnClickListener {
                Log.i("acali-rowView", descriptionText.text.toString())
            }
            **/

            return rowView

        }
}