package com.example.s525339.partygaurd_androidnachos;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

/**
 * It is the class where user can reset his password.The reset password would be sent to his email-id.
 */

public class ResetPasswordActivity extends AppCompatActivity {
    private GoogleApiClient client;
    String fileName = "sample";
    public String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "C:\\Users\\s525139\\Documents\\Oct9\\PartyGaurd_AndroidNachos\\app\\src\\main\\assets\\savedFile.txt";
    EditText emailTV;
    Button resetbt;
    private boolean errorThrown = true;
    String emailResponse = "";
    String azure_server = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        try {
            ApplicationInfo ai = getPackageManager().getApplicationInfo(this.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            azure_server = bundle.getString("azure_server");
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("AZURE_SERVICE", "Failed to load meta-data, NameNotFound: " + e.getMessage());
        } catch (NullPointerException e) {
            Log.e("AZURE_SERVICE", "Failed to load meta-data, NullPointer: " + e.getMessage());
        }

        emailTV = (EditText) findViewById(R.id.mailTV);
        resetbt = (Button) findViewById(R.id.resetBtn);
        final ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater().inflate(R.layout.guard_profile_action_bar, null);

        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(actionBarLayout);


//        final EditText email = (EditText) this.findViewById(R.id.mail);
//        final Button resetbt = (Button) this.findViewById(R.id.resetBtn);
        final File file = new File(Environment.getExternalStorageDirectory(), fileName);


        //        resetbt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(getApplicationContext(),"Instructions were sent to your email to reset password",Toast.LENGTH_LONG).show();
//                email.setFocusable(false);
//                email.setFocusableInTouchMode(false);
//                email.setClickable(false);
//            }
//        });

        resetbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Toast.makeText(getApplicationContext(), "A link has been sent to your email to reset password", Toast.LENGTH_SHORT).show();
                try {
                    emailResponse = new HttpAsyncTask().execute(azure_server + "api/ForgotPassword/ForgotPassword").get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                if (emailResponse.length()==0)
                {
                    Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "Reset link is sent to your registered email ID",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Email ID not registered",Toast.LENGTH_SHORT).show();
                }

                try {

                    FileOutputStream fo = new FileOutputStream(file);
                    fo.write(emailTV.getText().toString().getBytes());
                    fo.close();


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

//        loadbt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                try {
//                    int length=(int) file.length();
//                    byte[] bytes= new byte[length];
//
//
//                    FileInputStream fi= new FileInputStream(file);
//                    fi.read(bytes);
//
//                    String text=new String(bytes);
//                    textTv.setText(text);
//
//
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//
//        loadbt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                try {
//                    int length=(int) file.length();
//                    byte[] bytes= new byte[length];
//
//
//                    FileInputStream fi= new FileInputStream(file);
//                    fi.read(bytes);
//
//                    String text=new String(bytes);
//                    textTv.setText(text);
//
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });


//        File dir = new File(path);
//        dir.mkdirs();


    }


//    public void buttonSave (View view) {
//        File file=new File(path+"C:\\Users\\s525139\\Documents\\Oct9\\PartyGaurd_AndroidNachos\\app\\src\\main\\assets\\savedFile.txt");
//        String [] saveText= String.valueOf(email.getText()).split(System.getProperty("line.separator"));
//        email.setText("");
//        Save(file,saveText);
//
//    }
//
//
//


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


            String email= emailTV.getText().toString();
            jsonObject.put("Email", email);
//                jsonObject.put("Password", "Hello_456");
//                jsonObject.put("ConfirmPassword", "Hello_456");

            json = jsonObject.toString();
            StringEntity se = new StringEntity(json);
            httpPost.setEntity(se);

            httpPost.setHeader("accept", "json");
            httpPost.setHeader("Content-type", "application/json");

            HttpResponse httpResponse = httpclient.execute(httpPost);

            inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if (inputStream != null) {
                result = convertInputStreamToString(inputStream);
                Log.d("register", "inputStream result" + result);
            } else
                result = "Did not work!";
            Log.d("register", "result" + result);

        } catch (Exception e) {
            Log.d("json", "e.getLocalizedMessage()" + e.getLocalizedMessage());
        }

        //  return result
        return result;

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



