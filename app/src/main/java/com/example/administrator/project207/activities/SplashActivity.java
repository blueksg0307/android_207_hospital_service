package com.example.administrator.project207.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.SystemRequirementsChecker;
import com.example.administrator.project207.R;

import java.util.List;
import java.util.UUID;

public class SplashActivity extends Activity {

    public boolean finishReservation = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Toast.makeText(getApplicationContext(),"블루투스 키지 않으면 진료접수가 진행되지 않습니다",Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext(),"블루투스 메시지에 동의해주세요!!",Toast.LENGTH_SHORT).show();

        Handler hd = new Handler();
        hd.postDelayed(new Runnable() {
            @Override
            public void run() {

                finish();

            }
        }, 3000); // 3초 후 이미지를 닫습니다

    }



}
