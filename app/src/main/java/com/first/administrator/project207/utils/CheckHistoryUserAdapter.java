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

public class CheckHistoryUserAdapter extends BaseAdapter {

    private Context context;
    private List<CheckHistoryUser> userList;

    public CheckHistoryUserAdapter(Context context, List<CheckHistoryUser> userList){

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

        View v = View.inflate(context, R.layout.listview_checkmyhistory, null);

        TextView username =(TextView) v.findViewById(R.id.book_name);
        TextView wantDate =(TextView) v.findViewById(R.id.book_date);

        username.setText(userList.get(i).getUserName());
        wantDate.setText(userList.get(i).getBookDate());


        return v;
    }
}
