package com.example.s525339.partygaurd_androidnachos;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * It is where a new Host user can sign-up
 */
public class HostSignUp extends AppCompatActivity {

    String userChoosenTask;
    static final int REQUEST_CAMERA = 1;
    static final int SELECT_FILE = 1;
    EditText fname;
    EditText lname;
    EditText email;
    EditText mobileNumber;
    EditText password;
    EditText repass;
    EditText dateOfBirth;
    EditText subscriptionId;

    Button registerBTN;

    String firstName;
    String lastName;
    String mobilenumber;
    String dateOFBirth;
    String emailAddress;
    String userPassword;
    String repeatPassword;
    String subscriptionCode;
    DatePickerDialog selectDatePickerDialog;
    SimpleDateFormat dateFormatter;

    ImageView profileImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_sign_up);

        fname = (EditText) findViewById(R.id.hostProfileFirstNameET);
        lname = (EditText) findViewById(R.id.hostProfileLastNameET);
        email = (EditText) findViewById(R.id.hostEmailET);
        password = (EditText) findViewById(R.id.hostPasswordET);
        repass = (EditText) findViewById(R.id.hostRepeatPasswordET);
        mobileNumber = (EditText) findViewById(R.id.hostPhoneNumberET);
        dateOfBirth = (EditText) findViewById(R.id.hostDateOfBirthET);
        subscriptionId = (EditText) findViewById(R.id.hostSubscriptionId);
        registerBTN = (Button) findViewById(R.id.hostCreateAccountBtn);

        fname.setError("First name is required!");
        lname.setError("Last name is required!");
        dateOfBirth.setFocusable(true);
        password.setError("password is required");
        repass.setError("re-enter password is required");
        mobileNumber.setError("Mobile number is required");
        subscriptionId.setError("zipcode is required");

        dateFormatter = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
        dateFormatter.setLenient(false);
        Calendar newCalendar = Calendar.getInstance();
        selectDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                dateOfBirth.setText(dateFormatter.format(newDate.getTime()));
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        dateOfBirth.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    selectDatePickerDialog.show();
                    dateOfBirth.setInputType(0);

                } else {
                    selectDatePickerDialog.hide();
                    dateOfBirth.setInputType(0);

                }
            }
        });

        emailAddress = email.getText().toString();
        if (emailAddress.length() == 0) {
            email.setError("Email required!");
            if (isValidEmail(emailAddress)) {
                Toast.makeText(getApplicationContext(), "valid email address", Toast.LENGTH_SHORT).show();
            }
//            else{
//                showAlertValidation();
//            }
        }

        registerBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //new HttpAsyncTask().execute("http://partyguardservices20160912122440.azurewebsites.net/api/Account/Register");
                firstName = fname.getText().toString();
                lastName = lname.getText().toString();
                emailAddress = email.getText().toString();
                userPassword = password.getText().toString();
                repeatPassword = repass.getText().toString();
                mobilenumber = mobileNumber.getText().toString();
                dateOFBirth = dateOfBirth.getText().toString();
                subscriptionCode = subscriptionId.getText().toString();
                //System.out.println(userPassword);
                //System.out.println(repeatPassword);

                if (firstName.length() == 0 || lastName.length() == 0 || mobilenumber.length() == 0 || dateOFBirth.length() == 0 || subscriptionCode.length() == 0 || emailAddress.length() == 0 || userPassword.length() == 0 || repeatPassword.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Enter required Details", Toast.LENGTH_SHORT).show();
                }
//                if(firstName.length() == 0){
//                    Toast.makeText(getApplicationContext(),"Enter FirstName",Toast.LENGTH_SHORT).show();
//                }
//                if(lastName.length() == 0){
//                    Toast.makeText(getApplicationContext(),"Enter LastName",Toast.LENGTH_SHORT).show();
//                }
//                if(mobilenumber.length() == 0){
//                    Toast.makeText(getApplicationContext(),"Enter MobileNumber",Toast.LENGTH_SHORT).show();
//                }
//                if(dateOFBirth.length() == 0){
//                    Toast.makeText(getApplicationContext(),"Enter dateOfBirth",Toast.LENGTH_SHORT).show();
//                }
//                if(zip.length() == 0){
//                    Toast.makeText(getApplicationContext(),"Enter ZipCode",Toast.LENGTH_SHORT).show();
//                }
//                if(emailAddress.length() == 0) {
//                    Toast.makeText(getApplicationContext(), "Please enter an email address", Toast.LENGTH_SHORT).show();
//                }

                Boolean emailFlag = (isValidEmail(emailAddress));
                if (emailFlag == true) {
                    Toast.makeText(getApplicationContext(), "valid email address", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
                }

                if (userPassword.length() < 8) {
                    Toast.makeText(getApplicationContext(), "Password must have minimum of 8 characters", Toast.LENGTH_SHORT).show();
                }
                if (userPassword.length() > 16) {
                    Toast.makeText(getApplicationContext(), "Password must have maximum of 8 characters", Toast.LENGTH_SHORT).show();
                }

                if (!userPassword.equals(repeatPassword)) {
                    Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                } else {
                    Boolean passwordFlag = isValidPassword(userPassword);
                    if (passwordFlag == true) {
                        Toast.makeText(getApplicationContext(), "valid password", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Password must contain one uppercase, one lowercase, one special charater and one one digit", Toast.LENGTH_SHORT).show();
                    }
                }

//                Intent loginIntent = new Intent(getApplicationContext(),LoginActivity.class);
//                startActivity(loginIntent);
            }
        });
    }



        /* Code for S3*/
  //              int permission = ActivityCompat.checkSelfPermission(SignUpActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

//                if (permission != PackageManager.PERMISSION_GRANTED) {
//                    // We don't have permission so prompt the user
//                    ActivityCompat.requestPermissions(
//                            SignUpActivity.this,
//                            PERMISSIONS_STORAGE,
//                            REQUEST_EXTERNAL_STORAGE
//                    );
//                }

                // callback method to call credentialsProvider method.
  //              credentialsProvider();

                // callback method to call the setTransferUtility method
    //            setTransferUtility();
        /* Code for S3*/

                // ATTENTION: This was auto-generated to implement the App Indexing API.
                // See https://g.co/AppIndexing/AndroidStudio for more information.
  //              client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();



public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
        return false;
        } else {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
        }

public static boolean isValidPassword(final String password) {
        Pattern pattern;
        Matcher matcher;
final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,16})";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();
        }

    public void hostPicUpload(View v){
        selectImage();
    }
    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(HostSignUp.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result=Utility.checkPermission(HostSignUp.this);


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

    @Override
    public void onBackPressed() {

        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(HostSignUp.this);
        alertDialog.setTitle("Guard Registration Incomplete..");
        alertDialog.setMessage("Do you want to Exit?");
        alertDialog.setPositiveButton("yes",new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub

                finish();
            }
        });
        alertDialog.setNegativeButton("No",new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.cancel();
            }
        });

        android.app.AlertDialog alert=alertDialog.create();
        alert.show();
    }

}
