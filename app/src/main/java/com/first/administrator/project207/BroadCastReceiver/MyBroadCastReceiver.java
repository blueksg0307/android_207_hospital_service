package com.first.administrator.project207.BroadCastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.Response;
import com.first.administrator.project207.activities.MainActivity;
import com.first.administrator.project207.utils.Constants;
import com.first.administrator.project207.utils.ServerRequestQueue;

import org.json.JSONObject;

/**
 * Created by Administrator on 2017-07-10.
 */

public class MyBroadCastReceiver extends BroadcastReceiver {

    public static boolean ReservationFinished = false ;
    private static final String TAG = "BeaconProcess";
    private String UUID;
    private String userToken;
    private String userID;
    private ServerRequestQueue mRequestQueue;
    public static boolean isReservationFinished;
    private Context mContext ;

    @Override
    public void onReceive(final Context context, Intent intent) {

        mContext = context ;
        Log.d(TAG,"브로드캐스터 리시버 동작시작 ");
        mRequestQueue = ServerRequestQueue.getInstance(context);

        String FromBeaconConnect = intent.getAction();
        if(FromBeaconConnect.equals("GetBeaconUUID")){

            UUID = intent.getStringExtra("UUID").toString();
            userID = intent.getStringExtra("userID").toString();
            userToken = intent.getStringExtra("userToken").toString();
            Log.d(TAG,"나의 비콘 UUID " + UUID);
            Log.d(TAG,"나의 아이디: " + userID);
            Log.d(TAG,"나의 토큰 " + userToken);

            if(!isReservationFinished) {
                new BackgroundTask().execute();
            }
        }
    }

    class BackgroundTask extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute(){
            super.onPreExecute();

            Log.d(TAG,"백그라드운드 테스크 시작");

        }
        @Override
        protected String doInBackground(String... params) {


            Log.d(TAG, "background uuid" + UUID);
            Log.d(TAG, "userID" + userID);
            Log.d(TAG, "usertoken" + userToken);


            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {

                        JSONObject jsonObject = new JSONObject(response);
                        boolean success = jsonObject.getBoolean("success");

                        if (success) {

                            isReservationFinished = true;

                            Log.d(TAG,"예약결과"+ isReservationFinished);
                            Intent intent = new Intent(mContext, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mContext.startActivity(intent);
                        }

                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                }
            };
            mRequestQueue.addRequest(Constants.POST_REQUEST_URLS.BEACON_CONNECT, responseListener, null, UUID, userID, userToken);
            return null;
        }

    }
}




