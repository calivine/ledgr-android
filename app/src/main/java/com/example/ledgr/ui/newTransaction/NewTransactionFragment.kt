package com.example.ledgr.ui.newTransaction

import com.example.ledgr.data.LedgrDataSource
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StableIdKeyProvider
import androidx.recyclerview.selection.StorageStrategy
import com.example.ledgr.MainActivity
import com.example.ledgr.R
import com.example.ledgr.adapters.BudgetCategoryPickerAdapter
import com.example.ledgr.adapters.PickerItemLookup
import com.example.ledgr.adapters.RecyclerViewIdKeyProvider
import com.example.ledgr.data.DataBuilder
import com.example.ledgr.ui.widget.date.Date
import com.example.ledgr.viewmodels.NewTransactionViewModel
import com.example.ledgr.viewmodels.NewTransactionViewModelFactory
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.koushikdutta.ion.Ion
import kotlinx.android.synthetic.main.fragment_new_transaction.*
import kotlinx.coroutines.selects.select
import java.util.*


/**
 * A simple [Fragment] subclass.
 * create an instance of this fragment.
 */
class NewTransactionFragment : Fragment() {


    var date: MutableLiveData<String> = MutableLiveData()
    private var dateDisplay = Date()
    // var amountDisplay: MutableLiveData<String> = MutableLiveData()



    private lateinit var newTransactionViewModel: NewTransactionViewModel
    private lateinit var dataRepository: DataBuilder
    private lateinit var tracker: SelectionTracker<Long>

    private lateinit var categoryPickerAdapter: BudgetCategoryPickerAdapter

    private lateinit var chosenCategory: String



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_transaction, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryPickerAdapter = BudgetCategoryPickerAdapter(requireActivity())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val dateToggle = requireActivity().findViewById<TextView>(R.id.date_toggle)

        //
        val inflater = requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val numPad = inflater.inflate(R.layout.number_pad, number_pad_layout) as TableLayout
        val numberPad = numPad.getChildAt(0) as TableLayout


        transaction_button.setOnClickListener {
            transactionSubmit()
        }

        date_label.setOnClickListener {
            showDatePickerDialog()
        }

        dateToggle.setOnClickListener {
            showDatePickerDialog()
        }



        // Attach numberPad button handlers
        for (i in 0 until numberPad.childCount) {
            val row = numberPad.getChildAt(i) as TableRow

            Log.d("acaliTABLE", row.toString())

            for (j in 0 .. row.childCount-1) {
                val button = row.getChildAt(j) as Button
                Log.d("acaliTABLE", button.text.toString())
                button.setOnClickListener {
                    numberPadButtonHandler(it)
                }
            }
        }

        // Set Default date
        /*
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val displayMonth = c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ROOT).toString()

        val date = "$day $displayMonth $year"
        */
        val date = dateDisplay.display()
        date_label.text = date

        val sharedPref =
            activity?.getSharedPreferences(getString(R.string.api_token), Context.MODE_PRIVATE)
                ?: return
        val token = sharedPref.getString(getString(R.string.api_token), "")
        val url =
            "https://api.ledgr.site/budget?month=${Date().getCurrentMonth()}&year=${Date().getCurrentYear()}"

        horizontal_category_select.adapter = categoryPickerAdapter


        tracker = SelectionTracker.Builder<Long>("selectedItem",
            horizontal_category_select,
            StableIdKeyProvider(horizontal_category_select),
            // RecyclerViewIdKeyProvider(horizontal_category_select),
            PickerItemLookup(horizontal_category_select),
            // StorageStrategy.createStringStorage()
            StorageStrategy.createLongStorage()
        ).withSelectionPredicate(
            SelectionPredicates.createSelectAnything()
        ).build()

        categoryPickerAdapter.tracker = tracker


        tracker.addObserver(
            object : SelectionTracker.SelectionObserver<Long>() {
                override fun onSelectionChanged() {
                    super.onSelectionChanged()
                    //tracker.selection.isEmpty

                    // val selected = tracker.selection.toString()
                    val selected = try {
                        categoryPickerAdapter.bList[tracker.selection.last().toInt()].category.toString()
                    } catch (e: java.util.NoSuchElementException) {
                        ""
                    }
                    // val selected = categoryPickerAdapter.bList[tracker.selection.last().toInt()]

                    chosenCategory = selected


                    Log.d("acali", "Selection: ${selected}")


                    // Log.d("acali", selected)
                }
            }
        )




        dataRepository = DataBuilder()

        newTransactionViewModel = ViewModelProvider(this, NewTransactionViewModelFactory(requireActivity(), token!!.toString())).get(
            NewTransactionViewModel::class.java
        )

        newTransactionViewModel.get(url)

