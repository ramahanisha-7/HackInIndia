package com.example.crimecurber;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Emergency extends AppCompatActivity {

    private final int PICK_CONTACT = 1;// Constant to Pick Contacts
    private Button btnAdd,btnEdit;// Add Button Declared
    private TextView contactnumber;// TextView Declared

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);
        contactnumber = (TextView)findViewById(R.id.txtdisplay);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnEdit = (Button)findViewById(R.id.btnEdit);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callContacts();

            }
        });

    }

    private void callContacts() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent,PICK_CONTACT);

    }

    // Permission For Accessing Contacts
    @Override
    protected void onActivityResult(int reqCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        if (reqCode == PICK_CONTACT){
            if(resultCode == AppCompatActivity.RESULT_OK)
            {
                Uri contactData = data.getData();
                Cursor c = getContentResolver().query(contactData,null,null,null,null);

                if(c.moveToFirst()){
                    String name = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
                    contactnumber.setText(name);
                    Toast.makeText(this, name + " is now an emergency contact", Toast.LENGTH_LONG).show();
                }

            }
        }
    }
}
