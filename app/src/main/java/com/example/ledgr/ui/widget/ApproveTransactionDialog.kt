package com.example.ledgr.ui.widget

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import android.widget.SimpleAdapter
import android.widget.TextView
import androidx.collection.arraySetOf
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.example.ledgr.R
import com.example.ledgr.data.LedgrDataSource
import com.example.ledgr.data.model.PendingTransaction
import com.example.ledgr.ui.newTransaction.NewTransaction
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.fragment_new_transaction.*
import kotlinx.android.synthetic.main.pending_transaction.*
import kotlinx.coroutines.selects.select

class ApproveTransactionDialog(val pendingTransaction: PendingTransaction, val position: Int): DialogFragment() {

    internal lateinit var listener: ApproveTransactionListener

    interface ApproveTransactionListener {
        fun onDialogPositiveClick(dialog: DialogFragment, position: Int)
        fun onDialogNegativeClick(dialog: DialogFragment)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as ApproveTransactionListener
        } catch (e: ClassCastException) {
            throw java.lang.ClassCastException((context.toString() +
                    " must implement ApproveTransactionListener"))
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val sharedPref =
                it.getSharedPreferences(getString(R.string.api_token), Context.MODE_PRIVATE)
            val categoryList = sharedPref.getStringSet(getString(R.string.categories), arraySetOf())?.sorted()
                ?.toTypedArray()
            //val sorted = categoryList?.sorted()?.toTypedArray()

            var selectedCategory: Int = 0

            // builder.setView(inflater.inflate(R.layout.approve_transaction_dialog, null))
            builder.setTitle(getString(R.string.dialog_title))
            // builder.setMessage(text)
            builder.setPositiveButton(R.string.ok, DialogInterface.OnClickListener { dialog, which ->
                Log.d("acaliPositivbutton", "$selectedCategory")
                val token = sharedPref.getString(getString(R.string.api_token), "")
                val dataSource = LedgrDataSource(requireActivity(), token)
                val category = categoryList?.get(selectedCategory)
                category?.let { it1 ->
                    Log.d("acaliPositivbutton", it1)
                }

                // Package PendingTransaction in JsonObject
                // Add data to JSON Object

                val json = JsonObject()
                json.addProperty("date", pendingTransaction.date)
                json.addProperty("amount", pendingTransaction.amount)
                json.addProperty("description", pendingTransaction.description)
                json.addProperty("category", category)

                val result = NewTransaction(dataSource, json, pendingTransaction.id).create()
                Log.d("acaliPendingResult", result.toString())

                setFragmentResult("pendingTransactionApproved", bundleOf("index" to result.toString()))

                listener.onDialogPositiveClick(this, position)
            })
            builder.setNegativeButton(R.string.cancel, DialogInterface.OnClickListener { dialog, which ->
                Log.d("acaliPendingResult", "Negative Click")
                setFragmentResult("pendingTransactionApproved", bundleOf("index" to "negative"))
                listener.onDialogNegativeClick(this)

            })
            builder.setSingleChoiceItems(categoryList, -1, DialogInterface.OnClickListener { dialog, which ->
                // The 'which' argument contains the index position
                // of the selected item
                Log.d("acaliSelect", "$which")
                selectedCategory = which
                Log.d("acaliSelect", "$selectedCategory")


            })
            builder.setTitle("Choose category for ${pendingTransaction.description}")


            // notification_text.text = text
            builder.create()

        } ?: throw IllegalStateException("Activity cannot be null")
    }

}