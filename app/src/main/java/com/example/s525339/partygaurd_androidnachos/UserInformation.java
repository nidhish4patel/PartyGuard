package com.example.s525339.partygaurd_androidnachos;

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * THis is where we can see the User information.User name,emilID,mobile,image can be seen here.
 */

public class UserInformation extends ProfileDropDownMenu {

    AssetManager assetManager;

    String name;
    String firstName;
    String lastName;
    String emailId;
    String mobile;
    String imageURl;
    String age;
    String location;
    int count = 0;
    Boolean errorThrown = true;
    Bitmap bitmap;
    ArrayList<String> universitiesList;
    String universityName;
    ComplaintSingleton complaintSingleton;
    boolean error;
    private static final String BUCKET_NAME="gdp2/";
    private static final String S3_URL = "https://s3-us-west-2.amazonaws.com/";
    File defaultUser = new File("https://s3-us-west-2.amazonaws.com/gdp2/default_user.png");
    File fileToUpload = new File("/storage/emulated/0/DCIM/Camera/IMG_20161020_070009.jpg");
    File fileToDownload = new File("/storage/sdcard0/Pictures/MY");
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    AmazonS3 s3;
    TransferUtility transferUtility;
    String azure_server = "";

    InputStream inputStream;

    TextView editText;
    TextView saveText;
    EditText userFirstName;
    EditText userLastName;
    EditText userEmail;
    EditText userMobile;
    EditText userLocation;
    EditText userOrganization;
    EditText birthDate;
    TextView username;
    Spinner universitySpinner;

    String userChoosenTask;
    static final int REQUEST_CAMERA = 1;
    static final int SELECT_FILE = 1;
    ImageView imageView;
    File destination;
    String univeristySrvcURL = "";

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
        View contentview = inflater.inflate(R.layout.activity_user_information, null, false);
        drawer.addView(contentview, 0);

        universitiesList = new ArrayList<>();

        editText = (TextView) findViewById(R.id.editTV);
        saveText = (TextView) findViewById(R.id.saveTV);
        saveText.setVisibility(View.INVISIBLE);

        userFirstName = (EditText) findViewById(R.id.userFirstName);
        userLastName = (EditText)findViewById(R.id.userLastName);
        userEmail = (EditText) findViewById(R.id.userEmailID);
        userMobile = (EditText) findViewById(R.id.userMobileNumber);
        imageView = (ImageView) findViewById(R.id.userImage);
        userLocation = (EditText) findViewById(R.id.userLocation);
        userOrganization = (EditText) findViewById(R.id.userOrganization);
        birthDate = (EditText) findViewById(R.id.userAgeTV);
        username = (TextView) findViewById(R.id.userName);
        universitySpinner = (Spinner) findViewById(R.id.spinner);

        assetManager = getAssets();

        String fraternityJSON = getFraternityJSON();
        parseJSON(fraternityJSON);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, universitiesList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        universitySpinner.setAdapter(adapter);

        complaintSingleton = ComplaintSingleton.getInstance();
        emailId = complaintSingleton.getEmail_UserInfo();
        firstName = complaintSingleton.getFirstName_UserInfo();
        lastName = complaintSingleton.getLastName_UserInfo();
        name = firstName + lastName;
        universityName = complaintSingleton.getUniversity_UserInfo();
        mobile = complaintSingleton.getPhoneNumber_UserInfo();
//      age = complaintSingleton.getAge_UserInfo();
        imageURl = complaintSingleton.getImage_UserInfo();

        userEmail.setEnabled(false);
        userFirstName.setEnabled(false);
        userLastName.setEnabled(false);
        userMobile.setEnabled(false);
        birthDate.setEnabled(false);
        userLocation.setEnabled(false);
        universitySpinner.setVisibility(View.INVISIBLE);


