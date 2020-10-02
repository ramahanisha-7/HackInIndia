package com.example.crimecurber

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.crimecurber.Emergency
import com.example.crimecurber.FetchContacts

class Emergency : AppCompatActivity(), LocationListener {
    var db = DbHandler(this@Emergency)
    var copy_phone = ""
    var i = 1
    var permissions = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.SEND_SMS,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION)
    val SEND_SMS_PERMISSION_REQUEST_CODE = 111
    private val TRACK_LOCATION_REQUEST_CODE = 1
    private var sos_btn: Button? = null
    private var fetchBtn: Button? = null
    protected var locationManager: LocationManager? = null
    var GlobalMessage: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emergency)
        fetchBtn = findViewById<View>(R.id.fetchBtn) as Button
        fetchBtn!!.setOnClickListener {
            if (FetchContacts() != null) {
                val fetchContacts: FetchContacts? = FetchContacts()
                fetchContacts!!.setDb(db)
                DisplayContacts()
            } else Toast.makeText(applicationContext, "No contacts added", Toast.LENGTH_SHORT).show()
        }
        sos_btn = findViewById(R.id.sos_btn)
        if (checkLocation()) sos_btn.setEnabled(true) else requestLocPermission()
        if (checkPermission()) {
            sos_btn.setEnabled(true)
        } else {
            requestSmsPermission()
        }
        sos_btn.setOnClickListener(View.OnClickListener {
            FetchContacts()
            for (contacts in FetchContacts()!!) {
                val message = "Help me, I am sending you my location \n$GlobalMessage"
                val phoneNo = contacts.getPhoneNumber()
                if (checkPermission()) {
                    val smsManager = SmsManager.getDefault()
                    smsManager.sendTextMessage(phoneNo, null, message, null, null)
                } else {
                    Toast.makeText(this@Emergency, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        })
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), PackageManager.PERMISSION_GRANTED)
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.INTERNET), PackageManager.PERMISSION_GRANTED)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
            if (locationManager != null) {
                locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, this)
            }
            if (locationManager != null) {
                locationManager!!.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f, this)
            }
        } else {
            // Permission to access the location is missing. Show rationale and request permission
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true)
        }
    }

    fun AddContacts(view: View?) {
        val ContactName = findViewById<EditText>(R.id.name)
        val name = ContactName.text.toString()
        val phone_number = findViewById<EditText>(R.id.phone_number)
        val phoneNo = phone_number.text.toString()
        if (copy_phone != phoneNo) {
            if (i <= 5) {
                val con = Contacts()
                con.name = name
                con.phoneNumber = phoneNo
                db.addContact(con)
                Log.d("DbMill", "" + con.name + " " + con.phoneNumber)
                i++
                copy_phone = phoneNo
            } else {
                Toast.makeText(applicationContext, "You cannot add more than 5 contacts", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(applicationContext, "You have not provided any new number", Toast.LENGTH_SHORT).show()
        }
    }

    fun FetchContacts(): FetchContacts? {
        return db.allContacts
    }

    fun DisplayContacts() {
        val intent = Intent(this, FetchContacts::class.java)
        startActivity(intent)
    }

    fun DeleteContacts(v: View?) {
        val del_contact = findViewById<EditText>(R.id.delete_number)
        val del_con = del_contact.text.toString()
        if (FetchContacts() != null) {
            for (contacts in FetchContacts()!!) {
                if (contacts.getPhoneNumber() == del_con) {
                    db.deleteContact(contacts.getPhoneNumber())
                    copy_phone = ""
                    Log.d("DbMill", "" + contacts.getPhoneNumber())
                    break
                } else {
                    Toast.makeText(applicationContext, "No such contact available", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == SEND_SMS_PERMISSION_REQUEST_CODE && requestCode == TRACK_LOCATION_REQUEST_CODE) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sos_btn!!.isEnabled = true
            } else Toast.makeText(applicationContext, "Permission denied", Toast.LENGTH_LONG).show()
        }
    }

    private fun checkPermission(): Boolean {
        val checkPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
        return checkPermission == PackageManager.PERMISSION_GRANTED
    }

    private fun checkLocation(): Boolean {
        val checkLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        return checkLocation == PackageManager.PERMISSION_GRANTED
    }

    override fun onLocationChanged(location: Location) {
        try {
            val myLatitude = location.latitude.toString()
            val myLongitude = location.longitude.toString()
            GlobalMessage = " https://www.google.com/maps?q=$myLatitude,$myLongitude"
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
    override fun onProviderEnabled(provider: String) {}
    override fun onProviderDisabled(provider: String) {}
    private fun requestLocPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)) {
            AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of this and that")
                    .setPositiveButton("ok") { dialog, which -> ActivityCompat.requestPermissions(this@Emergency, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), TRACK_LOCATION_REQUEST_CODE) }
                    .setNegativeButton("cancel") { dialog, which -> dialog.dismiss() }
                    .create().show()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), TRACK_LOCATION_REQUEST_CODE)
        }
    }

    private fun requestSmsPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.SEND_SMS)) {
            AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of this and that")
                    .setPositiveButton("ok") { dialog, which -> ActivityCompat.requestPermissions(this@Emergency, arrayOf(Manifest.permission.SEND_SMS), SEND_SMS_PERMISSION_REQUEST_CODE) }
                    .setNegativeButton("cancel") { dialog, which -> dialog.dismiss() }
                    .create().show()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.SEND_SMS), SEND_SMS_PERMISSION_REQUEST_CODE)
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        const val MULTIPLE_PERMISSIONS = 10 // code you want.
    }
}