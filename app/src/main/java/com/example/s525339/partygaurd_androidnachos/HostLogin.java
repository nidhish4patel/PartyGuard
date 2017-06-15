package com.example.s525339.partygaurd_androidnachos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * It is HostLogin class .once the host user enters the app he lands in to the pg-team screen
 */

public class HostLogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_login);
        Button hostLoginBTN=(Button)findViewById(R.id.hostLoginBtn);
        hostLoginBTN.setOnClickListener(
                new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent loginIntent = new Intent(HostLogin.this, PgteamActivity.class);
                            startActivity(loginIntent);
                        }

                }
        );

//        Button createAccBTN=(Button)findViewById(R.id.hostCreatAccountBtn);
//            createAccBTN.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent signUpIntent = new Intent(HostLogin.this, SignUpActivity.class);
//                    startActivity(signUpIntent);
//                }
//
//            });

    }





//    hostLoginBTN.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            Intent loginIntent = new Intent(HostLogin.this, PgteamActivity.class);
//            startActivity(loginIntent);
//        }
//
//    });
//
//    Button signUPbutton = (Button) findViewById(R.id.hostCreatAccountBtn);
//    signUPbutton.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            Intent signUpIntent = new Intent(HostLogin.this, SignUpActivity.class);
//            startActivity(signUpIntent);
//        }
//
//    });
}
