package com.example.administrator.project207;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017-03-18.
 */

public class ReserveRequest extends StringRequest {

    final static private String URL = "http://chlqkrtk2.iptime.org:3000/add_reservation";
    private Map<String, String> parameters;

    public ReserveRequest(String userID, String userPurpose, String Year, String Month, String Date, String Time, Response.Listener<String> listener)
    {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("userID", userID);
        parameters.put("userPurpose",userPurpose);
        parameters.put("bookDay", Date);
        parameters.put("bookMonth", Month);
        parameters.put("bookYear", Year );
        parameters.put("bookTime", Time);


    }
    @Override
    public Map<String, String> getParams(){
        return parameters;


    }
}
