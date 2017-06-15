package com.example.s525339.partygaurd_androidnachos;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This is EmergencyActivity class
 */

public class EmergencyActivity extends AppCompatActivity {

    TextView emergencyTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);

//        emergencyTV= (TextView) findViewById(R.id.emergencyTV);
//        emergencyTV.setText("A message will be sent to police!!");

    }
}
