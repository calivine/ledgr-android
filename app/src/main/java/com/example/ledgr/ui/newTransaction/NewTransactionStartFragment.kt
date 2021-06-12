package com.example.ledgr.ui.newTransaction

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ledgr.R

class NewTransactionStartFragment : Fragment() {



    // private lateinit var viewModel: NewTransactionStartViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.new_transaction_start_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // viewModel = ViewModelProvider(this).get(NewTransactionStartViewModel::class.java)
        // TODO: Use the ViewModel
    }

}