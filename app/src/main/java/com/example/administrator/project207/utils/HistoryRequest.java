package com.example.administrator.project207.utils;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.administrator.project207.utils.Constants;
import com.example.administrator.project207.utils.Requests;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017-03-18.
 */

public class HistoryRequest extends StringRequest {

        private Map<String, String> parameters;

    public HistoryRequest(String userID, Response.Listener<String> listener)
        {
            super(Method.POST, Requests.POST_URLS.GET_HISTORY, listener, null);
            parameters = new HashMap<>();
            parameters.put("userid", userID);
        }
        @Override
        public Map<String, String> getParams(){
            return parameters;
    }
}
