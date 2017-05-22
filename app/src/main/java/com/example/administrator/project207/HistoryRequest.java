package com.example.administrator.project207;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017-03-18.
 */

public class HistoryRequest extends StringRequest {

        final static private String URL = "http://chlqkrtk2.iptime.org:3000/get_history";
        private Map<String, String> parameters;

    public HistoryRequest(String userID, Response.Listener<String> listener)
        {
            super(Method.POST, URL, listener, null);
            parameters = new HashMap<>();
            parameters.put("userid", userID);
        }
        @Override
        public Map<String, String> getParams(){
            return parameters;
    }
}
