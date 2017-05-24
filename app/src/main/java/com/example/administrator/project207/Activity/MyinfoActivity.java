package com.example.administrator.project207.Activity;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.administrator.project207.R;
import com.example.administrator.project207.utils.MyinfoEditRequest;

import org.json.JSONObject;

public class MyinfoActivity extends AppCompatActivity {

    private AlertDialog dialog ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo);

        Button Editbutton = (Button) findViewById(R.id.editbutton);
        Button Finishbutton = (Button) findViewById(R.id.finishbutton);

        final EditText IDtext = (EditText) findViewById(R.id.idtext);
        final EditText Nametext = (EditText) findViewById(R.id.nametext);
        final EditText Birthtext = (EditText) findViewById(R.id.Birthtext);
        final EditText Phonetext = (EditText) findViewById(R.id.Phonetext);

        Intent intent = getIntent();
        String userID = intent.getStringExtra("userID");
        String userName = intent.getStringExtra("userName");
        String userBirth = intent.getStringExtra("userBirth");
        String userNumber = intent.getStringExtra("userNumber");


        IDtext.setText(userID);
        IDtext.setEnabled(false);
        IDtext.setBackgroundColor(getResources().getColor(R.color.colorGray));

        Nametext.setText(userName);
        Nametext.setEnabled(false);
        Nametext.setBackgroundColor(getResources().getColor(R.color.colorGray));

        Birthtext.setText(userBirth);
        Birthtext.setEnabled(false);
        Birthtext.setBackgroundColor(getResources().getColor(R.color.colorGray));

        Phonetext.setText(userNumber);
        Phonetext.setEnabled(false);
        Phonetext.setBackgroundColor(getResources().getColor(R.color.colorGray));


        Editbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder =  new AlertDialog.Builder(MyinfoActivity.this);
                builder.setMessage("정보를 수정하겠습니까?")
                        .setPositiveButton("확인",null);

                Nametext.setEnabled(true);
                Birthtext.setEnabled(true);
                Phonetext.setEnabled(true);

            }
        });

        Finishbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userID = IDtext.getText().toString();
                String userName = Nametext.getText().toString();
                String userBirth = Birthtext.getText().toString();
                String userNumber = Phonetext.getText().toString();

                      if(IDtext.equals("") | Nametext.equals("") | Birthtext.equals("") | Phonetext.equals("")){

                          AlertDialog.Builder builder =  new AlertDialog.Builder(MyinfoActivity.this);
                          builder.setMessage("빈칸없이 정보를 입력해주세요")
                                  .setPositiveButton("확인",null)
                                  .create()
                                  .show();

                      }

                Response.Listener responseListener = new Response.Listener<String>(){

                    @Override
                    public void onResponse(String response) {


                      try{
                          JSONObject jsonObject = new JSONObject(response);
                          boolean success = jsonObject.getBoolean("success");

                          if(success){

                              AlertDialog.Builder builder =  new AlertDialog.Builder(MyinfoActivity.this);
                              builder.setMessage("정보변경에 성공했습니다")
                                      .setPositiveButton("확인",null);
                              finish();

                          }
                          else
                          {
                              AlertDialog.Builder builder =  new AlertDialog.Builder(MyinfoActivity.this);
                              builder.setMessage("정보변경에 실패했습니다. 다시 확인해주세요")
                                      .setPositiveButton("확인",null);
                          }




                      }
                        catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                };


                MyinfoEditRequest myinfoEditRequest = new MyinfoEditRequest(userID, userName, userBirth, userNumber, responseListener); // userBirth and userNumber are needed.
                RequestQueue queue = Volley.newRequestQueue(MyinfoActivity.this);
                queue.add(myinfoEditRequest);
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;


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
        Toast.makeText(this, "’뒤로’ 버튼을 한번 더 누르면 종료합니다." ,Toast.LENGTH_SHORT).show();
        lastTimeBackPressed = System.currentTimeMillis();
    }
}
