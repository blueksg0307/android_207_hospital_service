package com.example.administrator.project207;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017-03-19.
 */

public class BeaconRequest extends StringRequest {

    final static private String URL = "http://chlqkrtk2.iptime.org:3000/beacon_connect";
    private Map<String, String> parameters;

    public BeaconRequest(String beacon_uuid, String userid, Response.Listener<String> listener)
    {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("uuid", beacon_uuid);
        parameters.put("userid", userid);



    }
    @Override
    public Map<String, String> getParams()
    {
        return parameters;
    }

}