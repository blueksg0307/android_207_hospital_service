package com.first.administrator.project207.activities;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;


import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.SystemRequirementsChecker;
import com.first.administrator.project207.R;
import com.first.administrator.project207.Service.BeaconConnect;
import com.first.administrator.project207.fragments.CalenderFragment;
import com.first.administrator.project207.fragments.HomeFragment;
import com.first.administrator.project207.fragments.WaitingListFragment;
import com.first.administrator.project207.utils.ServerRequestQueue;

import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private ServerRequestQueue mRequestQueue;
    private AlertDialog dialog;
    private boolean success ;
    private String userID;
    private String userName;
    private String userBirth;
    private String userNumber;
    private String userPassword;
    private String userToken;
    boolean finishReservation = false;
    private boolean beaconSuccess ;
    private String userEmail;
    private String userList ;
    ///

    private int mSelectedPosition ;
    private FrameLayout frameLayout;
    private BottomNavigationView bottomNavigationView;

    ///

    //private HomeFragment homeFragment;
    private CalenderFragment calenderFragment;

    ////////////비콘관련
    private static final String TAG ="BeaconProcess";





    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.DoReservation:
                    Bundle bundle = new Bundle();
                    bundle.putString("userID", userID);
                    CalenderFragment calenderFragment = new CalenderFragment();
                    calenderFragment.setArguments(bundle);
                    FragmentManager DoReservationManager = getSupportFragmentManager();
                    FragmentTransaction ReservationFragmentTransaction = DoReservationManager.beginTransaction();
                    ReservationFragmentTransaction.replace(R.id.Fragment,calenderFragment);
                    ReservationFragmentTransaction.commit();
                    return true;

                case R.id.Home:

                    FragmentManager Main = getSupportFragmentManager();
                    FragmentTransaction  MainTransaction =  Main.beginTransaction();
                    MainTransaction.replace(R.id.Fragment, new HomeFragment());
                    MainTransaction.commit();


                        return true;

                case R.id.BottomWaitingList:

                    FragmentManager BottomWaitList = getSupportFragmentManager();
                    FragmentTransaction  BottomWaitListTransaction =  BottomWaitList.beginTransaction();
                    BottomWaitListTransaction.replace(R.id.Fragment, new WaitingListFragment());
                    BottomWaitListTransaction.commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbarnavigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        //Event Configuration of ActionBar Indicator, why is it special on different of other ID? //
        if (item.getItemId() == android.R.id.home){


            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("확인을 누르시면 로그아웃이 됩니다. 그래도 하시겠습니까?")
                    .setNegativeButton("아니요",null)
                    .setPositiveButton("네", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent Before = new Intent(MainActivity.this, LoginActivity.class);
                            boolean autoFlag = false ;
                            boolean splashFlag = false ;
                            Before.putExtra("AutoLoginFlag", autoFlag);
                            Before.putExtra("SplashFlag", splashFlag);
                            startActivity(Before);
                            dialog.dismiss();
                            finish();;
                        }
                    }).create().show();

            return true;

        }

        if (id == R.id.GoHome) {
            Toast.makeText(this, "이미 메인화면에 있습니다",Toast.LENGTH_LONG).show();
            return true;
        }

        if (id == R.id.Myinfo) {
            Intent ToMyinfo = new Intent(MainActivity.this, MyinfoActivity.class);
            startActivity(ToMyinfo);

            return true;
        }

        if (id == R.id.Logout) {
            Intent ToLogin = new Intent(MainActivity.this, LoginActivity.class);
            boolean autoFlag = false ;
            boolean splashFlag = false ;
            ToLogin.putExtra("AutoLoginFlag", autoFlag);
            ToLogin.putExtra("SplashFlag", splashFlag);

            startActivity(ToLogin);
            finish();
            return true;
        }

        if (id == R.id.Exit) {

            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Intent BeaconService = new Intent(MainActivity.this,BeaconConnect.class);
        SystemRequirementsChecker.checkWithDefaultDialogs(this);
        startService(BeaconService);


        //-----------Shape of Configuration Toolbar-------------------------------------------------
        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.toolbarnavigation);
        toolbar.canShowOverflowMenu();
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.button_back);
        FloatingActionButton addReservation = (FloatingActionButton) findViewById(R.id.floatingActionButton);


        FragmentManager Main = getSupportFragmentManager();
        FragmentTransaction  MainTransaction =  Main.beginTransaction();
        MainTransaction.replace(R.id.Fragment, new HomeFragment());
        MainTransaction.commit();

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigationView.setSelectedItemId(R.id.Home);

        //-----------Shape of Configuration Toolbar-------------------------------------------------


        ////////////////////////////////////////////////////////////////////////////
        SharedPreferences mPref = getSharedPreferences("Mypref", MODE_PRIVATE);
        Intent infoIntent = getIntent();
        userID = mPref.getString("userID","");
        userName = infoIntent.getStringExtra("userName");
        userBirth = infoIntent.getStringExtra("userBirth");
        userPassword = infoIntent.getStringExtra("userPassword");
        userEmail = infoIntent.getStringExtra("userEmail");
        userNumber = infoIntent.getStringExtra("userNumber");

        addReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent addReservation = new Intent(MainActivity.this, ReservationActivity.class);
                addReservation.putExtra("userID",userID);
                startActivity(addReservation);
            }
        });
    }
    @Override
    protected void onResume(){
        super.onResume();
    }
    @Override
    public void onPause(){
        super.onPause();
    }
    private long lastTimeBackPressed;
    @Override
    public void onBackPressed() {
        if(System.currentTimeMillis() - lastTimeBackPressed < 1500)
        {
            finish();
            return;
        }
        Toast.makeText(this, "’뒤로’ 버튼을 한번 더 누르면 종료합니다." ,Toast.LENGTH_SHORT).show();
        lastTimeBackPressed = System.currentTimeMillis();
    }
}
