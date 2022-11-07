package com.digital.moviesbuddypro.utils;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.digital.moviesbuddypro.CONFIG;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RequestAPI {

    public static void Call_Api (final Context context, final String url, final JSONObject jsonObject, final CallBack CallBack){

        if(!CONFIG.is_secure_info) {
            final String[] urlsplit = url.split("/");
            Log.d(CONFIG.TAG, url);

            if (jsonObject != null)
                Log.d(CONFIG.TAG + urlsplit[urlsplit.length - 1], jsonObject.toString());
        }

        //Toast.makeText(context, jsonObject.toString(), Toast.LENGTH_SHORT).show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                if(!CONFIG.is_secure_info) {
                    final String[] urlsplit = url.split("/");
                    Log.d(CONFIG.TAG + urlsplit[urlsplit.length - 1], response.toString());
                }

                /*SharedPreferences sharedPreferences = context.getSharedPreferences(CONFIG.pref_name,MODE_PRIVATE);
                 Toast.makeText(context, sharedPreferences.getString(CONFIG.u_id,""), Toast.LENGTH_SHORT).show();*/

                if(CallBack!=null) {
                    //Log.e("Responseses", response.toString());
                    CallBack.Response(response.toString());
                }

            }

            }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if(!CONFIG.is_secure_info) {
                    final String[] urlsplit = url.split("/");
                    Log.d(CONFIG.TAG + urlsplit[urlsplit.length - 1], error.toString());
                }

                Log.w("TAG", "Error: " + error
                        + "\nStatus Code " //+ error.networkResponse.statusCode
                        + "\nResponse Data " + jsonObject.toString()
                        + "\nCause " + error.getCause()
                        + "\nmessage " + error.getMessage());

                //Toast.makeText(context, "Error In Volley", Toast.LENGTH_SHORT).show();

                if(CallBack!=null)
                    CallBack.Response(error.toString());

            }
        })

        {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                //headers.put("Content-Type", "application/json");
                //headers.put("fb_id", CONFIG.sharedPreferences.getString(CONFIG.u_id,"0"));
                headers.put("device_version", CONFIG.sharedPreferences.getString(CONFIG.deviceVersion,"1"));
                headers.put("device_name", CONFIG.sharedPreferences.getString(CONFIG.deviceName,"NULL"));
                headers.put("token", CONFIG.sharedPreferences.getString(CONFIG.token,""));
                headers.put("android_id", CONFIG.sharedPreferences.getString(CONFIG.androidId,""));
                headers.put("app_version", "5");
                Log.d(CONFIG.TAG, headers.toString());
                return headers;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjReq);
    }
}
