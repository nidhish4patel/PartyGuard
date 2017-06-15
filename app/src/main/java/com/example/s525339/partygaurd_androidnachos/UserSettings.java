package com.example.s525339.partygaurd_androidnachos;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UTFDataFormatException;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * It is the UserSettings class where the user can reset his password.
 */

public class UserSettings extends ProfileDropDownMenu {

    EditText emailId;
    EditText newPassword;
    EditText repeatPassword;

    String email;
    String newPass;
    String repeatPass;
    boolean errorThrown = true;

    Button edit;
    Button save;

    private SharedPreferences loginPrefs;
    private SharedPreferences.Editor loginEditor;
    private final static String USERNAME_KEY = "partyGuardUser";
    private final static String PASSWORD_KEY = "partyGuardPwd";
    String azure_server = "";

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_user_settings);

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
        View contentview = inflater.inflate(R.layout.activity_user_settings, null, false);
        drawer.addView(contentview, 0);

        emailId = (EditText) findViewById(R.id.emailId);
        newPassword = (EditText) findViewById(R.id.newPassword);
        repeatPassword = (EditText) findViewById(R.id.reEnterPassword);
        edit = (Button) findViewById(R.id.profileEditBtn);
        save = (Button) findViewById(R.id.profileSaveBtn);
        emailId.setEnabled(false);
        newPassword.setEnabled(false);
        repeatPassword.setEnabled(false);
        save.setVisibility(View.INVISIBLE);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailId.setEnabled(true);
                newPassword.setEnabled(true);
                repeatPassword.setEnabled(true);
                emailId.setBackground(getResources().getDrawable(R.drawable.edittextstyle));
                newPassword.setBackground(getResources().getDrawable(R.drawable.edittextstyle));
                repeatPassword.setBackground(getResources().getDrawable(R.drawable.edittextstyle));
                save.setVisibility(View.VISIBLE);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                email = emailId.getText().toString();
                newPass = newPassword.getText().toString();
                repeatPass = repeatPassword.getText().toString();
                if (email.length() == 0 || newPass.length() == 0 || repeatPass.length() == 0) {
                    Toast.makeText(getApplicationContext(), "All the fields are required", Toast.LENGTH_SHORT).show();
                } else {
                    if (newPass.length() < 8) {
                        Toast.makeText(getApplicationContext(), "Password must have minimum of 8 characters", Toast.LENGTH_SHORT).show();
                    }
                    if (newPass.length() > 16) {
                        Toast.makeText(getApplicationContext(), "Password must have maximum of 8 characters", Toast.LENGTH_SHORT).show();
                    }
                    if (!newPass.equals(repeatPass)) {
                        Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                    } else {
                        Boolean passwordFlag = isValidPassword(newPass);
                        if (passwordFlag == true) {
                            emailId.setEnabled(false);
                            newPassword.setEnabled(false);
                            repeatPassword.setEnabled(false);
                            new HttpAsyncTask().execute(azure_server + "api/Account/NewPasswordSet");
                        }
                    }
                }
            }

        });

        loginPrefs = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginEditor = loginPrefs.edit();
        String vv = loginPrefs.getString(USERNAME_KEY, "");
        String cc = loginPrefs.getString(PASSWORD_KEY, "");

        Button logOutBTN = (Button) findViewById(R.id.submitBTN);

        logOutBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginEditor.remove(USERNAME_KEY);
                loginEditor.remove(PASSWORD_KEY);
                loginEditor.clear();
                loginEditor.commit();
                finish();
//                loginPrefs.getString(USERNAME_KEY,"");
//                String sharedPassword = loginPrefs.getString(PASSWORD_KEY,"");
                String vv = loginPrefs.getString(USERNAME_KEY, "");
                String cc = loginPrefs.getString(PASSWORD_KEY, "");
                if (loginPrefs.getString(USERNAME_KEY, "") == "" && loginPrefs.getString(PASSWORD_KEY, "") == "") {
                    Intent logOutIntent = new Intent(UserSettings.this, LoginActivity.class);
                    startActivity(logOutIntent);
                }

            }
        });
    }

    public static boolean isValidPassword(final String password) {
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,16})";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();
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
                String emailID = emailId.getText().toString();
                String pass = newPassword.getText().toString();
                String repPass = repeatPassword.getText().toString();

                jsonObject.put("Email", emailID);
                jsonObject.put("NewPassword", pass);
                jsonObject.put("ConfirmPassword", repPass);
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
            if (errorThrown == false) {
                Toast.makeText(getBaseContext(), "Registration Successful!", Toast.LENGTH_SHORT).show();
            } else {
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

//    private class HttpAsyncTask extends AsyncTask<String, String, String> {
//        @Override
//        protected String doInBackground(String... urls) {
//            return POST(urls[0]);
//        }
//
//
//        public String POST(String url) {
//            InputStream inputStream = null;
//            String result = "";
//            try {
//                            HttpClient httpclient = new DefaultHttpClient();
//                            HttpPost httpPost = new HttpPost(url);
//                            String json = "";
//                            JSONObject jsonObject = new JSONObject();
//                            jsonObject.put("Email", email);
//                            jsonObject.put("Password", newPass);
//                            jsonObject.put("ConfirmPassword", repeatPass);
//                            json = jsonObject.toString();
//                            StringEntity se = new StringEntity(json);
//                            httpPost.setEntity(se);
////                            httpPost.setHeader("accept", "json");
////                            httpPost.setHeader("Content-type", "application/json");
//                            HttpResponse httpResponse = httpclient.execute(httpPost);
//
//                            inputStream = httpResponse.getEntity().getContent();
//                            result = convertInputStreamToString(inputStream);
//                            if (result.contains("invalid") == true) {
//                                errorThrown = true;
//                                Log.d("PartyGuard", "result" + result);
//
//                            } else
//                                errorThrown = false;
//                            Log.d("PartyGuard", "inputStream result" + result);
//                            Toast.makeText(getApplicationContext(), "valid password", Toast.LENGTH_SHORT).show();
//
//                        } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            } catch (ClientProtocolException e) {
//                e.printStackTrace();
//            }else {
//                            Toast.makeText(getApplicationContext(), "Password must contain one uppercase, one lowercase, one special charater and one one digit", Toast.LENGTH_SHORT).show();
//
//                        }
//                    }
//                }
//            }
//            return result;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            if (errorThrown == false) {
//                Toast.makeText(getBaseContext(), "Registration Successful!", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(getBaseContext(), result, Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//
//
//    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
//        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//        String line = "";
//        String result = "";
//        while ((line = bufferedReader.readLine()) != null)
//            result += line;
//
//        inputStream.close();
//        return result;
//    }
//
//}
