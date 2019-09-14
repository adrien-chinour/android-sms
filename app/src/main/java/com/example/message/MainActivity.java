package com.example.message;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    /**
     * Use to validating SEND_SMS permission
     */
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;

    /**
     * Save input message to send
     */
    private String message;


    /**
     * Save all input phone numbers
     */
    private String[] phoneNumbers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Handle form when user want to send SMS on clicking submit button
     */
    public void handleForm(View view) {
        // get message and phone number from SMS form
        EditText phoneInput = findViewById(R.id.phone);
        EditText messageInput = findViewById(R.id.message);

        //convert input to string
        this.phoneNumbers = phoneInput.getText().toString().replaceAll("\\s", "").split(";");
        this.message = messageInput.getText().toString();

        // validating message length (should be > 0)
        if (message.length() < 1) {
            Toast.makeText(getApplicationContext(), "Veuillez écrire un message", Toast.LENGTH_LONG).show();
        }

        // check permissions to send SMS
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS);
        } else {
            sendSMS();
        }
    }

    /**
     * send SMS for each phoneNumbers provided by user
     */
    private void sendSMS() {

        for (String phoneNumber : this.phoneNumbers) {

            // validating phoneNumber before send message
            if (!phoneNumber.matches("^[+]?[0-9]{4,13}$")) {
                Toast.makeText(getApplicationContext(), "Le numéro de téléphone " + phoneNumber + " n'est pas valide.", Toast.LENGTH_LONG).show();
                return;
            }
            SmsManager.getDefault().sendTextMessage(phoneNumber, null, this.message, null, null);
        }

        // send validation message to user
        Toast.makeText(getApplicationContext(), "Votre message a bien été envoyé.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        // MY_PERMISSIONS_REQUEST_SEND_SMS : send SMS if user is ok for SEND_SMS permission
        if (requestCode == MY_PERMISSIONS_REQUEST_SEND_SMS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sendSMS();
            }
        }
    }

}
