package com.gmail.niko.den.vl

interface ContactListListener {
    fun navigateToContactListFragment()

    fun navigateToContactDetailsFragment(contactId: String)
}