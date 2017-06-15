package com.example.s525339.partygaurd_androidnachos;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutionException;

/**
 * It is where a guard user can claim the alert.
 */

public class ClaimAlertGuard extends AppCompatActivity {


    String name;
    String date;
    String incidence;
    String fraternity;
    String location;
    String comments;
    String status;
    String time;
    String imageURL;
    String age;
    String issueID;
    Boolean errorThrown = true;
    ComplaintSingleton complaintSingleton;
    String access_token;
    String loginSrvcResposnse = "";
    String responseForResolve="";
    Button claimBTN;
    Button resolveBTN;
    String azure_server = "";
    String accesstoken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_claim_alert_guard);
        try {
            ApplicationInfo ai = getPackageManager().getApplicationInfo(this.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            azure_server = bundle.getString("azure_server");
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("AZURE_SERVICE", "Failed to load meta-data, NameNotFound: " + e.getMessage());
        } catch (NullPointerException e) {
            Log.e("AZURE_SERVICE", "Failed to load meta-data, NullPointer: " + e.getMessage());
        }
        complaintSingleton = ComplaintSingleton.getInstance();
        access_token=complaintSingleton.getAccess_token();

        TextView userNameTV=(TextView)findViewById(R.id.userName);
        TextView userAgeTV=(TextView)findViewById(R.id.userAgeNum);
        TextView alertTV=(TextView)findViewById(R.id.typeOfAlert);
        TextView locationTV=(TextView)findViewById(R.id.locationInHome);
        TextView commentsTV=(TextView)findViewById(R.id.userComments);
        TextView timeTV=(TextView)findViewById(R.id.timeReport);
        TextView fraternityTV= (TextView) findViewById(R.id.userLocation);
        claimBTN=(Button)findViewById(R.id.claimBTN);
        resolveBTN=(Button) findViewById(R.id.resolveBTN);

        Intent alertDetialsIntent=getIntent();
        name=alertDetialsIntent.getStringExtra("Name");
        fraternity=alertDetialsIntent.getStringExtra("Fraternity");
        age=alertDetialsIntent.getStringExtra("Age");
        comments=alertDetialsIntent.getStringExtra("Comments");
        incidence=alertDetialsIntent.getStringExtra("Alert");
        location=alertDetialsIntent.getStringExtra("Location");
        time=alertDetialsIntent.getStringExtra("Time");
        issueID=alertDetialsIntent.getStringExtra("IssueID");

        userNameTV.setText(name);
        fraternityTV.setText(fraternity);
        userAgeTV.setText(age);
        commentsTV.setText(comments);
        alertTV.setText(incidence);
        locationTV.setText(location);
        timeTV.setText(time);

        claimBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ClaimAlertGuard.this,"Http Post: " + "Sending Claim Alert.",Toast.LENGTH_SHORT).show();
                try {
                    loginSrvcResposnse=new HttpAsyncTask().execute("http://partyguardservices20161110094537.azurewebsites.net/GuardAlertsClaim").get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
//                claimBTN.setBackgroundColor(R.color.colorWhite);
                claimBTN.setEnabled(false);
            }
        });
        resolveBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    responseForResolve=new HttpAsyncTaskForResolve().execute("http://partyguardservices20161110094537.azurewebsites.net/GuardAlertsResolve").get();
                    System.out.println(responseForResolve);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                resolveBTN.setEnabled(false);
                Toast.makeText(ClaimAlertGuard.this,"Sent for user's approval!.",Toast.LENGTH_SHORT).show();
            }
        });


        final ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater().inflate(R.layout.action_bar, null);
        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(actionBarLayout);


        Button alertBTN = (Button) findViewById(R.id.alertTabBTN);
        alertBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent alertIntent = new Intent(getApplicationContext(), GuardAlert.class);
                startActivity(alertIntent);
            }
        });

        Button historyBTN = (Button) findViewById(R.id.historyTabBTN);
        historyBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent historyIntent = new Intent(getApplicationContext(), GuardHistory.class);
                startActivity(historyIntent);
            }
        });

        Button profileBTN = (Button) findViewById(R.id.profileTabBTN);
        profileBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileIntent = new Intent(getApplicationContext(), GuardProfile.class);
                startActivity(profileIntent);
            }
        });


    }
    private class HttpAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... urls) {
            return POST(urls[0]);
        }


        public String POST(String url) {
            InputStream inputStream = null;
            String result = "";
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);

                String json = "";
//                JSONObject jsonObject = new JSONObject();
//                jsonObject.put("id", issueID);


//                httpPost.setHeader("accept", "json");
                httpPost.setHeader("Content-type", "application/json");
                httpPost.setHeader("Authorization","Bearer "+ access_token);

                JSONObject jsonObjectForPost=new JSONObject();

                jsonObjectForPost.put("id",issueID);

                 json = jsonObjectForPost.toString();
                StringEntity se = new StringEntity(json);
                httpPost.setEntity(se);

                HttpResponse httpResponse = httpclient.execute(httpPost);

                inputStream = httpResponse.getEntity().getContent();
                result = convertInputStreamToString(inputStream);

                // convert inputstream to string
                if (result.contains("invalid") == true) {
                    errorThrown = true;
                    Log.d("PartyGuard", "result" + result);

                } else
                    errorThrown = false;
                Log.d("PartyGuard", "inputStream result" + result);

            } catch (Exception e) {
                Log.d("json", "e.getLocalizedMessage()" + e.getLocalizedMessage());
            }

            //  return result
            return result;
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            if(errorThrown == false) {
                Toast.makeText(getBaseContext(), "Alert Claimed", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(getBaseContext(), result, Toast.LENGTH_SHORT).show();
            }
        }

    }
    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }
    private class HttpAsyncTaskForResolve extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... urls) {
            return POST(urls[0]);
        }


        public String POST(String url) {
            InputStream inputStream = null;
            String result = "";
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);

                String json = "";
//                JSONObject jsonObject = new JSONObject();
//                jsonObject.put("id", issueID);


//                httpPost.setHeader("accept", "json");
                httpPost.setHeader("Content-type", "application/json");
                httpPost.setHeader("Authorization","Bearer "+ access_token);

                System.out.println(accesstoken=access_token);
                JSONObject jsonObjectForPost=new JSONObject();

                jsonObjectForPost.put("id",issueID);

                json = jsonObjectForPost.toString();
                StringEntity se = new StringEntity(json);
                httpPost.setEntity(se);

                HttpResponse httpResponse = httpclient.execute(httpPost);

                inputStream = httpResponse.getEntity().getContent();
                result = convertInputStreamToString(inputStream);

                // convert inputstream to string
                if (result.contains("invalid") == true) {
                    errorThrown = true;
                    Log.d("PartyGuard", "result" + result);

                } else
                    errorThrown = false;
                Log.d("PartyGuard", "inputStream result" + result);

            } catch (Exception e) {
                Log.d("json", "e.getLocalizedMessage()" + e.getLocalizedMessage());
            }

            //  return result
            return result;
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            if(errorThrown == false) {
                Toast.makeText(getBaseContext(), "Alert Claimed", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(getBaseContext(), result, Toast.LENGTH_SHORT).show();
            }
        }

    }
    private static String convertInputStreamToStringForResolve(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }
}
