package com.gmail.niko.den.vl

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.gmail.niko.den.vl.databinding.FragmentContactListBinding
import kotlinx.coroutines.*

class ContactListFragment : Fragment(R.layout.fragment_contact_list) {
    private var contactListListener: ContactListListener? = null
    private var contactListServiceListener: ContactListServiceListener? = null
    private var contactList: List<Contact>? = null
    private var viewBinding: FragmentContactListBinding? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ContactListListener && context is ContactListServiceListener) {
            contactListListener = context
            contactListServiceListener = context
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = FragmentContactListBinding.bind(view)

        viewLifecycleOwner.lifecycleScope.launch {
            contactList = withContext(Dispatchers.IO) {
                while (!(contactListServiceListener?.serviceIsAvailable() as Boolean)) {
                }
                contactListServiceListener?.getContactList()
            }
            populateContactList()
        }

        (activity as AppCompatActivity).supportActionBar?.title =
            getString(R.string.contact_list)
    }

    private fun populateContactList() {
        viewBinding?.apply {
            contactImage.setImageURI(contactList?.get(0)?.avatarUri?.toUri())
            contactName.text = contactList?.get(0)?.name
            contactPhoneNumber.text = contactList?.get(0)?.firstPhoneNumber
            contactCard.setOnClickListener {
                contactListListener?.navigateToContactDetailsFragment(
                    contactList?.get(0)?.contactId.toString()
                )
            }
        }
    }

    override fun onDestroyView() {
        viewBinding = null
        super.onDestroyView()
    }

    override fun onDetach() {
        contactListListener = null
        contactListServiceListener = null
        super.onDetach()
    }
}