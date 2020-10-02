package com.example.crimecurber

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.util.*

class DbHandler(context: Context?) : SQLiteOpenHelper(context, Parameters.DB_NAME, null, Parameters.DB_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        val create = "CREATE TABLE " + Parameters.TABLE_NAME + "(" + Parameters.KEY_ID + " INTEGER PRIMARY KEY," + Parameters.KEY_NAME + " TEXT, " + Parameters.KEY_PHONE + " TEXT" + ")"
        db.execSQL(create)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}
    fun addContact(contact: Contacts) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(Parameters.KEY_NAME, contact.name)
        values.put(Parameters.KEY_PHONE, contact.phoneNumber)
        db.insert(Parameters.TABLE_NAME, null, values)
        db.close()
    }

    // Generate the query to read from the database
    val allContacts:

    //Loop through now
            List<Contacts>
        get() {
            val contactList: MutableList<Contacts> = ArrayList()
            val db = this.readableDatabase

            // Generate the query to read from the database
            val select = "SELECT * FROM " + Parameters.TABLE_NAME
            val cursor = db.rawQuery(select, null)

            //Loop through now
            if (cursor.moveToFirst()) {
                do {
                    val contact = Contacts()
                    contact.name = cursor.getString(1)
                    contact.phoneNumber = cursor.getString(2)
                    contactList.add(contact)
                } while (cursor.moveToNext())
            }
            cursor.close()
            return contactList
        }

    fun deleteContact(phone: String?) {
        val db = this.writableDatabase
        db.delete(Parameters.TABLE_NAME, Parameters.KEY_PHONE + "=?", arrayOf(phone.toString()))
        db.close()
    }
}