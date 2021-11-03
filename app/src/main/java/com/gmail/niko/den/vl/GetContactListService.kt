package com.gmail.niko.den.vl

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import kotlinx.coroutines.delay
import java.util.*

private val CONTACT_LIST: List<Contact> = listOf(
    Contact("1", "Name", "+7-(495)-098-61-22"))

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
        return CONTACT_LIST
    }

    suspend fun getContactDetails(contactId: String): Contact? {
        delay(2000) // эмуляция долгой работы
        return CONTACT_LIST.find { it.contactId == contactId }
    }
}