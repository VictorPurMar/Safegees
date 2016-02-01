package org.safegees.safegees.util;

import android.util.Base64;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;


/**
 * Created by victor on 30/1/16.
 */
public class HttpUrlConnection {
    private static final int READ_TIMEOUT = 150000;
    private static final int CONNECTION_TIMEOUT = 150000;


    public String performGetCall(String requestURL,
                                 HashMap<String, String> postDataParams, String userCredentials) {

        DefaultHttpClient httpclient = new DefaultHttpClient();
        final HttpParams httpConnParams = httpclient.getParams();
        HttpConnectionParams.setConnectionTimeout(httpConnParams, CONNECTION_TIMEOUT);
        HttpConnectionParams.setSoTimeout(httpConnParams, READ_TIMEOUT);
        HttpGet httpGet = new HttpGet(requestURL);
        //Add the auth header
        if (userCredentials != null) httpGet.addHeader("auth" , userCredentials);
        HttpResponse response = null;
        String responseStr = null;
        try {
            response = httpclient.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == 200) {
                responseStr = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
            }else{
                Log.e("GET ERROR" , "Error from Server");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseStr;

    }

    public String performPostCall(String requestURL,
                                 HashMap<String, String> postDataParams, String userCredentials) {

        DefaultHttpClient httpclient = new DefaultHttpClient();
        final HttpParams httpConnParams = httpclient.getParams();
        HttpConnectionParams.setConnectionTimeout(httpConnParams, CONNECTION_TIMEOUT);
        HttpConnectionParams.setSoTimeout(httpConnParams, READ_TIMEOUT);
        HttpPost httpPost = new HttpPost(requestURL);

        try {
             httpPost.setEntity(new StringEntity(getPostDataString(postDataParams)));
             httpPost.setHeader("Accept", "application/json");
             httpPost.setHeader("Content-type", "application/x-www-form-urlencoded; charset=UTF-8");
            //Add the auth header
            if (userCredentials != null) httpPost.addHeader("auth" , userCredentials);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        HttpResponse response = null;
        String responseStr = null;
        try {
            response = httpclient.execute(httpPost);
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == 200  || statusLine.getStatusCode() == 201) {
                responseStr = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
            }else{
                Log.e("GET ERROR" , "Error from Server");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseStr;

    }


    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }
}
