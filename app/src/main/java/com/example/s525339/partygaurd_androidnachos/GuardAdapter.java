package com.example.s525339.partygaurd_androidnachos;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
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
 * This is the adapter class for the PGteam activity.
 * If the Host clicks on particular guard,that guard's info. would be populated.
 */
public class GuardAdapter<T> extends ArrayAdapter<Guard> {

    ArrayList<Guard> guardsArrayList;
    Activity cntx;
    static Context mcontext;
  //  ToggleButton guardAvailable;
    boolean postGuardAvailable = false;
    String postGuardName = "";
    private static int[] listview_images = {R.mipmap.user_icon};;
    Boolean errorThrown = true;
    int guardID;
    ComplaintSingleton complaintSingleton;
    String toggleResponse = "";
    ArrayList<Guard> guardList;

    public GuardAdapter(Context context, int resource, int textViewID,ArrayList<Guard> data) {
        super(context, resource, textViewID,data);
        mcontext = context;
        cntx = (Activity) context;
        guardsArrayList = data;
    }

    public class Holder
    {
        TextView tv;
        ImageView img;
        Button profileBtn;
        ToggleButton guardAvailable;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        LayoutInflater inflater = cntx.getLayoutInflater();
        complaintSingleton=complaintSingleton.getInstance();
        final Holder holder=new Holder();
        View row = null;
        row = inflater.inflate(R.layout.pgteam_griditem, null);
        holder.tv=(TextView) row.findViewById(R.id.textView1);
        holder.img=(ImageView) row.findViewById(R.id.imageView1);
        ImageView guardImage = (ImageView)  row.findViewById(R.id.imageView1);
        holder.profileBtn= (Button) row.findViewById(R.id.profileBTN);
//        getting position
        holder.img.setTag(new Integer(position));
        if (guardsArrayList.get(position).getImageURL().length()>0){
            holder.img.setImageBitmap(getRoundedShape(decodeFile(getContext(),guardsArrayList.get(position).getImageURL() ), 1500));
        }else {
            holder.img.setImageBitmap(getRoundedShape(decodeFile(getContext(),"https://s3-us-west-2.amazonaws.com/gdp2/Heath-1.jpg" ), 1500));
        }




       postGuardName = guardsArrayList.get(position).getEmail();
       holder.guardAvailable = (ToggleButton) row.findViewById(R.id.pgTeamToggle);
        holder.guardAvailable.setChecked(guardsArrayList.get(position).isAvailable());
       holder.guardAvailable.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                boolean result = guardsArrayList.get(position).isAvailable();
                if(guardsArrayList.get(position).isAvailable() == true)
                {
                    guardID=guardsArrayList.get(position).getGuardID();
                    holder.guardAvailable.setChecked(false);
                    guardsArrayList.get(position).setAvailable(false);
                    postGuardAvailable = false;
                }
                else
                {
                   holder.guardAvailable.setChecked(true);
                    guardID=guardsArrayList.get(position).getGuardID();
                    guardsArrayList.get(position).setAvailable(true);
                    postGuardAvailable = true;
                }
                //new HttpAsyncTask().execute("http://partyguardservices20160912122440.azurewebsites.net/api/Account/Register");
                try {
                    toggleResponse= new HttpAsyncTask().execute("http://partyguardservices20161110094537.azurewebsites.net/api/HostUserProfile/GuardToggle").get();
                    String tempResponse = toggleResponse;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                //Toast.makeText(getContext(),"Http Post: " + String.valueOf(guardsArrayList.get(position).isAvailable()),Toast.LENGTH_SHORT).show();
            }
        });


        holder.tv.setText(guardsArrayList.get(position).getGuardName());
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        holder.guardAvailable.performClick();
            }
        });


        holder.profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              
                Intent guardIntent = new Intent(getContext(), GuardProfileInfo.class);
                guardIntent.putExtra("email", guardsArrayList.get(position).getEmail());
                guardIntent.putExtra("mobile", guardsArrayList.get(position).getMobile());
//                guardIntent.putExtra("url",guardsArrayList.get(position).getImageURL());
                guardIntent.putExtra("url","https://s3-us-west-2.amazonaws.com/gdp2/Heath-1.jpg");
                getContext().startActivity(guardIntent);
            }
        });


        return row;
    }

    private void parseJSON(String jsonToParse) {
        try {
            JSONArray arr = new JSONArray(jsonToParse);
            for (int i = 0; i < arr.length(); i++) {
                try {
                    JSONObject guard = arr.getJSONObject(i);
//                    JSONObject guardInfo=guard.getJSONObject("ApplicationUser");
                    boolean isActive=guard.getBoolean("isActive");

                    //String fraternityDescription = guard.getString("fraternityDescription");
//                    guardList.add(new Guard(isActive));
                } catch (JSONException jsone) {
                    Log.d("ERROR", jsone.getMessage());
                }
            }
        } catch (JSONException jsone) {
            Log.d("ERROR", jsone.getMessage());
        }
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
    //It makes the image round-shaped.
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



    private class HttpAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... urls) {
            return POST(urls[0]);
        }



        public String POST(String url) {
            InputStream inputStream = null;
            String access_token = complaintSingleton.getAccess_token();
            String result = "";
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
//                httpPost.setHeader("accept", "json");
                httpPost.setHeader("Content-type", "application/json");
                httpPost.setHeader("Authorization","Bearer "+ access_token);
                String json = "";

//                JSONObject jsonObject = new JSONObject();

                 JSONObject jsonObject = new JSONObject();


//                String guardName = postGuardName;
//                boolean guardAvailable = postGuardAvailable;
                JSONObject jsonObjectForPost = new JSONObject();
                JSONArray array = new JSONArray();
                JSONObject item = new JSONObject();

//                item.put("id", guardID);
                item.put("id", 1);

                array.put(item);

                jsonObjectForPost.put("guards", array);

                if(postGuardAvailable == true) {
                    jsonObjectForPost.put("type", "active");
                }
                else
                {
                    jsonObjectForPost.put("type", "inactive");
                }


                if (postGuardAvailable==true){
                    jsonObjectForPost.put("type","active");
                }
                else {
                    jsonObjectForPost.put("type","inactive");
                }



                json = jsonObjectForPost.toString();
                StringEntity se = new StringEntity(json);
                httpPost.setEntity(se);


//                httpPost.setHeader("accept", "json");
//                httpPost.setHeader("Content-type", "application/json");

                httpPost.setHeader("accept", "json");
                httpPost.setHeader("Content-type", "application/json");


                HttpResponse httpResponse = httpclient.execute(httpPost);

                inputStream = httpResponse.getEntity().getContent();
                result = convertInputStreamToString(inputStream);
                parseJSON(result);

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
                Toast.makeText(getContext(), "Guard Availability Changed", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), result, Toast.LENGTH_SHORT).show();
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
}

