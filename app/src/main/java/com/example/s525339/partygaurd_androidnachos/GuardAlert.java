package com.example.s525339.partygaurd_androidnachos;

import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.AdapterView;

import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ListView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

/**
 * It is the guardalert class where the total info. about the alert is shown.
 */


public class GuardAlert extends AppCompatActivity{

    //from push notifications-start
    String alertTypeFromPush;
    String locationFromPush;
    String userNameFromPush;
    String fraternityFromPush;
    //from push notifications-end


    ArrayList<AlertsForGuard_AlertTab> alertsListHost=new ArrayList<>();
    AssetManager assetManager;

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
    String guardAlertsSrvcURL ="";
    InputStream inputStream;
    String guardAlertsSrvcResposnse = "";
    ComplaintSingleton complaintSingleton;
    String azure_server = "";
    SwipeRefreshLayout swipeRefreshLayout;
    ListView locationsListView;
    AdapterForGuard_AlertTab alertAdapter;
    String basicLastName;
    String basicFirstName;
    String basicFullName;
    String basicAge;
    String issueID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guard_alert);
        try {
            ApplicationInfo ai = getPackageManager().getApplicationInfo(this.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            azure_server = bundle.getString("azure_server");
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("AZURE_SERVICE", "Failed to load meta-data, NameNotFound: " + e.getMessage());
        } catch (NullPointerException e) {
            Log.e("AZURE_SERVICE", "Failed to load meta-data, NullPointer: " + e.getMessage());
        }
        guardAlertsSrvcURL = azure_server + "api/IssueList";
        assetManager = getAssets();
        complaintSingleton = ComplaintSingleton.getInstance();
        final ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater().inflate(R.layout.action_bar, null);
        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(actionBarLayout);

        Button alertBTN = (Button)findViewById(R.id.guardAlertTabBTN);
        alertBTN.setBackgroundColor(getResources().getColor(R.color.IndianRed));
        alertBTN.setTextColor(getResources().getColor(R.color.colorWhite));


        Intent pushIntent=getIntent();
        //from push notifications-start

        alertTypeFromPush=pushIntent.getStringExtra("alertName");
        locationFromPush=pushIntent.getStringExtra("locationName");
        userNameFromPush=pushIntent.getStringExtra("userName");
        fraternityFromPush=pushIntent.getStringExtra("fraternityName");
        locationsListView = (ListView) findViewById(R.id.alertListView);
        AlertLogFromPush[] alertsArray={new AlertLogFromPush(alertTypeFromPush,locationFromPush,userNameFromPush,fraternityFromPush)};

        ArrayList<AlertLogFromPush> alertLogList;


        String fraternityJSON = getFraternityJSON();
        parseJSON(fraternityJSON);

        alertAdapter=new AdapterForGuard_AlertTab(this,R.layout.alertstab_lisitem_guarduser,R.id.upperAlertTV,alertsListHost);
        locationsListView.setAdapter(alertAdapter);


//        alertLogList=new ArrayList<>(Arrays.asList(alertsArray));

        locationsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(GuardAlert.this, ClaimAlertGuard.class);
                intent.putExtra("Name",alertsListHost.get(position).getName());
                intent.putExtra("Fraternity",alertsListHost.get(position).getFraternity());
                intent.putExtra("Age",alertsListHost.get(position).getAge());
                intent.putExtra("Comments",alertsListHost.get(position).getComments());
                intent.putExtra("Alert",alertsListHost.get(position).getIncidence());
                intent.putExtra("Location",alertsListHost.get(position).getLocation());
                intent.putExtra("Time",alertsListHost.get(position).getTime());
                intent.putExtra("IssueID",alertsListHost.get(position).getIssueID());
//                intent.putExtra("Name",alertsListHost.get(position).getName());
                startActivity(intent);
            }
        });

        //from push notifications-end
        Button guardProfileBTN = (Button) findViewById(R.id.guardProfileTabBTN);
        guardProfileBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent guardProfileIntent = new Intent(getApplicationContext(),GuardProfile.class);
                startActivity(guardProfileIntent);
            }
        });

        Button guardHistoryBTN = (Button) findViewById(R.id.guardHistoryTabBTN);
        guardHistoryBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent guardHistoryIntent = new Intent(getApplicationContext(),GuardHistory.class);
                startActivity(guardHistoryIntent);
            }
        });


        NotificationManager notificationManager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(1);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                alertAdapter.clear();
                String fraternityJSON = getFraternityJSON();
                parseJSON(fraternityJSON);
                alertAdapter=new AdapterForGuard_AlertTab(GuardAlert.this,R.layout.alertstab_lisitem_guarduser,R.id.upperAlertTV,alertsListHost);
                locationsListView.setAdapter(alertAdapter);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
