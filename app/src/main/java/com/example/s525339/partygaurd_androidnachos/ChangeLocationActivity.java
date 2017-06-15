package com.example.s525339.partygaurd_androidnachos;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * The user can change the  existing location to the new one.
 */
public class ChangeLocationActivity extends ProfileDropDownMenu {

    AssetManager assetManager;

    String imageURl;
    String univeristySrvcURL =  "";
    ImageView universityImageView;
    Bitmap bitmap;
    String universityname;
    int count = 0;
    ComplaintSingleton complaintSingleton;
    TextView edit;
    EditText currentUserLocation;

    ArrayList<String> universitiesList;
    Spinner spinner;

    TextView location;
    TextView newLocation;

    TextView save;
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
        universitiesList = new ArrayList<>();

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentview = inflater.inflate(R.layout.activity_change_location, null, false);
        drawer.addView(contentview, 0);

        complaintSingleton = ComplaintSingleton.getInstance();
        universityname = complaintSingleton.getUniversity_UserInfo();

        assetManager = getAssets();
        String fraternityJSON = getFraternityJSON();
        parseJSON(fraternityJSON);

        spinner = (Spinner) findViewById(R.id.universityMenu);
        spinner.setVisibility(View.INVISIBLE);
        universityImageView = (ImageView) findViewById(R.id.universityImage);

        edit = (TextView) findViewById(R.id.editTV);
        save = (TextView) findViewById(R.id.saveTV);
        save.setVisibility(View.INVISIBLE);

        currentUserLocation = (EditText) findViewById(R.id.currentLocationTV);
        currentUserLocation.setText(universityname);

        location = (TextView) findViewById(R.id.locationTV);
        newLocation = (TextView) findViewById(R.id.newLocationTV);
        newLocation.setVisibility(View.INVISIBLE);
        currentUserLocation.setEnabled(false);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentUserLocation.setVisibility(View.INVISIBLE);
                spinner.setVisibility(View.VISIBLE);
                location.setVisibility(View.INVISIBLE);
                edit.setVisibility(View.INVISIBLE);
                save.setVisibility(View.VISIBLE);
                newLocation.setVisibility(View.VISIBLE);

            }
        });

        //new LoadImage().execute(imageURl);



        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, universitiesList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                count = count+1;
                if(count>1) {
                    universityname = universitiesList.get(position);
                    Toast.makeText(getApplicationContext(), universityname, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                Toast.makeText(getApplicationContext(),"No changes made",Toast.LENGTH_SHORT).show();
            }

        });


    }
     //Gets the json from the service.
     private String getFraternityJSON() {
        InputStream input;
        String text = "";
        try {
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
                    universitiesList.add(universityArray.getString("univeristyName").toString());
                } catch (JSONException json) {
                    Log.d("ERROR", json.getMessage());
                }
            }
        } catch (JSONException json) {
            Log.d("ERROR", json.getMessage());
        }
    }
    //It gets the json file from url
    public static String fetchJSONFromURL(String urlstr) {
        StringBuilder jsonStrBuilder = new StringBuilder();
        try {
            URL url = new URL(urlstr);
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            while ((line = br.readLine()) != null)
                jsonStrBuilder.append(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonStrBuilder.toString();
    }

    private class LoadImage extends AsyncTask<String,Void,Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings) {

            try {
                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            universityImageView.setImageBitmap(bitmap);
        }
    }
}
