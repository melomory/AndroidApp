package com.gmail.niko.den.vl

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import kotlinx.coroutines.delay
import java.util.*

private val contactList: List<Contact> = listOf(
    Contact(
        avatarUri = "android.resource://com.gmail.niko.den.vl/" + R.drawable.android_logo_icon,
        contactId = "1",
        name = "Иванов Иван Иванович",
        firstPhoneNumber = "+7-(495)-098-61-22",
        secondPhoneNumber = "+7-(495)-098-61-54",
        firstEmailAddress = "one@example.com",
        secondEmailAddress = "two@example.com",
        birthday = GregorianCalendar.getInstance().apply {
            set(1986, 10, 19)
        } as GregorianCalendar,
        description = "описание контакта"))

class GetContactListService : Service() {
    private val binder = GetContactListServiceBinder()

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    inner class GetContactListServiceBinder : Binder() {
        val service = this@GetContactListService
    }

    suspend fun getContactList(): List<Contact> {
        delay(2000) // эмуляция долгой работы
        return contactList
    }

    suspend fun getContactDetails(contactId: String): Contact? {
        delay(2000) // эмуляция долгой работы
        return contactList.find { it.contactId == contactId }
    }
}