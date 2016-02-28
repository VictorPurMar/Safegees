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

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by victor on 30/1/16.
 */
public class HttpUrlConnection {
    private static final int READ_TIMEOUT = 150000;
    private static final int CONNECTION_TIMEOUT = 150000;
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
            if (statusLine.getStatusCode() == 200) {
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
}
