package com.example.ledgr.adapters

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.RecyclerView
import com.example.ledgr.R
import com.example.ledgr.data.model.PendingList
import com.example.ledgr.data.model.PendingTransaction
import com.example.ledgr.ui.widget.ApproveTransactionDialog
import com.example.ledgr.ui.widget.date.Date
import kotlinx.android.synthetic.main.pending_transaction.view.*
import kotlinx.android.synthetic.main.pending_transactions_header.view.*
import kotlin.properties.Delegates
import kotlin.reflect.KProperty

class PendingTransactionsAdapter(context: Activity) :
    RecyclerView.Adapter<PendingTransactionsAdapter.ViewHolder>() {
    var tracker: SelectionTracker<Long>? = null
    var pendingList: PendingList = PendingList("Transactions for Approval", ArrayList())

    internal lateinit var listener: PendingDialogListener


    init {
        setHasStableIds(true)
    }

    interface PendingDialogListener {
        fun onItemClicked(transaction: PendingTransaction)
        fun onCreateDialog()

    }

    companion object {
        private const val VIEW_TYPE_ITEM = 1
        private const val VIEW_TYPE_HEADER = 2

        private const val IC_EXPANDED_ROTATION_DEG = 0F
        private const val IC_COLLAPSED_ROTATION_DEG = 180F

        const val TAG = "acali PendingTransactions Adapter"
    }

    var isExpanded: Boolean by Delegates.observable(true) { _: KProperty<*>, _: Boolean, newExpandedValue: Boolean ->
        if (newExpandedValue) {
            notifyItemRangeInserted(1, pendingList.transactions.size)
            //To update the header expand icon
            notifyItemChanged(0)
        } else {
            notifyItemRangeRemoved(1, pendingList.transactions.size)
            //To update the header expand icon
            notifyItemChanged(0)
        }
    }

    private val onHeaderClickListener = View.OnClickListener {
        isExpanded = !isExpanded
    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val inflater = LayoutInflater.from(parent.context)

        return when(viewType) {
            VIEW_TYPE_HEADER -> ViewHolder.HeaderVH(
                inflater.inflate(
                    R.layout.pending_transactions_header,
                    parent,
                    false
                )
            )
            else -> ViewHolder.ItemVH(
                inflater.inflate(
                    R.layout.pending_transaction,
                    parent,
                    false
                )
            )
        }


        // return ViewHolder(inflater.inflate(R.layout.pending_transaction, parent, false))
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        try {
            listener = recyclerView.context as PendingDialogListener
        } catch (e: ClassCastException) {
            throw java.lang.ClassCastException((recyclerView.context.toString() +
                    " must implement PendingDialogListener"))
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder.ItemVH -> {
                val pendingTransaction = pendingList.transactions[position - 1]
                Log.d(TAG, "Position: $position")
                holder.bind(pendingTransaction, position-1, listener)
            }
            is ViewHolder.HeaderVH -> {
                val pSize = pendingList.transactions.size
                val headerText = if (pSize == 0) pendingList.header else "$pSize ${pendingList.header}"
                holder.bind(headerText, isExpanded, onHeaderClickListener)
            }
        }
        // val pendingTransaction = pendingList.pendingTransactions[position]

        // holder.text.text = pendingTransaction.description // get("text").toString()
        /**
        tracker?.let {
            holder.bind(pendingTransaction.description, it.isSelected(position.toLong()))
        }
        */

    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) VIEW_TYPE_HEADER else VIEW_TYPE_ITEM
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    /**
    override fun getItemCount(): Int {
        return pendingList.pendingTransactions.size
    }
    */
    override fun getItemCount(): Int = if (isExpanded) pendingList.transactions.size + 1 else 1

    fun deleteItem(position: Int) {
        pendingList.transactions.removeAt(position)
        notifyDataSetChanged()
    }

    open class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // val text = itemView.notification_text
        class ItemVH(itemView: View): ViewHolder(itemView) {
            fun bind(transaction: PendingTransaction, position: Int, listener: PendingDialogListener) {
                itemView.apply {
                    // this.isActivated = isActivated

                    this.notification_text.text = transaction.description
                    this.notification_amount.setFormattedText(transaction.amount.toString())
                    this.notification_date.text = Date().displayAsString(transaction.date.substringBefore(" ")) // transaction.date.substringBefore(" ")


                    this.setOnClickListener {
                        listener.onItemClicked(transaction)
                        val dialog = ApproveTransactionDialog(transaction, position)

                        // val dialog = ApproveTransactionDialog(selection.get("text").toString(), pendingTransaction, selectedId)

                        dialog.show((context as FragmentActivity).supportFragmentManager, "ApproveTransactionDialog")
                    }


                }
                // itemView.isActivated = isActivated
            }


        }

        class HeaderVH(itemView: View): ViewHolder(itemView) {
            internal val ptHeader = itemView.ptHeader
            internal val icExpand = itemView.icExpand

            fun bind(content: String, expanded: Boolean, onClickListener: View.OnClickListener) {
                ptHeader.text = content
                icExpand.rotation =
                    if (expanded) IC_EXPANDED_ROTATION_DEG else IC_COLLAPSED_ROTATION_DEG

                itemView.setOnClickListener(onClickListener)
            }
        }
        /**
        fun getItemDetails(): ItemDetailsLookup.ItemDetails<Long> =
            object : ItemDetailsLookup.ItemDetails<Long>() {
                override fun getPosition(): Int = absoluteAdapterPosition

                // override fun getSelectionKey(): Long = itemId
                override fun getSelectionKey(): Long =
                    itemId// getItemId(position) // getItemId(position).toString()

                override fun inSelectionHotspot(e: MotionEvent): Boolean {
                    return true
                }
            }
        */
    }
}
/**
class PendingItemLookup(private val recyclerView: RecyclerView) :
    ItemDetailsLookup<Long>() {
    override fun getItemDetails(e: MotionEvent): ItemDetails<Long>? {
        val view = recyclerView.findChildViewUnder(e.x, e.y)

        if (view != null) {
            return (recyclerView.getChildViewHolder(view) as PendingTransactionsAdapter.ViewHolder).getItemDetails()
        }
        return null
    }
}
 */
