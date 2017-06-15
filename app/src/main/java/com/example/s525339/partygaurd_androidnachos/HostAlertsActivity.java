package com.example.s525339.partygaurd_androidnachos;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
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
 * It is HostAlertsActivity class which shows the list of issues.User has to select issue only after selecting the location
 */

public class HostAlertsActivity extends AppCompatActivity{

//    HostAlert[] fraternityArray = {new HostAlert("Katie Harris", "Main Floor", "Image1")};
    ArrayList<HostAlert> alertsListHost=new ArrayList<>();
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
    String hostAlertsSrvcURL ="";
    String hostAlertsSrvcResposnse = "";
    InputStream inputStream;
    ComplaintSingleton complaintSingleton;
    String basicLastName;
    String basicFirstName;
    String basicFullName;
    String basicAge;
    String issueID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);
        complaintSingleton=complaintSingleton.getInstance();
        try {
            ApplicationInfo ai = getPackageManager().getApplicationInfo(this.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            azure_server = bundle.getString("azure_server");
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("AZURE_SERVICE", "Failed to load meta-data, NameNotFound: " + e.getMessage());
        } catch (NullPointerException e) {
            Log.e("AZURE_SERVICE", "Failed to load meta-data, NullPointer: " + e.getMessage());
        }
        hostAlertsSrvcURL = azure_server + "api/IssueList";
        final ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater().inflate(R.layout.action_bar, null);
        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(actionBarLayout);

        assetManager = getAssets();
        final View customDialogView= LayoutInflater.from(HostAlertsActivity.this).inflate(R.layout.activity_custom_dialog__report_location,null);
        TextView userNameTV= (TextView) customDialogView.findViewById(R.id.upperAlertTV);
        TextView issueNameTV= (TextView) customDialogView.findViewById(R.id.lowerAlertTV);

        Button alertBTN = (Button)findViewById(R.id.alertTabBTN);
        alertBTN.setBackgroundColor(getResources().getColor(R.color.IndianRed));
        alertBTN.setTextColor(getResources().getColor(R.color.colorWhite));


//        ListView alertListView = (ListView) findViewById(R.id.alertListView);
////        fraternityList = new ArrayList<>(Arrays.asList(fraternityArray));
//        HostAlertAdapter hostAlertAdapetr = new HostAlertAdapter(this, R.layout.host_alert_list_item_menu, R.id.upperAlertTV, alertsListHost);
//        alertListView.setAdapter(hostAlertAdapetr);

        ListView alertListView= (ListView) findViewById(R.id.alertListView);

        HostAlertAdapter hostAlertAdapter=new HostAlertAdapter(this,R.layout.host_alert_list_item_menu,R.id.upperAlertTV,alertsListHost);
        alertListView.setAdapter(hostAlertAdapter);

        String fraternityJSON = getFraternityJSON();
        parseJSON(fraternityJSON);

        alertListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(HostAlertsActivity.this, ClaimAlert.class);
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

        String alertsJSON = getAlertsJSON();
        parseAlertsJSON(alertsJSON);

        Button pgTeamBTN = (Button) findViewById(R.id.pgTeamTabBTN);
        pgTeamBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pgTeamIntent = new Intent(getApplicationContext(),PgteamActivity.class);
                startActivity(pgTeamIntent);
            }
        });

        final Button historyBTN = (Button) findViewById(R.id.historyTabBTN);
        historyBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent historyIntent = new Intent(getApplicationContext(),History.class);
                startActivity(historyIntent);
            }
        });

        Button profileBTN = (Button) findViewById(R.id.profileTabBTN);
        profileBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileIntent = new Intent(getApplicationContext(),HostUserProfileScreen.class);
                //pgTeamBTN.setBackgroundColor(getResources().getColor(R.color.colorGrey));
                //pgTeamBTN.setTextColor(getResources().getColor(R.color.colorCrimson));
                startActivity(profileIntent);
            }
        });


    }
    //Gets the Json from the service.
    private String getAlertsJSON() {
        InputStream input;
        String text = "";
        try {
//            input = assetManager.open("AlertsForHost.txt");
//
//            int size = input.available();
//            byte[] buffer = new byte[size];
//            input.read(buffer);
//            input.close();

            hostAlertsSrvcResposnse = new HttpAsyncTask().execute(hostAlertsSrvcURL).get();
            text = hostAlertsSrvcResposnse;
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
    //It parses the Json file .
    private void parseAlertsJSON(String jsonToParse) {
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
                    alertsListHost.add(new HostAlert(incidence,date,name,fraternity,location,  comments,  status,  time, imageURL,age,basicFullName,issueID));
                }
                catch(JSONException jsone) {
                    Log.d("ERROR", jsone.getMessage());
                }
            }
        }catch(JSONException jsone){
            Log.d("ERROR",jsone.getMessage());
        }
    }
    private String getFraternityJSON() {
        InputStream input;
        String text = "";
        try {
            input = assetManager.open("AlertsForHost.txt");

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
                        issueID= alertsArray.getString("issueID");
                        location = alertsArray.getString("Location");
                        comments = alertsArray.getString("Comments");
                        status = alertsArray.getString("Status");
                        time = alertsArray.getString("Time");
                        imageURL=alertsArray.getString("ImageURL");
                        age=alertsArray.getString("Age");
                        // CharSequence name_char=universityArray.getString("Name").toString();

                        System.out.println("iiiiiiiiiiiiiiiiiiiiiiiiiii"+incidence+ date+name+ fraternity+location+  comments+  status+  time+ imageURL);
//                    alertsListHost.add(new HostAlert(incidence,date,name,fraternity,location,  comments,  status,  time, imageURL,age));

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
            hostAlertsSrvcResposnse = result;
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
