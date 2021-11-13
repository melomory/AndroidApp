package com.gmail.niko.den.vl

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import java.util.*

private const val TIME_IN_SECONDS = 10000

class AlarmAssistant(private val context: Context?) {

    fun isAlarmUp(contactId: String): Boolean {
        val intent = Intent(context, ContactBirthdayReminderReceiver::class.java).apply {
            putExtra("CONTACT_ID", contactId)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            contactId.toInt(),
            intent,
            PendingIntent.FLAG_NO_CREATE
        )
        return pendingIntent != null
    }

//    fun scheduleBirthdayAlarm(contact: Contact, calendar: Calendar) {
//        val (day, month) = parseDate(contact.birthday.toString())
//        val hour = calendar.get(GregorianCalendar.HOUR_OF_DAY)
//        val min = calendar.get(GregorianCalendar.MINUTE)
//        setAlarm(
//            getTriggerTimeAlarm(day, month, hour, min).timeInMillis,
//            createAlarmPendingIntent(contact, null, calendar)
//        )
//    }

    fun scheduleBirthdayAlarm(contact: Contact) {
        val day = contact.birthday.get(GregorianCalendar.DAY_OF_MONTH)
        val month = contact.birthday.get(GregorianCalendar.MONTH)
        val hour = contact.birthday.get(GregorianCalendar.HOUR_OF_DAY)
        val min = contact.birthday.get(GregorianCalendar.MINUTE)
        setAlarm(
            getTriggerTimeAlarm(day, month, hour, min).timeInMillis,
            createAlarmPendingIntent(contact, null)
        )
    }

    fun rescheduleBirthdayAlarm(intent: Intent) {
        val date = intent.getStringExtra("BIRTHDAY_DATE")
        val hour = intent.getStringExtra("REMINDER_HOURS")?.toInt() as Int
        val min = intent.getStringExtra("REMINDER_MINUTES")?.toInt() as Int
        date?.let { date ->
            val (day, month) = parseDate(date)

            setAlarm(
                getTriggerTimeAlarm(day, month, hour, min).timeInMillis,
                createAlarmPendingIntent(null, intent)
            )
        }
    }

    private fun setAlarm(timeInMillisToAlarm: Long, pendingIntent: PendingIntent) {
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            timeInMillisToAlarm,
            pendingIntent
        )
        Log.d("ALARM_SET", "Alarm is set")
    }

    private fun getTriggerTimeAlarm(day: Int, month: Int, hour: Int, min: Int): Calendar {
        val triggerAlarmTime = GregorianCalendar.getInstance(Locale.getDefault()).apply {
            timeInMillis = System.currentTimeMillis()
            set(GregorianCalendar.MONTH, month)
            set(GregorianCalendar.HOUR_OF_DAY, hour)
            set(GregorianCalendar.MINUTE, min)
            set(GregorianCalendar.SECOND, 0)
            set(GregorianCalendar.MILLISECOND, 0)
        }

        val currentTime = GregorianCalendar.getInstance(Locale.getDefault()).apply {
            timeInMillis = System.currentTimeMillis()
        }
        triggerAlarmTime.timeInMillis = currentTime.timeInMillis + TIME_IN_SECONDS
        return triggerAlarmTime
    }

