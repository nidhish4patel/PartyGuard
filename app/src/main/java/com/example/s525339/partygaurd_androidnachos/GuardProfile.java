package com.example.s525339.partygaurd_androidnachos;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * This class shows the Guard profile information
 */

public class GuardProfile extends AppCompatActivity {

    EditText fname;
    EditText lname;
    EditText email;
    EditText mobileNumber;
    EditText dateOfBirth;
    Button edit;
    Button save;

    String firstName;
    String lastName;
    String emailId;
    String phone;
    ComplaintSingleton complaintSingleton;

    String userChoosenTask;
    static final int REQUEST_CAMERA = 1;
    static final int SELECT_FILE = 1;
    ImageView profileImageView;
    Button logOutBTN;
    private SharedPreferences loginPrefs;
    private SharedPreferences.Editor loginEditor;
    private final static String USERNAME_KEY = "partyGuardUser";
    private final static String PASSWORD_KEY = "partyGuardPwd";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guard_profile);
        loginPrefs= getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginEditor=loginPrefs.edit();
        logOutBTN= (Button) findViewById(R.id.guardlogOutBtn);

        final ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater().inflate(R.layout.action_bar, null);
        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(actionBarLayout);

                fname = (EditText) findViewById(R.id.guardprofileFirstNameET);
                lname = (EditText) findViewById(R.id.guardprofileLastNameET);
                email = (EditText) findViewById(R.id.guardprofileEmailET);
                mobileNumber = (EditText) findViewById(R.id.guardprofileContactET);
                dateOfBirth = (EditText) findViewById(R.id.guardprofileDOBET);

                edit = (Button)findViewById(R.id.guardprofileEditBtn);
                save = (Button)findViewById(R.id.guardprofileSaveBtn);
                logOutBTN= (Button) findViewById(R.id.guardlogOutBtn);


        Button profileBTN = (Button)findViewById(R.id.guardProfileTabBTN);
        profileBTN.setBackgroundColor(getResources().getColor(R.color.IndianRed));
        profileBTN.setTextColor(getResources().getColor(R.color.colorWhite));

                save.setVisibility(View.INVISIBLE);
                fname.setEnabled(false);
                lname.setEnabled(false);
                email.setEnabled(false);
                mobileNumber.setEnabled(false);
                dateOfBirth.setEnabled(false);

                        edit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                        fname.setEnabled(true);
                                        fname.setCursorVisible(true);
                                        lname.setEnabled(true);
                                        email.setEnabled(true);
                                        mobileNumber.setEnabled(true);
                                        dateOfBirth.setEnabled(true);
                                        fname.setBackground(getResources().getDrawable(R.drawable.edittextstyle));
                                        lname.setBackground(getResources().getDrawable(R.drawable.edittextstyle));
                                        email.setBackground(getResources().getDrawable(R.drawable.edittextstyle));
                                        mobileNumber.setBackground(getResources().getDrawable(R.drawable.edittextstyle));
                                        dateOfBirth.setBackground(getResources().getDrawable(R.drawable.edittextstyle));
                                        edit.setVisibility(View.INVISIBLE);
                                        save.setVisibility(View.VISIBLE);
                                    }
                            });

                        save.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                        fname.setEnabled(false);
                                        lname.setEnabled(false);
                                        email.setEnabled(false);
                                        mobileNumber.setEnabled(false);
                                        dateOfBirth.setEnabled(false);
                                        fname.setBackgroundResource(0);
                                        lname.setBackgroundResource(0);
                                        email.setBackgroundResource(0);
                                        mobileNumber.setBackgroundResource(0);
                                        dateOfBirth.setBackgroundResource(0);
                                        edit.setVisibility(View.VISIBLE);
                                        save.setVisibility(View.INVISIBLE);
                                    }
                           });


        //logOut= (Button) findViewById(R.id.profileSubmitBtn);


        logOutBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginEditor.remove(USERNAME_KEY);
                loginEditor.remove(PASSWORD_KEY);
                loginEditor.clear();
                loginEditor.commit();
                finish();
                //comment
//                loginPrefs.getString(USERNAME_KEY,"");
//                String sharedPassword = loginPrefs.getString(PASSWORD_KEY,"");
                String vv=loginPrefs.getString(USERNAME_KEY,"");
                String cc=loginPrefs.getString(PASSWORD_KEY,"");
                if (loginPrefs.getString(USERNAME_KEY,"")=="" && loginPrefs.getString(PASSWORD_KEY,"")==""){
                    Intent logOutIntent=new Intent(GuardProfile.this,LoginActivity.class);
                    startActivity(logOutIntent);
                }
            }
        });

        Button guardAlertBTN = (Button) findViewById(R.id.guardAlertTabBTN);
        guardAlertBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent guardAlertIntent = new Intent(getApplicationContext(),GuardAlert.class);
                startActivity(guardAlertIntent);
            }
        });

        Button guardHistoryBTN = (Button) findViewById(R.id.guardHistoryTabBTN);
        guardHistoryBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent guardHistoryIntent = new Intent(getApplicationContext(),GuardHistory.class);
                startActivity(guardHistoryIntent);
            }
        });

        complaintSingleton = ComplaintSingleton.getInstance();
        firstName = complaintSingleton.getFirstName_UserInfo();
        lastName = complaintSingleton.getLastName_UserInfo();
        emailId = complaintSingleton.getEmail_UserInfo();
        phone = complaintSingleton.getPhoneNumber_UserInfo();

        fname.setText(firstName);
        lname.setText(lastName);
        email.setText(emailId);
        mobileNumber.setText(phone);
        dateOfBirth.setText("10-08-1990");
    }

    public void guardPicUpload(View v){
        selectImage();
    }
    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(GuardProfile.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result=Utility.checkPermission(GuardProfile.this);


                if (items[item].equals("Take Photo")) {
                    userChoosenTask="Take Photo";
//                    if(result)
                    cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask="Choose from Library";
//                    if(result)
                    galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if(userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }


    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        profileImageView.setImageBitmap(bm);
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        profileImageView.setImageBitmap(thumbnail);
    }

}
