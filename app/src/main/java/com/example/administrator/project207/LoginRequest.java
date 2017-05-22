package com.example.administrator.project207;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017-03-19.
 */

public class LoginRequest extends StringRequest {

    final static private String URL = "http://chlqkrtk2.iptime.org:3000/login";
    private Map<String, String> parameters;

    public LoginRequest(String userID, String userPassword, String isphone, Response.Listener<String> listener)
    {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("userid", userID);
        parameters.put("userpw", userPassword);
        parameters.put("isphone", isphone);


    }
    @Override
    public Map<String, String> getParams()
    {
        return parameters;
    }

}

