package com.example.administrator.project207.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.administrator.project207.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class ChartAcitivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_acitivity);

        LineChart chart = (LineChart) findViewById(R.id.chart);

        List<Entry> entries = new ArrayList<Entry>();



        entries.add(new Entry(4f, 0));
        entries.add(new Entry(8f, 1));
        entries.add(new Entry(12f, 2));
        entries.add(new Entry(15f, 3));
        entries.add(new Entry(18f, 4));
        entries.add(new Entry(2f, 5));
        LineDataSet dataSet = new LineDataSet(entries, "방문자수"); // add entries to dataset
        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
    }
}
