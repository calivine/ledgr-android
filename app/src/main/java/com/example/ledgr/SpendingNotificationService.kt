package com.example.ledgr

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log

class SpendingNotificationService : NotificationListenerService() {
    /**
    private val binder = LocalBinder()

    inner class LocalBinder : Binder() {
    // Return this instance of LocalService so clients can call public methods
    fun getService(): SpendingNotificationService = this@SpendingNotificationService

    } */

    inner class ApplicationPackageNames {
        val CAPITAL_ONE = "com.konylabs.capitalone"
    }

    inner class InterceptedNotificationCode {
        val CAPITAL_ONE_CODE = 1
        val OTHER_NOTIFICATIONS_CODE = 2
    }

    /**
    override fun onBind(intent: Intent): IBinder {
    Log.d("acali", "SpendingNotificationService - onBind")
    val an = activeNotifications
    return binder
    }
     */

    override fun onListenerConnected() {
        super.onListenerConnected()
        Log.d("acali", "SpendingNotificationService - onListenerConnected")
        val an = activeNotifications
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?, rankingMap: RankingMap?) {
        super.onNotificationPosted(sbn, rankingMap)
        Log.d("acali", "SpendingNotificationService - onNotificationPosted")
        val notificationCode = sbn?.let { matchNotificationCode(it) }

        if (notificationCode != InterceptedNotificationCode().OTHER_NOTIFICATIONS_CODE) {
            val notificationIntent =
                Intent("com.example.ledgr.spendingnotificationservice")
            // notificationIntent.putExtra("Notification code", notificationCode)
            notificationIntent.apply {
                this.putExtra("code", notificationCode)
                this.putExtra(
                    "title",
                    sbn?.notification?.extras?.getString("android.title")
                )
                this.putExtra(
                    "text",
                    sbn?.notification?.extras?.getCharSequence("android.text").toString()
                )
            }
            sendBroadcast(notificationIntent)
        }
    }


    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        super.onNotificationRemoved(sbn)
        Log.d("acali", "SpendingNotificationService - onNotificationRemoved")
        // Get the code for StatusBarNotification that was removed.
        val notificationCode = matchNotificationCode(sbn)

        // Let's check if this Notification is the one we're looking for...
        if (notificationCode != InterceptedNotificationCode().OTHER_NOTIFICATIONS_CODE) {
            // val activeNotifications = emptyArray<StatusBarNotification>()
            // If there are active Notifications
            if (activeNotifications.isNotEmpty()) {
                for (i in activeNotifications.indices) {
                    // Check if code from list of active Notifications
                        // matches the one that was just removed.
                    if (notificationCode == matchNotificationCode(activeNotifications[i])) {
                        // Make an Intent to broadcast back to listener in MainActivity.
                        val notificationIntent =
                            Intent("com.example.ledgr.spendingnotificationservice")
                        // notificationIntent.putExtra("Notification code", notificationCode)
                        notificationIntent.apply {
                            this.putExtra("code", notificationCode)
                            this.putExtra(
                                "title",
                                sbn.notification.extras.getString("android.title")
                            )
                            this.putExtra(
                                "text",
                                sbn.notification.extras.getCharSequence("android.text").toString()
                            )
                        }
                        sendBroadcast(notificationIntent)
                        break
                    }
                }
            }
        }
    }

    private fun matchNotificationCode(sbn: StatusBarNotification): Int {
        val packageName: String = sbn.packageName

        return if (packageName == ApplicationPackageNames().CAPITAL_ONE) {
            InterceptedNotificationCode().CAPITAL_ONE_CODE
        } else {
            InterceptedNotificationCode().OTHER_NOTIFICATIONS_CODE
        }
    }

}