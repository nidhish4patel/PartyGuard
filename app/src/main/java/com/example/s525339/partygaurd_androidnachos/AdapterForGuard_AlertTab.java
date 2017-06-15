package com.example.s525339.partygaurd_androidnachos;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.StrictMode;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by s525140 on 10/23/2016.
 * This is the adapter class for the Guard's AlertTab activity.
 */
public class AdapterForGuard_AlertTab extends ArrayAdapter<AlertsForGuard_AlertTab> {
    ArrayList<AlertsForGuard_AlertTab> alertFraternitiesArrayList;
    Activity cntx;
    //    static Context mcontext;
//    private static int[] listview_images ={R.mipmap.user_icon};
    public AdapterForGuard_AlertTab(Context context, int resource, int textViewID, ArrayList<AlertsForGuard_AlertTab> data){
        super(context,resource,textViewID,data);
//        mcontext = context;
        cntx = (Activity) context;
        alertFraternitiesArrayList = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = super.getView(position,convertView,parent);
//        TextView textView1 = (TextView)view.findViewById(R.id.alertTV);
        TextView textView2 = (TextView)view.findViewById(R.id.upperAlertTV);
        TextView textview3=(TextView)view.findViewById(R.id.lowerAlertTV);
//        textView1.setText(alertFraternitiesArrayList.get(position).getName());
        ImageView fraternityImage = (ImageView) view.findViewById(R.id.alertImageHostIV);
        textView2.setText(alertFraternitiesArrayList.get(position).getBasicFullName());
        textview3.setText(alertFraternitiesArrayList.get(position).getLocation());
        fraternityImage.setImageBitmap(getRoundedShape(decodeFile(cntx, alertFraternitiesArrayList.get(position).getImageURL()),225));
        return view;
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
