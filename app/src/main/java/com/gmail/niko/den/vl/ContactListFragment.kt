package com.gmail.niko.den.vl

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment

class ContactListFragment : Fragment(R.layout.fragment_contact_list) {
    private var contactListListener: ContactListListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ContactListListener) {
            contactListListener = context
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<ConstraintLayout>(R.id.contactCard)
            .setOnClickListener {
                contactListListener?.navigateToContactDetailsFragment("1")
            }

        (activity as AppCompatActivity).supportActionBar?.title =
            getString(R.string.contact_list)
    }

    override fun onDetach() {
        super.onDetach()
        contactListListener = null
    }
}