//        swipeRefreshLayout.setOnRefreshListener(this);

        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
//        swipeRefreshLayout.post(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        swipeRefreshLayout.setRefreshing(true);
//                                        String fraternityJSON = getFraternityJSON();
//                                        parseJSON(fraternityJSON);
//                                    }
//                                }
//        );
    }
    private String getFraternityJSON() {
        InputStream input;
        String text = "";
        try {
//            input = assetManager.open("AlertsForHost.txt");
//
//            int size = input.available();
//            byte[] buffer = new byte[size];
//            input.read(buffer);
//            input.close();

            guardAlertsSrvcResposnse = new HttpAsyncTask().execute(guardAlertsSrvcURL).get();
            text = guardAlertsSrvcResposnse;
            return text;
            // byte buffer into a string
            //text = new String(buffer);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return text;
    }
    private void parseJSON(String jsonToParse) {
        try {
            JSONArray arr = new JSONArray(jsonToParse);
            for(int i=0; i < arr.length(); i++){
                try {
                    JSONObject alertsArray = arr.getJSONObject(i);
                    Log.d("alertsArray",alertsArray.toString());
                    JSONObject basicUserProfileModel=alertsArray.getJSONObject("BasicUserProfileModel");
                    basicLastName=basicUserProfileModel.getString("lastName");
                    basicFirstName=basicUserProfileModel.getString("firstName");
                    basicAge=basicUserProfileModel.getString("age");
                    basicFullName=basicFirstName+""+basicLastName;

                    name = alertsArray.getString("issueName");
                    date = alertsArray.getString("issueDate");
                    issueID= alertsArray.getString("issueID");
                    incidence = alertsArray.getString("issueType");
                    fraternity = alertsArray.getString("fraternityName");
                    location = alertsArray.getString("issueLocation");
                    comments = alertsArray.getString("comments");
                    status = alertsArray.getString("issueStatus");
                    time = alertsArray.getString("issueDate");
                    imageURL="https://drive.google.com/uc?id=0By5v-xaILwvHWW0tZlFfaW5FQzA";
                    age="19";
                    // CharSequence name_char=universityArray.getString("Name").toString();
//
//
                    alertsListHost.add(new AlertsForGuard_AlertTab(incidence,date,name,fraternity,location,  comments,  status,  time, imageURL,age,basicFullName,issueID));
                }
                catch(JSONException jsone) {
                    Log.d("ERROR", jsone.getMessage());
                }
            }
        }catch(JSONException jsone){
            Log.d("ERROR",jsone.getMessage());
        }
    }

    private class HttpAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... urls) {
            return POST(urls[0]);
        }

        public String POST(String url) {
            inputStream = null;
            String result = "";

            String access_token = complaintSingleton.getAccess_token();
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(url);

                httpGet.setHeader("Content-type", "application/json");
                httpGet.setHeader("Authorization","Bearer "+ access_token);
                HttpResponse httpResponse = httpclient.execute(httpGet);

                inputStream = httpResponse.getEntity().getContent();

                // convert inputstream to string
                if (inputStream != null) {
                    result = convertInputStreamToString(inputStream);
                    Log.d("Guard List", "inputStream result" + result);
                    System.out.print(inputStream);
                } else {
                    result = "Did not work!";
                    Log.d("Guard List", "result" + result);

                }

            } catch (Exception e) {
                Log.d("json", "e.getLocalizedMessage()" + e.getLocalizedMessage());
            }

//        });
            return result;
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            //parseJSON(result);
            guardAlertsSrvcResposnse = result;
        }

        private String convertInputStreamToString(InputStream inputStream) throws IOException {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            String result = "";
            while ((line = bufferedReader.readLine()) != null)
                result += line;

            inputStream.close();
            return result;

        }
    }
}
