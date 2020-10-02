package com.example.crimecurber

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class Direction : AppCompatActivity() {
    private var btnPolice: Button? = null
    private var btnMedical: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_direction)
        btnPolice = findViewById<View>(R.id.btnPolice) as Button
        btnMedical = findViewById<View>(R.id.btnMedical) as Button
        btnPolice!!.setOnClickListener { directPoliceStation() }
        btnMedical!!.setOnClickListener { directHospital() }
    }

    private fun directHospital() {
        if (ContextCompat.checkSelfPermission(this@Direction,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this@Direction, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        } else {
            val gmmIntentUri = Uri.parse("geo:0,0?q=Hospital")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }
    }

    private fun directPoliceStation() {
        if (ContextCompat.checkSelfPermission(this@Direction,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this@Direction, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        } else {
            val gmmIntentUri = Uri.parse("geo:0,0?q=Police Station")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
}