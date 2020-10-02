package com.example.crimecurber

import java.io.Serializable

object Parameters : Serializable {
    const val DB_VERSION = 1
    const val DB_NAME = "contacts_db"
    const val TABLE_NAME = "contacts_table"

    //Keys of our table in db
    const val KEY_ID = "id"
    const val KEY_NAME = "name"
    const val KEY_PHONE = "phone_number"
}