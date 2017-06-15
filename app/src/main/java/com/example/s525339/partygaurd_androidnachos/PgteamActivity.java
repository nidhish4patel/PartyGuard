package com.example.s525339.partygaurd_androidnachos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.os.StrictMode;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

/**
 * This screen shows the list of guards under the particlualr Host user.This is the landing page for the Host user.
 * The guard's name,image and whether they are available or not(A toggle-button is used for this purpose) is shown here.
 */
public class    PgteamActivity extends AppCompatActivity {
    GridView gv;
    Context context;
    ArrayList prgmName;
    ArrayList<Guard> guardList;
    AssetManager assetManager;
    private LayoutInflater inflater;
    String number;
    String email;
    String imageUrl;
    TextView guardName;
    int position;
    ComplaintSingleton complaintSingleton;
    String guardFirstName;
    String guardLastName;
    String guardEmail;
    String guardPhoneNumebr;
    String guardNameValue;
    boolean isAvailable;
    int guardID;

  //  Guard info;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    String pgteamSrvcURL = "";
    InputStream inputStream;
    String pgteamSrvcResposnse = "";
    String azure_server = "";

    //This is where the Pgteam activity starts..
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pgteam);
        try {
            ApplicationInfo ai = getPackageManager().getApplicationInfo(this.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            azure_server = bundle.getString("azure_server");
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("AZURE_SERVICE", "Failed to load meta-data, NameNotFound: " + e.getMessage());
        } catch (NullPointerException e) {
            Log.e("AZURE_SERVICE", "Failed to load meta-data, NullPointer: " + e.getMessage());
        }

        pgteamSrvcURL = azure_server + "api/HostUserProfile/DisplayGuardDetails";

        gv = (GridView) findViewById(R.id.gird_view);
        complaintSingleton = ComplaintSingleton.getInstance();
        final View customDialogView= LayoutInflater.from(PgteamActivity.this).inflate(R.layout.pgteam_griditem,null);
        guardName=(TextView)customDialogView.findViewById(R.id.textView1);
        ImageView im=(ImageView)customDialogView.findViewById(R.id.imageView1);
       // im.setImageBitmap(getRoundedShape(decodeFile(this, imageUrl), 1500));
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                position=i;
            }
        });
     //   info=new Guard();
        //gv.setAdapter(new ImageAdapter(this,prgmNameList,prgmImages));

        Button pgTeamBTN = (Button)findViewById(R.id.pgTeamTabBTN);
        pgTeamBTN.setBackgroundColor(getResources().getColor(R.color.IndianRed));
        pgTeamBTN.setTextColor(getResources().getColor(R.color.colorWhite));

        guardList = new ArrayList<>();
        assetManager = getAssets();


        String pgteamJSON = getPgteamJSON();
        parseJSON(pgteamJSON);

        GuardAdapter<Guard> guardTeamAdapter = new GuardAdapter<>(this, R.layout.pgteam_griditem, R.id.textView1, guardList);
        gv.setAdapter(guardTeamAdapter);
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View vi = inflater.inflate(R.layout.pgteam_griditem, null);
        final View pgItem= LayoutInflater.from(PgteamActivity.this).inflate(R.layout.pgteam_griditem,null);

        Button alertBTN = (Button) findViewById(R.id.alertTabBTN);
        alertBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent alertIntent = new Intent(getApplicationContext(), HostAlertsActivity.class);
                startActivity(alertIntent);
            }
        });

        Button historyBTN = (Button) findViewById(R.id.historyTabBTN);
        historyBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent historyIntent = new Intent(getApplicationContext(), History.class);
                startActivity(historyIntent);
            }
        });

        Button profileBTN = (Button) findViewById(R.id.profileTabBTN);
        profileBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileIntent = new Intent(getApplicationContext(), HostUserProfileScreen.class);
                startActivity(profileIntent);
            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void pgTeamGrid(View v){
//        Intent guardIntent = new Intent(PgteamActivity.this, GuardProfileInfo.class);
        Intent guardIntent = new Intent(PgteamActivity.this, GuardProfileInfo.class);
//        while (guardName.getText()==guardList.get(position).getGuardName()) {

            guardIntent.putExtra("url",guardList.get(position).getImageURL());
            guardIntent.putExtra("email", guardList.get(position).getEmail());
            guardIntent.putExtra("mobile", guardList.get(position).getMobile());
//        }
        startActivity(guardIntent);
//
    }

    //This method gets the json from the service
    private String getPgteamJSON() {
        InputStream input;
        String text = "";
        try {
//            input = assetManager.open("Sridhar_PGTeam");
//
//            int size = input.available();
//            byte[] buffer = new byte[size];
//            input.read(buffer);
//            input.close();

            // byte buffer into a string
            //text = new String(buffer);
            pgteamSrvcResposnse = new PGTeamService().execute(pgteamSrvcURL).get();
            text = pgteamSrvcResposnse;
//            parseJSON(text);


            return text;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return text;
    }

    //This method parse the json file and extract the required info.
    private void parseJSON(String jsonToParse) {
        try {
            JSONArray arr = new JSONArray(jsonToParse);
            for (int i = 0; i < arr.length(); i++) {
                try {
                    JSONObject guard = arr.getJSONObject(i);
                    JSONObject guardInfo=guard.getJSONObject("ApplicationUser");
                    guardEmail=guardInfo.getString("Email");
                    guardPhoneNumebr=guardInfo.getString("PhoneNumber");
                    guardFirstName = guard.getString("firstName");
                    guardLastName = guard.getString("lastName");
                    guardID=guard.getInt("guardProfileID");
                    isAvailable = guard.getBoolean("isActive");
                    email="guarduser@gmail.com";
//                    number=guard.getString("PhoneNumber");
                    number="6605415457";
//                    email=guard.getString("EmailAddress");
//                    imageUrl = guard.getString("imgUrl");
                    imageUrl="https://s3-us-west-2.amazonaws.com/gdp2/Heath-1.jpg";

                    guardNameValue = guardFirstName + " " + guardLastName;
                    //String fraternityDescription = guard.getString("fraternityDescription");
                    guardList.add(new Guard(guardNameValue, isAvailable, imageUrl,guardPhoneNumebr,guardEmail,guardID));
                } catch (JSONException jsone) {
                    Log.d("ERROR", jsone.getMessage());
                }
            }
        } catch (JSONException jsone) {
            Log.d("ERROR", jsone.getMessage());
        }
    }

    public Bitmap decodeFile(Context context, String resId) {
        Bitmap avatarBmp = null;
        try
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy); // lets user to do network activity from main thread.
            InputStream in = new java.net.URL(resId).openStream();
            avatarBmp = BitmapFactory.decodeStream(in);
            in.close();
        }
        catch(Exception e)
        {
            e.getLocalizedMessage();
        }
        return avatarBmp;
    }

    // This method makes the image round-shaped
    public static Bitmap getRoundedShape(Bitmap scaleBitmapImage,int width) {
        // TODO Auto-generated method stub
        int targetWidth = width;
        int targetHeight = width;
//        Bitmap targetBitmap = Bitmap.createBitmap(100,
//                100,Bitmap.Config.ARGB_8888);
        Bitmap targetBitmap = Bitmap.createBitmap(targetWidth,
                targetHeight,Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(targetBitmap);
        Path path = new Path();

        path.addCircle(((float) targetWidth)/2,
                ((float) targetHeight - 1) / 2,

                (Math.min(((float) targetWidth + 1200)/2,((float) targetHeight)) / 2),
                Path.Direction.CCW);
        canvas.clipPath(path);
        Bitmap sourceBitmap = scaleBitmapImage;
        canvas.drawBitmap(sourceBitmap,
                new Rect(0, 0, sourceBitmap.getWidth(),sourceBitmap.getHeight()),
//                new Rect(0, 0, sourceBitmap.getWidth(),sourceBitmap.getHeight())
                new Rect(70, 70, targetWidth,targetHeight), null);
        return targetBitmap;
    }


    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Pgteam Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.s525339.partygaurd_androidnachos/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Pgteam Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.s525339.partygaurd_androidnachos/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    private class PGTeamService extends AsyncTask<String, String, String> {
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
                HttpPost httpPost = new HttpPost(url);

                String json = "";
                json = "univeristyName=" + "Northwest Missouri";
                StringEntity se = new StringEntity(json);
                httpPost.setHeader("Authorization","Bearer "+ access_token);
                HttpResponse httpResponse = httpclient.execute(httpPost);

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
            pgteamSrvcResposnse = result;
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