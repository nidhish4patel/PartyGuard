package com.example.s525339.partygaurd_androidnachos;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * It is where the guard user can see all the alerts that are allocated to him by host user.
 */
public class GuardHistory extends AppCompatActivity {

    ArrayList<AlertsForHistoryTab_GuardUser> alertsListHost=new ArrayList<>();
    AdapterForHistoryTab_GuardUser hostAlertAdapter;
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
    String azure_server = "";
    String userFraternity_UserInfo;
    ComplaintSingleton complaintSingleton;
    InputStream inputStream;
    String basicFirstName;
    String basicLastName;
    String fullName;
    String issueLocation;
    String basicUserImage;
    JSONObject basicUserObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guard_history);
        complaintSingleton=complaintSingleton.getInstance();
        userFraternity_UserInfo=complaintSingleton.getFraternityName_UserInfo();
        System.out.println(complaintSingleton.getFraternityName_UserInfo());
        try {
            ApplicationInfo ai = getPackageManager().getApplicationInfo(this.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            azure_server = bundle.getString("azure_server");
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("AZURE_SERVICE", "Failed to load meta-data, NameNotFound: " + e.getMessage());
        } catch (NullPointerException e) {
            Log.e("AZURE_SERVICE", "Failed to load meta-data, NullPointer: " + e.getMessage());
        }

        assetManager = getAssets();


        try {
            userFraternity_UserInfo = userFraternity_UserInfo.replace(" ","%20");
            String userResponse = new HttpAsyncTaskForUserInfo().execute(azure_server+ "FraternityAlerts/"+userFraternity_UserInfo).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        ListView alertListView= (ListView) findViewById(R.id.alertListView);

        hostAlertAdapter=new AdapterForHistoryTab_GuardUser(this,R.layout.historytab_listitem_guarduser,R.id.upperAlertTV,alertsListHost);
        alertListView.setAdapter(hostAlertAdapter);

//        String fraternityJSON = getFraternityJSON();
//        parseJSON(fraternityJSON);

        final ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater().inflate(R.layout.action_bar, null);
        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(actionBarLayout);

        Button historyBTN = (Button)findViewById(R.id.guardHistoryTabBTN);
        historyBTN.setBackgroundColor(getResources().getColor(R.color.IndianRed));
        historyBTN.setTextColor(getResources().getColor(R.color.colorWhite));


        Button guardAlertBTN = (Button) findViewById(R.id.guardAlertTabBTN);
        guardAlertBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent guardAlertIntent = new Intent(getApplicationContext(),GuardAlert.class);
                startActivity(guardAlertIntent);
            }
        });

        Button guardProfileBTN = (Button) findViewById(R.id.guardProfileTabBTN);
        guardProfileBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent guardProfileIntent = new Intent(getApplicationContext(),GuardProfile.class);
                startActivity(guardProfileIntent);
            }
        });
    }
    private String getFraternityJSON() {
        InputStream input;
        String text = "";
        try {
            input = assetManager.open("AlertsForHostHistory.txt");

            int size = input.available();
            byte[] buffer = new byte[size];
            input.read(buffer);
            input.close();

            // byte buffer into a string
            text = new String(buffer);
            return text;
        }
        catch (IOException e)
        {
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

                    name = alertsArray.getString("Name");
                    date = alertsArray.getString("Date");
                    incidence = alertsArray.getString("Incident");
                    fraternity = alertsArray.getString("Fraternity");
                    location = alertsArray.getString("Location");
                    comments = alertsArray.getString("Comments");
                    status = alertsArray.getString("Status");
                    time = alertsArray.getString("Time");
                    imageURL=alertsArray.getString("ImageURL");
                    age=alertsArray.getString("Age");
                    // CharSequence name_char=universityArray.getString("Name").toString();


//                    alertsListHost.add(new AlertsForHistoryTab_GuardUser(incidence,date,name,fraternity,location,  comments,  status,  time, imageURL,age));






                }
                catch(JSONException jsone) {
                    Log.d("ERROR", jsone.getMessage());
                }
            }
        }catch(JSONException jsone){
            Log.d("ERROR",jsone.getMessage());
        }
    }


    private class HttpAsyncTaskForUserInfo extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... urls) {
            return POST(urls[0]);
        }


        public String POST(String url) {
            inputStream = null;
            String result = "";
            StringBuilder body = new StringBuilder();

            String access_token = complaintSingleton.getAccess_token();
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(url);

                String json = "";
                json = "univeristyName=" + "Northwest Missouri";
//                httpGet.setHeader("Authorization","Bearer "+ access_token);

                HttpResponse httpResponse = httpclient.execute(httpGet);

                StatusLine statusLine = httpResponse.getStatusLine();
                int statusCode=statusLine.getStatusCode();
                if (statusCode== HttpStatus.SC_OK){

                    body.append(statusLine + "\n");
                    HttpEntity e = httpResponse.getEntity();
                    String entity = EntityUtils.toString(e);

                    parseUserInfoJSON(entity);
                    System.out.println(entity);
                    body.append(entity);
                }else {
                    body.append(statusLine + "\n");
                    // System.out.println(statusLine);
                }

                inputStream = httpResponse.getEntity().getContent();

                // convert inputstream to string
                if (inputStream != null) {
                    result = convertInputStreamToString(inputStream);
                    Log.d("LOGIN", "inputStream result" + result);
                    System.out.print(inputStream);
                } else {
                    result = "Did not work!";
                    Log.d("LOGIN", "result" + result);

                }

            } catch (Exception e) {
                Log.d("json", "e.getLocalizedMessage()" + e.getLocalizedMessage());
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            String response = result;
            parseJSON(result);
            System.out.println(result);

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
        private void parseUserInfoJSON(String jsonToParse) {
            try {
                JSONArray loginObject = new JSONArray(jsonToParse);
                for(int i=0; i < loginObject.length(); i++){
                    basicUserObject=loginObject.getJSONObject(i);
                    issueLocation=basicUserObject.getString("issueLocation");

                    JSONObject basicUserInfo=basicUserObject.getJSONObject("BasicUserProfileModel");
//                    basicUserImage=basicUserInfo.getString("imgUrl");
                    basicUserImage="https://drive.google.com/uc?id=0By5v-xaILwvHWW0tZlFfaW5FQzA";
                    basicFirstName=basicUserInfo.getString("firstName");
                    basicLastName=basicUserInfo.getString("lastName");

                    fullName=basicFirstName+" "+basicLastName;
                    alertsListHost.add(new AlertsForHistoryTab_GuardUser(fullName,issueLocation,basicUserImage));
                }


            }catch(JSONException jsone){
                Log.d("ERROR",jsone.getMessage());
            }
        }
    }

}
