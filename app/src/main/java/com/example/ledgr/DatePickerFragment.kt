package com.example.ledgr

import android.app.DatePickerDialog
import android.app.Dialog

import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.MutableLiveData
import kotlinx.android.synthetic.main.fragment_new_transaction.*
import java.util.*


class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {
    var liveDate: MutableLiveData<String> = MutableLiveData()
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current date as the default date in the picker
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        Log.d("acaliDATEPICKER", "onCreateDiaglogCalled")

        // Create a new instance of DatePickerDialog and return it
        return DatePickerDialog(requireActivity(), this, year, month, day)
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {

        val date: String = if (day - 10 >= 0) {
            "${year}-${month+1}-${day}"
        } else {
            "${year}-${month+1}-0${day}"
        }

        Log.d("acali-DATEPICKERonDateSet", date)

        liveDate.postValue(date)
        date_label.text = date

        setFragmentResult("datePicker", bundleOf("date" to date))
    }

    override fun onPause() {
        super.onPause()
        Log.d("acaliDATEPICKERFRAGMENT", "onPause was called")
    }

    override fun onStop() {
        super.onStop()
        Log.d("acaliDATEPICKERFRAGMENT", "onStop was called")
    }

    override fun onResume() {
        super.onResume()

        Log.d("acaliDATEPICKERFRAGMENT:", "onResume was called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("acaliDATEPICKERFRAGMENT:", "onDestroy was called")
    }


}