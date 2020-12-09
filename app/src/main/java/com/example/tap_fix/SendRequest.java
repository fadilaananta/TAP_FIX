package com.example.tap_fix;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;


public class SendRequest{
    private KitInterface kitInterface;
    private LoginActivity loginActivity;
    String code;

    @SuppressLint("RestrictedApi")
    public void postData() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject object = new JSONObject();
        try {
            //input your API parameters
            object.put("nama",loginActivity.name);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Enter the correct url for your api service site
        StringRequest stringRequest = new StringRequest(Request.Method.POST, code,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(),"Berhasil!",Toast.LENGTH_SHORT).show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                //here we pass params
                params.put("nama",loginActivity.name);
                params.put("nim",loginActivity.nim);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}
