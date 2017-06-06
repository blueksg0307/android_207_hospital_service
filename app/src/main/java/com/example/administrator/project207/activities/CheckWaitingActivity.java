package com.example.administrator.project207.activities;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.administrator.project207.R;
import com.example.administrator.project207.fragments.ReservationListFragment;
import com.example.administrator.project207.fragments.WaitingListFragment;

public class CheckWaitingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_waiting);

        final Button ReservationList = (Button) findViewById(R.id.reservationlistFragment);
        final Button WaitingList = (Button) findViewById(R.id.waitinglistFragment);

        WaitingList.setBackgroundColor(getResources().getColor(R.color.ORRANGE));
        ReservationList.setBackgroundColor(getResources().getColor(R.color.GOLD));
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.Fragment, new WaitingListFragment());
        fragmentTransaction.commit();


        ReservationList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ReservationList.setBackgroundColor(getResources().getColor(R.color.ORRANGE));
                WaitingList.setBackgroundColor(getResources().getColor(R.color.GOLD));
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.Fragment, new ReservationListFragment());
                fragmentTransaction.commit();
            }
        });

        WaitingList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                WaitingList.setBackgroundColor(getResources().getColor(R.color.ORRANGE));
                ReservationList.setBackgroundColor(getResources().getColor(R.color.GOLD));
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.Fragment, new WaitingListFragment());
                fragmentTransaction.commit();
            }
        });
    }
}