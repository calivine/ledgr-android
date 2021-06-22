package com.example.ledgr.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.ledgr.R
import com.example.ledgr.data.model.BudgetCategory
import kotlinx.android.synthetic.main.budget_item.view.*

class BudgetCategoryPickerAdapter(private val mBudget: List<BudgetCategory>, private val context: Activity) : RecyclerView.Adapter<BudgetCategoryPickerAdapter.ViewHolder>() {
    inner class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icon = itemView.budget_icon
        val category = itemView.budget_category

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BudgetCategoryPickerAdapter.ViewHolder {
        val context = context
        val inflater = LayoutInflater.from(context)
        val budgetItem = inflater.inflate(R.layout.budget_picker_item, parent, false)
        return ViewHolder(budgetItem)
    }

    override fun onBindViewHolder(holder: BudgetCategoryPickerAdapter.ViewHolder, position: Int) {
        val budget: BudgetCategory = mBudget.get(position)
        holder.category.text = budget.category
        val draw =  context.resources.getIdentifier(budget.icon, "drawable", context.packageName)
        holder.icon.setImageDrawable(ResourcesCompat.getDrawable(context.resources, draw, null))


    }

    override fun getItemCount(): Int {
        return mBudget.size    }

}