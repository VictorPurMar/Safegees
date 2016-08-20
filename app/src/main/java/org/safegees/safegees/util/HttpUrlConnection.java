/**
 *   HttpUrlConnection.java
 *
 *   Future class description
 *
 *
 *   Copyright (C) 2016  Victor Purcallas <vpurcallas@gmail.com>
 *
 *   Safegees is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Safegees is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with ARcowabungaproject.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.safegees.safegees.util;


import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by victor on 30/1/16.
 */
public class HttpUrlConnection {
    private static final int READ_TIMEOUT = 350000;
    private static final int CONNECTION_TIMEOUT = 350000;
    private static String KEY_HEADER_AUTHORIZED = "auth";


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
            if (statusLine.getStatusCode() == 200 || (statusLine.getStatusCode()>200 && statusLine.getStatusCode() < 300)) {
                responseStr = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
            }else{
                Log.e("GET ERROR" , response.getStatusLine().toString());
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
            String postDataParamsString = getDataString(postDataParams);
             httpPost.setEntity(new StringEntity(postDataParamsString));
             httpPost.setHeader("Accept", "application/json");
             httpPost.setHeader("Content-type", "application/x-www-form-urlencoded; charset=UTF-8");
            //Add the auth header
            if (userCredentials != null) httpPost.addHeader(KEY_HEADER_AUTHORIZED, userCredentials);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        HttpResponse response = null;
        String responseStr = null;
        try {
            response = httpclient.execute(httpPost);
            if (response != null){
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == 200  || statusLine.getStatusCode() == 201 || statusLine.getStatusCode() == 204) {
                    //responseStr = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
                    responseStr = statusLine.getReasonPhrase();
                }else{
                    Log.e("POST ERROR", response.getStatusLine().toString());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseStr;

    }

    public static String performPostFileCall(String requestURL,String userCredentials, File file) {

        String response = null;
        MultipartEntityBuilder reqEntity = MultipartEntityBuilder.create();
        reqEntity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        //reqEntity.addPart("avatar", new FileBody(file, ContentType.MULTIPART_FORM_DATA));
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(requestURL);
        httppost.addHeader(KEY_HEADER_AUTHORIZED,userCredentials);
        httppost.addHeader("ContentType","image/png");
        httppost.addHeader("Referer","https://safegees.appspot.com/v1/user/image/upload/");
        httppost.addHeader("Origin","https://safegees.appspot.com");
        httppost.addHeader("Upgrade-Insecure-Requests","1");
        httppost.addHeader("User-Agent","Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36");
        reqEntity.addBinaryBody("avatar", file, ContentType.create("image/png"), file.getName());
        httppost.setEntity(reqEntity.build());
        HttpResponse httpResponse = null;
        try {
            httpResponse = httpclient.execute(httppost);
            Log.e("IMAGE", httpResponse.getStatusLine().getStatusCode() + ":" +  httpResponse.getStatusLine().getReasonPhrase());
            //response = EntityUtils.toString(httpResponse.getEntity());
            response = httpResponse.getStatusLine().getReasonPhrase();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (httpResponse.getStatusLine().getStatusCode() == 200) return response;
        return null;
    }


    private String getDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
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

    /* Something wrong with put method
    public String performPutCall(String requestURL,
                                 HashMap<String, String> postDataParams, String userCredentials){


        DefaultHttpClient httpclient = new DefaultHttpClient();
        final HttpParams httpConnParams = httpclient.getParams();
        HttpConnectionParams.setConnectionTimeout(httpConnParams, CONNECTION_TIMEOUT);
        HttpConnectionParams.setSoTimeout(httpConnParams, READ_TIMEOUT);
        //PutMethod httpPut = new PutMethod(requestURL)
        HttpPut httpPut = new HttpPut(requestURL);

        try {
            String postDataParamsString = getDataString(postDataParams);
            //httpPut.setEntity(new UrlEncodedFormEntity(postDataParams));
            httpPut.setEntity(new StringEntity(postDataParamsString));
            httpPut.setHeader("version", "1.0.0");
            //httpPut.setHeader("Content-type", "multipart/form-data");
            //httpPut.setHeader("cache-control","no-cache");
            //Add the auth header
            if (userCredentials != null) httpPut.addHeader(KEY_HEADER_AUTHORIZED, userCredentials);
        } catch (UnsupportedEncodingException e) {
            //Next line was written a cat in Dubrotnik
            //https://www.instagram.com/p/BJVxRkoAOGG/?taken-by=victorpurmar
            jñ.............................................................................nn m,mmm     m
            e.printStackTrace();
        }

        try {
            response = httpclient.execute(httpPut);
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == 200  || statusLine.getStatusCode() == 201) {
                //responseStr = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
                responseStr = statusLine.getReasonPhrase();
            }else{
                Log.e("POST ERROR", response.getStatusLine().toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseStr;
    }
    */


}
