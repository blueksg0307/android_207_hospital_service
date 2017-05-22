package com.example.administrator.project207;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Administrator on 2017-03-22.
 */

public class RegisterRequest extends StringRequest {


    final static private String URL = "http://chlqkrtk2.iptime.org:3000/test";
    private Map<String, String> parameters ;

    public RegisterRequest(String userID, String userPassword, String userName, String userBirth, String userEmail, Response.Listener<String> listener)
    {
        super(Method.POST, URL, listener, null);

        parameters = new HashMap<>();
        parameters.put("userid", userID);
        parameters.put("userpw", userPassword);
        parameters.put("username", userName);
        parameters.put("birth", userBirth);
        parameters.put("email",userEmail);




    }

    @Override
    public Map<String, String> getParams(){
        return parameters ;

    }
}
