package com.first.administrator.project207.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.estimote.sdk.SystemRequirementsChecker;
import com.first.administrator.project207.BroadCastReceiver.MyBroadCastReceiver;
import com.first.administrator.project207.R;

public class SplashActivity extends Activity {

    public boolean autoLogin = true ;
    public MyBroadCastReceiver myBroadCastReceiver;
    public static boolean bluetoothPermission = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        myBroadCastReceiver.isReservationFinished = false ;
        //이 동의가 없으면 프로그램이 진행되질 않는구나... 이 권한이 가장 중요하네


        Handler hd = new Handler();
        hd.postDelayed(new Runnable() {
            @Override
            public void run() {

                finish();

            }
        }, 3000); // 3초 후 이미지를 닫습니다

    }

}
