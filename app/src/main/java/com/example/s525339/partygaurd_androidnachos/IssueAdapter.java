package com.example.s525339.partygaurd_androidnachos;

        import android.content.Context;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.TextView;

        import java.util.ArrayList;

        /**
 + * Created by s525140 on 9/13/2016.
         This is the adapetr class for the Issues activity
 + */
        public class IssueAdapter extends ArrayAdapter<Issues> {

                ArrayList<Issues> locationsArrayList;

                public IssueAdapter(Context context, int resource, int textViewID, ArrayList<Issues> data){
                super(context, resource, textViewID, data);
                locationsArrayList=data;
            }
        public View getView(int position, View convertView, ViewGroup parent){
                View view=super.getView(position, convertView, parent);
                TextView text1=(TextView) view.findViewById(R.id.locationTV);
                text1.setText(locationsArrayList.get(position).getIssueName());
                return view;
            }
    }