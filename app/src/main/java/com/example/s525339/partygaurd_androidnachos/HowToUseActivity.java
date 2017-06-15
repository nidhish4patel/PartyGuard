package com.example.s525339.partygaurd_androidnachos;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * It is where a new user knows about our App by looking at the text-info and videos
 */

public class HowToUseActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private LinearLayout imagesLayout;
    private ImageView[] images;
    private int[] layouts;
    private Button btnSkip, btnNext;
    private PrefManager prefManager;
    String url1="https://drive.google.com/uc?id=0Bx9MmrMsdE2dVTN1bFFKa1ZlczQ";
    String url2="https://drive.google.com/uc?id=0Bx9MmrMsdE2deDlqTjVWV3pCRG8";
    String url3="https://drive.google.com/uc?id=0Bx9MmrMsdE2dOWwxQVV1V0haTUE";
    //String url="http://danieldebo.com/wp-content/uploads/2016/01/partyguard-597x480.png";
    private TextView appTv;
    String azure_server = "";
    private final static String USERNAME_KEY = "username";

    private final static String SAVED_KEY = "saved";
    private boolean isSaved = false;

    private SharedPreferences loginPrefs;
    private String userName,password,displayMessage;
    //    private ImageView imageView1;
//    private ImageView imageView2;
//    private ImageView imageView3;
//    private LayoutInflater inflater;
    Bitmap image1BitMap;
    //  private GoogleDriveActivity activity;



    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Create default options which will be used for every
        //  displayImage(...) call if no options will be passed to this method
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config);

        init();
        try {
            ApplicationInfo ai = getPackageManager().getApplicationInfo(this.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            azure_server = bundle.getString("azure_server");
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("AZURE_SERVICE", "Failed to load meta-data, NameNotFound: " + e.getMessage());
        } catch (NullPointerException e) {
            Log.e("AZURE_SERVICE", "Failed to load meta-data, NullPointer: " + e.getMessage());
        }
        final ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater().inflate(R.layout.action_bar, null);
        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(actionBarLayout);




        // Checking for first time launch - before calling setContentView()
        prefManager = new PrefManager(this);
        if (!prefManager.isFirstTimeLaunch()) {
            //  launchHomeScreen();
        }

        setContentView(R.layout.activity_how_to_use);

        viewPager = (ViewPager) findViewById(R.id.view_pager);

        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        imagesLayout = (LinearLayout) findViewById(R.id.imageDots);
        btnSkip = (Button) findViewById(R.id.btn_skip);
        btnNext = (Button) findViewById(R.id.btn_next);


        // layouts of all welcome sliders
        // add few more layouts if you want
        layouts = new int[]{
                R.layout.welcome_slide1,
                R.layout.welcome_slide2,
                R.layout.welcome_slide3
        };

        // adding bottom dots
        addBottomDots(0);

        addImage(0);

        // making notification bar transparent
        //  changeStatusBarColor();

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        userName = loginPrefs.getString(USERNAME_KEY," ");
        if(userName.equals("")){

        }
        else
        {
            Toast.makeText(HowToUseActivity.this,"Login Successful!!", Toast.LENGTH_SHORT).show();
        }

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //  launchHomeScreen();

              //  launchHomeScreen();

                startActivity(new Intent(HowToUseActivity.this, UniversityActivity.class));
                finish();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checking for last page
                // if last page home screen will be launched
                int current = getItem(+1);
                if (current < layouts.length) {
                    // move to next screen
                    viewPager.setCurrentItem(current);
                } else {

                    // launchHomeScreen();

                   // launchHomeScreen();

                    startActivity(new Intent(HowToUseActivity.this, UniversityActivity.class));
                    finish();
                }
            }
        });

        // new LoadImage().execute(url3);
    }

    private void addImage(int currentPage)
    {
        //images = new ImageView[layouts.length];

        ImageView   image = new ImageView(this);
        imagesLayout.removeAllViews();
        if(currentPage == 0)
        {
            // image.setImageResource(R.drawable.partyguard);

            // image.setImageBitmap(image1BitMap);

            ImageLoader.getInstance().displayImage(url1, image);
            //  appTv.setText("App information");

        }
        else if(currentPage == 1)
        {
            ImageLoader.getInstance().displayImage(url2, image);
            // image.setImageResource(R.drawable.text);
        }
        else if(currentPage == 2)
        {
            ImageLoader.getInstance().displayImage(url3, image);
            // image.setImageResource(R.drawable.guard);
        }

        imagesLayout.addView(image);

        // imagesLayout.removeAllViews();
//        for (int i = 0; i < images.length; i++) {
//            images[i] = new ImageView(this);
//            if(i==0) {
//                images[i].setImageResource(R.drawable.partyguard);
//            }
//            else if(i==1)
//            {
//                images[i].setImageResource(R.drawable.text);
//            }
//            else if(i==2)
//            {
//                images[i].setImageResource(R.drawable.guard);
//            }
//            imagesLayout.addView(images[i]);
//        }
//        if (dots.length > 0)
//        images[currentPage].setImageResource(R.mipmap.ic_launcher);
    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

    private void init() {

        loginPrefs = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        Log.e("LogPefs", loginPrefs + "");
        Log.e("LogPefs", loginPrefs.getString(USERNAME_KEY,"") + "");
        Log.e("LogPefs", loginPrefs.getBoolean(SAVED_KEY, false)+ "");


    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

//    private void launchHomeScreen() {
//        prefManager.setFirstTimeLaunch(false);
//        startActivity(new Intent(HowToUseActivity.this, MainActivity.class));
//        finish();
//    }

    //	viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);
            addImage(position);

            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == layouts.length - 1) {
                // last page. make button text to GOT IT
                btnNext.setText(getString(R.string.start));
                btnSkip.setVisibility(View.GONE);
            } else {
                // still pages are left
                btnNext.setText(getString(R.string.next));
                btnSkip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };


    /**
     * View pager adapter
     */
    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

//            inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            View sliderView = inflater.inflate(R.layout.welcome_slide1, null);

            //final View imageView_1= LayoutInflater.from(HowToUseActivity.this).inflate(R.layout.welcome_slide1,null);

            //final View imageView_2= LayoutInflater.from(HowToUseActivity.this).inflate(R.layout.welcome_slide2,null);
            //final View imageView3IV= LayoutInflater.from(HowToUseActivity.this).inflate(R.layout.welcome_slide3,null);

            //imageView1=(ImageView) imageView_1.findViewById(R.id.imag);
            //   imageView1.setImageResource(R.drawable.partyguard);

            //imageView2=(ImageView) imageView_2.findViewById(R.id.imag2);
            //  imageView2.setImageResource(R.drawable.text);

            //imageView3=(ImageView) imageView3IV.findViewById(R.id.imag3);
            //  imageView3.setImageResource(R.drawable.guard);
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
//            ImageView img1 = (ImageView) view.findViewById(R.id.imag);
//            img1.setImageResource(R.mipmap.ic_launcher);
//            Bitmap bitmap;
//            bitmap=GetBitmapfromUrl("http://test-dashboard1.seeloz.com/system/images/products_images/86/5454544114_1401886223?1401886223");
//            i.setImageBitmap(bitmap);
//            img1.getDrawable("https://drive.google.com/open?id=0Bx9MmrMsdE2dTGxxX0JTZUhObHM");
//            if(position==0){
//                imageView1.setImageResource(R.drawable.partyguard);
//
//
//            }
//            if(position==1){
//                imageView2.setImageResource(R.drawable.text);
//
//
//            }
//            if(position==2){
//                imageView3.setImageResource(R.drawable.guard);
//
//
//            }
            container.addView(view);
            return view;

        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

//    private class LoadImage extends AsyncTask<String,Void,Bitmap> {
//
//        @Override
//        protected Bitmap doInBackground(String... strings) {
//
//            try {
//                URL url = new URL(strings[0]);
//                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
//                connection.connect();
//                InputStream inputStream = connection.getInputStream();
//                bitmap = BitmapFactory.decodeStream(inputStream);
//                return bitmap;
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Bitmap bitmap) {
//            super.onPostExecute(bitmap);
//           image1BitMap = bitmap;
//          //  ImageView firstImage = images[0];
//          //  image.setImageBitmap(bitmap);
////            imageView2.setImageBitmap(bitmap);
////            imageView3.setImageBitmap(bitmap);
//        }
//    }
}