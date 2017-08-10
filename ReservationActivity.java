package com.first.administrator.project207.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.first.administrator.project207.BroadCastReceiver.MyBroadCastReceiver;
import com.first.administrator.project207.R;
import com.first.administrator.project207.utils.Constants;
import com.first.administrator.project207.utils.MyDatabaseManager;
import com.first.administrator.project207.utils.ServerRequestQueue;


import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class ReservationActivity extends AppCompatActivity {

    private AlertDialog dialog;
    private String HourOfDay;
    private String Minute ;
    private String Year ;
    private String MonthOfYear;
    private String DayOfMonth;
    private TextView SelectTime;
    private TextView SelectDate;
    private ServerRequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        final MyDatabaseManager dbManager = new MyDatabaseManager(getApplicationContext(), "sample.db", null, 1);
        mRequestQueue = ServerRequestQueue.getInstance(getApplicationContext());
        final SharedPreferences mPref = getSharedPreferences("Mypref", MODE_PRIVATE);

        //----------------------------- View -------------------------------------------------------


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        final TextView SelectName = (TextView) findViewById(R.id.selectname);
        final TextView SelectBirth = (TextView) findViewById(R.id.selectbirth);
        SelectDate = (TextView) findViewById(R.id.selectdate);
        SelectTime = (TextView) findViewById(R.id.selecttime);
        final EditText WriteMemo = (EditText) findViewById(R.id.specialtext);
        final TextView DefaultName = (TextView) findViewById(R.id.defaultname);
        final TextView DefaultBirth = (TextView) findViewById(R.id.defaultbirth);
        TextView DefaultDate = (TextView) findViewById(R.id.defaultdate);
        TextView DefaultTime = (TextView) findViewById(R.id.defaulttime);

        Button RequestButton = (Button) findViewById(R.id.requestbutton);
        Button BackButton = (Button) findViewById(R.id.backbutton);
        WriteMemo.setText("메모");

    //-----------------------------------------------------------------------------------------------

        final Calendar Today = Calendar.getInstance();
        DefaultDate.setText(Today.get(Calendar.YEAR)+"년" + " " +(Today.get(Calendar.MONTH)+1)+"월"+ " " + Today.get(Calendar.DATE)+"일");
        DefaultTime.setText(Today.get(Calendar.HOUR_OF_DAY) + "시");

        HourOfDay = String.valueOf(Today.get(Calendar.HOUR_OF_DAY));
        Minute = String.valueOf(Today.get(Calendar.MINUTE));
        Year = String.valueOf(Today.get(Calendar.YEAR));
        MonthOfYear = String.valueOf((Today.get(Calendar.MONTH)+1));
        DayOfMonth = String.valueOf(Today.get(Calendar.DATE));

        SelectName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final CharSequence[] items = { "가족 등록을 해주세요", "최대 10명까지 가능합니다","","","",""};
                ArrayList<String> Name = new ArrayList<String>(dbManager.PrintName());
                int count = 0 ;
                for(int i = 1 ; i <= Name.size(); i ++)
                {items[count] = Name.get(count);count ++;}

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ReservationActivity.this);
                alertDialogBuilder.setTitle("선택 목록 대화 상자");
                alertDialogBuilder.setItems(items,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                SelectName.setText(items[id].toString());
                                dialog.dismiss();}});
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
        });


        SelectBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final CharSequence[] items = { "생년월일 등록을 해주세요", "최대 10명까지 가능합니다","","","",""};
                ArrayList<String> Birth = new ArrayList<String>(dbManager.PrintBirth());
                int count = 0 ;
                for(int i = 1 ; i <= Birth.size(); i ++)
                {items[count] = Birth.get(count);count ++;}

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ReservationActivity.this);
                alertDialogBuilder.setTitle("선택 목록 대화 상자");
                alertDialogBuilder.setItems(items,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {

                                // 프로그램을 종료한다
                                SelectBirth.setText(items[id]);
                                dialog.dismiss();
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        SelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DatePickerDialog(ReservationActivity.this, dateSetListener, Today.get(Calendar.YEAR), (Today.get(Calendar.MONTH)), Today.get(Calendar.DATE)).show();

            }
        });

        SelectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new TimePickerDialog(ReservationActivity.this, timeSetListener, Today.get(Calendar.HOUR_OF_DAY), Today.get(Calendar.MINUTE), false).show();
            }
        });


        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });


                RequestButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //여기서 sharepreper이 null 이 될수도 있으니 조건을 추가하는게 좋겠지
                        //근데 아예 인텐트를 받아와서 처리하면 이런문제가 안생기긴함.
                        String userID = mPref.getString("userID","");
                        String userPurpose = WriteMemo.getText().toString();

                        String userName = SelectName.getText().toString();
                        String userBirth = SelectBirth.getText().toString();
                        String reservationDate = SelectDate.getText().toString();
                        String reservationTime = SelectTime.getText().toString();

                        if (userName.equals("이름선택")) {

                            userName = DefaultName.getText().toString();
                        }
                        if(userBirth.equals("생년월일")){

                            userBirth = DefaultBirth.getText().toString();
                        }

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonresponse = new JSONObject(response);
                            boolean success = jsonresponse.getBoolean("success");

                            if (success) {

                                AlertDialog.Builder builder = new AlertDialog.Builder(ReservationActivity.this);
                                dialog = builder.setMessage("예약완료!")
                                        .setPositiveButton("네", null)
                                        .create();
                                dialog.show();
                                MyBroadCastReceiver.isReservationFinished = false ;
                                finish();

                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ReservationActivity.this);
                                dialog = builder.setMessage("예약실패")
                                        .setNegativeButton("yes", null)
                                        .create();
                                dialog.show();

                            }
                        } catch (Exception e) {e.printStackTrace();}
                    }
                };

                mRequestQueue.addRequest(Constants.POST_REQUEST_URLS.ADD_RESERVATION, responseListener, null,
                        userID, userPurpose, Year, MonthOfYear, DayOfMonth, HourOfDay,userName,userBirth,Minute);
            }
         });
    }

            private TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {

                @Override

                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                    HourOfDay = String.valueOf(hourOfDay);
                    Minute = String.valueOf(minute);
                    SelectTime.setText(HourOfDay + "시" + Minute + "분");
                }
            };

            private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

                @Override

                public void onDateSet(DatePicker view, int year, int monthOfYear,

                                      int dayOfMonth) {

                    Year = String.valueOf(year);
                    MonthOfYear = String.valueOf(monthOfYear+1);
                    DayOfMonth = String.valueOf(dayOfMonth);
                    SelectDate.setText(Year + "년" + " " + MonthOfYear + "월" + " " + DayOfMonth + "일");
                }
            };
}
