package com.example.administrator.project207.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.example.administrator.project207.R;

/**
 * Created by Administrator on 2017-05-25.
 */

public class Pop extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop);


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * 0.9), (int) (height * 0.75));
    }
}
