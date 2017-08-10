package com.first.administrator.project207.utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.first.administrator.project207.R;

import java.util.List;

/**
 * Created by Administrator on 2017-04-05.
 */

public class ReservationListAdapter extends BaseAdapter {

    private Context context;
    private List<User> userList;

    public ReservationListAdapter(Context context, List<User> userList){

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

        View v = View.inflate(context, R.layout.reservationuser, null);

        TextView userCount =(TextView) v.findViewById(R.id.usercount);
        TextView username =(TextView) v.findViewById(R.id.userName);
        TextView wantDate =(TextView) v.findViewById(R.id.book_date);
        
        userCount.setText(userList.get(i).getUserCount()+"");
        username.setText("이름 : " + userList.get(i).getUserName());
        wantDate.setText(userList.get(i).getBookDate()+"시");

        if(userCount == null){

            wantDate.setText("진료대기중인 환자가 없습니다.");
        }

        v.setTag(userList.get(i).getUserCount());

        return v;
    }
}
