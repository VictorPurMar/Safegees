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
import android.graphics.Bitmap;
import android.graphics.Path;
import android.util.Log;
import android.widget.Toast;

import junit.framework.Assert;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == 200  || statusLine.getStatusCode() == 201 || statusLine.getStatusCode() == 204) {
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

    /*
    public String performPostFileCall(String requestURL,
                                  HashMap<String, String> postDataParams, String userCredentials, File file) {

        DefaultHttpClient httpclient = new DefaultHttpClient();
        final HttpParams httpConnParams = httpclient.getParams();
        HttpConnectionParams.setConnectionTimeout(httpConnParams, CONNECTION_TIMEOUT);
        HttpConnectionParams.setSoTimeout(httpConnParams, READ_TIMEOUT);
        HttpPost httpPost = new HttpPost(requestURL);

        try {
            String postDataParamsString = getDataString(postDataParams);
            httpPost.setEntity(new StringEntity(postDataParamsString));
            //Add file
            FileEntity fileEntity = new FileEntity(file,"UTF-8");
            httpPost.setEntity(fileEntity);

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
            if (statusLine.getStatusCode() == 200  || statusLine.getStatusCode() == 201 || statusLine.getStatusCode() == 204) {
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

    public static String performPostFileCall(String requestURL,
                                           String userCredentials, File file) {
        String charset = "UTF-8";
        String responseStr = "ok";

        CloseableHttpClient httpClient = null;

        try {

            httpClient = HttpClientBuilder.create().build();

            HttpPost postRequest = new HttpPost(requestURL);
            postRequest.addHeader(KEY_HEADER_AUTHORIZED,userCredentials);

            MultipartEntityBuilder reqEntity = MultipartEntityBuilder.create();
            reqEntity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);


            /*
            ByteArrayBody bab = new ByteArrayBody(bytearray, file.getName());
            reqEntity.addPart("file", bab);
            postRequest.setEntity(reqEntity.build());
            */

            /*
            FileBody f = new FileBody(file);
            reqEntity.addPart("file",f);
            postRequest.setEntity(reqEntity.build());
            */

            reqEntity.addBinaryBody(file.getName(), file, ContentType.create("image/jpeg"), file.getName());


            postRequest.setEntity(reqEntity.build());
            HttpResponse response = httpClient.execute(postRequest);
            int statusCode = response.getStatusLine().getStatusCode();
            responseStr = response.getStatusLine().getReasonPhrase();

            //httpClient.execute(postRequest);// takes time

        } catch (Exception e) {
            Log.w("uploadToBlobStore", "postToUrl Exception e = " + e);
            e.printStackTrace();
            return null;
        } finally {
            if (httpClient != null) {
                Log.w("uploadToBlobStore", "connection.closing ");
                try {
                    httpClient.close();
                } catch (IOException e) {
                    Log.w("uploadToBlobStore", "connection.closing error e = "
                            + e);
                    e.printStackTrace();
                    return null;
                }
            }
        }

        return "ok";

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
            e.printStackTrace();
        }

        HttpResponse response = null;
        String responseStr = null;
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

    public String multipartRequest(String urlTo, Map<String, String> parmas, String filepath, String filefield, String fileMimeType) throws Exception {
        HttpURLConnection connection = null;
        DataOutputStream outputStream = null;
        InputStream inputStream = null;

        String twoHyphens = "--";
        String boundary = "*****" + Long.toString(System.currentTimeMillis()) + "*****";
        String lineEnd = "\r\n";

        String result = "";

        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;

        String[] q = filepath.split("/");
        int idx = q.length - 1;

        try {
            File file = new File(filepath);
            FileInputStream fileInputStream = new FileInputStream(file);

            URL url = new URL(urlTo);
            connection = (HttpURLConnection) url.openConnection();

            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("User-Agent", "Android Multipart HTTP Client 1.0");
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.writeBytes(twoHyphens + boundary + lineEnd);
            outputStream.writeBytes("Content-Disposition: form-data; name=\"" + filefield + "\"; filename=\"" + q[idx] + "\"" + lineEnd);
            outputStream.writeBytes("Content-Type: " + fileMimeType + lineEnd);
            outputStream.writeBytes("Content-Transfer-Encoding: binary" + lineEnd);

            outputStream.writeBytes(lineEnd);

            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            while (bytesRead > 0) {
                outputStream.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }

            outputStream.writeBytes(lineEnd);

            // Upload POST Data
            Iterator<String> keys = parmas.keySet().iterator();
            while (keys.hasNext()) {
                String key = keys.next();
                String value = parmas.get(key);

                outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                outputStream.writeBytes("Content-Disposition: form-data; name=\"" + key + "\"" + lineEnd);
                outputStream.writeBytes("Content-Type: text/plain" + lineEnd);
                outputStream.writeBytes(lineEnd);
                outputStream.writeBytes(value);
                outputStream.writeBytes(lineEnd);
            }

            outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);


            if (200 != connection.getResponseCode()) {
                throw new Exception("Failed to upload code:" + connection.getResponseCode() + " " + connection.getResponseMessage());
            }

            inputStream = connection.getInputStream();

            result = this.convertStreamToString(inputStream);

            fileInputStream.close();
            inputStream.close();
            outputStream.flush();
            outputStream.close();

            return result;
        } catch (Exception e) {
            throw new Exception(e);
        }

    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
