package com.example.s525339.partygaurd_androidnachos;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

/**
 * This is the profileDRopMenu class.It is used for the singleton pattern
 */
public class ProfileDropDownMenu extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public DrawerLayout drawer;
    String imageURl;
    String firstName;
    String lastName;
    String name;
    AssetManager assetManager;
    TextView userName;
    ComplaintSingleton complaintSingleton;
    String imageUrl="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile_drop_down_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        assetManager = getAssets();

        complaintSingleton = ComplaintSingleton.getInstance();
        imageUrl = complaintSingleton.getImage_UserInfo();

        firstName = complaintSingleton.getFirstName_UserInfo();
        lastName = complaintSingleton.getLastName_UserInfo();
        name = firstName+lastName;

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_profile_drop_down_menu);
        ImageView profileImage = (ImageView) headerLayout.findViewById(R.id.profile_image);
        userName = (TextView)headerLayout.findViewById(R.id.userName);
        if(imageUrl.equals("null") || imageUrl.length()==0 || imageUrl == null)
        {
            // NoCode
        }
        else
        {
            profileImage.setImageBitmap(getRoundedShape(decodeFile(this, imageUrl), 225));
        }
        userName.setText(name);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile_drop_down_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
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

    //It makes the image round-shaped
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
                (Math.min(((float) targetWidth + 400)/2,((float) targetHeight)) / 2),
                Path.Direction.CCW);
        canvas.clipPath(path);
        Bitmap sourceBitmap = scaleBitmapImage;
        canvas.drawBitmap(sourceBitmap,
                new Rect(0, 0, sourceBitmap.getWidth(),sourceBitmap.getHeight()),
                new Rect(0,0, targetWidth,targetHeight), null);
        return targetBitmap;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if(id == R.id.home){
            Intent intent = new Intent(getBaseContext(), UniversityActivity.class);
            startActivity(intent);
        }

        else if (id == R.id.profile) {
            Intent intent = new Intent(getBaseContext(), UserInformation.class);
            startActivity(intent);
        }

        else if (id == R.id.log) {

        Intent intent = new Intent(getBaseContext(), UserLogActivity.class);
        startActivity(intent);

        }

        else if (id == R.id.location) {

            Intent intent = new Intent(getBaseContext(), ChangeLocationActivity.class);
            startActivity(intent);

        }
        else if (id==R.id.settings){
            Intent intent = new Intent(getBaseContext(), UserSettings.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
