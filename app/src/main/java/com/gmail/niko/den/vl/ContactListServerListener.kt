package com.gmail.niko.den.vl

interface ContactListServiceListener {
    fun serviceIsAvailable(): Boolean

    suspend fun getContactList(): List<Contact>?

    suspend fun getContactDetails(contactId: String): Contact?
}