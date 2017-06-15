package com.example.s525339.partygaurd_androidnachos;

import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

/**
 * This screen is used to select the 'Location' where the issue is going on.A basic user could select a location
 * after he selects university name and fraternity name.
 */
public class LocationActivity extends ProfileDropDownMenu {

    Locations[] locationsArray={new Locations("Upstairs"),new Locations("Main Floor"), new Locations("Basement"), new Locations("Deck")};
    ArrayList<Locations> locationsList;
    ImageView locationImageView;
//    RoundedImage roundedImage;
    ArrayList<Fraternities> fraternitiesArrayList;
    Activity cntx;
    static Context mcontext;
    private static int[] listview_images ={R.mipmap.user_icon_location};
    TextView fraternity;
    Intent profileIntent;
    ComplaintSingleton complaintSingleton;
    String userName="Katie Harris";
    Boolean errorThrown = true;
    String issueLocation;
    String azure_server = "";

    TextView locationNameFromCustomDialogTV;
    EditText commentsFromCustomDialogET;

    TextView issueNameFromLocationActivity;


    String alertNameFromCustomDialog;
    String locationNameFromCustomDialog;
    String commentsFromCustomDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_location);

        try {
            ApplicationInfo ai = getPackageManager().getApplicationInfo(this.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            azure_server = bundle.getString("azure_server");
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("AZURE_SERVICE", "Failed to load meta-data, NameNotFound: " + e.getMessage());
        } catch (NullPointerException e) {
            Log.e("AZURE_SERVICE", "Failed to load meta-data, NullPointer: " + e.getMessage());
        }

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentview = inflater.inflate(R.layout.activity_location, null, false);
        drawer.addView(contentview, 0);


        issueNameFromLocationActivity=(TextView)findViewById(R.id.issueNameTV);


        final ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater().inflate(R.layout.action_bar, null);

//        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayShowHomeEnabled(false);
//        actionBar.setDisplayShowTitleEnabled(false);
//        actionBar.setDisplayShowCustomEnabled(true);
//        actionBar.setCustomView(actionBarLayout);

        TextView fraternityName=(TextView)findViewById(R.id.fraternityNameTV);
        TextView fraternityFullName=(TextView)findViewById(R.id.fraternityFullNameTV);
        TextView issueName=(TextView)findViewById(R.id.issueNameTV);
        complaintSingleton = ComplaintSingleton.getInstance();
        String issueNameFromReportIssue=complaintSingleton.getIssueType();
        String fraternityNameFromIssue=complaintSingleton.getFraternityName();
        String fraternityDescFromIssue=complaintSingleton.getFraternityDesc();
        issueName.setText(issueNameFromReportIssue);
        fraternityName.setText(fraternityNameFromIssue);
        fraternityFullName.setText(fraternityDescFromIssue);


        ListView locationsListView = (ListView) findViewById(R.id.locationListView);
        locationsList=new ArrayList<>(Arrays.asList(locationsArray));
        LocationAdapter locationAdapter= new LocationAdapter(this, R.layout.location_list_item,R.id.locationTV,locationsList);
        locationsListView.setAdapter(locationAdapter);

        final TextView alertNameFromCustomDialogTV=(TextView)findViewById(R.id.alertTypeNameTV);
        locationNameFromCustomDialogTV=(TextView) findViewById(R.id.locationNameTV);






        locationsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                if (position==0){
                    issueLocation=locationsArray[position].getLocationName();
                }else if (position==1){
                    issueLocation=locationsArray[position].getLocationName();
                }else if (position==2){
                    issueLocation=locationsArray[position].getLocationName();
                }else if (position==3){
                    issueLocation=locationsArray[position].getLocationName();
                }
                final View customDialogView= LayoutInflater.from(LocationActivity.this).inflate(R.layout.activity_custom_dialog__report_location,null);
                commentsFromCustomDialogET=(EditText) customDialogView.findViewById(R.id.dialogBoxCommentET);
                //updating issue and location in custom dialog box---starting
                CharSequence locationName=  parent.getItemAtPosition(position).toString();
                TextView alertName=(TextView) customDialogView.findViewById(R.id.alertTypeNameTV);
                alertName.setText(issueNameFromLocationActivity.getText().toString());
                TextView locationNameValue=(TextView) customDialogView.findViewById(R.id.locationNameTV);
                locationNameValue.setText(issueLocation);
                Button negativeBTN=(Button) customDialogView.findViewById(R.id.cancleBTN);
                Button positiveBTN=(Button) customDialogView.findViewById(R.id.acceptBTN);




                TextView locationNameInCustTv=(TextView)customDialogView.findViewById(R.id.locationNameTV);



                positiveBTN.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        alertNameFromCustomDialog=issueNameFromLocationActivity.getText().toString();
                        commentsFromCustomDialog = commentsFromCustomDialogET.getText().toString();
                        Calendar c = Calendar.getInstance();
                        System.out.println("<<<<<<<<<<<<========Current time ========>>>>>>>> " + c.getTime());

                        complaintSingleton.setIssueLocation(issueLocation);
                        Intent profileIntent = new Intent(getApplicationContext(),UniversityActivity.class);
                        new HttpAsyncTask().execute(azure_server + "api/IssuesModels");
//                        profileIntent.putExtra("AlertName",alertNameFromCustomDialog);
//                        profileIntent.putExtra("Location",locationNameFromCustomDialog);
//                        profileIntent.putExtra("Comments",commentsFromCustomDialog);

                        startActivity(profileIntent);
                        createNotification();
                    }
                });
                negativeBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent cancleIntent=new Intent(getApplicationContext(),LocationActivity.class);
                        startActivity(cancleIntent);
                    }
                });


                //updating issue and location in custom dialog box---ENDING

                AlertDialog.Builder builder=new AlertDialog.Builder(LocationActivity.this);


                builder.setView(customDialogView);


                AlertDialog alert=builder.create();
                alert.show();

                alert.getWindow().setBackgroundDrawableResource(R.color.colordarkgrey);
                alert.setOnShowListener(new DialogInterface.OnShowListener() {

                    @Override
                    public void onShow(DialogInterface dialog) {

                    }
                });

            }
        });


        String imageURL = complaintSingleton.getFraternityImageURL();

        ImageView fraternityImage = (ImageView) findViewById(R.id.fraternityImageView);
        fraternityImage.setImageBitmap(getRoundedShape(decodeFile(this, imageURL),225));


    }

    public void createNotification(){

        Intent notifyIntent=new Intent(LocationActivity.this,GuardAlert.class);
        notifyIntent.putExtra("alertName",alertNameFromCustomDialog);
        notifyIntent.putExtra("locationName",locationNameFromCustomDialog);
        notifyIntent.putExtra("userName",userName);
        notifyIntent.putExtra("fraternityName",complaintSingleton.getFraternityDesc());


        TaskStackBuilder taskStackBuilder=TaskStackBuilder.create(getApplicationContext());
        taskStackBuilder.addParentStack(GuardAlert.class);
        taskStackBuilder.addNextIntent(notifyIntent);

        PendingIntent pendingIntent=taskStackBuilder.getPendingIntent(123,PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent actionPendingIntent=PendingIntent.getActivity(this,222,notifyIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder=new NotificationCompat.Builder(this);
        builder.setContentTitle("AndroidNachos");
        builder.setContentText(locationNameFromCustomDialog+"--"+alertNameFromCustomDialog);
//        builder.setContentText("Harish, it is!!");
        builder.setSmallIcon(R.drawable.notification);
        builder.setContentIntent(pendingIntent);
        builder.setDefaults(Notification.DEFAULT_SOUND);
        builder.addAction(R.drawable.notification,"Open",actionPendingIntent);

        Notification notification=builder.build();
        NotificationManager notificationManager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1,notification);
    }
    public void fieldToFraternityIntent(View v){
        Intent fraternityFieldToFraternityIntent=new Intent(LocationActivity.this,FraternitiesActivity.class);
        complaintSingleton = ComplaintSingleton.getInstance();
        fraternityFieldToFraternityIntent.putExtra("universityName",complaintSingleton.getUniverSityName());
        startActivity(fraternityFieldToFraternityIntent);
    }

    public void intentForIssue(View v){
        Intent issueFieldToIssueIntent=new Intent(LocationActivity.this,ReportIssueActivity.class);
        complaintSingleton = ComplaintSingleton.getInstance();
        issueFieldToIssueIntent.putExtra("IssueName",complaintSingleton.getIssueType());
        issueFieldToIssueIntent.putExtra("universityName",complaintSingleton.getUniverSityName());
        issueFieldToIssueIntent.putExtra("fraternityName",complaintSingleton.getFraternityName());
        issueFieldToIssueIntent.putExtra("fraternityFullName",complaintSingleton.getFraternityDesc());
        startActivity(issueFieldToIssueIntent);
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
   //Makes the image round-shaped
    public static Bitmap getRoundedShape(Bitmap scaleBitmapImage,int width) {
        // TODO Auto-generated method stub
        int targetWidth = width;
        int targetHeight = width;
        Bitmap targetBitmap = Bitmap.createBitmap(targetWidth,
                targetHeight,Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(targetBitmap);
        Path path = new Path();
        path.addCircle(((float) targetWidth)/2,
                ((float) targetHeight - 1) / 2,
                (Math.min(((float) targetWidth + 100)/2,((float) targetHeight)) / 2),
                Path.Direction.CCW);
        canvas.clipPath(path);
        Bitmap sourceBitmap = scaleBitmapImage;
        canvas.drawBitmap(sourceBitmap,
                new Rect(0, 0, sourceBitmap.getWidth(),sourceBitmap.getHeight()),
                new Rect(0, 0, targetWidth,targetHeight), null);
        return targetBitmap;
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
                httpPost.setHeader("Authorization","Bearer "+ complaintSingleton.getAccess_token());

                String json = "";
                JSONObject jsonObject = new JSONObject();


                String issueNamePost = complaintSingleton.getIssueType();
                String issueLocationPost = complaintSingleton.getIssueLocation();
                String issueDatePost = "2016-11-06T20:24:59.67822-06:00";
                String issueCommentsPost = complaintSingleton.getIssueComments();
                String fraternityNamePost = complaintSingleton.getFraternityDesc();

                jsonObject.put("issueName", issueNamePost);
                jsonObject.put("issueLocation", issueLocationPost);
                jsonObject.put("issueDate", issueDatePost);
                jsonObject.put("comments", issueCommentsPost);
                jsonObject.put("fraternityName", fraternityNamePost);

                json = jsonObject.toString();
                StringEntity se = new StringEntity(json);
                httpPost.setEntity(se);

                httpPost.setHeader("accept", "json");
                httpPost.setHeader("Content-type", "application/json");

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
                Toast.makeText(getBaseContext(), "Alert Issued to Fraternity", Toast.LENGTH_SHORT).show();
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
}
