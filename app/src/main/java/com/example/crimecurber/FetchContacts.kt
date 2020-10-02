package com.example.crimecurber

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.crimecurber.FetchContacts
import java.util.*

class FetchContacts : AppCompatActivity() {
    var db = DbHandler(this@FetchContacts)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fetch_contacts)
        val contacts = ArrayList<String>()

        // Get all contacts
        val allContacts = db.allContacts
        val listView = findViewById<ListView>(R.id.listView)
        for (contact in allContacts!!) {
            contacts.add(contact.name + " (" + contact.phoneNumber + ")")
        }
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, contacts)
        listView.adapter = arrayAdapter
        intent
    }

    fun setDb(db: DbHandler) {
        this.db = db
    }
}