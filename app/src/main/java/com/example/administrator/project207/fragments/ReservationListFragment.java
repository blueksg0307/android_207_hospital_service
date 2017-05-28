package com.example.administrator.project207.fragments;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.administrator.project207.R;
import com.example.administrator.project207.User;
import com.example.administrator.project207.UserListAdapter;
import com.example.administrator.project207.utils.Constants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ReservationListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ReservationListFragment() {
        // Required empty public constructor
    }

    public static ReservationListFragment newInstance(String param1, String param2) {
        ReservationListFragment fragment = new ReservationListFragment();
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


    private ListView userListView ;
    private UserListAdapter adapter ;
    private List<User> userList;



    @Override
    public void onActivityCreated(Bundle b){
        super.onActivityCreated(b);
                /*
                new BackgroundTask().execute();
                userListView = (ListView) getView().findViewById(R.id.UserListView);
                userList = new ArrayList<>();
                adapter = new UserListAdapter(getContext().getApplicationContext(), userList);
                userListView.setAdapter(adapter);
*/

    }
    @Override
    public  void onResume(){
        super.onResume();
        new BackgroundTask().execute();
        userListView = (ListView) getView().findViewById(R.id.UserListView);
        userList = new ArrayList<>();
        adapter = new UserListAdapter(getContext().getApplicationContext(), userList);
        userListView.setAdapter(adapter);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reservation_list, container, false);
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

    class BackgroundTask extends AsyncTask<Void, Void, String>
    {
        String target = Constants.POST_REQUEST_URLS.GET_RESERVATION;

        @Override
        protected void onPreExecute(){

        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(target);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;
                StringBuilder stringBuilder = new StringBuilder();
                while ((temp = bufferedReader.readLine()) != null)
                {
                    stringBuilder.append(temp + "\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();
            }
            catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void onProgressUpdate(Void... values){
            super.onProgressUpdate(values);
        }

        @Override
        public void onPostExecute(String result){

            try{


                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                int count = 0;
                int userCount;
                String userName;
                String bookDate;

                while(count < jsonArray.length())
                {
                    JSONObject object = jsonArray.getJSONObject(count);
                    userCount = count + 1;
                    userName = object.getString("name");
                    bookDate = object.getString("reservationtime");
                    User user = new User(userCount, userName, bookDate);
                    userList.add(user);
                    count++;

                }
                if(count == 0 ){

                    AlertDialog.Builder builder = new AlertDialog.Builder(ReservationListFragment.this.getActivity());
                    AlertDialog dialog;
                    dialog = builder.setMessage("예약 대기중인 환자가 없습니다")
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
    }
}