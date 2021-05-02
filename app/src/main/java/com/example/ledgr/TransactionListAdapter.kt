package com.example.ledgr

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.TextView

class TransactionListAdapter(private val context: Activity, private val transactions: ArrayList<Map<*, *>>)
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
            val rowView = inflator.inflate(R.layout.row, parent, false)

            val descriptionText = rowView.findViewById(R.id.description) as TextView
            val dateText = rowView.findViewById(R.id.date) as TextView
            val amountText = rowView.findViewById(R.id.amount) as TextView
            val categoryText = rowView.findViewById(R.id.category) as TextView

            val transaction = getItem(position) as Map<*,*>

            descriptionText.text = transaction["description"].toString()
            dateText.text = transaction["date"].toString()
            amountText.text = transaction["amount"].toString()
            categoryText.text = transaction["category"].toString()
            /**
            rowView.setOnClickListener {
                Log.i("acali-rowView", descriptionText.text.toString())
            }
            **/

            return rowView

        }
}