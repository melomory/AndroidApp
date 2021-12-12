package com.gmail.niko.den.vl

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class ContactBirthdayReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        NotificationAssistant.createNotification(
            context,
            intent,
            intent.getStringExtra("REMINDER_MESSAGE")
        )

        //AlarmAssistant(context).rescheduleBirthdayAlarm(intent)
    }
}

