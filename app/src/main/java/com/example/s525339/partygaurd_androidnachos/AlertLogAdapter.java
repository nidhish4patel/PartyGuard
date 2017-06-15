package com.example.s525339.partygaurd_androidnachos;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by S525039 on 10/10/2016.
 * This is the adapter class for AlertLog
 */
public class AlertLogAdapter extends ArrayAdapter {

    ArrayList<AlertLog> logsList;
    public AlertLogAdapter(Context context, int resource, int textViewId, ArrayList<AlertLog> data) {
        super(context, resource,textViewId,data);
        logsList=data;
    }

    public View getView(int position,View convertView,ViewGroup parent){
        View view = super.getView(position,convertView,parent);
        TextView textView1 = (TextView)view.findViewById(R.id.alertTV);
        TextView textView2 = (TextView)view.findViewById(R.id.dateTV);
        TextView textview3=(TextView)view.findViewById(R.id.timeTV);
        textView1.setText(logsList.get(position).getAlerts());
        textView2.setText(logsList.get(position).getDate());
        textview3.setText(logsList.get(position).getTime());
        return view;
    }
}
