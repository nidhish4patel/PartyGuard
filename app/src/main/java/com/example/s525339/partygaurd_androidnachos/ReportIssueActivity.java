package com.example.s525339.partygaurd_androidnachos;

        import android.app.Activity;
        import android.app.AlarmManager;
        import android.app.PendingIntent;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.content.pm.ApplicationInfo;
        import android.content.pm.PackageManager;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.graphics.Canvas;
        import android.graphics.Path;
        import android.graphics.Rect;
        import android.os.CountDownTimer;
        import android.os.StrictMode;
        import android.support.v7.app.AlertDialog;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.support.v7.widget.Toolbar;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.AdapterView;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ImageView;
        import android.widget.ListView;

        import android.widget.RelativeLayout;

        import android.widget.TextView;
        import android.widget.Toast;

        import java.io.InputStream;
        import java.util.ArrayList;
        import java.util.Arrays;
        import java.util.Calendar;
        import java.util.GregorianCalendar;

        import static com.example.s525339.partygaurd_androidnachos.R.id.view;

/**
 * This is where basic user can report the issue.He can select the existing issues or can provide othertype of issue.
 */

public class ReportIssueActivity extends ProfileDropDownMenu {

        Issues[] issuesArray={new Issues("Accident"),new Issues("Feeling Unsafe"), new Issues("Fight"), new Issues("Other")};
        ArrayList<Issues> issuesList;
        ImageView locationImageView;
        ArrayList<Fraternities> fraternitiesArrayList;
        Activity cntx;
        static Context mcontext;
        TextView fraternityNameTV;
        TextView fraternityDescTV;
        String universityName;
        String fraternityNAMEINTENT;
        String fraternityFULLNAMEINTENT;
        String issueName;
        Button emergencyBTN;
        private CountDownTimer countDownTimer;
        long millisecFinished;
        View emergencyDialogBox;
        String azure_server = "";

       // private static int[] listview_images =   {R.mipmap.user_icon_location};
        ComplaintSingleton complaintSingleton;
        EditText issueTypeFromDialogET;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                //setContentView(R.layout.activity_report_issue);

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
            View contentview = inflater.inflate(R.layout.activity_report_issue, null, false);
            drawer.addView(contentview, 0);

                    complaintSingleton = ComplaintSingleton.getInstance();
                    fraternityNameTV = (TextView) findViewById(R.id.fraternityNameTV);
                    fraternityDescTV = (TextView) findViewById(R.id.fraternityFullNameTV);
                    emergencyBTN= (Button) findViewById(R.id.emergencyBTN);

                    //fraternityNAMEINTENT=complaintSingleton.getFraternityName();
                    //fraternityFULLNAMEINTENT=complaintSingleton.getFraternityDesc();
                    fraternityNameTV.setText(complaintSingleton.getFraternityName());
                    fraternityDescTV.setText(complaintSingleton.getFraternityDesc());




//                final ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater().inflate(R.layout.action_bar, null);
//                final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
//                actionBar.setDisplayShowHomeEnabled(false);
//                actionBar.setDisplayShowTitleEnabled(false);
//                actionBar.setDisplayShowCustomEnabled(true);
//                actionBar.setCustomView(actionBarLayout);
                emergencyBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        start();

                    }
                });

                ListView issueListView = (ListView) findViewById(R.id.issueListView);
                issuesList=new ArrayList<>(Arrays.asList(issuesArray));
                IssueAdapter issueAdapter= new IssueAdapter(this, R.layout.location_list_item,R.id.locationTV,issuesList);
                issueListView.setAdapter(issueAdapter);
                Intent fraternityIntent = getIntent();
                String imageURL = complaintSingleton.getFraternityImageURL();

            emergencyDialogBox= LayoutInflater.from(ReportIssueActivity.this).inflate(R.layout.activity_emergency,null);
            issueTypeFromDialogET=(EditText) emergencyDialogBox.findViewById(R.id.dialogBoxCommentET);
//                                        complaintSingleton.setIssueTypeDialog(issueTypeFromDialogET.getText().toString());
            Button acceptBTN=(Button) emergencyDialogBox.findViewById(R.id.acceptBTN);


            acceptBTN.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    Intent profileIntent = new Intent(getApplicationContext(),UniversityActivity.class);

                    startActivity(profileIntent);
                }
            });

                    issueListView.setOnItemClickListener(
                            new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    if (position==0){
                                        issueName=issuesArray[position].getIssueName();
                                    }else if (position==1){
                                        issueName=issuesArray[position].getIssueName();
                                    }else if (position==2){
                                        issueName=issuesArray[position].getIssueName();
                                    }else if (position==3){
                                        issueName=issuesArray[position].getIssueName();
                                    }
                                    complaintSingleton=ComplaintSingleton.getInstance();
                                    complaintSingleton.setIssueType(issueName);
                                    if (issueName=="Other"){

                                        final View customDialogView= LayoutInflater.from(ReportIssueActivity.this).inflate(R.layout.activity_custom_dialog__report_issue__other_type,null);
                                        issueTypeFromDialogET=(EditText) customDialogView.findViewById(R.id.dialogBoxCommentET);
//                                        complaintSingleton.setIssueTypeDialog(issueTypeFromDialogET.getText().toString());
                                        Button negativeBTN=(Button) customDialogView.findViewById(R.id.cancleBTN);
                                        Button positiveBTN=(Button) customDialogView.findViewById(R.id.acceptBTN);

                                        positiveBTN.setOnClickListener(new View.OnClickListener() {

                                            @Override
                                            public void onClick(View v) {

                                                Intent profileIntent = new Intent(getApplicationContext(),LocationActivity.class);

                                                startActivity(profileIntent);
                                            }
                                        });
                                        negativeBTN.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent cancleIntent=new Intent(getApplicationContext(),ReportIssueActivity.class);
                                                startActivity(cancleIntent);
                                            }
                                        });
