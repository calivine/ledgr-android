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
import androidx.preference.Preference
import androidx.preference.PreferenceDataStore
import androidx.preference.PreferenceManager
import com.example.ledgr.adapters.PendingTransactionsAdapter
import com.example.ledgr.data.LedgrDataSource
import com.example.ledgr.data.model.PendingTransaction
import com.example.ledgr.ui.widget.ApproveTransactionDialog
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_navigation.*
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.notification_card.*


class MainActivity : AppCompatActivity(), ApproveTransactionDialog.ApproveTransactionListener, PendingTransactionsAdapter.PendingDialogListener, Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener {

    private lateinit var navController: NavController

    private val ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners"
    private val ACTION_NOTIFICATION_LISTENER_SETTINGS = "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"

    private lateinit var enableNotificationListenerAlertDialog: AlertDialog
    private lateinit var sharedPreferences: SharedPreferences

    companion object {
        const val TAG = "acali MainActivity"
    }


    override fun onCreate(savedInstanceState: Bundle?) {

        // setTheme(R.style.AppTheme_Blue)
        val sharedPref =
            getSharedPreferences(getString(R.string.api_token), Context.MODE_PRIVATE)
                ?: return

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        sharedPreferences.registerOnSharedPreferenceChangeListener { sharedPreferences, key ->


        }
        val prefTheme = PreferenceManager.getDefaultSharedPreferences(this).getString("theme", "AppTheme")

        Log.e(TAG, "$prefTheme")


        var theme = sharedPref.getInt(getString(R.string.current_theme), R.style.AppTheme)
        Log.e(TAG, "$theme")
        // setTheme(theme)
        theme = R.style.AppTheme
        val tesTheme = resources.getIdentifier(prefTheme, "style", this.packageName)
        Log.e(TAG, "$theme = $tesTheme")
        setTheme(tesTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.main_toolbar))
        supportActionBar!!.title = ""

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.layout_frame) as NavHostFragment
        navController = navHostFragment.navController

        val bottomNavigationView = bottom_navigation
        bottomNavigationView.setupWithNavController(navController)

        if (!isNotificationServiceEnabled()) {
            enableNotificationListenerAlertDialog = buildNotificationServiceAlertDialog()
            enableNotificationListenerAlertDialog.show()
        }

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

    override fun onUserInteraction() {
        super.onUserInteraction()
        Log.d(TAG, "onUserInteraction")
    }



    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.d(TAG, "onNewIntent $intent")
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
        Log.d(TAG, "onPause was called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop was called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume was called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy was called")

        // unregisterReceiver(changeBroadcastReceiver)
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart was called")
    }

    override fun onDialogPositiveClick(dialog: DialogFragment, position: Int) {
        Log.d(TAG, "onDialogPositiveClick $dialog was returned")
        val frag = supportFragmentManager.findFragmentById(R.id.layout_frame)
        val frags = frag?.childFragmentManager?.fragments!![0] as DashboardFragment
        frags.removeFromAdapter(position)

    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {
        Log.d(TAG, "MainActivity negative click")

    }

    override fun onCreateDialog() {
        Log.d(TAG, "onCreateDialog")
    }

    override fun onItemClicked(transaction: PendingTransaction) {
        Log.d(TAG, "$transaction")

    }

    override fun onPreferenceClick(preference: Preference?): Boolean {
        preference?.onPreferenceChangeListener = this
        Log.e(TAG, "Pending Preference value is: ")
        return true
    }

    override fun onPreferenceChange(preference: Preference?, newValue: Any?): Boolean {
        preference?.onPreferenceChangeListener = this
        Log.e(TAG, "Pending Preference value is: $newValue")

        return true
    }






}
