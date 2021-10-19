package com.gmail.niko.den.vl

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), ContactListListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.toolbar))
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
}