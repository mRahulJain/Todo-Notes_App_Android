package com.example.todo

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val nh = NotifyHelper(context)
        nh.getNotification()
    }

}