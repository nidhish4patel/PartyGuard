package com.example.s525339.partygaurd_androidnachos;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by S525140 on 10/19/2016.
 * This is the adapter class for AlertLog_guarduser
 */
public class AlertLogAdapter_GuardUser extends ArrayAdapter<AlertLogFromPush> {
    ArrayList<AlertLogFromPush> alertsList;
    public AlertLogAdapter_GuardUser(Context context, int resource, int textViewID, ArrayList<AlertLogFromPush> data){
        super(context,resource,textViewID,data);
        alertsList=data;
    }
    public View getView(int position, View convertView, ViewGroup parent){
        View view=super.getView(position, convertView, parent);
        TextView text1=(TextView) view.findViewById(R.id.upperTV);
        text1.setText(alertsList.get(position).getUserNameFromPush());
        TextView text2=(TextView) view.findViewById(R.id.lowerTV);
        text2.setText(alertsList.get(position).getLocationFromPush());
        return view;
    }
}
