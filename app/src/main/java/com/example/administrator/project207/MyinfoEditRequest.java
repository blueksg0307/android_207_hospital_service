package com.example.administrator.project207;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017-03-19.
 */

public class MyinfoEditRequest extends StringRequest {

    final static private String URL = "http://chlqkrtk2.iptime.org:3000/Account_Edit";
    private Map<String, String> parameters;

    public MyinfoEditRequest(String userID, String userName, String userBirth, String userNumber, Response.Listener<String> listener)
    {
        super(Method.POST, URL, listener, null);
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