package com.example.melaAttendance.activity;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.example.melaAttendance.R;
import com.example.melaAttendance.adapter.SelectAttedenceTypeAdapter;
import com.example.melaAttendance.adapter.SelectShgAdapter;
import com.example.melaAttendance.database.AttendanceFlagData;
import com.example.melaAttendance.database.ShgsDetailsOnLoginData;
import com.example.melaAttendance.fragments.About_us_fragment;
import com.example.melaAttendance.fragments.Contact_us_fragment;
import com.example.melaAttendance.fragments.MainFragment;
import com.example.melaAttendance.fragments.Profile;
import com.example.melaAttendance.utils.AppUtility;
import com.example.melaAttendance.utils.DateFactory;
import com.example.melaAttendance.utils.GPSTracker;
import com.example.melaAttendance.utils.PreferenceManager;
import com.example.melaAttendance.utils.ProjectPrefrences;
import com.google.android.material.navigation.NavigationView;
import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Fragment fragment = null;
    TextView drname, drmobile;
    String shg, mobile,shgRegistrationId;
   public static Toolbar toolbar;
    public OnBackPressedListener onBackPressedListener;
    RecyclerView selectAttedenceRV;
    SelectAttedenceTypeAdapter selectAttedenceTypeAdapter;
    List<AttendanceFlagData> selectAttedenceTypes;
    String getSharedPrefSavedDate ;
    public static final int RequestPermissionCode = 1;
    private  Handler  handler;
    Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home3);
//        SplashScreen.getInstance().getDaoSession().getSelectedProductDetailsDao().deleteAll();
        //getSupportActionBar().setTitle("Home");
        toolbar = findViewById(R.id.toolbar);

        handler=new Handler();
        runnable=  new Runnable() {
            @Override
            public void run() {
                System.exit(0);
            }
        };
        startHandler();

        //-------------navigation drawew-------------
        setSupportActionBar(toolbar);
        shgRegistrationId=ProjectPrefrences.getInstance().getSharedPrefrencesData(PreferenceManager.getPrefKeyShgRegId(),HomeActivity.this);
        mobile = ProjectPrefrences.getInstance().getSharedPrefrencesData(PreferenceManager.getPrefKeyShgMobile(),HomeActivity.this);
        shg = ProjectPrefrences.getInstance().getSharedPrefrencesData(PreferenceManager.getPrefShgNameForProfile(),HomeActivity.this);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        //NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView = navigationView.getHeaderView(0);
        //----
        drname = (TextView) hView.findViewById(R.id.tvUserName1);
        drmobile = (TextView) hView.findViewById(R.id.tvUserMobile2);
        //----
        drname.setText(shg);
        drmobile.setText(mobile);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
       // EnableRuntimePermission();
        if(CheckingPermissionIsEnabledOrNot())
        {
           // Toast.makeText(HomeActivity.this, "All Permissions Granted Successfully", Toast.LENGTH_LONG).show();
        }
        else {
            RequestMultiplePermission();
        }
        getLocation();

 /*       getSharedPrefSavedDate  = ProjectPrefrences.getInstance().getSharedPrefrencesData(PreferenceManager.getPrefKeyNextDayDate(),HomeActivity.this);
        if(getSharedPrefSavedDate.equalsIgnoreCase("")){
          //  getSharedPrefSavedDate = DateFactory.getInstance().getTodayDate();
            ProjectPrefrences.getInstance().saveSharedPrefrecesData(PreferenceManager.getPrefKeyNextDayDate(), DateFactory.getInstance().getTodayDate(),HomeActivity.this);
        }
      //  Toast.makeText(HomeActivity.this, "---"+getSharedPrefSavedDate, Toast.LENGTH_LONG).show();

        getSharedPrefSavedDate  = ProjectPrefrences.getInstance().getSharedPrefrencesData(PreferenceManager.getPrefKeyNextDayDate(),HomeActivity.this);
        if(getSharedPrefSavedDate.equalsIgnoreCase(DateFactory.getInstance().getTodayDate())){
          //  Toast.makeText(HomeActivity.this, "checking dates" ,Toast.LENGTH_LONG).show();
        }
        else {
          //  Toast.makeText(HomeActivity.this, "--in else----" ,Toast.LENGTH_LONG).show();
            updateFlagOfAllParticipant();
            ProjectPrefrences.getInstance().saveSharedPrefrecesData(PreferenceManager.getPrefKeyNextDayDate(), DateFactory.getInstance().getTodayDate(),HomeActivity.this);
        }*/
        fragment = new MainFragment();
        displayFragment(fragment);
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        stopHandler();
        startHandler();
    }

    public void stopHandler() {
        handler.removeCallbacks(runnable);
    }
    public void startHandler() {
        handler.postDelayed(runnable, 30*60*1000);
    }


    private void EnableRuntimePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity.this,
                ACCESS_FINE_LOCATION)) {
            //  Toast.makeText(AttendanceActivity.this, "CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(HomeActivity.this, new String[]{
                    ACCESS_FINE_LOCATION}, RequestPermissionCode);
        }
    }



