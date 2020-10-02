package com.example.crimecurber

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class Call : AppCompatActivity() {
    private var btnCall: Button? = null
    private var btnCall102 //button declaration
            : Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_call)
        btnCall = findViewById<View>(R.id.btnCall) as Button
        btnCall102 = findViewById<View>(R.id.btnCall102) as Button
        btnCall!!.setOnClickListener { makePhoneCall() }
        btnCall102!!.setOnClickListener { makePhoneCall102() }
    }

    // Method For Calling 102
    private fun makePhoneCall102() {
        if (ContextCompat.checkSelfPermission(this@Call,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this@Call, arrayOf(Manifest.permission.CALL_PHONE), REQUEST_CALL)
        } else {
            val dial = "tel:102"
            startActivity(Intent(Intent.ACTION_CALL, Uri.parse(dial)))
        }
    }

    //Method For Calling 100
    private fun makePhoneCall() {
        if (ContextCompat.checkSelfPermission(this@Call,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this@Call, arrayOf(Manifest.permission.CALL_PHONE), REQUEST_CALL)
        } else {
            val dial = "tel:100"
            startActivity(Intent(Intent.ACTION_CALL, Uri.parse(dial)))
        }
    }

    //Permission request
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == REQUEST_CALL) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall()
                makePhoneCall102()
            } else {
                Toast.makeText(this, "PERMISSION DENIED", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val REQUEST_CALL = 1 // Constant required to call
    }
}