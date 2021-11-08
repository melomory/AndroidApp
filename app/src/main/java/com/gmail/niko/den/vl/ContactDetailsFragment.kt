package com.gmail.niko.den.vl

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.gmail.niko.den.vl.databinding.FragmentContactDetailsBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Фрагмент с детальной информацией о контакте
 */
class ContactDetailsFragment : Fragment(R.layout.fragment_contact_details) {
    private var contactListListener: ContactListListener? = null
    private var contactListServiceListener: ContactListServiceListener? = null
    private var contact: Contact? = null
    private var viewBinding: FragmentContactDetailsBinding? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ContactListListener && context is ContactListServiceListener) {
            contactListListener = context
            contactListServiceListener = context
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = FragmentContactDetailsBinding.bind(view)

        viewLifecycleOwner.lifecycleScope.launch {
            contact = withContext(Dispatchers.IO) {
                while (!(contactListServiceListener?.serviceIsAvailable() as Boolean)) {
                }
                contactListServiceListener?.getContactDetails(arguments?.getString("CONTACT_ID")
                    .toString())
            }
            populateContactDetails()
        }

        (activity as AppCompatActivity).supportActionBar?.title =
            getString(R.string.contact_details)
    }

    private fun populateContactDetails() {
        viewBinding?.apply {
            contactImage.setImageResource(R.drawable.android_logo_icon)
            contactName.text = contact?.name
            contactPhoneNumber1.text = contact?.phoneNumber
            contactPhoneNumber2.text = getString(R.string.contact_details_phone_number_2_example)
            contactEmail1.text = getString(R.string.contact_details_email_1_example)
            contactEmail2.text = getString(R.string.contact_details_email_2_example)
            contactDescription.text = getString(R.string.contact_details_description)
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

    companion object {
        fun newInstance(contactId: String) = ContactDetailsFragment().apply {
            arguments = bundleOf("CONTACT_ID" to contactId)
        }
    }
}