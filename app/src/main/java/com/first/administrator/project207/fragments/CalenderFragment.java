package com.first.administrator.project207.fragments;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ListView;

import com.android.volley.Response;
import com.first.administrator.project207.utils.CheckHistoryUser;
import com.first.administrator.project207.utils.CheckHistoryUserAdapter;
import com.first.administrator.project207.utils.CheckReservationUser;
import com.first.administrator.project207.utils.CheckReservationUserAdapter;
import com.first.administrator.project207.R;
import com.first.administrator.project207.utils.Constants;
import com.first.administrator.project207.utils.ServerRequestQueue;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CalenderFragment extends Fragment {

    private ListView ReserveListView ;
    private ListView HistoryListView ;

    private CheckHistoryUser checkHistoryUser;
    private CheckReservationUser checkReservationUser;

    private CheckReservationUserAdapter checkReservationUserAdapter;
    private CheckHistoryUserAdapter checkHistoryUserAdapter;

    private List<CheckReservationUser> userList;
    private List<CheckHistoryUser> userList2;

    private ServerRequestQueue mRequestQueue;
    private String myValue ;
    private CalendarView calendarView;
    private String userID ;
    private SharedPreferences mPref;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    public CalenderFragment() {
        // Required empty public constructor

    }

    public static CalenderFragment newInstance(String param1, String param2) {
        CalenderFragment fragment = new CalenderFragment();
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

        userID = getArguments().getString("userID");
        mRequestQueue = ServerRequestQueue.getInstance(getActivity());




    }

    @Override
    public void onActivityCreated(Bundle b) {
        super.onActivityCreated(b);

        calendarView = (CalendarView) getView().findViewById(R.id.calendarView);
        ReserveListView = (ListView) getView().findViewById(R.id.UserListView1);
        HistoryListView = (ListView) getView().findViewById(R.id.UserListView2) ;
        userList = new ArrayList<CheckReservationUser>();
        userList2 = new ArrayList<CheckHistoryUser>();
        checkReservationUserAdapter = new CheckReservationUserAdapter(getActivity(), userList);
        checkHistoryUserAdapter = new CheckHistoryUserAdapter(getActivity(), userList2);
        ReserveListView.setAdapter(checkReservationUserAdapter);
        HistoryListView.setAdapter(checkHistoryUserAdapter);

        new CheckReserMyReservation().execute();
        new CheckHistory().execute();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        Bundle bundle = this.getArguments();
        myValue = bundle.getString("userList");

        return inflater.inflate(R.layout.fragment_reserve, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
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

    class CheckReserMyReservation extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {return null;}

        @Override
        public void onPostExecute(String result) {

            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {

                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("response");
                        int count  = 0 ;


                            while (count < jsonArray.length()) {
                                JSONObject object = jsonArray.getJSONObject(count);

                                int usercount = count + 1;
                                String bookDate = object.getString("reservationtime");
                                String Purpose = object.getString("purpose");
                                String  userName = object.getString("id");

                                checkReservationUser = new CheckReservationUser(usercount, userName, bookDate, Purpose);
                                userList.add(checkReservationUser);
                                count++;
                            }
                            checkReservationUserAdapter.notifyDataSetChanged();
                        }
                        catch (Exception e){e.printStackTrace();}
                    }


            };
            mRequestQueue.addRequest(Constants.POST_REQUEST_URLS.CHECK_RESERVATION, responseListener, null, userID);}
    }

    class CheckHistory extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {return null;}

        @Override
        public void onPostExecute(String result) {

            Response.Listener<String> responseListener2 = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {

                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("response");
                        int count  = 0 ;

                        if (jsonArray.length() == count){

                            String bookDate = "진료기록이 없습니다";
                            String Purpose = "";
                            String userName = "";
                            int usercount = 0 ;

                            checkHistoryUser = new CheckHistoryUser(usercount, userName, bookDate, Purpose);
                            userList2.add(checkHistoryUser);
                        }
                        else {
                            while (count < jsonArray.length()) {
                                JSONObject object = jsonArray.getJSONObject(count);
                                int usercount = count + 1;
                                String bookDate = object.getString("wanttime");
                                String Purpose = object.getString("purpose");
                                String userName = userID;
                                checkHistoryUser = new CheckHistoryUser(usercount, userName, bookDate, Purpose);
                                userList2.add(checkHistoryUser);
                                count++;
                            }
                        }
                        checkHistoryUserAdapter.notifyDataSetChanged();
                    }
                    catch (Exception e){

                        e.printStackTrace();
                    }
                }


            };mRequestQueue.addRequest(Constants.POST_REQUEST_URLS.GET_HISTORY, responseListener2, null, userID);}
    }
}

