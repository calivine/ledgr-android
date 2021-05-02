package com.example.ledgr.ui.budget

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ProgressBar
import android.widget.TextView
import com.example.ledgr.R
import com.example.ledgr.data.model.BudgetCategory
import kotlin.math.roundToInt

class BudgetProgressListAdapter(private val context: Activity, private val budget: ArrayList<BudgetCategory>)
    : BaseAdapter() {
    override fun getCount(): Int {
        return budget.size
    }

    override fun getItem(position: Int): Any {
        return budget[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflator = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView = inflator.inflate(R.layout.budget_row, parent, false)

        val progressBar = rowView.findViewById(R.id.progress_bar) as ProgressBar
        val categoryText = rowView.findViewById(R.id.budget_category) as TextView
        val plannedText = rowView.findViewById(R.id.budget_planned) as TextView

        /**
         * Depreciated Version
        val budget = getItem(position) as Map<*,*>
        val label = "$${budget["actual"].toString()} of $${budget["planned"].toString()}"
        categoryText.text = budget["category"].toString()
        plannedText.text = label
        progressBar.max = budget["planned"].toString().toInt()
        progressBar.progress = budget["actual"].toString().toFloat().roundToInt()

         **/

        val budget : BudgetCategory = getItem(position) as BudgetCategory

        val label = "$${budget.actual} of $${budget.planned}"
        categoryText.text = budget.category
        plannedText.text = label
        progressBar.max = budget.planned.roundToInt()
        progressBar.progress = budget.actual.toString().toFloat().roundToInt()

        rowView.setOnClickListener {
            val budgetToastText = "${categoryText.text}: $label"
            Log.i("acali-rowView", budgetToastText)
        }

        return rowView

    }
}