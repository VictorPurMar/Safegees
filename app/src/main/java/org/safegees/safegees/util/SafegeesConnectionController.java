package org.safegees.safegees.util;

//  This is a list of Server services:
//
//  1. User register
//  POST
//  Url: https://safegees.appspot.com/v1/user/
//  Body:
//      email: owner@mail.com
//      password: password
//  ---------------------
//  2. Allow contact to see my position (and info)
//  POST
//  Url: https://safegees.appspot.com/v1/user/authorized/
//  Header:
//      auth:owner@mail.com:password
//  Body:
//      authorized_email: contact@mail.com
//  -----------------------
//  3. Update my position
//  POST
//  Url: https://safegees.appspot.com/v1/position
//  Header:
//      auth:owner@mail.com:password
//  Body:
//      position: latitud, longitud
//  -----------------------
//  4. Get contacts position (and info)
//  GET
//  Url: https://safegees.appspot.com/v1/position/
//  Header:
//      auth: owner@mail.com:password
//  -----------------------
//  5. See points of interest on map
//  GET
//  Url: https://safegees.appspot.com/v1/map/
//  -----------------------
//  6. See if user is registered
//  POST
//  Url: https://safegees.appspot.com/v1/user/login/
//
//


import android.util.Log;

import java.util.HashMap;

/**
 * Created by victor on 30/1/16.
 */
public class SafegeesConnectionController {

    //URL
    static String WEB_BASE = "https://safegees.appspot.com/v1/";
    static String GET_POSITION = "user/position/";
    static String GET_LOGIN = "user/login/";
    static String SET_POSITION = "user/position/";
    static String OWN_REGISTER = "user/";
    static String AUTHORIZE_USER = "user/authorized/";
    static String GET_POINTS_OF_INTEREST = "map";

    //POST keys
    static String KEY_HEADER_AUTHORIZED = "auth";
    static String POST_KEY_BODY_EMAIL = "email";
    static String POST_KEY_BODY_PASSWORD = "password";
    static String POST_KEY_BODY_AUTHORIZED_EMAIL = "authorized_email";
    static String POST_KEY_BODY_POSITION = "position";

    //TEST
    static String TEST_USER_NAME = "alvaro@gmail.com";
    static String TEST_USER_PASSWORD = "das";




    public void getPointsOfInterest(){
        String url = WEB_BASE+GET_POINTS_OF_INTEREST;
        HttpUrlConnection httpUrlConnection = new HttpUrlConnection();
        String response = null;
        HashMap<String, String> mp = new HashMap<String, String>();
        //mp.put(TEST_USER_NAME, TEST_USER_PASSWORD);
        response = new HttpUrlConnection().performGetCall(url, mp, null);
        Log.i("RESPONSE",response);
    }

    public void getContactsData(){
        String url = WEB_BASE+GET_POSITION;
        HttpUrlConnection httpUrlConnection = new HttpUrlConnection();
        String response = null;
        HashMap<String, String> mp = new HashMap<String, String>();
        String auth = TEST_USER_NAME+":"+TEST_USER_PASSWORD;
        response = new HttpUrlConnection().performGetCall(url, mp, auth);
        Log.i("RESPONSE",response);
    }

    public boolean checkLogging(String user, String password){
        String url = WEB_BASE+GET_LOGIN;
        HttpUrlConnection httpUrlConnection = new HttpUrlConnection();
        String response = null;
        HashMap<String, String> mp = new HashMap<String, String>();
        mp.put("email",user);
        mp.put("password", password);
        String auth = user+":"+password;
        response = new HttpUrlConnection().performPostCall(url, mp, auth);

        if (response != null){
            Log.i("RESPONSE",response);
            return true;
        }else{
            Log.e("LOGGIN","No logged");
            return false;
        }
    }

    public boolean userRegister(String user, String password){
        String url = WEB_BASE+OWN_REGISTER;
        HttpUrlConnection httpUrlConnection = new HttpUrlConnection();
        String response = null;
        HashMap<String, String> mp = new HashMap<String, String>();
        mp.put("email",user);
        mp.put("password", password);
        String auth = user+":"+password;
        response = new HttpUrlConnection().performPostCall(url, mp, null);

        if (response != null){
            Log.i("RESPONSE",response);
            return true;
        }else{
            Log.e("REGISTER","Not registered");
            return false;
        }
    }






}
