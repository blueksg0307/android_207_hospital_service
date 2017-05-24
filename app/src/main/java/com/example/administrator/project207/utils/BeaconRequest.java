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

public class BeaconRequest extends StringRequest {

    private Map<String, String> parameters;

    public BeaconRequest(String beacon_uuid, String userid, Response.Listener<String> listener)
    {
        super(Method.POST, Requests.POST_URLS.BEACON_CONNECT, listener, null);
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