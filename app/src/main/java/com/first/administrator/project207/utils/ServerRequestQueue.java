package com.first.administrator.project207.utils;

import android.app.Application;
import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Awesometic on 2017-05-26.
 * This is a Singleton, and it manages the queue which contains all POST requests called from application
 *
 * References
 * a. https://developer.android.com/training/volley/requestqueue.html
 * b. https://stackoverflow.com/questions/16626032/volley-post-get-parameters
 */

public class ServerRequestQueue {
    private static ServerRequestQueue mInstance;
    private RequestQueue mRequestQueue;

    public static ServerRequestQueue getInstance(Context context) {
        if (mInstance == null) {
            synchronized (ServerRequestQueue.class) {
                if (mInstance == null) {
                    mInstance = new ServerRequestQueue(context);
                }
            }
        }

        return mInstance;
    }

    private ServerRequestQueue(Context context) {
        if (context instanceof Application) {
            Cache cache = new DiskBasedCache(context.getCacheDir(), 10 * 1024 * 1024);
            Network network = new BasicNetwork(new HurlStack());
            mRequestQueue = new RequestQueue(cache, network);
            mRequestQueue.start();
        } else {
            mInstance = null;
        }
    }

    public void addRequest(String url, Response.Listener<String> successListener, Response.ErrorListener errorListener, final String... params) {
        StringRequest stringRequest = null;

        switch (url) {
            case Constants.POST_REQUEST_URLS.LOGIN:
                stringRequest = new StringRequest(Request.Method.POST, url, successListener, errorListener) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> parameters = new HashMap<>();
                        parameters.put("userid", params[0]);
                        parameters.put("userpw", params[1]);
                        parameters.put("isphone", params[2]);

                        return parameters;
                    }
                };
                break;
            case Constants.POST_REQUEST_URLS.REGISTER_VALIDATE:
                stringRequest = new StringRequest(Request.Method.POST, url, successListener, errorListener) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> parameters = new HashMap<>();
                        parameters.put("userid", params[0]);

                        return parameters;
                    }
                };
                break;
            case Constants.POST_REQUEST_URLS.ACCOUNT_EDIT:
                stringRequest = new StringRequest(Request.Method.POST, url, successListener, errorListener) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> parameters = new HashMap<>();
                        parameters.put("userID", params[0]);
                        parameters.put("userName", params[1]);
                        parameters.put("userBirth", params[2]);
                        parameters.put("userNumber", params[3]);

                        return parameters;
                    }
                };
                break;
            case Constants.POST_REQUEST_URLS.BEACON_CONNECT:
                stringRequest = new StringRequest(Request.Method.POST, url, successListener, errorListener) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> parameters = new HashMap<>();
                        parameters.put("uuid", params[0]);
                        parameters.put("userid", params[1]);
                        parameters.put("userToken", params[2]);

                        return parameters;
                    }
                };
                break;

            case Constants.POST_REQUEST_URLS.ADD_RESERVATION:
                stringRequest = new StringRequest(Request.Method.POST, url, successListener, errorListener) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> parameters = new HashMap<>();
                        parameters.put("userID", params[0]);
                        parameters.put("userPurpose",params[1]);
                        parameters.put("bookYear", params[2]);
                        parameters.put("bookMonth", params[3]);
                        parameters.put("bookDay", params[4]);
                        parameters.put("bookTime", params[5]);
                        parameters.put("userName", params[6]);
                        parameters.put("userBirth", params[7]);
                        parameters.put("bookMin", params[8]);

                        return parameters;

                    }
                };
                break;
            case Constants.POST_REQUEST_URLS.CHECK_RESERVATION:
                stringRequest = new StringRequest(Request.Method.POST, url, successListener, errorListener) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> parameters = new HashMap<>();
                        parameters.put("userid", params[0]);

                        return parameters;
                    }
                };
                break;
            case Constants.POST_REQUEST_URLS.GET_HISTORY:
                stringRequest = new StringRequest(Request.Method.POST, url, successListener, errorListener) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> parameters = new HashMap<>();
                        parameters.put("userid", params[0]);

                        return parameters;
                    }
                };
                break;
            case Constants.POST_REQUEST_URLS.Register:
                stringRequest = new StringRequest(Request.Method.POST, url, successListener, errorListener) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> parameters = new HashMap<>();
                        parameters.put("userid", params[0]);
                        parameters.put("userpw", params[1]);
                        parameters.put("username", params[2]);
                        parameters.put("birth", params[3]);
                        parameters.put("phone", params[4]);

                        return parameters;
                    }
                };
                break;

            case Constants.POST_REQUEST_URLS.Fcm_CheckWaitingcount:
                stringRequest = new StringRequest(Request.Method.POST, url, successListener, errorListener) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> parameters = new HashMap<>();
                        parameters.put("userid", params[0]);


                        return parameters;
                    }
                };
                break;

            case Constants.POST_REQUEST_URLS.Fcm_Success:
                stringRequest = new StringRequest(Request.Method.POST, url, successListener, errorListener) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> parameters = new HashMap<>();
                        parameters.put("token", params[0]);


                        return parameters;
                    }
                };
                break;

            default:
                break;
        }

        if (stringRequest != null) {
            stringRequest.setTag(Constants.POST_REQUEST_TAGS.DEFAULT);

            mRequestQueue.add(stringRequest);
        }
    }

    // Edit Constants.POST_REQUEST_TAGS to cancel specific requests
    public void cancelRequests(String tag) {
        switch (tag) {

            default:
                break;
        }
    }

    public void cancelAllRequests() {
        if (mRequestQueue != null)
            mRequestQueue.cancelAll(Constants.POST_REQUEST_TAGS.DEFAULT);
    }
}
