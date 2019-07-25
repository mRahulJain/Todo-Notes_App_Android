package com.example.todo

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.Worker
import androidx.work.WorkerParameters

class AlarmReceiver(context: Context, workerParameters: WorkerParameters) : Worker(context, workerParameters) {
    val req = context

    override fun doWork(): Result {
        val nh = NotifyHelper(req)
        nh.getNotification()
        return Result.success()
    }
}