private void updateFlagOfAllParticipant(){
    List<AttendanceFlagData> attendanceFlagDataListForUpdateFlag=SplashScreen.getInstance()
            .getDaoSession()
            .getAttendanceFlagDataDao()
            .queryBuilder()
            .build()
            .list();
    AppUtility.getInstance().showLog("updated object"+attendanceFlagDataListForUpdateFlag,HomeActivity.class);
    for(AttendanceFlagData attendanceFlagData:attendanceFlagDataListForUpdateFlag) {
        attendanceFlagData.setParticipantFlag("NA");
        attendanceFlagData.setMorningFlag("NA");
        attendanceFlagData.setEeningFlag("NA");
        SplashScreen.getInstance().getDaoSession().getAttendanceFlagDataDao().update(attendanceFlagData);
    }

    List<AttendanceFlagData> attendanceFlagDataListForUpdateFlag1=SplashScreen.getInstance()
                .getDaoSession()
                .getAttendanceFlagDataDao()
                .queryBuilder()
                .build()
                .list();
        for(AttendanceFlagData attendanceFlagData:attendanceFlagDataListForUpdateFlag){
          AppUtility.getInstance().showLog("updatedflags"+attendanceFlagData.getParticipantFlag(),HomeActivity.class);
        }
}

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (onBackPressedListener != null) {
                onBackPressedListener.doBack();
            } else {
               super.onBackPressed();
               // onBackPressedListener.doBack();
            }
        }
    }
        public interface OnBackPressedListener {
            void doBack();
        }

        public void setOnBackPressedListener(OnBackPressedListener onBackPressedListener) {
            this.onBackPressedListener = onBackPressedListener;
        }

    private void RequestMultiplePermission() {
        ActivityCompat.requestPermissions(HomeActivity.this, new String[]
                {
                        CAMERA,
                        ACCESS_FINE_LOCATION,
                        READ_EXTERNAL_STORAGE
                }, RequestPermissionCode);

    }
    public boolean CheckingPermissionIsEnabledOrNot() {
        int FirstPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        int SecondPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        int ThirdPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SecondPermissionResult==PackageManager.PERMISSION_GRANTED &&
                ThirdPermissionResult == PackageManager.PERMISSION_GRANTED;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case RequestPermissionCode:
                if (grantResults.length > 0) {

                    boolean CameraPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean GetLocationPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadExternalStoragePermission = grantResults[2] == PackageManager.PERMISSION_GRANTED;

                    if (CameraPermission&&GetLocationPermission&&ReadExternalStoragePermission) {

                        Toast.makeText(HomeActivity.this, "Permission Granted", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(HomeActivity.this,"Permission Denied",Toast.LENGTH_LONG).show();
                    }
                }

                break;
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
       /* if (id == R.id.menu_Home) {

            toolbar.setTitle("Home");
            SplashScreen.getInstance().getDaoSession().getProductAvailableQuantityDataDao().deleteAll();
            SplashScreen.getInstance().getDaoSession().getSelectedProductDetailsDao().deleteAll();
            fragment = new MainFragment();
            fragment.setArguments(bundle);
            displayFragment(fragment);
        }*/
    /*     if (id == R.id.menu_profile) {
            toolbar.setTitle("Profile");
            fragment = new Profile();
            displayFragment(fragment);
        }*//* else if (id == R.id.menu_attendance) {

        }*/  if (id == R.id.menu_contact_us) {
            toolbar.setTitle("Contact us");
            fragment = new Contact_us_fragment();
            displayFragment(fragment);
        } else if (id == R.id.menu_About_us) {
            toolbar.setTitle("About us");
            fragment = new About_us_fragment();
            displayFragment(fragment);
        }else if(id==R.id.Home)
        {
            toolbar.setTitle("Mela Attendance");
            fragment=new MainFragment();
            displayFragment(fragment);
        }



        /* else if (id == R.id.menu_history) {
            toolbar.setTitle("Sale History");
            fragment = new HistoryFragment();
            fragment.setArguments(bundle);
            displayFragment(fragment);
        }*/ else if (id == R.id.menu_logout) {

            clearAllSharedPrefrences();
            clearingAllTableOfLocalDB();

            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

        /*else if(id == R.id.menu_SelectShg){
            Intent intent = new Intent(HomeActivity.this, SelectShgActivity.class);
            startActivity(intent);
        }*/

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void clearAllSharedPrefrences(){
        ProjectPrefrences.getInstance().removeSharedPrefrencesData(PreferenceManager.getPrefKeyLoginDone(), HomeActivity.this);
        ProjectPrefrences.getInstance().removeSharedPrefrencesData(PreferenceManager.getPrefKeyMpin(),HomeActivity.this);
        ProjectPrefrences.getInstance().removeSharedPrefrencesData(PreferenceManager.getPrefPhonenoForMpin(),HomeActivity.this);
    }
  private void  clearingAllTableOfLocalDB(){

            SplashScreen.getInstance().getDaoSession().getAttendanceFlagDataDao().deleteAll();
            SplashScreen.getInstance().getDaoSession().getBillDetailsDataDao().deleteAll();
            SplashScreen.getInstance().getDaoSession().getSelectedProductDetailsDao().deleteAll();
            SplashScreen.getInstance().getDaoSession().getShgDetailsDataDao().deleteAll();
            SplashScreen.getInstance().getDaoSession().getShgMemberDetailsDataDao().deleteAll();
            SplashScreen.getInstance().getDaoSession().getShgsDetailsOnLoginDataDao().deleteAll();
            SplashScreen.getInstance().getDaoSession().getSoldProductDetailsDataDao().deleteAll();
    }


    public void displayFragment(Fragment fr) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame_layout, fr);
        ft.commit();

    }

    public String getLocation() {
        String stringLatitude="",stringLongitude="";
        GPSTracker gpsTracker = new GPSTracker(HomeActivity.this);
        if (gpsTracker.getIsGPSTrackingEnabled()) {
            stringLatitude = String.valueOf(gpsTracker.latitude);
           // lati = Double.parseDouble(stringLatitude);
             stringLongitude = String.valueOf(gpsTracker.longitude);
           // longi = Double.parseDouble(stringLongitude);
            try {
//                geocoder = new Geocoder(this, Locale.getDefault());
//                addresses = geocoder.getFromLocation(lati, longi, 1);
//                address = addresses.get(0).getAddressLine(0);
//                //Log.d("TAG","address"+address);
//                String city = addresses.get(0).getLocality();
//                //Log.d("TAG","address"+city);
//                String country = addresses.get(0).getCountryName();
//                String postalCode = addresses.get(0).getPostalCode();
//                String knownName = addresses.get(0).getFeatureName();
//                tvAttendanceAddress.setText(address);
//                ProjectPrefrences.getInstance().saveSharedPrefrecesData(PreferenceManager.getPrefKeyInAddress(), address, AttendanceActivity.this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings

            gpsTracker.showSettingsAlert();
        }
        return stringLatitude+""+stringLongitude;

    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
