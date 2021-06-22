package com.example.ledgr


import android.content.*
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

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

        changeBroadcastReceiver = ChangeBroadcastReceiver()
        val intentFilter: IntentFilter = IntentFilter()
        intentFilter.addAction("com.example.ledgr.spendingnotificationservice")
        registerReceiver(changeBroadcastReceiver, intentFilter)

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
            val receivedNotificationCode = intent?.getIntExtra("Notification code", -1)
            Log.d("acali", "Main Activity ${receivedNotificationCode.toString()}")
        }

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

        unregisterReceiver(changeBroadcastReceiver)
    }

    override fun onRestart() {
        super.onRestart()
        Log.d("acali-MainActivity", "onRestart was called")
    }



}
