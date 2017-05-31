package com.example.administrator.project207.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Response;
import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.SystemRequirementsChecker;
import com.example.administrator.project207.R;
import com.example.administrator.project207.utils.CheckPermission;
import com.example.administrator.project207.utils.Constants;
import com.example.administrator.project207.Service.MyFirebaseInstanceIDService;
import com.example.administrator.project207.utils.ServerRequestQueue;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private ServerRequestQueue mRequestQueue;

    private BeaconManager beaconManager ;
    private Region region;
    private String beacon_uuid;
    private int beacon_Major;
    private int beacon_Minor;
    private AlertDialog dialog;
    private boolean success ;
    private String userID;
    private String userName;
    private String userBirth;
    private String userNumber;
    private String userToken;
    private boolean finishReservation ;
    private boolean beaconSuccess ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRequestQueue = ServerRequestQueue.getInstance(getApplicationContext());

        final ImageView booking  = (ImageView) findViewById(R.id.booking);
        final ImageView myinfo   = (ImageView) findViewById(R.id.myinfo);
        final ImageView callbook = (ImageView) findViewById(R.id.callbook);
        final ImageView checkbook  = (ImageView) findViewById(R.id.bookcheck);
        final ImageView waitingList   = (ImageView) findViewById(R.id.waitinglist);
        final ImageView history = (ImageView) findViewById(R.id.histroy);

        Intent Information = getIntent();
        userID = Information.getStringExtra("userID");
        userName = Information.getStringExtra("userName");
        userBirth = Information.getStringExtra("userBirth");
        userNumber = Information.getStringExtra("userNumber");

        //If each permission have not been checked, not going to be working.
        //However checked, going to store Permission success into CheckPermission class
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            CheckPermission.readStoragePermission = true ;
        }


        //FireBase Testing
        FirebaseMessaging.getInstance().subscribeToTopic("news");
        userToken = FirebaseInstanceId.getInstance().getToken().toString();
        //


        myinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Intent = new Intent(MainActivity.this, MyinfoActivity.class);
                Intent.putExtra("userID" , userID);
                Intent.putExtra("userName" , userName);
                Intent.putExtra("userBirth", userBirth);
                Intent.putExtra("userNumber", userNumber);

                MainActivity.this.startActivity(Intent);
            }
        });

        booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Intent = new Intent(MainActivity.this, ReserveActivity.class);
                Intent.putExtra("userID" , userID);
                Intent.putExtra("userName" , userName);
                Intent.putExtra("userBirth" , userBirth);
                MainActivity.this.startActivity(Intent);
            }
        });

        callbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri uri = Uri.parse("tel:043-494-5451");
                Intent intent = new Intent(Intent.ACTION_DIAL,uri);
                startActivity(intent);


            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Response.Listener histroyresponseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                        try {

                            JSONObject jsonObject = new JSONObject(response);


                            if(!(jsonObject.equals(""))) {


                                Intent Intent = new Intent(MainActivity.this, HistoryActivity.class);
                                Intent.putExtra("userList", jsonObject.toString());
                                MainActivity.this.startActivity(Intent);


                            }
                            else{

                                Toast.makeText(MainActivity.this,"진료기록이 없습니다",Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };

                mRequestQueue.addRequest(Constants.POST_REQUEST_URLS.GET_HISTORY, histroyresponseListener, null, userID);

            }
        });

        waitingList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CheckWaitingActivity.class);
                MainActivity.this.startActivity(intent);


            }
        });

        checkbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Response.Listener responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            if(!(jsonObject.equals(""))) {

                                Intent Intent = new Intent(MainActivity.this, CheckbookActivity.class);
                                Intent.putExtra("userList", jsonObject.toString());
                                MainActivity.this.startActivity(Intent);


                            }
                            else{

                                builder.setMessage("예약을 하지 않았습니다")
                                        .setNegativeButton("확인",null)
                                        .create()
                                        .show();

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                mRequestQueue.addRequest(Constants.POST_REQUEST_URLS.CHECK_RESERVATION, responseListener, null, userID);
            }
        });

    }

    @Override
    protected void onResume(){
        super.onResume();
        SystemRequirementsChecker.checkWithDefaultDialogs(this);
        beaconManager = new BeaconManager(this);
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startRanging(region);
            }
        });
        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, List<Beacon> list) {
                if(!list.isEmpty()){

                    Beacon nearestBeacon = list.get(0);
                    beacon_Major = nearestBeacon.getMajor();
                    beacon_Minor = nearestBeacon.getMinor();
                    beacon_uuid = nearestBeacon.getProximityUUID().toString();
                    Intent Information = getIntent();
                    userID = Information.getStringExtra("userID");
                    if(!finishReservation) {

                        new BackgroundTask().execute();

                    }

                }
            }
        });

        region = new Region("ranged region",
                UUID.fromString("43CBDA6E-28FA-4F5B-AF12-416CAF3E3737"),
                null,null);


    }

    @Override
    public void onPause(){
        beaconManager.stopRanging(region);
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

    class BackgroundTask extends AsyncTask<Void, Void, String> {



        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    beaconSuccess = jsonObject.getBoolean("success");

                    //After Recognizing ReservationSuccess with beaconConnection, Message is going to be happend.
                    if(beaconSuccess){

                        finishReservation = true ;
                        /*
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("진료접수가 성공하였습니다. 예약을 확인하시겠습니까?.")
                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        Intent Intent = new Intent(MainActivity.this, CheckbookActivity.class);
                                        startActivity(Intent);

                                    }
                                })
                                .setNegativeButton("아니요", null)
                                .create()
                                .show();*/
                    }

                } catch (Exception e) {

                    e.printStackTrace();}
            }
        };

        @Override
        protected String doInBackground(Void... params) {

            try {

                mRequestQueue.addRequest(Constants.POST_REQUEST_URLS.BEACON_CONNECT, responseListener, null, beacon_uuid, userID, userToken);
                Thread.sleep(300000);



            }catch (Exception e){

                e.printStackTrace();

            }

            return null;
        }

    }
}