package com.gmail.niko.den.vl

//import android.app.TimePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.gmail.niko.den.vl.databinding.FragmentContactDetailsBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

/**
 * Фрагмент с детальной информацией о контакте
 */
class ContactDetailsFragment : Fragment(R.layout.fragment_contact_details) {
    private var contactListListener: ContactListListener? = null
    private var contactListServiceListener: ContactListServiceListener? = null
    private var contact: Contact? = null
    private var viewBinding: FragmentContactDetailsBinding? = null
    private var alarmAssistant: AlarmAssistant? = null

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
        alarmAssistant = AlarmAssistant(context)
    }

    private fun populateContactDetails() {
        viewBinding?.apply {

            contact?.let { contact ->
                contactImage.setImageURI(contact.avatarUri.toUri())
                contactName.text = contact.name
                contactPhoneNumber1.text = contact.firstPhoneNumber
                contactPhoneNumber2.text = contact.secondPhoneNumber
                contactEmail1.text = contact.firstEmailAddress
                contactEmail2.text = contact.secondEmailAddress
                contactBirthdayDate.text = getFormattedDate(contact.birthday)
                contactDescription.text = contact.description
                birthdayReminderSwitch.isVisible = true
                alarmAssistant?.let { alarmAssistant ->
                    birthdayReminderSwitch.isChecked = alarmAssistant.isAlarmUp(contact.contactId)
                }

                birthdayReminderSwitch.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
//                        val calendar = GregorianCalendar.getInstance().apply {
//                            TimePickerDialog(
//                                activity,
//                                0,
//                                { _, hour, min ->
//                                    this.set(GregorianCalendar.HOUR_OF_DAY, hour)
//                                    this.set(GregorianCalendar.MINUTE, min)
//                                },
//                                this.get(GregorianCalendar.HOUR_OF_DAY),
//                                this.get(GregorianCalendar.MINUTE),
//                                true
//                            ).show()
//                        }
                        alarmAssistant?.scheduleBirthdayAlarm(contact)
                    } else {
                        alarmAssistant?.cancelAlarm(contact)
                    }
                }
            }
        }
    }

    private fun getFormattedDate(calendar: GregorianCalendar): String {
        val formatter = SimpleDateFormat("dd MMM")
        formatter.calendar = calendar
        return formatter.format(calendar.time)
    }

    override fun onDestroyView() {
        alarmAssistant = null
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