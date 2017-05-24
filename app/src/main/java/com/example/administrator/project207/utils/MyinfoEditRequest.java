package com.example.administrator.project207.utils;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.administrator.project207.utils.Constants;
import com.example.administrator.project207.utils.Requests;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017-03-19.
 */

public class MyinfoEditRequest extends StringRequest {

    private Map<String, String> parameters;

    public MyinfoEditRequest(String userID, String userName, String userBirth, String userNumber, Response.Listener<String> listener)
    {
        super(Method.POST, Requests.POST_URLS.ACCOUNT_EDIT, listener, null);
        parameters = new HashMap<>();
        parameters.put("userID", userID);
        parameters.put("userName", userName);
        parameters.put("userBirth", userBirth);
        parameters.put("userNumber", userNumber);


    }
    @Override
    public Map<String, String> getParams()
    {
        return parameters;
    }

}