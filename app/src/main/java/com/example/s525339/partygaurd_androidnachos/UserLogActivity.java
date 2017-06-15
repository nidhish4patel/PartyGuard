package com.example.s525339.partygaurd_androidnachos;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * This is where first user can lon-on to the app.Basic,Host and guard user can use this screen.
 */

public class UserLogActivity extends ProfileDropDownMenu {

    ArrayList<AlertLog> alertsHistory =new ArrayList<>();

    AssetManager assetManager;

    String name;
    String date;
    String incidence;
    String fraternity;
    String location;
    String comments;
    String status;
    String time;
    String azure_server = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            ApplicationInfo ai = getPackageManager().getApplicationInfo(this.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            azure_server = bundle.getString("azure_server");
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("AZURE_SERVICE", "Failed to load meta-data, NameNotFound: " + e.getMessage());
        } catch (NullPointerException e) {
            Log.e("AZURE_SERVICE", "Failed to load meta-data, NullPointer: " + e.getMessage());
        }
        //setContentView(R.layout.activity_user_log);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
       // toolbar.setTitle("PartyGuard");
        LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentview = inflater.inflate(R.layout.activity_user_log,null,false);
        drawer.addView(contentview,0);
        assetManager = getAssets();

        ListView listView = (ListView)findViewById(R.id.logList);
        //alertsHistory = new ArrayList<>(Arrays.asList(alertLogsArray));
        AlertLogAdapter logAdapter = new AlertLogAdapter(this,R.layout.alertlog_listitem,R.id.alertTV,alertsHistory);
        listView.setAdapter(logAdapter);

        String fraternityJSON = getFraternityJSON();
        parseJSON(fraternityJSON);
    }

    private String getFraternityJSON() {
        InputStream input;
        String text = "";
        try {
            input = assetManager.open("Vinod_History_JSON.txt");

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
                    JSONObject universityArray = arr.getJSONObject(i);
                    if(universityArray.getString("Name").equals("Rahul Dravid")) {
                        name = universityArray.getString("Name");
                        date = universityArray.getString("Date");
                        incidence = universityArray.getString("Incident");
                        fraternity = universityArray.getString("Fraternity");
                        location = universityArray.getString("Location");
                        comments = universityArray.getString("Comments");
                        status = universityArray.getString("Status");
                        time = universityArray.getString("Time");
                        // CharSequence name_char=universityArray.getString("Name").toString();

                        System.out.println(name + " "  + " " + date + " " + incidence+ " "+fraternity+" "+location+" "+comments+" "+status);
                        alertsHistory.add(new AlertLog(incidence,date,time));





                    }
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
