package com.gmail.niko.den.vl

import android.app.NotificationManager
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), ContactListListener, ContactListServiceListener {
    private var getContactListService: GetContactListService? = null
    private lateinit var serviceConnection: ServiceConnection
    private var isBound = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.toolbar))

        serviceConnection = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                getContactListService =
                    (service as GetContactListService.GetContactListServiceBinder).service
                isBound = true
                if ( savedInstanceState == null) {
                    navigateToContactListFragment()
                    val contactId = intent.getStringExtra("CONTACT_ID")
                    contactId?.let {
                        navigateToContactDetailsFragment(contactId)
                    }
                }
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                getContactListService = null
                isBound = false
            }
        }

        val intent = Intent(this, GetContactListService::class.java)
        bindService(intent, serviceConnection, BIND_AUTO_CREATE)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            NotificationAssistant.createNotificationChannel(
                this,
                NotificationManager.IMPORTANCE_DEFAULT,
                getString(R.string.app_name),
                getString(R.string.app_notification_channel_description)
            )
        }
    }

    override fun navigateToContactListFragment() {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container_view, ContactListFragment())
                .commit()
    }

    override fun navigateToContactDetailsFragment(contactId: String) {
        supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.fragment_container_view,
                ContactDetailsFragment.newInstance(contactId))
            .addToBackStack("ContactDetail")
            .commit()
    }

    override fun serviceIsAvailable(): Boolean {
        return isBound
    }

    override suspend fun getContactList(): List<Contact>? {
        return getContactListService?.getContactList()
    }

    override suspend fun getContactDetails(contactId: String): Contact? {
        return getContactListService?.getContactDetails(contactId)
    }

    override fun onDestroy() {
        if (isBound) {
            unbindService(serviceConnection)
        }
        super.onDestroy()
    }
}