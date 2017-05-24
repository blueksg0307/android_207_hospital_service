package com.example.administrator.project207.Activity;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.administrator.project207.R;
import com.example.administrator.project207.utils.ReserveRequest;

import org.json.JSONObject;


public class ReserveActivity extends AppCompatActivity {

    private String userID ;
    private String userName;
    private String userBirth;
    private String userPurpose;
    private AlertDialog dialog;
    private boolean success ;
    private ArrayAdapter diesaeAdapter;
    private Spinner diesaeSpinner;
    private String reserved_time;
    private String reserved_year;
    private String reserved_date;
    private String reserved_month;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve);

        final EditText idText = (EditText) findViewById(R.id.idText);
        final EditText nameText = (EditText) findViewById(R.id.nametext);
        final EditText birthText = (EditText) findViewById(R.id.birthtext);
        diesaeSpinner = (Spinner) findViewById(R.id.Disease_spinner);
        diesaeAdapter = ArrayAdapter.createFromResource(this, R.array.disease, android.R.layout.simple_spinner_dropdown_item);
        diesaeSpinner.setAdapter(diesaeAdapter);

        final Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        userName = intent.getStringExtra("userName");
        userBirth = intent.getStringExtra("userBirth");
        reserved_time = intent.getStringExtra("reserved_time");
        reserved_date = String.valueOf(intent.getIntExtra("reserved_date",-1));
        reserved_month = String.valueOf(intent.getIntExtra("reserved_month",-1));
        reserved_year = String.valueOf(intent.getIntExtra("reserved_year",-1));



        idText.setText(userID);
        idText.setBackgroundColor(getResources().getColor(R.color.colorGray));
        idText.setEnabled(false);
        nameText.setText(userName);
        birthText.setText(userBirth);


        Button reservationButton = (Button) findViewById(R.id.reservation);
        Button setDateButton = (Button) findViewById(R.id.setdate);

        setDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent setdate = new Intent(ReserveActivity.this, SetDateActivity.class);
                setdate.putExtra("userID",userID);
                setdate.putExtra("userName",userName);
                setdate.putExtra("userPurpose",userPurpose);
                setdate.putExtra("userBirth", userBirth);
                ReserveActivity.this.startActivity(setdate);
                finish();

            }
        });


        reservationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userID = intent.getStringExtra("userID");

                if(userName.equals("")){
                 Toast.makeText(ReserveActivity.this, "진료받으실분의 이름을 입력해주세요", Toast.LENGTH_SHORT).show();}

                if(userBirth.equals("")){
                    Toast.makeText(ReserveActivity.this, "진료받으실분의 생일을 입력해주세요", Toast.LENGTH_SHORT).show();}

                if(reserved_time.equals("")){
                    Toast.makeText(ReserveActivity.this, "진료받으실 시간을 선택해주세요", Toast.LENGTH_SHORT).show();}

                if(reserved_year.equals("") | reserved_date.equals("") | reserved_month.equals("")){
                    Toast.makeText(ReserveActivity.this, "진료받으실 날짜을 선택해주세요", Toast.LENGTH_SHORT).show();}

                userPurpose = diesaeSpinner.getSelectedItem().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonresponse = new JSONObject(response);
                            success = jsonresponse.getBoolean("success");


                            if (success) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ReserveActivity.this);
                                dialog = builder.setMessage("예약완료!")
                                        .setPositiveButton("네", null)
                                        .create();
                                dialog.show();
                                finish();

                            }
                            else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ReserveActivity.this);
                                dialog = builder.setMessage("예약실패")
                                        .setNegativeButton("yes", null)
                                        .create();
                                dialog.show();
                            }
                        }
                        catch (Exception e){

                            e.printStackTrace();
                        }
                    }
                };
                ReserveRequest reserveRequest = new ReserveRequest(userID, userPurpose, reserved_year, reserved_month, reserved_date, reserved_time, responseListener);
                RequestQueue queue = Volley.newRequestQueue(ReserveActivity.this);
                queue.add(reserveRequest);

            }
        });

    }
    @Override
    protected void onStart(){
        super.onStart();

    }


    @Override
    protected void onStop(){
        super.onStop();
        if(dialog != null)
        {
            dialog.dismiss();
            dialog = null ;
        }
    }

    private long lastTimeBackPressed;
    @Override
    public void onBackPressed() {
        if(System.currentTimeMillis() - lastTimeBackPressed < 1500)
        {
            finish();
            return;
        }
        Toast.makeText(this, "’뒤로’ 버튼을 한번 더 누르면 종료합니다.." ,Toast.LENGTH_SHORT).show();
        lastTimeBackPressed = System.currentTimeMillis();
    }

}
