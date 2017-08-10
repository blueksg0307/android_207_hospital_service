package com.first.administrator.project207.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.first.administrator.project207.R;
import com.first.administrator.project207.utils.Constants;
import com.first.administrator.project207.utils.ServerRequestQueue;

import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    private ServerRequestQueue mRequestQueue;

    private String FileName = "MyFile";
    private String userID;
    private String userPassword;
    private String userName;
    private String userBirth;
    private String userPhone;
    private AlertDialog dialog;
    private boolean validate = false;
    private boolean success ;
    private EditText idText ;
    private EditText passwordText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mRequestQueue = ServerRequestQueue.getInstance(getApplicationContext());

        final EditText idText = (EditText) findViewById(R.id.idText);
        final EditText passwordText = (EditText) findViewById(R.id.passwordText);
        final EditText nameText = (EditText) findViewById(R.id.nameText);
        final EditText birthText = (EditText) findViewById(R.id.birthText);
        final EditText passwordCheck = (EditText) findViewById(R.id.emailText);
        final EditText phoneText = (EditText) findViewById(R.id.phoneText);

        final Button validateButton = (Button) findViewById(R.id.validateButton);
        final Button registerButton = (Button) findViewById(R.id.registerButton);
        final Button cancelButton = (Button) findViewById(R.id.cancelButton);




        validateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String userID = idText.getText().toString();

                if (validate) {
                    return;
                }


                if (userID.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setMessage("아이디를 입력해주세요.")
                            .setPositiveButton("yes", null)
                            .create();
                    dialog.show();
                    return;
                }

                Response.Listener<String> responseListener = new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {


                           JSONObject jsonresponse = new JSONObject(response);
                            boolean success = jsonresponse.getBoolean("success");

                            if (success) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                dialog = builder.setMessage("사용가능합니다.")
                                        .setPositiveButton("yes", null)
                                        .create();
                                dialog.show();
                                idText.setEnabled(false);
                                validate = true;
                                idText.setBackgroundColor(getResources().getColor(R.color.colorGray));
                                validateButton.setBackgroundColor(getResources().getColor(R.color.colorGray));
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                dialog = builder.setMessage("같은 아이디가 이미 존재합니다.")
                                        .setNegativeButton("yes", null)
                                        .create();
                                dialog.show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };

                mRequestQueue.addRequest(Constants.POST_REQUEST_URLS.REGISTER_VALIDATE, responseListener, null, userID);
            }
        });


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                final String userID = idText.getText().toString();
                final String userPassword = passwordText.getText().toString();
                final String userName = nameText.getText().toString();
                final String userBirth = birthText.getText().toString();
                final String userPhone = phoneText.getText().toString();
                final String PasswordCheck = passwordCheck.getText().toString();

                if(!(userPassword.equals(PasswordCheck))){

                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setMessage("비밀번호가 서로 같지 않습니다. 다시 확인해주세요")
                            .create();
                    dialog.show();

                }
                if(userBirth.length() > 6)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setMessage("생년월일은 앞자리 6개만 공백없이 입력해주세요")
                            .create();
                    dialog.show();

                }

                if (!validate) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setMessage("중복확인 버튼을 눌러주세요")
                            .create();
                    dialog.show();
                    return;

                }

                if (userID.equals("") || userPassword.equals("") || userName.equals("") || userBirth.equals("")  || userPhone.equals("") || passwordCheck.equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setMessage("모든 정보를 기입해주세요")
                            .create();
                    dialog.show();
                    return;
                }

                Response.Listener<String> responseListener = new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonresponse = new JSONObject(response);
                            boolean success = jsonresponse.getBoolean("success");


                            if (success) {


                                SharedPreferences sharedPreferences = getSharedPreferences("Mypref", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("userID",userID);
                                editor.putString("userPassword",userPassword);
                                editor.putString("userName",userName);
                                editor.putString("userBirth",userBirth);
                                editor.commit();
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                dialog = builder.setMessage("가입완료")
                                        .setPositiveButton("네", null)
                                        .create();
                                 dialog.show();

                                finish();


                            }

                            else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                dialog = builder.setMessage("가입실패")
                                        .setNegativeButton("yes", null)
                                        .create();
                                dialog.show();
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                };

                mRequestQueue.addRequest(Constants.POST_REQUEST_URLS.Register, responseListener, null, userID, userPassword, userName, userBirth, userPhone);
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