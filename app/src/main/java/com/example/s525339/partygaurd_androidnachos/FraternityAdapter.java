package com.example.s525339.partygaurd_androidnachos;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by S525339 on 9/7/2016.
 * This is the adapter class for the fraternity activity.
 */
public class FraternityAdapter extends ArrayAdapter<Fraternities> {

    ArrayList<Fraternities> fraternitiesArrayList;
    Activity cntx;
    static Context mcontext;
    private static int[] listview_images =
            {R.mipmap.user_icon};

    public FraternityAdapter(Context context, int resource, int textViewID, ArrayList<Fraternities> data) {
        super(context, resource, textViewID, data);
        mcontext = context;
        cntx = (Activity) context;
        fraternitiesArrayList = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        LayoutInflater inflater = cntx.getLayoutInflater();
        View row = null;
        row = inflater.inflate(R.layout.fraternity_list_item_menu, null);
        TextView text1 = (TextView) row.findViewById(R.id.upperTV);
        TextView text2 = (TextView) row.findViewById(R.id.lowerTV);
//        if (fraternitiesArrayList.get(position).getFraternityName()!="No") {


            ImageView fraternityImage = (ImageView) row.findViewById(R.id.fraternityListIV);
            text1.setText(fraternitiesArrayList.get(position).getFraternityName());
            text2.setText(fraternitiesArrayList.get(position).getFraternityDescription());
            fraternityImage.setImageBitmap(getRoundedShape(decodeFile(cntx, fraternitiesArrayList.get(position).getImageId()), 255));
//        }
        return row;
    }
//    @Override
//    public boolean isEnabled(int position){
//        fraternitiesArrayList.get(position).getFraternityName();
//        boolean enabled = false;
//        if (fraternitiesArrayList.get(position).getFraternityName()=="isa"){
//            enabled=false;
//        }else {
//            enabled=true;
//        }
//        return enabled;
//    }

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
}
