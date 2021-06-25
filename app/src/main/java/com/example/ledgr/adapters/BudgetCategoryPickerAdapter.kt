package com.example.ledgr.adapters

import android.app.Activity
import android.content.res.Resources
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.ItemKeyProvider
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.RecyclerView
import com.example.ledgr.R
import com.example.ledgr.data.model.BudgetCategory
import kotlinx.android.synthetic.main.budget_item.view.*
import kotlinx.android.synthetic.main.budget_picker_item.view.*

class BudgetCategoryPickerAdapter(private val context: Activity) : RecyclerView.Adapter<BudgetCategoryPickerAdapter.ViewHolder>() {
    var tracker: SelectionTracker<Long>? = null
    var bList: List<BudgetCategory> = arrayListOf()

    init {
        setHasStableIds(true)
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
        if (bList.isNotEmpty()) {
            // Log.d("acali", "Picker-populating view at $position")
            val budget: BudgetCategory = bList[position]
            holder.category.text = budget.category
            val draw =  context.resources.getIdentifier(budget.icon, "drawable", context.packageName)
            holder.icon.setImageDrawable(ResourcesCompat.getDrawable(context.resources, draw, null))
            tracker?.let {
                // Log.d("acali", it.isSelected(position.toLong()).toString())
                holder.bind(budget, it.isSelected(position.toLong()))
            }
        }
        else {
            Log.d("acali", "Empty bList")
        }



    }

    override fun getItemId(position: Int): Long {
        // Log.d("acali", "Position $position")
        //val id = super.getItemId(position)
        // Log.d("acali", "ItemID $id")
        // return super.getItemId(position)
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return bList.size
    }

    inner class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icon: ImageButton = itemView.budget_picker_icon
        val category: TextView = itemView.budget_picker_label

        fun bind(value: BudgetCategory, isActivated: Boolean = false) {
            // Log.d("acali", "Picker adapter ${itemView.background.toString()}")
            itemView.isActivated = isActivated

        }

        fun getItemDetails(): ItemDetailsLookup.ItemDetails<Long> =
            object : ItemDetailsLookup.ItemDetails<Long>() {
                override fun getPosition(): Int = absoluteAdapterPosition
                // override fun getSelectionKey(): Long = itemId
                override fun getSelectionKey(): Long = itemId// getItemId(position) // getItemId(position).toString()
                override fun inSelectionHotspot(e: MotionEvent): Boolean {
                    return true
                }
            }

    }
}

class PickerItemLookup(private val recyclerView: RecyclerView) :
    ItemDetailsLookup<Long>() {
    override fun getItemDetails(e: MotionEvent): ItemDetails<Long>? {
        val view = recyclerView.findChildViewUnder(e.x, e.y)
        if (view != null) {
            return (recyclerView.getChildViewHolder(view) as BudgetCategoryPickerAdapter.ViewHolder).getItemDetails()
        }
        return null
    }
}

class RecyclerViewIdKeyProvider(private val recyclerView: RecyclerView): ItemKeyProvider<Long>(
    ItemKeyProvider.SCOPE_MAPPED) {
    override fun getKey(position: Int): Long? {
        return recyclerView.adapter?.getItemId(position)
    }

    override fun getPosition(key: Long): Int {
        val viewHolder = recyclerView.findViewHolderForItemId(key)
        return viewHolder?.layoutPosition ?: RecyclerView.NO_POSITION
    }


}

