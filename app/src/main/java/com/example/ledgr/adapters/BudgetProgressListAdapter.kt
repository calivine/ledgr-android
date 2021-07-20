package com.example.ledgr.adapters

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.RecyclerView
import com.example.ledgr.R
import com.example.ledgr.data.model.BudgetCategory
import kotlinx.android.synthetic.main.budget_item.view.*


class BudgetProgressListAdapter(private val mBudget: List<BudgetCategory>, private val context: Activity) : RecyclerView.Adapter<BudgetProgressListAdapter.ViewHolder>() {
    inner class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icon: ImageView = itemView.budget_icon
        val category: TextView = itemView.budget_category
        val planned: TextView = itemView.budget_planned
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = context
        val inflater = LayoutInflater.from(context)

        val budgetView = inflater.inflate(R.layout.budget_item, parent, false)

        return ViewHolder(budgetView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val budget: BudgetCategory = mBudget[position]
        val label = "$${budget.formatActualForDisplay()} of $${budget.formatPlannedForDisplay()}"
        val draw =  context.resources.getIdentifier(budget.icon, "drawable", context.packageName)


        holder.also {
            it.category.text = budget.category
            it.planned.text = label
            it.icon.setImageDrawable(ResourcesCompat.getDrawable(context.resources, draw, null))
        }

        // Set Budget Category's icon.
        // holder.icon.setImageDrawable(ResourcesCompat.getDrawable(context.resources, draw, null))

        // Navigate to Budget Detail fragments.
        holder.itemView.setOnClickListener {
            val budgetToastText = "${holder.category.text}: $label"
            Log.i("acali-rowView", budgetToastText)

            val bundle = Bundle().apply {
                this.putString("category", holder.category.text.toString())
            }

            holder.itemView.findNavController().navigate(R.id.action_dashboard_to_budgetDetailFragment, bundle, navOptions {
                anim {
                    enter = android.R.animator.fade_in
                    exit = android.R.animator.fade_out

                }
            })

        }

    }

    override fun getItemCount(): Int {
        return mBudget.size
    }
}