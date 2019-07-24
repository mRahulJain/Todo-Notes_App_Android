package com.example.todo

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat

class NotifyHelper(base: Context?) : ContextWrapper(base) {

    fun getNotification() {
        val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            nm.createNotificationChannel(NotificationChannel("first","Todo Notifier",NotificationManager.IMPORTANCE_DEFAULT))
        }

        val i = Intent(this, MainActivity::class.java)

        val pi = PendingIntent.getActivity(this, 123, i, PendingIntent.FLAG_UPDATE_CURRENT)
        val clickableNotification =  NotificationCompat.Builder(this, "first")
            .setContentTitle("Todo")
            .setContentText("You have a work to be completed.")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setOngoing(true)
            .build()

        nm.notify(2, clickableNotification)
    }

}