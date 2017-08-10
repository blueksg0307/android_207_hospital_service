package com.first.administrator.project207.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.first.administrator.project207.R;
import com.first.administrator.project207.utils.Constants;
import com.first.administrator.project207.utils.ServerRequestQueue;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private String LoginID;
    private String LoginPW;
    private ServerRequestQueue mRequestQueue;
    private String userPW;
    private String userNumber;
    private String userBirth;
    private String userName;
    private String isphone = ("1");
    private String userID ;
    private String userPassword;
    private Boolean autoFlag = true ;
    private Boolean splashFlag = true ;
    private JSONObject checkReserction ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences sharedPreferences = getSharedPreferences("Mypref", MODE_PRIVATE);
        mRequestQueue = ServerRequestQueue.getInstance(getApplicationContext());
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);

        Intent FlagIntent = getIntent();
        autoFlag = FlagIntent.getBooleanExtra("AutoLoginFlag",true);
        splashFlag = FlagIntent.getBooleanExtra("SplashFlag", true);
        if(splashFlag) {startActivity(new Intent(this, SplashActivity.class));}

        final EditText idText = (EditText) findViewById(R.id.idText);
        final EditText passwordText = (EditText) findViewById(R.id.passwordText);
        final TextView signupButton = (TextView) findViewById(R.id.signupButton);
        final Button loginButton = (Button) findViewById(R.id.loginButton);


    if(autoFlag) {
        if (!(sharedPreferences.getString("userID", "").equals("") && !(sharedPreferences.getString("userPassword", "").equals("")))) {

            userID = sharedPreferences.getString("userID", "");
            userPassword = sharedPreferences.getString("userPassword", "");

            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {

                        JSONObject jsonresponse = new JSONObject(response);
                        boolean success = jsonresponse.getBoolean("success");

                        if (success) {

                            String userName = jsonresponse.getString("name");
                            String userBirth = jsonresponse.getString("birth");
                            String userNumber = jsonresponse.getString("phone");
                            Intent Intent = new Intent(LoginActivity.this, MainActivity.class);
                            Intent.putExtra("userID", userID);
                            Intent.putExtra("userPassword", userPassword);
                            Intent.putExtra("userName", userName);
                            Intent.putExtra("userBirth", userBirth);
                            Intent.putExtra("userNumber", userNumber);
                            LoginActivity.this.startActivity(Intent);
                            finish();

                        }} catch (Exception e) {e.printStackTrace();}
                }
            };mRequestQueue.addRequest(Constants.POST_REQUEST_URLS.LOGIN, responseListener, null, userID, userPassword, isphone);}}

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               Intent Intent = new Intent(LoginActivity.this, RegisterActivity.class);
               LoginActivity.this.startActivity(Intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String userID = idText.getText().toString();
                final String userPassword = passwordText.getText().toString();

                Response.Listener<String> LoginResponseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonresponse = new JSONObject(response);
                            boolean success = jsonresponse.getBoolean("success");
                            if (success) {

                                userName = jsonresponse.getString("name");
                                userBirth = jsonresponse.getString("birth");
                                userNumber = jsonresponse.getString("phone");
                                SharedPreferences sharedPreferences = getSharedPreferences("Mypref", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("userID",userID);
                                editor.putString("userPassword",userPassword);
                                editor.putString("userName",userName);
                                editor.putString("userBirth",userBirth);
                                editor.commit();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("userID", userID);
                                intent.putExtra("userName", userName);
                                intent.putExtra("userBirth", userBirth);
                                intent.putExtra("userNumber", userNumber);
                                startActivity(intent);
                                finish();

                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                builder.setMessage("로그인에 실패하셨습니다. 아이디 , 비밀번호를 확인해주세요")
                                        .setNegativeButton("확인", null)
                                        .create()
                                        .show();
                            }
                        } catch (Exception e) {e.printStackTrace();}}
                };
                mRequestQueue.addRequest(Constants.POST_REQUEST_URLS.LOGIN, LoginResponseListener, null, userID, userPassword, isphone);
            }
        });
    }
    @Override
    protected void onStop() {super.onStop();}

    private long lastTimeBackPressed;
    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - lastTimeBackPressed < 1500) {finish();return;}
        Toast.makeText(this, "’뒤로’ 버튼을 한번 더 누르면 종료합니다.", Toast.LENGTH_SHORT).show();
        lastTimeBackPressed = System.currentTimeMillis();
    }
}