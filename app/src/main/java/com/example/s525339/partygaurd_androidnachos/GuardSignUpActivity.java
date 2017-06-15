package com.example.s525339.partygaurd_androidnachos;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;

import android.app.DatePickerDialog;

import android.content.ContentUris;
import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.cognito.Dataset;
import com.amazonaws.mobileconnectors.cognito.DefaultSyncCallback;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.mobileconnectors.cognito.CognitoSyncManager;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A new Guard can sign-up by using this screen where validations for email id and password are provided.
 */

public class GuardSignUpActivity extends AppCompatActivity {

    private GoogleApiClient client;

    EditText fname;
    EditText lname;
    EditText email;
    EditText mobileNumber;
    EditText password;
    EditText repass;
    EditText subscriptionNum;
    Boolean errorThrown = true;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private Button btnSelect;
    private ImageView ivImage;
    private String userChoosenTask;
    ObjectMetadata myObjectMetadata;
    Map<String, String> userMetadata;
    CognitoCachingCredentialsProvider credentialsProvider;
    private static final String MY_BUCKET = "partyguard";
    File destination;
    Bitmap profileImage;
    ArrayList<String> universitiesList;
    Spinner spinner;
    String universityname;
    AssetManager assetManager;
    String firstName;
    String lastName;
    String mobilenumber;
    String dateOFBirth;
    String subscriptionCode;
    String emailAddress;
    String userPassword;
    String repeatPassword;
    String subscription;
    DatePickerDialog selectDatePickerDialog;
    SimpleDateFormat dateFormatter;
    boolean error;
    private static final String BUCKET_NAME="gdp2/";
    private static final String S3_URL = "https://s3-us-west-2.amazonaws.com/";
    File defaultUser = new File("https://s3-us-west-2.amazonaws.com/gdp2/default_user.png");
    String azure_server = "";



