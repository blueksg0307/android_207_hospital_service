package com.example.administrator.project207.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.SystemRequirementsChecker;
import com.example.administrator.project207.R;
import com.example.administrator.project207.utils.BeaconRequest;
import com.example.administrator.project207.utils.CheckbookRequest;
import com.example.administrator.project207.utils.HistoryRequest;

import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

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
    private boolean finishReservation ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

                Uri uri = Uri.parse("tel:010-7759-5051");
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

                HistoryRequest historyRequest = new HistoryRequest(userID, histroyresponseListener);
                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                queue.add(historyRequest);

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
                            Toast.makeText(MainActivity.this, jsonObject.toString(),Toast.LENGTH_LONG).show();

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
                CheckbookRequest checkbookRequest = new CheckbookRequest(userID, responseListener);
                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                queue.add(checkbookRequest);
            }
        });

    }

    @Override
    protected void onResume(){
        super.onResume();
        SystemRequirementsChecker.checkWithDefaultDialogs(this);
        finishReservation = false ;
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
                   if(finishReservation = false){ new BackgroundTask().execute();}

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

        private boolean success ;

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    success = jsonObject.getBoolean("success");
                    if(success){

                        Toast.makeText(getApplicationContext(),"진료접수가 정상적으로 되었습니다",Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("진료접수가 정상적으로 되었습니다. 진료순서를 확인하시겠습니까?")
                                .setPositiveButton("네", null)
                                .setNegativeButton("아니요",null)
                                .create()
                                .show();

                    }
                    else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("진료접수가 실패하였습니다. 직접 진료접수를 진행하세요.")
                                .setPositiveButton("확인", null)
                                .create()
                                .show();

                    }
                } catch (Exception e) {

                    e.printStackTrace();}
            }
        };

        @Override
        protected String doInBackground(Void... params) {

            try {

                if(!success) {
                    BeaconRequest beaconRequest = new BeaconRequest(beacon_uuid, userID, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                    queue.add(beaconRequest);
                    Thread.sleep(300000);

                }
                else{

                    finishReservation = true ;
                }
            }catch (Exception e){

                e.printStackTrace();

            }

            return null;
        }

    }
}