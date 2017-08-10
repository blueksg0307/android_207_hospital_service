package com.first.administrator.project207.Service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;
import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.SystemRequirementsChecker;
import com.estimote.sdk.connection.internal.protocols.Operation;
import com.first.administrator.project207.BroadCastReceiver.MyBroadCastReceiver;
import com.first.administrator.project207.activities.MainActivity;
import com.first.administrator.project207.activities.SplashActivity;
import com.first.administrator.project207.utils.Constants;
import com.first.administrator.project207.utils.ServerRequestQueue;
import com.google.firebase.iid.FirebaseInstanceId;
import org.json.JSONObject;
import java.util.List;
import java.util.UUID;


public class BeaconConnect extends Service {

    private static final String TAG ="BeaconProcess";

    public static boolean isBeaconChecked ;
    private BeaconManager beaconManager ;
    private Region region;
    private String beacon_uuid;
    private int beacon_Major;
    private int beacon_Minor;
    private SharedPreferences sharedPreferences;
    private String userToken;
    private String userID;
    public MyBroadCastReceiver broadCastReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Service 동작시작.");


        sharedPreferences = getSharedPreferences("Mypref", MODE_PRIVATE);
        beaconManager = new BeaconManager(getApplicationContext());
        userToken = FirebaseInstanceId.getInstance().getToken();
        userID = sharedPreferences.getString("userID", "");


        Log.d(TAG, "userToken:"+ userToken);
        Log.d(TAG, "myID:"+ userID);
        Log.d(TAG, "beaconcheck:"+ isBeaconChecked);

        if(!isBeaconChecked) {
            beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
                @Override
                public void onServiceReady() {
                    beaconManager.startRanging(region);
                    Log.d(TAG, "비콘검색시작");

                }
            });
        }

        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, List<Beacon> list) {

                if(!list.isEmpty()) {
                    Log.d(TAG, "리스너시작");

                    Beacon nearestBeacon = list.get(0);
                    beacon_Major = nearestBeacon.getMajor();
                    beacon_Minor = nearestBeacon.getMinor();
                    beacon_uuid = nearestBeacon.getProximityUUID().toString();
                    Intent BroadCastBeaconInfo = new Intent("GetBeaconUUID");
                    BroadCastBeaconInfo.putExtra("UUID", beacon_uuid.toString());
                    BroadCastBeaconInfo.putExtra("userID", userID);
                    BroadCastBeaconInfo.putExtra("userToken", userToken);
                    sendBroadcast(BroadCastBeaconInfo);
                    Log.d(TAG, "UUID:"+ beacon_uuid.toString());
                    isBeaconChecked = true;
                    //여기서 FCM 이 오면 좋겠다
            }
            else {
                    isBeaconChecked = false;
            }
            }});

        region = new Region("ranged region",
                UUID.fromString("43CBDA6E-28FA-4F5B-AF12-416CAF3E3737"),
                null, null);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}