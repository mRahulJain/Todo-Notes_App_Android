package com.example.todo

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import androidx.core.app.NotificationCompat

class NotifyHelper(base: Context?) : ContextWrapper(base) {

    var nm : NotificationManager? = null

    @TargetApi(Build.VERSION_CODES.O)
    fun createChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)  {
            val channel = NotificationChannel("first","default" , NotificationManager.IMPORTANCE_DEFAULT)
            channel.enableLights(true)
            channel.enableVibration(true)
            channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
            channel.lightColor = android.R.color.holo_blue_light

            notificationManager()!!.createNotificationChannel(channel)
        }
    }

    fun notificationManager() : NotificationManager? {
        if(nm == null) {
            nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        }
        return nm
    }

    fun getNotification() : NotificationCompat.Builder {
        return NotificationCompat.Builder(applicationContext, "first")
            .setContentTitle("first")
            .setContentText("NOTIFICATION")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
    }

}