    File fileToUpload = new File("/storage/emulated/0/DCIM/Camera/IMG_20161020_070009.jpg");
    File fileToDownload = new File("/storage/sdcard0/Pictures/MY");
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    AmazonS3 s3;
    TransferUtility transferUtility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guard_sign_up);
        try {
            ApplicationInfo ai = getPackageManager().getApplicationInfo(this.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            azure_server = bundle.getString("azure_server");
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("AZURE_SERVICE", "Failed to load meta-data, NameNotFound: " + e.getMessage());
        } catch (NullPointerException e) {
            Log.e("AZURE_SERVICE", "Failed to load meta-data, NullPointer: " + e.getMessage());
        }
        universitiesList = new ArrayList<>();
        assetManager = getAssets();
        String UniversityJSON = getUniversityJSON();
        parseJSON(UniversityJSON);

        error = false;
        Button signUpCreatAccBtn=(Button)findViewById(R.id.createAccountBtn);
        fname= (EditText) findViewById(R.id.profileFirstNameET);
        lname= (EditText) findViewById(R.id.profileLastNameET);
        email= (EditText) findViewById(R.id.EmailET);
        password= (EditText) findViewById(R.id.passwordET);
        repass= (EditText) findViewById(R.id.repeatPasswordET);
        mobileNumber = (EditText)findViewById(R.id.phoneNumberET);
        subscriptionNum = (EditText)findViewById(R.id.subscriptionCode);
        ivImage=(ImageView)findViewById(R.id.profileImageView);
     //   spinner = (Spinner) findViewById(R.id.universityMenu);

        fname.setError("First name is required!");
        lname.setError("Last name is required!");
        password.setError("password is required");
        repass.setError("re-enter password is required");
        mobileNumber.setError("Mobile number is required");
        subscriptionNum.setError("subscriptionCode is required");
        emailAddress = email.getText().toString();
        if(emailAddress.length() == 0) {
            email.setError("Email required!");
            if (isValidEmail(emailAddress)) {
                //Toast.makeText(getApplicationContext(),"valid email address",Toast.LENGTH_SHORT).show();
            }
//            else{
//                showAlertValidation();
//            }
        }

        signUpCreatAccBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                firstName = fname.getText().toString();
                lastName = lname.getText().toString();
                emailAddress = email.getText().toString();
                userPassword = password.getText().toString();
                repeatPassword = repass.getText().toString();
                mobilenumber = mobileNumber.getText().toString();
                subscriptionCode = subscriptionNum.getText().toString();
                //System.out.println(userPassword);
                //System.out.println(repeatPassword);

                if (firstName.length() == 0 || lastName.length() == 0 || mobilenumber.length() == 0 || subscriptionCode.length() == 0 || emailAddress.length() == 0 || userPassword.length() == 0 || repeatPassword.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Enter required Details", Toast.LENGTH_SHORT).show();
                    error = true;
                }


                if (mobilenumber.length() > 0) {
                    if (mobilenumber.length() != 10) {
                        Toast.makeText(getApplicationContext(), "Mobile Number must be of length 10", Toast.LENGTH_SHORT).show();
                        error = true;
                    }
                }

                if (emailAddress.length() > 0) {
                    Boolean emailFlag = (isValidEmail(emailAddress));
                    if (emailFlag != true) {
                        error = true;
                        Toast.makeText(getApplicationContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
                    }
                }

                if (userPassword.length() > 0 && repeatPassword.length() > 0) {

                    if (userPassword.length() < 8) {
                        Toast.makeText(getApplicationContext(), "Password must have minimum of 8 characters", Toast.LENGTH_SHORT).show();
                    }
                    if (userPassword.length() > 16) {
                        Toast.makeText(getApplicationContext(), "Password must have maximum of 8 characters", Toast.LENGTH_SHORT).show();
                    }
                    if (!userPassword.equals(repeatPassword)) {
                        error = true;
                        Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                    } else {
                        Boolean passwordFlag = isValidPassword(userPassword);
                        if (passwordFlag != true) {
                            error = true;
                            Toast.makeText(getApplicationContext(), "Password must contain atleast one uppercase, one lowercase, one special character and one one digit", Toast.LENGTH_SHORT).show();
                        }
                    }

                }

                 /* Code for S3*/
                int permission = ActivityCompat.checkSelfPermission(GuardSignUpActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

                if (permission != PackageManager.PERMISSION_GRANTED) {
                    // We don't have permission so prompt the user
                    ActivityCompat.requestPermissions(
                            GuardSignUpActivity.this,
                            PERMISSIONS_STORAGE,
                            REQUEST_EXTERNAL_STORAGE
                    );
                }

                // callback method to call credentialsProvider method.
                credentialsProvider();

                // callback method to call the setTransferUtility method
                setTransferUtility();

                setFileToUpload();
                //S3Object s3Image = s3.getObject(new GetObjectRequest(BUCKET_NAME, emailAddress.toString()));


                /* Code for S3*/
                if (!(error)) {
                    new HttpAsyncTask().execute(azure_server + "api/Account/Register");
                    if(errorThrown == false) {
                        Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(loginIntent);
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(),"Please check the details",Toast.LENGTH_SHORT).show();
                }
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public static boolean isValidPassword(final String password) {
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()-_+=]).{8,16})";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public static boolean isValiduserName(final String userName) {
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{3,15})";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(userName);
        return matcher.matches();
    }

    public void uploadPicture(View v){
        selectImage();
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if(userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
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

    private String getUniversityJSON() {
        InputStream input;
        String text = "";
        try {
            input = assetManager.open("Gandhapuneni_Fraternities_JSON.txt");

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
    //It parses the Json file
    private void parseJSON(String jsonToParse) {
        try {
            JSONArray arr = new JSONArray(jsonToParse);
            for(int i=0; i < arr.length(); i++){
                try
                {
                    JSONObject universityArray = arr.getJSONObject(i);
                    universitiesList.add(universityArray.getString("universityName"));
                    //imageURl = universityArray.getString("universityImageUrl");
                }
                catch(JSONException json) {
                    Log.d("ERROR", json.getMessage());
                }
            }
        }catch(JSONException json){
            Log.d("ERROR",json.getMessage());
        }
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

        ivImage.setImageBitmap(thumbnail);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm=null;
        if (data != null) {
            try {

                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                if(bm !=null) {
                    Uri selectedImageURI = data.getData();

                    destination = new File(getPath(getApplicationContext(),selectedImageURI));
                    //new File(getRealPathFromURI_API19(getApplicationContext(),selectedImageURI));
                    //new File(getRealPathFromURI(selectedImageURI));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ivImage.setImageBitmap(bm);
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
  //User can select the image from library
    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(GuardSignUpActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result=Utility.checkPermission(GuardSignUpActivity.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask ="Take Photo";
//                    if(result)
                    cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask ="Choose from Library";
//                    if(result)
                    galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
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

                String firstName = fname.getText().toString();
                String lastName = lname.getText().toString();
                String guardUserCode="d23a6ae3";
                String emailID = email.getText().toString();
                String mobilenumber = mobileNumber.getText().toString();

                String subscriptionValue = subscriptionNum.getText().toString();

                String pass = password.getText().toString();
//                String repPass = repass.getText().toString();

                String userType="guard";


                String imageURL = S3_URL+BUCKET_NAME+emailID;
                profileImage = ((BitmapDrawable)ivImage.getDrawable()).getBitmap();



//                jsonObject.put("ConfirmPassword", repPass);
                jsonObject.put("firstName", firstName);
                jsonObject.put("lastName", lastName);
                jsonObject.put("guardUserCode",subscriptionValue);
                jsonObject.put("Email", emailID);
                jsonObject.put("PhoneNumber", mobilenumber);
                //jsonObject.put("imgUrl", imageURL);
                jsonObject.put("Password", pass);
                jsonObject.put("userType", "guard");

                json = jsonObject.toString();
                StringEntity se = new StringEntity(json);
                httpPost.setEntity(se);

                httpPost.setHeader("accept", "json");
                httpPost.setHeader("Content-type", "application/json");

                HttpResponse httpResponse = httpclient.execute(httpPost);

                inputStream = httpResponse.getEntity().getContent();
                result = convertInputStreamToString(inputStream);

                System.out.println("Result" + result  + " <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<,");

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
                Toast.makeText(getBaseContext(), "Registration Successful!", Toast.LENGTH_SHORT).show();
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

    /*
    Code for S3
     */

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
                email.getText().toString(),    /* The key for the uploaded object */
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
                Toast.makeText(GuardSignUpActivity.this,"S3 upload:" + ex.getMessage(),Toast.LENGTH_SHORT).show();
            }

        });
    }

}


