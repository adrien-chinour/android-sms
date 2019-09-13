package com.example.message;

import android.Manifest;
import android.content.Context;
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

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;

    private String message;

    private String[] phoneNumbers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println(ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED);
    }

    public void handleForm(View view) {
        // get message and phone number from SMS form
        EditText phoneInput = findViewById(R.id.phone);
        EditText messageInput = findViewById(R.id.message);

        //convert input to string
        this.phoneNumbers = phoneInput.getText().toString().split(";");
        this.message = messageInput.getText().toString();

        // check permissions to send SMS
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS);
        } else {
            sendSMS();
        }
    }

    private void sendSMS() {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_LONG;

        for (String phoneNumber : this.phoneNumbers) {
            if (!phoneNumber.matches("^[+]?[0-9]{4,13}$")) {
                Toast toast = Toast.makeText(context, "Le numéro de téléphone " + phoneNumber + " n'est pas valide.", duration);
                toast.show();
                return;
            }
            SmsManager.getDefault().sendTextMessage(phoneNumber, null, this.message, null, null);
        }

        // send validation message to user
        Toast toast = Toast.makeText(context, "Votre message a bien été envoyé.", duration);
        toast.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_SEND_SMS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sendSMS();
            }
        }
    }

}
