package com.first.administrator.project207.utils;

/**
 * Created by Awesometic on 2017-05-22.
 */

public class Constants {
    
    public static final String HOST = "http://chlqkrtk2.iptime.org";


    public interface POST_REQUEST_URLS {
        String LOGIN = HOST + "/login";
        String REGISTER_VALIDATE = HOST + "/mobile/register_validate";
        String ACCOUNT_EDIT = HOST + "/Account_Edit";

        String BEACON_CONNECT = HOST + "/beacon_connect";

        String GET_RESERVATION = HOST + "/get_reservation";
        String ADD_RESERVATION = HOST + "/add_reservation";
        String CHECK_RESERVATION = HOST + "/check_reservation";

        String GET_HISTORY = HOST + "/get_history";
        String GET_WAITING = HOST + "/get_waiting";

        // Register new account
        String Register = HOST + "/register";

        //FCM
        String Fcm_Success = HOST + "/alert/success";
        String Fcm_CheckWaitingcount = HOST + "/check_waiting_count";

    }

    // Change tags to suit your appetite
    public interface POST_REQUEST_TAGS {
        String DEFAULT = "default";
    }
}
