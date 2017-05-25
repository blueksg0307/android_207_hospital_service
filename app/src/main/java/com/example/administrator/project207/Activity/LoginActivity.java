package com.example.administrator.project207.Activity;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.administrator.project207.R;
import com.example.administrator.project207.utils.LoginRequest;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private AlertDialog dialog ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        startActivity(new Intent(this,SplashActivity.class));
        TextView signupButton = (TextView) findViewById(R.id.signupButton);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent Intent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(Intent);

            }
        });

        final EditText idText = (EditText) findViewById(R.id.idText);
        final EditText passwordText = (EditText) findViewById(R.id.passwordText);
        final Button loginButton = (Button) findViewById(R.id.loginButton);
        final TextView AppInfo = (TextView) findViewById(R.id.AppInfo);

        AppInfo.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginActivity.this,Pop.class);
                startActivity(intent);

            }
        });


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String userID = idText.getText().toString();
                final String userPassword = passwordText.getText().toString();
                final String isphone = ("1");

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
                                Intent.putExtra("userName", userName);
                                Intent.putExtra("userBirth", userBirth);
                                Intent.putExtra("userNumber", userNumber);
                                LoginActivity.this.startActivity(Intent);
                                finish();

                            }
                            else {

                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                dialog = builder.setMessage("로그인에 실패하셨습니다. 아이디 , 비밀번호를 확인해주세요")
                                        .setNegativeButton("확인", null)
                                        .create();
                                dialog.show();}
                        } catch (Exception e) {

                            e.printStackTrace();}
                    }
                };
                LoginRequest loginRequest = new LoginRequest(userID, userPassword, isphone, responseListener);
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(loginRequest);
            }
        });
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
        Toast.makeText(this, "’뒤로’ 버튼을 한번 더 누르면 종료합니다." ,Toast.LENGTH_SHORT).show();
        lastTimeBackPressed = System.currentTimeMillis();
    }
}