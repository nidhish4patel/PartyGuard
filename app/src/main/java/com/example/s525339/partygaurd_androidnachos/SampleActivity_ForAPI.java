package com.example.s525339.partygaurd_androidnachos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * This is the SampleActivity_ForAPI class which holds Alertname,Location,Comments.
 */
public class SampleActivity_ForAPI extends AppCompatActivity {

    TextView alertNameInSample;
    TextView locationInSample;
    TextView commentsInSample;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_activity__for_api);

        alertNameInSample=(TextView)findViewById(R.id.alertNameInSampleTV);
        locationInSample=(TextView)findViewById(R.id.locationInSampleTV);
        commentsInSample=(TextView)findViewById(R.id.commentsInSampleTV);

        Intent commentsIntent=getIntent();
        alertNameInSample.setText(commentsIntent.getStringExtra("AlertName"));
        locationInSample.setText(commentsIntent.getStringExtra("Location"));
        commentsInSample.setText(commentsIntent.getStringExtra("Comments"));

    }
}
