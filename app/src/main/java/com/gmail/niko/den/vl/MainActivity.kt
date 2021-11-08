package com.gmail.niko.den.vl

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
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
                }
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                getContactListService = null
                isBound = false
            }
        }

        val intent = Intent(this, GetContactListService::class.java)
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
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