        editText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                userFirstName.setEnabled(true);
                userFirstName.setCursorVisible(true);
                userLastName.setEnabled(true);
                userEmail.setEnabled(true);
                userMobile.setEnabled(true);
                birthDate.setEnabled(true);
                userOrganization.setEnabled(true);
                userFirstName.setBackground(getResources().getDrawable(R.drawable.edittextstyle));
                userLastName.setBackground(getResources().getDrawable(R.drawable.edittextstyle));
                userMobile.setBackground(getResources().getDrawable(R.drawable.edittextstyle));
                userEmail.setBackground(getResources().getDrawable(R.drawable.edittextstyle));
                userOrganization.setBackground(getResources().getDrawable(R.drawable.edittextstyle));
                birthDate.setBackground(getResources().getDrawable(R.drawable.edittextstyle));
                editText.setVisibility(View.INVISIBLE);
                saveText.setVisibility(View.VISIBLE);
                userLocation.setVisibility(View.INVISIBLE);
                universitySpinner.setVisibility(View.VISIBLE);
                universitySpinner.setFocusable(true);
                universitySpinner.setFocusableInTouchMode(true);
            }
        });

        if(!imageUrl.equals("null"))
        {
            imageView.setImageBitmap(getRoundedShape(decodeFile(this, imageURl), 225));
        }

        username.setText(name);
        userFirstName.setText(firstName);
        userLastName.setText(lastName);
        userEmail.setText(emailId);
        userLocation.setText(universityName);
        userMobile.setText(mobile);
        //birthDate.setText(age);

        universitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                count = count + 1;
                if (count > 1) {
                    universityName = universitiesList.get(position);
                    userLocation.setText(universityName);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                Toast.makeText(getApplicationContext(), "No changes made", Toast.LENGTH_SHORT).show();
            }

        });

        saveText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                userFirstName.setVisibility(View.VISIBLE);
                userFirstName.setBackgroundResource(0);
                userLastName.setVisibility(View.VISIBLE);
                userLastName.setBackgroundResource(0);
                userFirstName.setEnabled(false);
                userLastName.setEnabled(false);
                userEmail.setBackgroundResource(0);
                userEmail.setEnabled(false);
                userMobile.setBackgroundResource(0);
                userMobile.setEnabled(false);
                birthDate.setEnabled(false);
                birthDate.setBackgroundResource(0);
                userOrganization.setEnabled(false);
                userOrganization.setBackgroundResource(0);
                userLocation.setEnabled(false);
                saveText.setVisibility(View.INVISIBLE);
                editText.setVisibility(View.VISIBLE);
                universitySpinner.setClickable(false);
                universitySpinner.setFocusable(false);
                universitySpinner.setFocusableInTouchMode(false);
                universitySpinner.clearFocus();

                new HttpAsyncTask().execute(azure_server + "api/Account/UpdateProfile");
                try {
                    new HttpAsyncTaskForUserInfo().execute(azure_server+ "Api/Account/UserInfo").get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                editText.setEnabled(true);
            }
        });

        int permission = ActivityCompat.checkSelfPermission(UserInformation.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    UserInformation.this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }

        // callback method to call credentialsProvider method.
        credentialsProvider();

        // callback method to call the setTransferUtility method
        setTransferUtility();

        setFileToUpload();


    }

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

    public Bitmap decodeFile(Context context, String resId) {
        Bitmap avatarBmp = null;
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy); // lets user to do network activity from main thread.
            InputStream in = new java.net.URL(resId).openStream();
            avatarBmp = BitmapFactory.decodeStream(in);
            in.close();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
        return avatarBmp;
    }

    public static Bitmap getRoundedShape(Bitmap scaleBitmapImage, int width) {
        // TODO Auto-generated method stub
        int targetWidth = width;
        int targetHeight = width;
        Bitmap targetBitmap = Bitmap.createBitmap(targetWidth,
                targetHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(targetBitmap);
        Path path = new Path();

        path.addCircle(((float) targetWidth) / 2,
                ((float) targetHeight - 1) / 2,

                (Math.min(((float) targetWidth + 1200) / 2, ((float) targetHeight)) / 2),
                Path.Direction.CCW);
        canvas.clipPath(path);
        Bitmap sourceBitmap = scaleBitmapImage;
        canvas.drawBitmap(sourceBitmap,
                new Rect(0, 0, sourceBitmap.getWidth(), sourceBitmap.getHeight()),
                new Rect(0, 0, targetWidth, targetHeight), null);
        return targetBitmap;
    }

    public void picUpload(View v) {
        selectImage();
    }
    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(UserInformation.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(UserInformation.this);
                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if (userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                if(bm !=null){
                    Uri selectedImageURI = data.getData();

                    destination = new File(getPath(getApplicationContext(),selectedImageURI));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        imageView.setImageBitmap(bm);
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".png");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        imageView.setImageBitmap(thumbnail);
    }

    public static String getRealPathFromURI_API19(Context context, Uri uri){
        String filePath = "";
        String wholeID = DocumentsContract.getDocumentId(uri);

        // Split at colon, use second item in the array
        String id = wholeID.split(":")[1];

        String[] column = { MediaStore.Images.Media.DATA };

        // where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, sel, new String[]{ id }, null);

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }

        cursor.close();

        return filePath;
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        String[] filePathColumn  = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query( MediaStore.Images.Media.EXTERNAL_CONTENT_URI, filePathColumn, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            int idx = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToPosition(idx);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
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

                String json = "";
                JSONObject jsonObject = new JSONObject();

                String fName = userFirstName.getText().toString();
                String lName = userLastName.getText().toString();
                String email = userEmail.getText().toString();
                String mobile = userMobile.getText().toString();
                String imageURL = S3_URL+BUCKET_NAME+email;
                Long phone = Long.parseLong(mobile);
                //jsonObject.put("Email", email);
                jsonObject.put("firstName", fName);
                jsonObject.put("lastName", lName);
                jsonObject.put("PhoneNumber", mobile);
                jsonObject.put("age","20");
                jsonObject.put("imgUrl", imageURL);
                json = jsonObject.toString();
                StringEntity se = new StringEntity(json);
                httpPost.setEntity(se);

                httpPost.setHeader("accept", "json");
                httpPost.setHeader("Content-type", "application/json");
                httpPost.setHeader("Authorization","Bearer "+ complaintSingleton.getAccess_token());
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
            if (errorThrown == false) {
                Toast.makeText(getBaseContext(), "Edit Successful", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getBaseContext(), result, Toast.LENGTH_SHORT).show();
            }
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

    public void credentialsProvider(){

        // Initialize the Amazon Cognito credentials provider
//        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
//                getApplicationContext(),
//                "us-east-1:dbacd6aa-9393-475e-b687-059de565eab2", // Identity Pool ID
//                Regions.US_EAST_1 // Region
//        );

        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "us-west-2:845d597c-66c5-4ed0-bcfd-a7bf377cb715", // Identity Pool ID
                Regions.US_WEST_2 // Region
        );

        setAmazonS3Client(credentialsProvider);

    }

    /**
     *  Create a AmazonS3Client constructor and pass the credentialsProvider.
     * @param credentialsProvider
     */
    public void setAmazonS3Client(CognitoCachingCredentialsProvider credentialsProvider){

        // Create an S3 client
        s3 = new AmazonS3Client(credentialsProvider);

        // Set the region of your S3 bucket
        s3.setRegion(Region.getRegion(Regions.US_WEST_2));

    }

    public void setTransferUtility(){

        transferUtility = new TransferUtility(s3, getApplicationContext());
    }

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @author paulburke
     */
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    /**
     * This method is used to upload the file to S3 by using TransferUtility class
     *
     */
    public void setFileToUpload(){
        if(destination == null)
        {
            fileToUpload = defaultUser;
        }
        else
        {
            fileToUpload = destination;
        }


        TransferObserver transferObserver = transferUtility.upload(
                "gdp2",     /* The bucket to upload to */
                userEmail.getText().toString(),    /* The key for the uploaded object */
                fileToUpload,/* The file where the data to upload exists */
                CannedAccessControlList.PublicRead
        );
        File f = fileToUpload;
        transferObserverListener(transferObserver);
    }

    /**
     *  This method is used to Download the file to S3 by using transferUtility class
     * @param view
     **/
    public void setFileToDownload(View view){

        TransferObserver transferObserver = transferUtility.download(
                "test.numetric",     /* The bucket to download from */
                "MY",    /* The key for the object to download */
                fileToDownload        /* The file to download the object to */
        );

        transferObserverListener(transferObserver);

    }

    /**
     * This is listener method of the TransferObserver
     * Within this listener method, we got status of uploading and downloading file,
     * to diaplay percentage of the part of file to be uploaded or downloaded to S3
     * It display error, when there is problem to upload and download file to S3.
     * @param transferObserver
     */

    public void transferObserverListener(TransferObserver transferObserver){

        transferObserver.setTransferListener(new TransferListener(){

            @Override
            public void onStateChanged(int id, TransferState state) {
                Log.e("statechange", state+"");
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                //   int percentage = (int) (bytesCurrent/bytesTotal * 100);
                //  Log.e("percentage",);
            }

            @Override
            public void onError(int id, Exception ex) {
                Log.e("error","error");
                error = false;
                //  Toast.makeText(SignUpActivity.this,"S3 upload:" + ex.getMessage(),Toast.LENGTH_SHORT).show();
            }

        });
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
                httpGet.setHeader("Authorization","Bearer "+ access_token);
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
//            parseJSON(result);
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
                JSONObject loginObject = new JSONObject(jsonToParse);
                try {
                    userEmail.setText(loginObject.getString("Email"));
                    userFirstName.setText(loginObject.getString("FirstName"));
                    userLastName.setText(loginObject.getString("LastName"));
                    complaintSingleton.setUserType_UserInfo(loginObject.getString("UserType"));
                    if(loginObject.getString("UserType").equals("basic")) {
                        userLocation.setText(loginObject.getString("University"));
                    }
                    userMobile.setText(loginObject.getString("PhoneNumber"));
                    //complaintSingleton.setAge_UserInfo(loginObject.getString("Age"));
                    //complaintSingleton.setImage_UserInfo(loginObject.getString("Image"));
                }
                catch(JSONException jsone)
                {
                    Log.d("ERROR", jsone.getMessage());
                }
            }catch(JSONException jsone){
                Log.d("ERROR",jsone.getMessage());
            }
        }
    }
}


