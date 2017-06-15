package com.example.s525339.partygaurd_androidnachos;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

/**
 * This is where user selects university from drop-down menu.
 */

public class UniversityActivity extends ProfileDropDownMenu {

    ArrayList<String> universitiesList;
    AssetManager assetManager;
    ComplaintSingleton complaintSingleton;
    String univeristySrvcURL = "";
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

        univeristySrvcURL = azure_server + "api/UniversityModels";
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentview = inflater.inflate(R.layout.activity_university, null, false);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("PartyGuard");
        drawer.addView(contentview, 0);

        universitiesList = new ArrayList<>();
        assetManager = getAssets();
        String fraternityJSON = getFraternityJSON();
        parseJSON(fraternityJSON);
//        final ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater().inflate(R.layout.action_bar, null);
//        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayShowHomeEnabled(false);
//        actionBar.setDisplayShowTitleEnabled(false);
//        actionBar.setDisplayShowCustomEnabled(true);
//        actionBar.setCustomView(actionBarLayout);
        ListView universityLV = (ListView) findViewById(R.id.universityLV);
        ArrayAdapter<String> universityAdapter = new ArrayAdapter<String>(this, R.layout.university_list_item, R.id.universityTV, universitiesList);
        universityLV.setAdapter(universityAdapter);

        universityLV.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        ProgressDialog progress = new ProgressDialog(UniversityActivity.this);
//                        progress.setTitle("Loading...");
//                        progress.setMessage("Wait while loading..");
//                        progress.setIndeterminate(true);
////                        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
////                        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//                        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
//                        progress.show();

                        ProgressDialog progressDialog=new ProgressDialog(UniversityActivity.this);
                        progressDialog.setTitle("Loading..!");
                        progressDialog.setMessage("Wait until it loads..");
//                        progressDialog.setIndeterminate(true);
                        progressDialog.setCancelable(false);
                        progressDialog.show();

// To dismiss the dialog
//                        progress.dismiss();
                        Intent fraternityIntent = new Intent(getApplicationContext(), FraternitiesActivity.class);
                        fraternityIntent.putExtra("universityName", universitiesList.get(position));
                        complaintSingleton = ComplaintSingleton.getInstance();
                        complaintSingleton.setUniverSityName(universitiesList.get(position));
                        startActivity(fraternityIntent);
                        Toast.makeText(getApplicationContext(), String.format("%s", universitiesList.get(position)), Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    private String getFraternityJSON() {
        InputStream input;
        String text = "";
        try {
//            input = assetManager.open("Gandhapuneni_Fraternities_JSON.txt");
//
//            int size = input.available();
//            byte[] buffer = new byte[size];
//            input.read(buffer);
//            input.close();
//
//            // byte buffer into a string
//            text = new String(buffer);
            text = fetchJSONFromURL(univeristySrvcURL);
            return text;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return text;
    }
    //It parses the json file
    private void parseJSON(String jsonToParse) {
        try {
            JSONArray arr = new JSONArray(jsonToParse);
            for (int i = 0; i < arr.length(); i++) {
                try {
                    JSONObject universityArray = arr.getJSONObject(i);
                    //universitiesList.add(universityArray.getString("universityName").toString());
                    universitiesList.add(universityArray.getString("univeristyName").toString());
                } catch (JSONException json) {
                    Log.d("ERROR", json.getMessage());
                }
            }
        } catch (JSONException json) {
            Log.d("ERROR", json.getMessage());
        }
    }
     //It fetches the Json file from url

    public static String fetchJSONFromURL(String urlstr) {
        StringBuilder jsonStrBuilder = new StringBuilder();
        try {
            URL url = new URL(urlstr);
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy); // lets user to do network activity from main thread.
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            while ((line = br.readLine()) != null)
                jsonStrBuilder.append(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonStrBuilder.toString();


    }
}
