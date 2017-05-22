package com.example.administrator.project207;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.administrator.project207.utils.Constants;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017-03-18.
 */

public class ValidateRequest extends StringRequest {

        private Map<String, String> parameters;

    public ValidateRequest(String userID, Response.Listener<String> listener)
        {
            super(Method.POST, Constants.POST_URLS.REGISTER_VALIDATE, listener, null);
            parameters = new HashMap<>();
            parameters.put("userid", userID);
        }
        @Override
        public Map<String, String> getParams(){
            return parameters;
    }
}
