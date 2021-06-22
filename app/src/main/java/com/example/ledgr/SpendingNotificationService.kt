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
        val notificationCode = sbn?.let { matchNotificationCode(it) }
        Log.d("acali", "SpendingNotificationService - onNotificationPosted")
        if (notificationCode != InterceptedNotificationCode().OTHER_NOTIFICATIONS_CODE) {
            val notificationIntent = Intent("com.example.ledgr.spendingnotificationservice")
            notificationIntent.putExtra("Notification code", notificationCode)
            sendBroadcast(notificationIntent)
        }
    }




    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        val notificationCode = matchNotificationCode(sbn)
        if (notificationCode != InterceptedNotificationCode().OTHER_NOTIFICATIONS_CODE) {
            val activeNotifications = emptyArray<StatusBarNotification>()
            if (activeNotifications.isNotEmpty()) {
                for (i in activeNotifications.indices) {
                    if (notificationCode == matchNotificationCode(activeNotifications[i])) {
                        val notificationIntent = Intent("com.example.ledgr.spendingnotificationservice")
                        notificationIntent.putExtra("Notification code", notificationCode)
                        sendBroadcast(notificationIntent)
                        break
                    }
                }
            }
        }
    }

    private fun matchNotificationCode(sbn: StatusBarNotification) : Int {
        val packageName: String = sbn.packageName
        val key = sbn.key
        if (packageName == ApplicationPackageNames().CAPITAL_ONE) {
            return InterceptedNotificationCode().CAPITAL_ONE_CODE

        }
        else {
            return InterceptedNotificationCode().OTHER_NOTIFICATIONS_CODE
        }
    }

}