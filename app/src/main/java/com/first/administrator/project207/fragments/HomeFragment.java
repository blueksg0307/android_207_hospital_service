package com.first.administrator.project207.fragments;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.first.administrator.project207.R;
import com.first.administrator.project207.Service.BeaconConnect;
import com.first.administrator.project207.activities.MainActivity;
import com.first.administrator.project207.activities.ReservationActivity;
import com.first.administrator.project207.utils.Constants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HomeFragment extends Fragment {

    private TextView WaitingCount   ;
    private TextView WaitingTime    ;
    private boolean CheckBeaconis ;
    SQLiteDatabase db;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public HomeFragment() {

    }
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(Bundle b) {
        super.onActivityCreated(b);
        new WaitingBackgroundTask().execute();
        WaitingCount = (TextView) getView().findViewById(R.id.count);
        WaitingTime = (TextView) getView().findViewById(R.id.waitingtime);
        Button ReservationOnSpot = (Button) getView().findViewById(R.id.rightnow);


        ReservationOnSpot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(BeaconConnect.isBeaconChecked){

                    Intent intent = new Intent(getActivity(), ReservationActivity.class);
                    startActivity(intent);

                }
                else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("병원 내에서만 가능합니다. + 버튼으로 예약을 이용하세요")
                            .create().show();
                }
            }
        });
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    class WaitingBackgroundTask extends AsyncTask<Void, Void, String> {

        String target = Constants.POST_REQUEST_URLS.GET_WAITING;

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(target);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;
                StringBuilder stringBuilder = new StringBuilder();
                while ((temp = bufferedReader.readLine()) != null) {
                    stringBuilder.append(temp + "\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        public void onPostExecute(String result) {

            int usercount  = 0 ;

            try {

                JSONObject jsonObject = new JSONObject(result);
                JSONArray  jsonArray = jsonObject.getJSONArray("response");
                usercount = jsonArray.length();

            } catch (Exception e) {
                e.printStackTrace();
            }
            WaitingCount.setText(String.valueOf(usercount)+"명");
            WaitingTime.setText(String.valueOf(usercount*5.5)+"분");
        }
    }
}
