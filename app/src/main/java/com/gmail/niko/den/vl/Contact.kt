package com.gmail.niko.den.vl

import java.util.*

data class Contact(
    val avatarUri: String,
    val contactId: String,
    val name: String,
    val firstPhoneNumber: String,
    val secondPhoneNumber: String,
    val firstEmailAddress: String,
    val secondEmailAddress: String,
    val birthday: GregorianCalendar,
    val description: String
)