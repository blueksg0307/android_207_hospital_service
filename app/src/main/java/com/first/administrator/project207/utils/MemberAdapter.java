package com.first.administrator.project207.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.first.administrator.project207.R;
import com.first.administrator.project207.activities.MyinfoActivity;

import java.util.List;

/**
 * Created by Administrator on 2017-04-05.
 */

public class MemberAdapter extends BaseAdapter {

    private Context context;
    private List<Member> userList;

    public MemberAdapter(Context context, List<Member> userList) {

        this.context = context;
        this.userList = userList;

    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public Object getItem(int i) {
        return userList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        View v = View.inflate(context, R.layout.listview_member, null);
        TextView username = (TextView) v.findViewById(R.id.book_name);
        TextView wantDate = (TextView) v.findViewById(R.id.book_date);
        username.setText("이름:" + userList.get(i).getUserName());
        wantDate.setText("생년월일: " + userList.get(i).getUserBirth());

    return v;
    }
}


