package com.gmail.niko.den.vl

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment

/**
 * Фрагмент с детальной информацией о контакте
 */
class ContactDetailsFragment : Fragment(R.layout.fragment_contact_details) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title =
            getString(R.string.contact_details)
    }

    companion object {
        fun newInstance(contactId: String) = ContactDetailsFragment().apply {
            arguments = bundleOf("CONTACT_ID" to contactId)
        }
    }
}