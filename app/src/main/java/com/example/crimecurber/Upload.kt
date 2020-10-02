package com.example.crimecurber

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class Upload : AppCompatActivity() {
    var btnChoose: Button? = null
    var btnUpload: Button? = null
    var txtdata: EditText? = null
    var FilePathUri: Uri? = null
    var storageReference: StorageReference? = null
    var databaseReference: DatabaseReference? = null
    var Image_Request_Code = 7
    var progressDialog: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)
        storageReference = FirebaseStorage.getInstance().getReference("Videos")
        databaseReference = FirebaseDatabase.getInstance().getReference("Videos")
        btnChoose = findViewById<View>(R.id.btnChoose) as Button
        btnUpload = findViewById<View>(R.id.btnUpload) as Button
        txtdata = findViewById<View>(R.id.txtdata) as EditText
        progressDialog = ProgressDialog(this@Upload)
        btnChoose!!.setOnClickListener {
            val intent = Intent()
            intent.type = "video/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Video"), Image_Request_Code)
        }
        btnUpload!!.setOnClickListener { UploadVideo() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.data != null) {
            FilePathUri = data.data
            Toast.makeText(applicationContext, "Video Chosen", Toast.LENGTH_SHORT).show()
        }
    }

    fun GetFileExtension(uri: Uri?): String? {
        val contentResolver = contentResolver
        val mimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri!!))
    }

    fun UploadVideo() {
        if (FilePathUri != null) {
            progressDialog!!.setTitle("Video is Uploading...")
            progressDialog!!.show()
            val storageReference2 = storageReference!!.child(System.currentTimeMillis().toString() + "." + GetFileExtension(FilePathUri))
            storageReference2.putFile(FilePathUri!!)
                    .addOnSuccessListener { taskSnapshot ->
                        val TempImageName = txtdata!!.text.toString().trim { it <= ' ' }
                        progressDialog!!.dismiss()
                        Toast.makeText(applicationContext, "Video Uploaded Successfully ", Toast.LENGTH_LONG).show()
                        val imageUploadInfo = uploadinfo(TempImageName, taskSnapshot.uploadSessionUri.toString())
                        val ImageUploadId = databaseReference!!.push().key
                        databaseReference!!.child(ImageUploadId!!).setValue(imageUploadInfo)
                    }
                    .addOnFailureListener { e ->
                        progressDialog!!.dismiss()
                        Toast.makeText(this@Upload, "Failed " + e.message, Toast.LENGTH_SHORT).show()
                    }
                    .addOnProgressListener { taskSnapshot ->
                        val progress = 100.0 * taskSnapshot.bytesTransferred / taskSnapshot
                                .totalByteCount
                        progressDialog!!.setMessage("Uploading " + progress.toInt() + "%")
                    }
        } else {
            Toast.makeText(this@Upload, "Please Select Video or Add Video Location", Toast.LENGTH_LONG).show()
        }
    }
}