        newTransactionViewModel.categories.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            val budgetList: JsonArray = it as JsonArray
            val viewList = dataRepository.budgetPickerOptions(budgetList)
            categoryPickerAdapter.bList = viewList
            categoryPickerAdapter.notifyDataSetChanged()
        })
    }

    private fun numberPadButtonHandler(view: View) {
        view as Button
        // Log.d("acaliNUMPAD:", view.text.toString())
        when (view.text.toString()) {
            "1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "." -> numberPadClick(view.text.toString())
            // Backspace
            "\u02FF" -> numberPadBackspace()
        }
    }

    private fun transactionSubmit() {
        val form: MutableMap<String, TextInputEditText> = mutableMapOf()
        // val apiKey = arguments?.getString("api").toString().removeSurrounding("\"")
        // val username = arguments?.getString("username").toString().removeSurrounding("\"")
        val sharedPref = activity?.getSharedPreferences(getString(R.string.api_token), Context.MODE_PRIVATE) ?: return
        val token = sharedPref.getString(getString(R.string.api_token), "")
        form["desc"] = requireActivity().findViewById(R.id.description)

        // Validate user input
        form.forEach {
            if (form[it.key]?.text.toString() == "") {
                Toast.makeText(activity, "${form[it.key]?.hint.toString()} is required", Toast.LENGTH_LONG).show()
                return
            }
        }

        val toastDisplay = "$${amount_display.text} spent at ${form["desc"]?.text ?: "Nowhere"}"
        Toast.makeText(activity, toastDisplay, Toast.LENGTH_LONG ).show()

        // Format date for submitting to server
        val date = dateDisplay.getCompleteDate(date_label.text.toString())

        // Add data to JSON Object
        val json = JsonObject()
        json.addProperty("date", date)
        json.addProperty("amount", amount_display.text.toString())
        json.addProperty("description", form["desc"]?.text.toString())
        json.addProperty("category", chosenCategory ?: "Misc")

        form.forEach {
            form[it.key]?.setText("")
        }
        form["desc"]?.requestFocus()

        Log.d("acali-ledgrJSON: ", json.toString())

        val ledgr = LedgrDataSource(requireActivity(), token)
        val res = ledgr.post("https://api.ledgr.site/transactions", json)  // ledgr.newTransaction().postLegacy(json, this.startMainActivity())
        Log.d("acali-ledgrResult: ", res.toString())
        /*
        Ion.getDefault(activity).conscryptMiddleware.enable(false)
        Ion.with(activity)
            .load("POST","https://ledgr.site/api/activity/transaction?api_token=LHWmiGNoaVbrgYTv7qETIVpoNkJ8H9IB1Y3Ze72voXY5Oei8Pyl7gp2Apfpw")
            .setLogging("acali-API:", Log.INFO)
            .setJsonObjectBody(json)
            .asJsonObject()
            .setCallback { ex, result ->
                Log.i("acali-RESULT:", result.toString())
                if (ex != null) {
                    Log.i("acali-ERROR:", ex.message)
                }
                // Return to main activity
                startMainActivity()
            }

         */
        startMainActivity()
    }

    private fun showDatePickerDialog() {
        // val c = Calendar.getInstance()

        // Create a new instance of DatePickerDialog and return it
        val dpd = DatePickerDialog(requireActivity(), { _, year, month, day ->
            // val dateShow = "${year}-${monthOfYear+1}-${dayOfMonth}"
            val formattedDay = if (day - 10 >= 0) {
                day
            } else {
                "0${day}"
            }
            val formattedMonth = if (month + 1 >= 10  ) {
                month+1
            } else {
                "0${month+1}"
            }
            Log.d("acalidpd:", formattedDay.toString())
            Log.d("acalidpd:", formattedMonth.toString())
            val date = "${year}-${formattedMonth}-${formattedDay}"
            /*
            val date: String = if (day - 10 >= 0) {
                "${year}-${month+1}-${day}"
            } else {
                "${year}-${month+1}-0${day}"
            }

             */
            date_label.text = dateDisplay.displayAsString(date)

        }, dateDisplay.year, dateDisplay.month, dateDisplay.day)
        dpd.show()
    }

    private fun startMainActivity() {
        Log.d("acaliNewTransactionFragment", "starting MainActivity")
        val myIntent = Intent(activity, MainActivity::class.java)
        myIntent.putExtra("username", arguments?.getString("username").toString().removeSurrounding("\""))
        myIntent.putExtra("api", arguments?.getString("api").toString().removeSurrounding("\""))
        return startActivity(myIntent)
    }

    override fun onPause() {
        super.onPause()
        Log.d("acaliNewTransactionFragment", "onPause was called")
    }

    override fun onStop() {
        super.onStop()
        Log.d("acaliNewTransactionFragment", "onStop was called")
    }

    override fun onResume() {
        super.onResume()
        date.observe(this, {
            Log.d("acali-Observer", it)
            // date__label.text = it
        })
        Log.d("acaliNewTransactionFragment", "onResume was called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("acaliNewTransactionFragment", "onDestroy was called")
    }

    private fun numberPadClick(number:String) {
        val displayText = amount_display.text.toString()
        Log.i("acaliNUMPADCLICK", amount_display.text.toString())
        if ((number == "." && amount_display.text.contains('.'))) {
                return
        }
        else if (displayText.contains(Regex("^[\\d]+\\.[\\d]{2}")) && displayText != "0.00") {
            return
        }
        if (displayText == "0.00" || displayText == "0") {

            if (number == ".") {
                if (amount_display.text.contains('.')) {
                    return
                }
                else {
                    val new = "${amount_display.text}${number}"
                    amount_display.text = new
                }

            }
            else {
                amount_display.text = number
            }

        }
        else {
            val new = "${amount_display.text}${number}"
            amount_display.text = new
        }
    }

    private fun numberPadBackspace() {
        val displayText = amount_display.text.toString()
        amount_display.text = displayText.dropLast(1)
        if (amount_display.text == "") {
            amount_display.text = resources.getString(R.string.amount_label)
        }

    }

    /*

    private fun serializeDate(date: String): String {
        val c = Calendar.getInstance()
        val months = c.getDisplayNames(Calendar.MONTH, Calendar.LONG, Locale.ROOT)
        val dateArray = date.split(' ')
        Log.i("acaliserializeDate", dateArray.toString())
        val monthNum = months[dateArray[1]]
        if (monthNum != null) {
            return "${dateArray[2]}-${monthNum+1}-${dateArray[0]}"
        }
        return ""
    }

    private fun dateToString(date:String): String {
        val months = mutableListOf<String>("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")


        val dateArray = date.split('-')
        val monthString = months[dateArray[1].toInt() -1]
        return "${dateArray[2]} $monthString ${dateArray[0]}"
    }

     */
}

