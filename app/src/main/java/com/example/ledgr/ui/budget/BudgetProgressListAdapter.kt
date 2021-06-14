package com.example.ledgr.ui.budget

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.example.ledgr.R
import com.example.ledgr.data.model.BudgetCategory

class BudgetProgressListAdapter(
    private val context: Activity,
    private val budget: ArrayList<BudgetCategory>
)
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
        val rowView = inflator.inflate(R.layout.budget_item, parent, false)

        // val progressBar = rowView.findViewById(R.id.progress_bar) as ProgressBar
        val categoryText = rowView.findViewById(R.id.budget_category) as TextView
        val plannedText = rowView.findViewById(R.id.budget_planned) as TextView
        val icon = rowView.findViewById(R.id.budget_icon) as ImageView
        // val progressBarTest = rowView.findViewById(R.id.progress_bar_example) as com.example.ledgr.ui.widget.ProgressBar




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

        val label = "$${budget.formatActualForDisplay()} of $${budget.formatPlannedForDisplay()}"
        categoryText.text = budget.category
        plannedText.text = label
        val draw = context.resources.getIdentifier(budget.icon, "drawable", context.packageName)

        // progressBar.max = budget.planned.roundToInt()
        // progressBar.progress = budget.actual.toString().toFloat().roundToInt()

        /**
        icon.setImageDrawable(
            iconMap[budgetIcon]?.let {
                ResourcesCompat.getDrawable(
                    context.resources,
                    it,
                    null)
            })
        */
        icon.setImageDrawable(ResourcesCompat.getDrawable(context.resources, draw, null))
        /**
        progressBarTest.apply {
            setActual(budget.actual)
            setPlanned(budget.planned)
        }
        */

        rowView.setOnClickListener {
            val budgetToastText = "${categoryText.text}: $label"
            Log.i("acali-rowView", budgetToastText)

            val bundle = Bundle().apply {
                this.putString("category", categoryText.text.toString())
            }

            val targetFragment : Fragment = BudgetDetailFragment::class.java.newInstance()
            targetFragment.arguments = bundle

            val transaction = context as AppCompatActivity
            transaction.supportFragmentManager.beginTransaction().replace(R.id.layout_frame, targetFragment).addToBackStack(categoryText.text.toString()).commit()


            //transaction.replace(R.id.layout_frame, targetFragment).commit()
            /**
            val myIntent = Intent(context, BudgetDetailsActivity::class.java)


            myIntent.putExtra("category", categoryText.text.toString())

            context.startActivity(myIntent)
            */
        }

        return rowView

    }
}