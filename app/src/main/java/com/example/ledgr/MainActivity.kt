package com.example.ledgr


import android.app.Activity
import android.content.*
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.ledgr.adapters.PendingTransactionsAdapter
import com.example.ledgr.data.LedgrDataSource
import com.example.ledgr.data.model.PendingTransaction
import com.example.ledgr.ui.widget.ApproveTransactionDialog
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_navigation.*
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.notification_card.*


class MainActivity : AppCompatActivity(), ApproveTransactionDialog.ApproveTransactionListener, PendingTransactionsAdapter.PendingDialogListener {

    lateinit var selectedFragment: Fragment
    private lateinit var navController: NavController

    private val ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners"
    private val ACTION_NOTIFICATION_LISTENER_SETTINGS = "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"

    private lateinit var changeBroadcastReceiver: ChangeBroadcastReceiver
    private lateinit var enableNotificationListenerAlertDialog: AlertDialog




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setTheme(R.style.AppTheme_Blue)
        val sharedPref =
            getSharedPreferences(getString(R.string.api_token), Context.MODE_PRIVATE)
                ?: return
        val theme = sharedPref.getInt(getString(R.string.current_theme), R.style.AppTheme)
        setTheme(theme)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.main_toolbar))
        supportActionBar!!.title = ""


        /**
        val api = intent.getStringExtra("api")


        val bundle = Bundle().apply {
            this.putString("api", api)
        }
        */

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.layout_frame) as NavHostFragment
        navController = navHostFragment.navController

        val bottomNavigationView = bottom_navigation
        bottomNavigationView.setupWithNavController(navController)

        if (!isNotificationServiceEnabled()) {
            enableNotificationListenerAlertDialog = buildNotificationServiceAlertDialog()
            enableNotificationListenerAlertDialog.show()
        }
        /**
        changeBroadcastReceiver = ChangeBroadcastReceiver()
        val intentFilter: IntentFilter = IntentFilter().apply {
            addAction("com.example.ledgr.spendingnotificationservice")
        }
        // intentFilter.addAction("com.example.ledgr.spendingnotificationservice")
        registerReceiver(changeBroadcastReceiver, intentFilter)
        */


    }

    private fun isNotificationServiceEnabled(): Boolean {
        val packageName = packageName
        val flat = Settings.Secure.getString(contentResolver, ENABLED_NOTIFICATION_LISTENERS)
        if (!TextUtils.isEmpty(flat)) {
            val names = flat.split(":")
            for (i in 0 until names.size) {
                val cn = ComponentName.unflattenFromString(names[i])
                if (cn != null) {
                    if (TextUtils.equals(packageName, cn.packageName)) {
                        return true
                    }
                }

            }
        }
        return false
    }

    class ChangeBroadcastReceiver(): BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d("acali-MainActivity.ChangeBroadcastReceiver", "onReceive was called")
            val receivedNotificationCode = intent?.getIntExtra("code", -1)
            val title = intent?.getStringExtra("title")
            val text = intent?.getStringExtra("text")



            Log.d("acali", "Main Activity ${receivedNotificationCode.toString()} $title $text")

            context as Activity

            val sharedPref =
                context.getSharedPreferences(context.getString(R.string.api_token), Context.MODE_PRIVATE)
                    ?: return
            val token = sharedPref.getString(context.getString(R.string.api_token), "")

            // val newCard = CardView(context)
            // val cardText = TextView(context)
            // cardText.text = text

            // newCard.addView(cardText)

            // context.recent_purchases.text = text
            // context.new_spending_layout.addView(newCard)
            // context.new_spending_layout.visibility = View.VISIBLE
            // context.new_spending_layout.getChildAt(0).visibility = View.VISIBLE
            // context.new_spending_layout.getChildAt(1).visibility = View.VISIBLE
            // context.recent_purchases.visibility = View.VISIBLE
            /**
            val requestBody: JsonObject = JsonObject()
            requestBody.addProperty("text", text)


            val response = LedgrDataSource(context, token).post("https://api.ledgr.site/transactions/pending", requestBody)
            */
            // Log.d("acali", "MainActivity $response")


        }
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        Log.d("acali", "onUserInteraction")
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.d("acali", "onNewIntent $intent")
    }


    private fun buildNotificationServiceAlertDialog(): AlertDialog {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle(R.string.notification_listener_service)
        alertDialogBuilder.setMessage(R.string.notification_listener_service_explanation)
        alertDialogBuilder.setPositiveButton(R.string.yes, DialogInterface.OnClickListener { dialog, which ->
            startActivity(Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS))
        })

        alertDialogBuilder.setNegativeButton(R.string.no, DialogInterface.OnClickListener { dialog, which ->

        })

        return alertDialogBuilder.create()
    }



    override fun onPause() {
        super.onPause()
        Log.d("acali-MainActivity", "onPause was called")
    }

    override fun onStop() {
        super.onStop()
        Log.d("acali-MainActivity", "onStop was called")
    }

    override fun onResume() {
        super.onResume()

        Log.d("acali-MainActivity", "onResume was called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("acali-MainActivity", "onDestroy was called")

        // unregisterReceiver(changeBroadcastReceiver)
    }

    override fun onRestart() {
        super.onRestart()
        Log.d("acali-MainActivity", "onRestart was called")
    }

    override fun onDialogPositiveClick(dialog: DialogFragment, position: Int) {
        Log.d("acali", "onDialogPositiveClick $dialog was returned")
        val frag = supportFragmentManager.findFragmentById(R.id.layout_frame)
        val frags = frag?.childFragmentManager?.fragments!![0] as DashboardFragment
        frags.removeFromAdapter(position)

    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {
        Log.d("acali", "MainActivity negative click")

    }

    override fun onCreateDialog() {
        Log.d("acali main", "onCreateDialog")
    }

    override fun onItemClicked(transaction: PendingTransaction) {
        Log.d("acali main", "$transaction")

    }




}
