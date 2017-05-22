package com.example.administrator.project207;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

public class SetDateActivity extends AppCompatActivity {

    private Spinner spinner;
    private ArrayAdapter adapter;
    private DatePicker datePicker;
    private int reserved_year;
    private int reserved_date;
    private int reserved_month;
    private String reserved_time;
    private String userID ;
    private String userName;
    private String userBirth;
    private String userPurpose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_date);

        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        userName = intent.getStringExtra("userName");
        userPurpose = intent.getStringExtra("userPurpose");
        userBirth = intent.getStringExtra("userBirth");

        spinner = (Spinner) findViewById(R.id.spinner);
        adapter = ArrayAdapter.createFromResource(this, R.array.time, android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        Button button = (Button) findViewById(R.id.button);

        datePicker = (DatePicker) findViewById(R.id.datePicker);
        datePicker.init(


                datePicker.getYear(),
                datePicker.getMonth(),
                datePicker.getDayOfMonth(),

                new DatePicker.OnDateChangedListener(){

                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        reserved_date = dayOfMonth;
                        reserved_month = monthOfYear+1;
                        reserved_year = year ;

                        String msg = String.format("선택하신 날짜는 %d / %d / %d 입니다.", reserved_year,reserved_month,reserved_date);
                        Toast.makeText(SetDateActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                reserved_time = spinner.getSelectedItem().toString();
                Toast.makeText(SetDateActivity.this, reserved_time, Toast.LENGTH_SHORT).show();
                Intent Intent = new Intent(SetDateActivity.this, ReserveActivity.class);
                Intent.putExtra("reserved_year",reserved_year);
                Intent.putExtra("reserved_time", reserved_time);
                Intent.putExtra("reserved_date", reserved_date);
                Intent.putExtra("reserved_month", reserved_month);
                Intent.putExtra("userID", userID);
                Intent.putExtra("userName", userName);
                Intent.putExtra("userPurpose", userPurpose);
                Intent.putExtra("userBirth", userBirth);
                SetDateActivity.this.startActivity(Intent);
                finish();
            }
        });
    }
}
