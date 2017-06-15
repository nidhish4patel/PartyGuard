package com.example.s525339.partygaurd_androidnachos;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.JsonParser;

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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * This is the Log-in screen for the basic ,guard and host user.
 */

public class LoginActivity extends AppCompatActivity{

    private Button loginBtn;
    private EditText userNameET, passwordET;
    private CheckBox check;
    private SharedPreferences loginPrefs;
    private SharedPreferences.Editor loginEditor;
    String userName,password;
    private final static String USERNAME_KEY = "partyGuardUser";
    private final static String PASSWORD_KEY = "partyGuardPwd";
    private final static String SAVED_KEY = "saved";
    SavedSharedPreference autologin;
    private Intent loginIntent;
    String email = "";
    String passWord="";
    private boolean isSaved = false;
    JSONObject usersArray;
    String userNameForValidation,passwordForValidation;
    ComplaintSingleton complaintSingleton;

    private boolean validLogin = false;

    private GoogleApiClient client;

//    userInfo variable
    String entity;
    //    userInfo variable

    InputStream inputStream;
    JSONObject obj;
    String dataOfJson;
    AssetManager assetsManager;
    ArrayList<UserData_Login> userDataList=new ArrayList<>();
    ArrayList<Credentials_Validations> credentialsList=new ArrayList<>();
    String pswd;
    String uname;
    private boolean autoLogin = false;
    String loginSrvcResposnse = "";
    String azure_server = "";
    //Fetch Azure server URL

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        try {
            ApplicationInfo ai = getPackageManager().getApplicationInfo(this.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            azure_server = bundle.getString("azure_server");
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("AZURE_SERVICE", "Failed to load meta-data, NameNotFound: " + e.getMessage());
        } catch (NullPointerException e) {
            Log.e("AZURE_SERVICE", "Failed to load meta-data, NullPointer: " + e.getMessage());
        }

        autoLogin = false;
        assetsManager = getAssets();
        userNameET = (EditText) findViewById(R.id.emailET);
        passwordET = (EditText) findViewById(R.id.passwordET);
        userNameET.setText("");
        passwordET.setText("");
        complaintSingleton = ComplaintSingleton.getInstance();

        Button loginBTN = (Button) findViewById(R.id.loginBtn);
        Button createAccBTN = (Button) findViewById(R.id.creatAccountBtn);
        init();
        String sharedUserName = loginPrefs.getString(USERNAME_KEY, "");
        String sharedPassword = loginPrefs.getString(PASSWORD_KEY, "");

        pswd = passwordET.getText().toString();
        uname = userNameET.getText().toString();
            loginBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    userNameForValidation = userNameET.getText().toString();
                    passwordForValidation = passwordET.getText().toString();

                    try {
                        loginSrvcResposnse = new HttpAsyncTask().execute(azure_server + "token").get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    if(loginSrvcResposnse.length()>0) {
                        String credentialsJSON = loginSrvcResposnse;//getCredentialsValidationJSON();
                        parseCredentialsJSON(credentialsJSON, userNameForValidation, passwordForValidation);
                        if(validLogin) {
                            try {
                                String userResponse = new HttpAsyncTaskForUserInfo().execute(azure_server+ "Api/Account/UserInfo").get();
                                validLogin = true;
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Login Failed!",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
            if (sharedUserName.length() > 0 && sharedPassword.length() > 0) {
                fillData();
                pswd = passwordET.getText().toString();
                uname = userNameET.getText().toString();
                if (uname.length() > 0 && pswd.length() > 0) {
                    if (loginPrefs.getString(USERNAME_KEY, "").length() > 0 && loginPrefs.getString(PASSWORD_KEY, "").length() > 0) {
                        autoLogin = true;
                        loginBTN.performClick();

                    } else {
                        userNameET.setText("");
                        passwordET.setText("");
                    }
                }
                client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
            }
        createAccBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new HttpAsyncTask().execute(azure_server + "token");


                Intent createAccIntent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(createAccIntent);
            }
        });
    }

    private String getCredentialsValidationJSON(){
        InputStream input;
        String text = "";
        String txt="Credentials-Validation_Login.txt";
        try {
            input = assetsManager.open(txt);

            int size = input.available();
            byte[] buffer = new byte[size];
            input.read(buffer);
            input.close();
            text = new String(buffer);
            return text;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return text;

    }
    private void parseCredentialsJSON(String jsonToParse, String uName, String pWord ) {

        try {

            JSONObject loginObject = new JSONObject(jsonToParse);
            if (loginObject.getString("userName").equals(uName)) {

                if (loginObject.getString("access_token").length() > 0) {
                    saveData(uName, pWord);
                    validLogin = true;
                    complaintSingleton.setAccess_token(loginObject.getString("access_token").toString());
                }
                else
                {
                    Toast.makeText(this,"Login Failed!",Toast.LENGTH_SHORT).show();
                }
        }
        }catch(JSONException jsone){
            Log.d("ERROR",jsone.getMessage());
        }
    }
    private void fillData(){
        userName = loginPrefs.getString(USERNAME_KEY,"");
        password = loginPrefs.getString(PASSWORD_KEY,"");
        passwordET.setText(password);
        userNameET.setText(userName);
    }

    private void saveData(String userName, String password){
        loginEditor.putString(USERNAME_KEY, userName);
        loginEditor.putString(PASSWORD_KEY, password);
        loginEditor.putBoolean(SAVED_KEY, isSaved);
        loginEditor.commit();
    }

    private void init() {
        loginBtn = (Button) findViewById(R.id.loginBtn);
        userNameET = (EditText) findViewById(R.id.emailET);
        passwordET = (EditText) findViewById(R.id.passwordET);
        loginPrefs = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginEditor = loginPrefs.edit();

    }

    @Override
    protected void onStop(){
        autologin.setUserName(getApplicationContext(),email);
        autologin.setPassword(getApplicationContext(),passWord);
        super.onStop();
    }
    protected void onDestroy(){
        autologin.setUserName(getApplicationContext(),email);
        autologin.setPassword(getApplicationContext(),passWord);
        super.onDestroy();
    }

    public void NavigateHostSignUp(View v) {
        Intent hostSignUpIntent = new Intent(LoginActivity.this, GuardSignUpActivity.class);
        startActivity(hostSignUpIntent);
    }

    public void ForgotPassword(View v) {
        Intent forgotIntent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
        startActivity(forgotIntent);
    }

    private class HttpAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... urls) {
            return POST(urls[0]);
        }

        public String POST(String url) {
            inputStream = null;
            String result = "";
            String email = userNameET.getText().toString();
            String password = passwordET.getText().toString();
            String grant_type = "password";
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
                String json = "";
                json = "username=" + email + "&password=" + password + "&grant_type=password";
                StringEntity se = new StringEntity(json);
                httpPost.setEntity(se);
                httpPost.setHeader("accept", "text");
                httpPost.setHeader("Content-type", "text/plain");
                HttpResponse httpResponse = httpclient.execute(httpPost);
                inputStream = httpResponse.getEntity().getContent();
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
            parseJSON(result);
            loginSrvcResposnse = result;
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

                String json = "";
                json = "univeristyName=" + "Northwest Missouri";
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
            parseJSON(result);
            System.out.println(result);
            redirectUser(complaintSingleton.getUserType_UserInfo().toString());
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

    private String getLoginJSON() {
        InputStream input;
        String text = "";
        String txt="Login_JSON.txt";
        try {
            input = assetsManager.open(txt);

            int size = input.available();
            byte[] buffer = new byte[size];
            input.read(buffer);
            input.close();
            text = new String(buffer);
            return text;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return text;
    }
    //It Parses the json file
    private void parseJSON(String jsonToParse) {
        System.out.print("++++++++++++++"+"++++++++++++");
        try {
            JSONArray arr = new JSONArray(jsonToParse);
            for(int i=0; i < arr.length(); i++){
                try {
                    JSONObject universityArray = arr.getJSONObject(i);
                    String at=universityArray.getString("access_token");
                    userDataList.add(new UserData_Login(universityArray.getString("expires_in"),universityArray.getString("access_token"),universityArray.getString(".expires"),universityArray.getString(".issued"),universityArray.getString("token_type"),universityArray.getString("userName")));




//                        if(universityArray.getString("universityName").equals("Northwest Missori State University"))
//                        {
//                            JSONArray fraternityArray = universityArray.getJSONArray("Fraternities");
//                            for(int j=0; j < fraternityArray.length(); j++) {
//                                JSONObject fraternity = fraternityArray.getJSONObject(i);
//                                String fraternityName = fraternity.getString("fraternityName");
//                                String fraternityDescription = fraternity.getString("fraternityDescription");
////                                userDataList.add(new UserData_Login(accessToken,tokenType,expireIn,username,issued,expires));
//                            }
//                        }

                }
                catch(JSONException jsone)
                {
                    Log.d("ERROR", jsone.getMessage());
                }
            }
        }catch(JSONException jsone){
            Log.d("ERROR",jsone.getMessage());
        }
    }

    private void parseUserInfoJSON(String jsonToParse) {
        try {
            JSONObject loginObject = new JSONObject(jsonToParse);
                try {
                    complaintSingleton.setEmail_UserInfo(loginObject.getString("Email"));
                    complaintSingleton.setFirstName_UserInfo(loginObject.getString("FirstName"));
                    complaintSingleton.setLastName_UserInfo(loginObject.getString("LastName"));
                    complaintSingleton.setUserType_UserInfo(loginObject.getString("UserType"));
                    if(loginObject.getString("UserType").equals("basic")) {
                        complaintSingleton.setUniversity_UserInfo(loginObject.getString("University"));
                    }
                    if (loginObject.getString("UserType").equals("guard")){
                        complaintSingleton.setFraternityName_UserInfo(loginObject.getString("Fraternity"));
                        System.out.println(loginObject.getString("Fraternity"));
                    }
                    complaintSingleton.setPhoneNumber_UserInfo(loginObject.getString("PhoneNumber"));
                    complaintSingleton.setStatus_UserInfo(loginObject.getString("Status"));
                    complaintSingleton.setAge_UserInfo(loginObject.getString("Age"));
                    complaintSingleton.setImage_UserInfo(loginObject.getString("Image"));
                }
                catch(JSONException jsone)
                {
                    Log.d("ERROR", jsone.getMessage());
                }
        }catch(JSONException jsone){
            Log.d("ERROR",jsone.getMessage());
        }
    }

    private void redirectUser(String userType) {
        boolean loginSuccess = false;
        if (userType.equals("basic")) {
            if (autoLogin == true) {
                loginIntent = new Intent(LoginActivity.this, UniversityActivity.class);
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                loginSuccess = true;

                this.finish();
            } else {
                loginIntent = new Intent(LoginActivity.this, HowToUseActivity.class);
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                loginSuccess = true;
                this.finish();
            }
            startActivity(loginIntent);
        }
        else if (userType.equals("guard")) {
            loginIntent = new Intent(LoginActivity.this, GuardAlert.class);

            loginIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            loginSuccess = true;
            startActivity(loginIntent);

            this.finish();


        }
        else  if (userType.equals("host")){
            loginIntent = new Intent(LoginActivity.this, PgteamActivity.class);
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            loginSuccess = true;
            startActivity(loginIntent);


            this.finish();

        }
        if(loginSuccess == false)
        {
            Toast.makeText(this,"Login Failed!",Toast.LENGTH_SHORT).show();
        }
        if(loginSuccess == true)
        {
            Toast.makeText(this,"Login Successful!",Toast.LENGTH_SHORT).show();
        }
    }
}


