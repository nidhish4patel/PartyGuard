package com.example.s525339.partygaurd_androidnachos;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * This is the AlertHistory class for the Host user.Host user can see all the unresolved alerts here.
 */

public class History extends AppCompatActivity {

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

    String basicLastName;
    String basicFirstName;
    String basicFullName;
    String basicAge;
    String issueID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        try {
            ApplicationInfo ai = getPackageManager().getApplicationInfo(this.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            azure_server = bundle.getString("azure_server");
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("AZURE_SERVICE", "Failed to load meta-data, NameNotFound: " + e.getMessage());
        } catch (NullPointerException e) {
            Log.e("AZURE_SERVICE", "Failed to load meta-data, NullPointer: " + e.getMessage());
        }
        final ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater().inflate(R.layout.action_bar, null);
        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(actionBarLayout);
        assetManager = getAssets();

        Button historyBTN = (Button) findViewById(R.id.historyTabBTN);
        historyBTN.setBackgroundColor(getResources().getColor(R.color.IndianRed));
        historyBTN.setTextColor(getResources().getColor(R.color.colorWhite));

        ListView alertListView= (ListView) findViewById(R.id.alertListView);

        HostAlertAdapter hostAlertAdapter=new HostAlertAdapter(this,R.layout.hosthistory_listitem,R.id.upperAlertTV,alertsListHost);
        alertListView.setAdapter(hostAlertAdapter);

        String fraternityJSON = getFraternityJSON();
        parseJSON(fraternityJSON);

        alertListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(History.this, ClaimAlert.class);

                startActivity(intent);
            }
        });

        Button alertBTN = (Button) findViewById(R.id.alertTabBTN);
        alertBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent alertIntent = new Intent(getApplicationContext(),HostAlertsActivity.class);
                startActivity(alertIntent);
            }
        });

        Button pgTeamBTN = (Button) findViewById(R.id.pgTeamTabBTN);
        pgTeamBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pgTeamIntent = new Intent(getApplicationContext(),PgteamActivity.class);
                startActivity(pgTeamIntent);
            }
        });


        Button profileBTN = (Button) findViewById(R.id.profileTabBTN);
        profileBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileIntent = new Intent(getApplicationContext(),HostUserProfileScreen.class);
                startActivity(profileIntent);
            }
        });


    }
    //Gets the Json file
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

    //It Parses the json file and extracts  the information for the respected fields.
    private void parseJSON(String jsonToParse) {
        try {
            JSONArray arr = new JSONArray(jsonToParse);
            for(int i=0; i < arr.length(); i++){
                try {
                    JSONObject alertsArray = arr.getJSONObject(i);
                    JSONObject basicUserProfileModel=alertsArray.getJSONObject("BasicUserProfileModel");
                    basicLastName=basicUserProfileModel.getString("lastName");
                    basicFirstName=basicUserProfileModel.getString("firstName");
                    basicAge=basicUserProfileModel.getString("age");
                    basicFullName=basicFirstName+""+basicLastName;

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



                    alertsListHost.add(new HostAlert(incidence,date,name,fraternity,location,  comments,  status,  time, imageURL,age,basicFullName,issueID));

                    //alertsListHost.add(new HostAlert(incidence,date,name,fraternity,location,  comments,  status,  time, imageURL,age));







                }
                catch(JSONException jsone) {
                    Log.d("ERROR", jsone.getMessage());
                }
            }
        }catch(JSONException jsone){
            Log.d("ERROR",jsone.getMessage());
        }
    }
}
