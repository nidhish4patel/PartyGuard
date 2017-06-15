package com.example.s525339.partygaurd_androidnachos;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * This screen  shows the list of fraternities under the selected university.
 * It shows each fraternity name,discription and image.
 */
public class FraternitiesActivity extends ProfileDropDownMenu implements UniversityDialogFragment.University {

    //Fraternities[] fraternityArray = {new Fraternities("SigEp","Sigma Phi Epsilon","Image1"),new Fraternities("TKE","Tau Kappa Epsilon","Image1"),new Fraternities("Phi Delt","Phi Delta Theta","Image1"),new Fraternities("Phi Sig","Phi Sigma Kappa","Image1"),};
    ArrayList<Fraternities> fraternityList = new ArrayList<>();
    String fraternityJSON;
    String universityName = "";
    AssetManager assetManager;
    ComplaintSingleton complaintSingleton;
//    String fraternitySrvcURL = "http://partyguardservices20161025060016.azurewebsites.net/api/FraternityModels/FraternityList";
    String fraternitySrvcURL = "";
    InputStream inputStream;
    String fraternitySrvcResposnse = "";
    String azure_server = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_fraternities);
        try {
            ApplicationInfo ai = getPackageManager().getApplicationInfo(this.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            azure_server = bundle.getString("azure_server");
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("AZURE_SERVICE", "Failed to load meta-data, NameNotFound: " + e.getMessage());
        } catch (NullPointerException e) {
            Log.e("AZURE_SERVICE", "Failed to load meta-data, NullPointer: " + e.getMessage());
        }
        fraternitySrvcURL = azure_server + "FraternityList";
        complaintSingleton = ComplaintSingleton.getInstance();
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentview = inflater.inflate(R.layout.activity_fraternities, null, false);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("PartyGuard");
        drawer.addView(contentview, 0);

        assetManager = getAssets();
//        final ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater().inflate(R.layout.action_bar, null);
//        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayShowHomeEnabled(false);
//        actionBar.setDisplayShowTitleEnabled(false);
//        actionBar.setDisplayShowCustomEnabled(true);
//        actionBar.setCustomView(actionBarLayout);
        Intent universityIntent = getIntent();
        universityName = universityIntent.getStringExtra("universityName");

        Intent gettingIntentFromLocation = getIntent();
        universityName = gettingIntentFromLocation.getStringExtra("universityName");

        if (!(universityName.equals(""))) {
            String fraternityJSON = getFraternityJSON();
            parseJSON(fraternityJSON);
            ListView fraternityListView = (ListView) findViewById(R.id.fraternityListView);
            //fraternityList = new ArrayList<>(Arrays.asList(fraternityArray));
            FraternityAdapter fraternityAdapter = new FraternityAdapter(this, R.layout.fraternity_list_item_menu, R.id.upperTV, fraternityList);
            fraternityListView.setAdapter(fraternityAdapter);

            fraternityListView.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent fraternityIntent = new Intent(FraternitiesActivity.this, ReportIssueActivity.class);
                            fraternityIntent.putExtra("FraternityName", fraternityList.get(position).getFraternityName().toString());
                            fraternityIntent.putExtra("FraternityDescription", fraternityList.get(position).fraternityDescription.toString());
                            fraternityIntent.putExtra("FraternityImage", fraternityList.get(position).getImageId().toString());


                            complaintSingleton.setFraternityName(fraternityList.get(position).getFraternityName().toString());
                            complaintSingleton.setFraternityDesc(fraternityList.get(position).fraternityDescription.toString());
                            complaintSingleton.setFraternityImageURL(fraternityList.get(position).getImageId().toString());
                            startActivity(fraternityIntent);
                            //Toast.makeText(getApplicationContext(),String.format("%d",position),Toast.LENGTH_LONG).show();
                        }
                    }
            );
        }


    }
    //It gets the json from the service
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
            fraternitySrvcResposnse = new HttpAsyncTask().execute(fraternitySrvcURL).get();
            text = fraternitySrvcResposnse;
            return text;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return text;
    }
   //It parses the json file and extract the field values.
    private void parseJSON(String jsonToParse) {
        try {
            JSONArray arr = new JSONArray(jsonToParse);
            for (int i = 0; i < arr.length(); i++) {
                try {
                    JSONObject universityArray = arr.getJSONObject(i);
                    JSONObject universityObj = universityArray.getJSONObject("UniversityModel");
                    if (universityObj.getString("univeristyName").equals(universityName)) {
                        String fraternityName = universityArray.getString("nickName");
                        String fraternityDescription = universityArray.getString("fraternityName");
                        String fraternityPaymentStatus=universityArray.getString("paymentStatus");
                        //String fraternityImage = fraternity.getString("fraternityeImageURL");
                        fraternityList.add(new Fraternities(fraternityName, fraternityDescription, "https://drive.google.com/uc?id=0By5v-xaILwvHWW0tZlFfaW5FQzA",fraternityPaymentStatus));

                    }
                } catch (JSONException jsone) {
                    Log.d("ERROR", jsone.getMessage());
                }
            }
        } catch (JSONException jsone) {
            Log.d("ERROR", jsone.getMessage());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.fraternities_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.university:
                chooseUniversity();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void chooseUniversity() {
        UniversityDialogFragment universityDF = new UniversityDialogFragment();
        universityDF.show(getSupportFragmentManager(), "TAG");
    }

    @Override
    public void getUniversityName(String universityName) {
        Toast.makeText(getApplicationContext(), universityName, Toast.LENGTH_SHORT).show();
    }

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

                String json = "";
                json = "univeristyName=" + "Northwest Missouri";
                StringEntity se = new StringEntity(json);
                httpGet.setHeader("Authorization","Bearer "+ access_token);
                HttpResponse httpResponse = httpclient.execute(httpGet);

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

//        });
            return result;
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            //parseJSON(result);
            fraternitySrvcResposnse = result;
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
