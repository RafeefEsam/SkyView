package com.example.skyview.workmanager

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager

class NotificationBroadCast: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notificationWork = OneTimeWorkRequest.Builder(NotificationWorker::class.java)
            .build()
        WorkManager.getInstance(context!!).enqueue(notificationWork)
    }
}