//                                        Intent dialog=new Intent(ReportIssueActivity.this,CustomDialog_ReportIssue_OtherType.class);
//                                        startActivity(dialog);
                                        AlertDialog.Builder builder=new AlertDialog.Builder(ReportIssueActivity.this);
                                        builder.setView(customDialogView);
                                        AlertDialog alert=builder.create();
                                        alert.show();
                                        alert.getWindow().setBackgroundDrawableResource(R.color.colordarkgrey);
                                        alert.setOnShowListener(new DialogInterface.OnShowListener() {

                                            @Override
                                            public void onShow(DialogInterface dialog) {

                                            }
                                        });

                                    }
                                    else {

                                        Intent fraternityIntent= new Intent(ReportIssueActivity.this,LocationActivity.class);
//
                                        startActivity(fraternityIntent);
                                    }

                                    //Toast.makeText(getApplicationContext(),String.format("%d",position),Toast.LENGTH_LONG).show();
                                }


                            }
                    );

                ImageView fraternityImage = (ImageView) findViewById(R.id.fraternityImageView);
                fraternityImage.setImageBitmap(getRoundedShape(decodeFile(this, imageURL),225));

                    RelativeLayout fraternityLayout = (RelativeLayout) findViewById(R.id.fraternityLayout);
                    fraternityLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent fraternityIntent = new Intent(ReportIssueActivity.this,FraternitiesActivity.class);
                            complaintSingleton = ComplaintSingleton.getInstance();
                            String universityName = complaintSingleton.getUniverSityName();
                            fraternityIntent.putExtra("universityName",complaintSingleton.getUniverSityName());
                        }
                    });


                    Intent issueIntent=getIntent();
                    complaintSingleton = ComplaintSingleton.getInstance();
                    issueName=issueIntent.getStringExtra("IssueName");
                    universityName=issueIntent.getStringExtra("universityName");
                    fraternityNAMEINTENT=issueIntent.getStringExtra("fraternityName");
                    fraternityFULLNAMEINTENT=issueIntent.getStringExtra("fraternityFullName");


                }


    public void start(){

        countDownTimer=new CountDownTimer(10*1000+1,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                millisecFinished=millisUntilFinished/1000;
//                fraternityNameTV.setText(""+millisUntilFinished/1000);

                if (millisUntilFinished/1000==1){

                    AlertDialog.Builder builder=new AlertDialog.Builder(ReportIssueActivity.this);
                    builder.setView(emergencyDialogBox);
                    AlertDialog alert=builder.create();
                    alert.show();
                    emergencyBTN.setEnabled(false);
//                    Intent intent=new Intent(ReportIssueActivity.this,EmergencyActivity.class);
//                    startActivity(intent);
                }
            }

            @Override
            public void onFinish() {
                emergencyBTN.setEnabled(true);
//                fraternityNameTV.setText("Done");
            }
        };
        countDownTimer.start();
    }
    private void cancel(){
        if (countDownTimer!=null){
            countDownTimer.cancel();
            countDownTimer=null;
        }
    }
//        public void emergencyButton(View v){
////            Intent emergencyIntent=new Intent(ReportIssueActivity.this,EmergencyActivity.class);
////            startActivity(emergencyIntent);
//            setupAlarm(30);
//        }
    private void setupAlarm(int seconds) {

        Long time = new GregorianCalendar().getTimeInMillis()+ (seconds);
        Intent intentAlarm = new Intent(this, EmergencyActivity.class);
        Log.d("location", "Setup the alarm");
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP,time, PendingIntent.getBroadcast(this,1,  intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));

        // Finish the currently running activity
        //ValueActivity.this.finish();
        Toast.makeText(this,"Intent !!",Toast.LENGTH_SHORT).show();
    }

          public void intentForFraternity(View v)
          {
                Intent fraternityIntent=new Intent(ReportIssueActivity.this,FraternitiesActivity.class);
                complaintSingleton = ComplaintSingleton.getInstance();
                fraternityIntent.putExtra("universityName",complaintSingleton.getUniverSityName());
                startActivity(fraternityIntent);
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
