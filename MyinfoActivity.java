package com.first.administrator.project207.activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.first.administrator.project207.R;
import com.first.administrator.project207.utils.CheckHistoryUser;
import com.first.administrator.project207.utils.CheckReservationUser;
import com.first.administrator.project207.utils.Constants;
import com.first.administrator.project207.utils.Member;
import com.first.administrator.project207.utils.MemberAdapter;
import com.first.administrator.project207.utils.MyDatabaseManager;
import com.first.administrator.project207.utils.ServerRequestQueue;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyinfoActivity extends AppCompatActivity {

    private ServerRequestQueue mRequestQueue;
    private SharedPreferences sharedPreferences ;
    private ListView MemberListView ;
    private List<Member> memberList ;
    private MemberAdapter memberAdapter;
    private Member member;
    private String userID;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (item.getItemId() == android.R.id.home){

            Intent Before = new Intent(MyinfoActivity.this, MainActivity.class);
            startActivity(Before);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo);

        mRequestQueue = ServerRequestQueue.getInstance(getApplicationContext());
        sharedPreferences = getSharedPreferences("Mypref",MODE_PRIVATE);
        final MyDatabaseManager dbManager = new MyDatabaseManager(getApplicationContext(), "sample.db", null, 1);


        //-----------Shape of Configuration Toolbar-------------------------------------------------
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.button_back);

        //-----------Shape of Configuration Toolbar-------------------------------------------------

        TextView birthText = (TextView) findViewById(R.id.MyBirth);
        TextView nameText = (TextView) findViewById(R.id.MyName);
        Button addMemberButton = (Button) findViewById(R.id.AddMember);
        Button editMyinfobutton = (Button) findViewById(R.id.changedMe);
        Button EditMemberButton = (Button) findViewById(R.id.editMember);
        Button DeleteMemberButton = (Button) findViewById(R.id.deleteMember);

        birthText.setText(sharedPreferences.getString("userBirth",""));
        nameText.setText(sharedPreferences.getString("userName",""));

        String faminfo = new String(dbManager.PrintData().toString());
        MemberListView = (ListView) findViewById(R.id.memberListView);
        memberList = new ArrayList<Member>();
        memberAdapter = new MemberAdapter(getApplicationContext(), memberList);
        MemberListView.setAdapter(memberAdapter);

        memberList.clear();
        int count = 0 ;
        ArrayList<String> famName = new ArrayList<String>(dbManager.PrintName());
        ArrayList<String> famBirth = new ArrayList<String>(dbManager.PrintBirth());

        for (int i = 1; i <= famName.size(); i++){

            member = new Member(count,famName.get(count),famBirth.get(count));
            memberList.add(member);
            count ++ ;
        }


        memberAdapter.notifyDataSetChanged();

        //가족추가하기에 대한 이벤트//
        addMemberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Context context = MyinfoActivity.this;
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);

                final EditText NameBox = new EditText(context);
                NameBox.setHint("가족이름 입력");
                layout.addView(NameBox);

                final EditText BirthBox = new EditText(context);
                BirthBox.setHint("생년월일을 6자리 입력 ex)820328 ");
                layout.addView(BirthBox);

                AlertDialog.Builder EditMyMember = new AlertDialog.Builder(MyinfoActivity.this);
                EditMyMember.setTitle("가족추가");
                EditMyMember.setView(layout);
                EditMyMember.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = NameBox.getText().toString();
                        String birth = BirthBox.getText().toString();
                        try {

                            dbManager.insert("insert into FamilyList values(null, '" + name + "', " + birth + ");");
                            Toast.makeText(MyinfoActivity.this,name+"님이 등록되었습니다",Toast.LENGTH_LONG).show();


                        }catch (Exception e){e.printStackTrace();}
                        dialog.dismiss();
                    }});
                // 취소 버튼 설정
                EditMyMember.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();     //닫기
                        // Event
                    }
                });
                EditMyMember.show();}});

        //가족정보 삭제하기
        DeleteMemberButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Context context = MyinfoActivity.this;
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);

                final EditText NameBox = new EditText(context);
                NameBox.setHint("삭제할 가족이름을 입력해주세요");
                layout.addView(NameBox);

                AlertDialog.Builder EditMyMember = new AlertDialog.Builder(MyinfoActivity.this);
                EditMyMember.setTitle("가족삭제");
                EditMyMember.setMessage("삭제할 가족의 정보를 입력해주세요");
                EditMyMember.setView(layout);

                EditMyMember.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = NameBox.getText().toString();

                        try {

                            dbManager.delete("delete from FamilyList where name = '" + name + "';");
                            Toast.makeText(MyinfoActivity.this,name+"님이 삭제되었습니다",Toast.LENGTH_LONG).show();


                        }catch (Exception e){e.printStackTrace();}
                        dialog.dismiss();     //닫기
                    }
                });
                // 취소 버튼 설정
                EditMyMember.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();}});EditMyMember.show();}});

        //가족정보 변경하기
        EditMemberButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Context context = MyinfoActivity.this;
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);

                final EditText NameBox = new EditText(context);
                NameBox.setHint("변경할 가족 이름을 입력");
                layout.addView(NameBox);

                final EditText BirthBox = new EditText(context);
                BirthBox.setHint("새로운 생년월일을 입력");

                layout.addView(BirthBox);

                AlertDialog.Builder EditMyMember = new AlertDialog.Builder(MyinfoActivity.this);
                EditMyMember.setTitle("가족 정보 변경");
                EditMyMember.setView(layout);


                EditMyMember.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = NameBox.getText().toString();
                        String birth = BirthBox.getText().toString();

                        try {

                            dbManager.update("update FamilyList set birth = " + birth + " where name = '" + name + "';");
                            Toast.makeText(MyinfoActivity.this,name+"님의 정보가 변경되었습니다",Toast.LENGTH_LONG).show();


                        }catch (Exception e){e.printStackTrace();}
                        dialog.dismiss();     //닫기
                    }
                });
                // 취소 버튼 설정
                EditMyMember.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();}});EditMyMember.show();}});



        //내 정보 변경하기에 대한 이벤트//
        editMyinfobutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Context context = MyinfoActivity.this;
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);

                final EditText NameBox = new EditText(context);
                NameBox.setHint("바꾸실 이름을 입력해주세요");
                layout.addView(NameBox);

                final EditText BirthBox = new EditText(context);
                BirthBox.setHint("바꾸실 생년월일");
                layout.addView(BirthBox);

                AlertDialog.Builder EditMyself = new AlertDialog.Builder(MyinfoActivity.this);
                EditMyself.setTitle("내 정보 바꾸기");
                EditMyself.setMessage("새로운 정보를 입력해주세요");
                EditMyself.setView(layout);


                EditMyself.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                userID = sharedPreferences.getString("userID","");
                                final String userName = NameBox.getText().toString();
                                final String userBirth = BirthBox.getText().toString();


                                Response.Listener<String> responseListener = new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        AlertDialog.Builder builder = new AlertDialog.Builder(MyinfoActivity.this);

                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            boolean success = jsonObject.getBoolean("success");
                                            if (success) {
                                                builder.setMessage("성공적으로 변경이 완료되었습니다").create().show();
                                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                                editor.putString("userName", userName);
                                                editor.putString("userBirth",userBirth );
                                                editor.commit();

                                            } else builder.setMessage("변경이 실패했습니다.").create().show();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                };
                                mRequestQueue.addRequest(Constants.POST_REQUEST_URLS.ACCOUNT_EDIT, responseListener, null, userID, userName, userBirth);
                            }
                        });
                // 취소 버튼 설정
                EditMyself.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();     //닫기
                    }});EditMyself.show();}});}

    @Override
    protected void onStop() {
        super.onStop();

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
