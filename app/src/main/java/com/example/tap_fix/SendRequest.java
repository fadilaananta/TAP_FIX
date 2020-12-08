package com.example.tap_fix;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;


public class SendRequest extends AsyncTask<String, Void, String> {
    private KitInterface kitInterface;
    private LoginActivity loginActivity;
    String code;
    protected void onPreExecute(){}

    protected String doInBackground(String... arg0) {

        loginActivity = new LoginActivity();
        try{

            //Enter script URL Here
            URL url = new URL(code);

            JSONObject postDataParams = new JSONObject();
            JSONArray arr = new JSONArray();

            //int i;
            //for(i=1;i<=70;i++)


            //    String usn = Integer.toString(i);

            //Passing scanned code as parameter

            postDataParams.put("name",loginActivity.getName());
            postDataParams.put("nim",loginActivity.getNim());


            Log.e("params",postDataParams.toString());

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));
            writer.flush();
            writer.close();
            os.close();


            int responseCode=conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {

                BufferedReader in=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuffer sb = new StringBuffer("");
                String line="";

                while((line = in.readLine()) != null) {

                    sb.append(line);

                }

                in.close();
                return sb.toString();

            }
            else {
                return new String("false : "+responseCode);
            }
        }
        catch(Exception e){
            Log.d("TAG", "doInBackground: " + e.getMessage());
            return new String("Exception: " + e.getMessage());
        }
    }

/*    @Override
    protected void onPostExecute(String result) {
        Toast.makeText(getApplicationContext(), result,
                Toast.LENGTH_LONG).show();

    }*/


    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }
}
