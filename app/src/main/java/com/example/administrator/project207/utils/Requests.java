package com.example.administrator.project207.utils;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import java.util.HashMap;
import java.util.Map;
/**
 * Created by 김선광 2017.05.25
 */

public class Requests extends StringRequest {

    private Map<String, String> StringItems ;

    public interface POST_URLS {

        String LOGIN = "http://chlqkrtk2.iptime.org:3000/login";
        String REGISTER_VALIDATE = "http://chlqkrtk2.iptime.org:3000/mobile/register_validate";
        String ACCOUNT_EDIT = "http://chlqkrtk2.iptime.org:3000/Account_Edit";
        String BEACON_CONNECT = "http://chlqkrtk2.iptime.org:3000/beacon_connect";
        String GET_RESERVATION = "http://chlqkrtk2.iptime.org:3000/get_reservation";
        String ADD_RESERVATION = "http://chlqkrtk2.iptime.org:3000/add_reservation";
        String CHECK_RESERVATION = "http://chlqkrtk2.iptime.org:3000/check_reservation";
        String GET_HISTORY = "http://chlqkrtk2.iptime.org:3000/get_history";
        String GET_WAITING = "http://chlqkrtk2.iptime.org:3000/get_waiting";
        String TEST01 = "http://chlqkrtk2.iptime.org:3000/test";

    }
    public Requests(String URL, Response.Listener<String> listener, String ... v){

        super(Method.POST,URL, listener, null);
        String Index[] = {"First", "Second", "Third","Fourth", "Fifth", "Sixth"};
        StringItems = new HashMap<String, String>();
        for(int i = 0; i< v.length; i++) {

            StringItems.put(Index[i], v[i]);
        }
    }

    @Override
    public Map<String, String> getParams()
    {
        return StringItems;
    }

}
