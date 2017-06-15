package com.example.s525339.partygaurd_androidnachos;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.DatagramChannel;
import java.util.ArrayList;

/**
 * It is where the Guard user can see and edit his profile information
 */

public class GuardProfileInfo extends AppCompatActivity {

    ArrayList<Guard> guardList;
    AssetManager assetManager;
    String email;
    String imageUrl;
    int phone;
    ImageView guardImage;
    String azure_server = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guard_profile_info);
        try {
            ApplicationInfo ai = getPackageManager().getApplicationInfo(this.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            azure_server = bundle.getString("azure_server");
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("AZURE_SERVICE", "Failed to load meta-data, NameNotFound: " + e.getMessage());
        } catch (NullPointerException e) {
            Log.e("AZURE_SERVICE", "Failed to load meta-data, NullPointer: " + e.getMessage());
        }
        final ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater().inflate(R.layout.guard_profile_action_bar, null);
        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(actionBarLayout);

        TextView et1 = (TextView) findViewById(R.id.userEmailID) ;
        TextView et2=(TextView) findViewById(R.id.userMobileNumber);
        guardImage = (ImageView)findViewById(R.id.guardUserImage);

        System.out.println("++++++++++++++++");
        Intent in=getIntent();
        in.getStringExtra("mobile");
        in.getStringExtra("email");
        imageUrl = in.getStringExtra("url");
        System.out.println("++++++++++++++++"+in.getStringExtra("mobile")+"}}}}}}}}}}}}}}}}}"+in.getStringExtra("email"));
        et1.setText(in.getStringExtra("email"));
        et2.setText(""+in.getStringExtra("mobile"));

        guardImage.setImageBitmap(getRoundedShape(decodeFile(this, imageUrl), 1500));
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
}
