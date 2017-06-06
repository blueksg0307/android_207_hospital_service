package com.example.administrator.project207.activities;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.example.administrator.project207.HistoryListAdapter;
import com.example.administrator.project207.R;
import com.example.administrator.project207.User;
import com.example.administrator.project207.UserListAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {


    private ListView listView;
    private HistoryListAdapter adapter;
    private List<User> userList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Intent intent = getIntent();
        listView = (ListView) findViewById(R.id.UserListView);
        userList = new ArrayList<User>();
        adapter = new HistoryListAdapter(HistoryActivity.this, userList);
        listView.setAdapter(adapter);


        try{

            JSONObject jsonObject = new JSONObject(intent.getStringExtra("userList"));
            JSONArray jsonArray = jsonObject.getJSONArray("response");
            String bookDate;
            String Purpose;
            int count = 0 ;

            while (count < jsonArray.length())
            {
                JSONObject object = jsonArray.getJSONObject(count);
                int usercount = count + 1;
                bookDate = object.getString("wanttime");
                Purpose = object.getString("purpose");
                User user = new User(usercount, bookDate, Purpose);
                userList.add(user);
                count++;
            }
            if(count ==0){
                AlertDialog dialog ;
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                dialog = builder.setMessage("진료 기록이 없습니다")
                                .setPositiveButton("확인", null)
                                .create();
                                dialog.show();
            }

            adapter.notifyDataSetChanged();

        }
        catch (Exception e){

            e.printStackTrace();
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
        Toast.makeText(this, "’뒤로’ 버튼을 한번 더 누르면 뒤로갑니다.." ,Toast.LENGTH_SHORT).show();
        lastTimeBackPressed = System.currentTimeMillis();
    }
}
