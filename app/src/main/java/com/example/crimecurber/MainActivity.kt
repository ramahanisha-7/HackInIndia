package com.example.crimecurber

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.crimecurber.MainActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.AuthUI.IdpConfig.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import java.util.*

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private var cameraCard: CardView? = null
    private var uploadCard: CardView? = null
    private var emergencyCard: CardView? = null
    private var callCard: CardView? = null
    private var directionCard: CardView? = null
    private var helpCard: CardView? = null

    // Firebase instance variables
    private var mFirebaseAuth: FirebaseAuth? = null
    private var mAuthStateListener: AuthStateListener? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Firebase
        mFirebaseAuth = FirebaseAuth.getInstance()


        //defining cards
        cameraCard = findViewById<View>(R.id.camera_card) as CardView //casting
        uploadCard = findViewById<View>(R.id.upload_card) as CardView
        emergencyCard = findViewById<View>(R.id.emergency_card) as CardView
        callCard = findViewById<View>(R.id.call_card) as CardView
        directionCard = findViewById<View>(R.id.direction_card) as CardView
        helpCard = findViewById<View>(R.id.help_card) as CardView
        val btnsignout = findViewById<View>(R.id.btnsignout) as Button
        cameraCard!!.setOnClickListener(this)
        uploadCard!!.setOnClickListener(this)
        emergencyCard!!.setOnClickListener(this)
        callCard!!.setOnClickListener(this)
        directionCard!!.setOnClickListener(this)
        helpCard!!.setOnClickListener(this)

        //Imageview setup
        cameraCard!!.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
            startActivityForResult(intent, 0)
        }


        //Initialization of Firebase Auth
        mAuthStateListener = AuthStateListener {
            val auth = FirebaseAuth.getInstance()
            if (auth.currentUser != null) {
                // already signed
//                    Toast.makeText(getApplicationContext() , "You are now signed In. Welcome to Crime Curber.", Toast.LENGTH_SHORT).show();
            } else {
                // not signed in
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setIsSmartLockEnabled(false)
                                .setAvailableProviders(Arrays.asList(
                                        GoogleBuilder().build(),
                                        EmailBuilder().build(),
                                        AnonymousBuilder().build()))
                                .setTheme(R.style.LoginTheme) // Theme for firebase Ui
                                .setLogo(R.mipmap.ic_launcher_foreground) // Background Logo
                                .build(),
                        RC_SIGN_IN)
            }
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(applicationContext, "You are now Signed In. Welcome to Crime Curber.", Toast.LENGTH_SHORT).show()
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(applicationContext, "Sign in Cancelled.", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    //Firebase methods
    override fun onPause() {
        super.onPause()
        mFirebaseAuth!!.removeAuthStateListener(mAuthStateListener!!)
    }

    override fun onResume() {
        super.onResume()
        mFirebaseAuth!!.addAuthStateListener(mAuthStateListener!!)
    }

    //Firebase Signout Impemntation
    override fun onClick(v: View) {
        val i: Intent
        if (v.id == R.id.btnsignout) {
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener { // user is now signed out
                        startActivity(Intent(this@MainActivity, MainActivity::class.java))
                        Toast.makeText(applicationContext, "Signed Out", Toast.LENGTH_SHORT).show()
                        finish()
                    }
        }
        when (v.id) {
            R.id.upload_card -> {
                i = Intent(this, Upload::class.java)
                startActivity(i)
            }
            R.id.emergency_card -> {
                i = Intent(this, Emergency::class.java)
                startActivity(i)
            }
            R.id.call_card -> {
                i = Intent(this, Call::class.java)
                startActivity(i)
            }
            R.id.direction_card -> {
                i = Intent(this, Direction::class.java)
                startActivity(i)
            }
            R.id.help_card -> {
                i = Intent(this, Help::class.java)
                startActivity(i)
            }
            else -> {
            }
        }
    }

    companion object {
        const val RC_SIGN_IN = 1 // for sigin purpose
    }
}