//    private fun getTriggerTimeAlarm(day: Int, month: Int, hour: Int, min: Int): Calendar {
//        val triggerAlarmTime = GregorianCalendar.getInstance(Locale.getDefault()).apply {
//            timeInMillis = System.currentTimeMillis()
//            set(GregorianCalendar.MONTH, month - 1)
//            set(GregorianCalendar.HOUR_OF_DAY, hour)
//            set(GregorianCalendar.MINUTE, min)
//            set(GregorianCalendar.SECOND, 0)
//            set(GregorianCalendar.MILLISECOND, 0)
//        }
//
//        val currentTime = GregorianCalendar.getInstance(Locale.getDefault()).apply {
//            timeInMillis = System.currentTimeMillis()
//        }
//
//        if (triggerAlarmTime.before(currentTime)) {
//            triggerAlarmTime.add(GregorianCalendar.YEAR, 1)
//            if (isBirthdayInLeapYear(day, month)) {
//                triggerAlarmTime.add(
//                    GregorianCalendar.YEAR,
//                    yearsToLeapYear(triggerAlarmTime.get(GregorianCalendar.YEAR))
//                )
//            }
//        }
//        triggerAlarmTime.set(GregorianCalendar.DAY_OF_MONTH, day)
//        return triggerAlarmTime
//    }

    private fun createAlarmPendingIntent(
        contact: Contact?,
        intent: Intent?,
    ): PendingIntent {
        val alarmIntent = Intent(context, ContactBirthdayReminderReceiver::class.java)
        var contactId = ""
        contact?.let {
            contactId = it.contactId
            alarmIntent.apply {
                putExtra("CONTACT_ID", contactId)
                putExtra("REMINDER_MESSAGE", "${R.string.notification_message} ${it.name}")
                putExtra("BIRTHDAY_DATE", it.birthday)
                putExtra("REMINDER_HOURS", "${contact.birthday?.get(GregorianCalendar.HOUR_OF_DAY)}")
                putExtra("REMINDER_MINUTES", "${contact.birthday?.get(GregorianCalendar.MINUTE)}")
            }
        }
        intent?.let {
            contactId = it.getStringExtra("CONTACT_ID") as String
            alarmIntent.apply {
                putExtra("CONTACT_ID", contactId)
                putExtra("REMINDER_MESSAGE", it.getStringExtra("REMINDER_MESSAGE"))
                putExtra("BIRTHDAY_DATE", it.getStringExtra("BIRTHDAY_DATE"))
                putExtra("REMINDER_HOURS", it.getStringExtra("REMINDER_HOURS"))
                putExtra("REMINDER_MINUTES", it.getStringExtra("REMINDER_MINUTES"))
            }
        }
        return PendingIntent.getBroadcast(
            context,
            contactId.toInt(),
            alarmIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

//    private fun createAlarmPendingIntent(
//        contact: Contact?,
//        intent: Intent?,
//        calendar: Calendar?,
//    ): PendingIntent {
//        val alarmIntent = Intent(context, ContactBirthdayReminderReceiver::class.java)
//        var contactId = ""
//        contact?.let {
//            contactId = it.contactId
//            alarmIntent.apply {
//                putExtra("CONTACT_ID", contactId)
//                putExtra("REMINDER_MESSAGE", ""${R.string.notification_message} ${it.name}"")
//                putExtra("BIRTHDAY_DATE", it.birthday)
//                putExtra("REMINDER_HOURS", "${calendar?.get(GregorianCalendar.HOUR_OF_DAY)}")
//                putExtra("REMINDER_MINUTES", "${calendar?.get(GregorianCalendar.MINUTE)}")
//            }
//        }
//        intent?.let {
//            contactId = it.getStringExtra("CONTACT_ID") as String
//            alarmIntent.apply {
//                putExtra("CONTACT_ID", contactId)
//                putExtra("REMINDER_MESSAGE", it.getStringExtra("REMINDER_MESSAGE"))
//                putExtra("BIRTHDAY_DATE", it.getStringExtra("BIRTHDAY_DATE"))
//                putExtra("REMINDER_HOURS", it.getStringExtra("REMINDER_HOURS"))
//                putExtra("REMINDER_MINUTES", it.getStringExtra("REMINDER_MINUTES"))
//            }
//        }
//        return PendingIntent.getBroadcast(
//            context,
//            contactId.toInt(),
//            alarmIntent,
//            PendingIntent.FLAG_UPDATE_CURRENT
//        )
//    }

    private fun isBirthdayInLeapYear(day: Int, month: Int): Boolean {
        return day == 29 && month == 2
    }

    private fun yearsToLeapYear(currentYear: Int): Int {
        var years = 1
        while (!GregorianCalendar().isLeapYear(currentYear + years)) {
            ++years
        }
        return years
    }

    private fun parseDate(date: String): List<Int> {
        val parsed = date.split(" ")
        val day = parsed[0].toInt()
        val month = parsed[1].toInt()
        return listOf(day, month)
    }

    fun cancelAlarm(contact: Contact) {
        (context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager).cancel(
            createAlarmPendingIntent(contact, null)
        )
    }
}