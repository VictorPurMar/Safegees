/**
 *   SafegeesConnectionManager.java
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


import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.bonuspack.kml.KmlDocument;
import org.osmdroid.bonuspack.kml.KmlFolder;
import org.safegees.safegees.R;
import org.safegees.safegees.gui.view.MainActivity;
import org.safegees.safegees.model.User;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by victor on 30/1/16.
 */
public class SafegeesConnectionManager {

    private static final String SEPARATOR = File.separator;
    //URL
    static String WEB_BASE = "https://safegees.appspot.com/v1/";
    static String SERVICE_POSITION = "position";
    static String SERVICE_LOGIN = "login";
    static String SERVICE_KEY_USER = "user";
    static String SERVICE_AUTHORIZE = "authorized";
    static String SERVICE_POI = "map";


    //Not implemented on server
    static String KEY_NAME = "name";
    static String KEY_SURNAME = "surname";
    static String KEY_PHONE = "telephone";
    static String KEY_BIO = "topic";
    static String KEY_MAIL = "email";
    static String KEY_PASSWORD = "password";
    static String KEY_USER = "user";



    public void getPointsOfInterest(Context context){
        String url = WEB_BASE + SERVICE_POI;
        HashMap<String, String> mp = new HashMap<String, String>();
        String response = new HttpUrlConnection().performGetCall(context, url, mp, null);
        //Store the response in conf preferences with key : contacts_data_mailfromuser
        if (this.isJSONValid(response)) {
            //Store the response in conf preferences with key : contacts_data_mailfromuser
            MainActivity.DATA_STORAGE.putString(context.getResources().getString(R.string.KEY_POINTS_OF_INTEREST), response);
        }
    }

    public void getContactsData(Context context, String userEmail, String password){
        String url = WEB_BASE + SERVICE_KEY_USER + SEPARATOR + SERVICE_POSITION;
        HashMap<String, String> mp = new HashMap<String, String>();
        String auth = userEmail+":"+password;
        String response  = new HttpUrlConnection().performGetCall(context, url, mp, auth);

        if (this.isJSONValid(response)) {
            //Store the response in conf preferences with key : contacts_data_mailfromuser
            MainActivity.DATA_STORAGE.putString(context.getResources().getString(R.string.KEY_CONTACTS_DATA) + "_" + userEmail, response);
        }
    }

    ////////////////////////////////////////////////////////////////////
    //NOT IMPLEMENTED ON SERVER
    ////////////////////////////////////////////////////////////////////
    public void getUserBasic(Context context, User user){
        String url = WEB_BASE + SERVICE_KEY_USER + SEPARATOR + SERVICE_POSITION;
        HashMap<String, String> mp = new HashMap<String, String>();
        String auth = user.getAuth();
        String response  = new HttpUrlConnection().performGetCall(context, url, mp, auth);
        if (this.isJSONValid(response)) {
            //Store the response in conf preferences with key : contacts_data_mailfromuser
            MainActivity.DATA_STORAGE.putString(context.getResources().getString(R.string.KEY_USER_BASIC) + "_" + user.getEmail(), response);
        }
    }
    ////////////////////////////////////////////////////////////////////



    public boolean updateUserPosition(Context context, String userEmail, String userPassword, String position){
        //Get user password and data from storage
        String url = WEB_BASE + SERVICE_KEY_USER + SEPARATOR + SERVICE_POSITION + SEPARATOR;
        HashMap<String, String> mp = new HashMap<String, String>();
        mp.put("position",position);
        String auth = userEmail+":"+userPassword;
        String response = new HttpUrlConnection().performPostCall(context, url, mp, auth);
        if (response != null){
            Log.i("RESPONSE",response);
            return true;
        }else{
            Log.e("LOGGIN", "No logged");
            return false;
        }

    }

    ////////////////////////////////////////////////////////////////////
    //NOT IMPLEMENTED ON SERVER
    ////////////////////////////////////////////////////////////////////

    public boolean updateUserName(Context context, User user) {
        String url = WEB_BASE + SERVICE_KEY_USER + SEPARATOR + KEY_NAME + SEPARATOR;
        HashMap<String, String> mp = new HashMap<String, String>();
        mp.put(KEY_NAME, user.getName());
        String auth = user.getAuth();
        String response = new HttpUrlConnection().performPostCall(context, url, mp, auth);
        if (response != null) {
            Log.i("RESPONSE", response);
            return true;
        } else {
            Log.e("NAME", "Not updated");
            return false;
        }
    }

    public boolean updateUserSurname(Context context, User user) {
        String url = WEB_BASE + SERVICE_KEY_USER + SEPARATOR + KEY_SURNAME + SEPARATOR;
        HashMap<String, String> mp = new HashMap<String, String>();
        mp.put(KEY_SURNAME, user.getSurname());
        String auth = user.getAuth();
        String response = new HttpUrlConnection().performPostCall(context, url, mp, auth);
        if (response != null) {
            Log.i("RESPONSE", response);
            return true;
        } else {
            Log.e("NAME", "Not updated");
            return false;
        }
    }

    public boolean updateUserBio(Context context, User user) {
        String url = WEB_BASE + SERVICE_KEY_USER + SEPARATOR + KEY_BIO + SEPARATOR;
        HashMap<String, String> mp = new HashMap<String, String>();
        mp.put(KEY_BIO, user.getBio());
        String auth = user.getAuth();
        String response = new HttpUrlConnection().performPostCall(context, url, mp, auth);
        if (response != null) {
            Log.i("RESPONSE", response);
            return true;
        } else {
            Log.e("NAME", "Not updated");
            return false;
        }
    }

    public boolean updateUserPhone(Context context, User user) {
        String url = WEB_BASE + SERVICE_KEY_USER + SEPARATOR + KEY_BIO + SEPARATOR;
        HashMap<String, String> mp = new HashMap<String, String>();
        mp.put(KEY_PHONE, user.getPhoneNumber());
        String auth = user.getAuth();
        String response = new HttpUrlConnection().performPostCall(context, url, mp, auth);
        if (response != null) {
            Log.i("RESPONSE", response);
            return true;
        } else {
            Log.e("NAME", "Not updated");
            return false;
        }
    }
    ////////////////////////////////////////////////////////////////////


    //Actually the server only stores email and passowd
    public boolean userRegister(Context context, String user, String password, String name, String surname, String telephone, String topic){
        String url = WEB_BASE + SERVICE_KEY_USER + SEPARATOR;
        HashMap<String, String> mp = new HashMap<String, String>();
        mp.put(KEY_MAIL,user);
        mp.put(KEY_PASSWORD, password);
        mp.put(KEY_NAME, name);
        mp.put(KEY_SURNAME, surname);
        mp.put(KEY_PHONE, telephone);
        mp.put(KEY_BIO, topic);
        String response = new HttpUrlConnection().performPostCall(context, url, mp, null);
        if (response != null){
            Log.i("RESPONSE",response);
            return true;
        }else{
            Log.e("REGISTER","Not registered");
            return false;
        }
    }

    public boolean checkLogging(Context context, String user, String password){
        String url = WEB_BASE + SERVICE_KEY_USER + SEPARATOR + SERVICE_LOGIN + SEPARATOR;
        HashMap<String, String> mp = new HashMap<String, String>();
        mp.put("email",user);
        mp.put("password", password);
        String auth = user+":"+password;
        String response  = new HttpUrlConnection().performPostCall(context, url, mp, auth);

        if (response != null){
            Log.i("RESPONSE",response);
            return true;
        }else{
            Log.e("LOGGIN","No logged");
            return false;
        }
    }




    public boolean addNewContact(Context context, String userMail, String userPassword, String contactEmail){
        String url = WEB_BASE + SERVICE_KEY_USER + SEPARATOR + SERVICE_AUTHORIZE + SEPARATOR;

        HashMap<String, String> mp = new HashMap<String, String>();
        mp.put( context.getResources().getString(R.string.POST_KEY_BODY_AUTHORIZED_EMAIL),contactEmail);
        String auth = userMail+":"+userPassword;
        String response  = new HttpUrlConnection().performPostCall(context, url, mp, auth);

        if (response != null){
            Log.i("RESPONSE", response);
            return true;
        }else{
            Log.e("ADD_CONTACT", "Contact not added");
            return false;
        }
    }

    public boolean isJSONValid(String test) {
        try {
            new JSONObject(test);
        } catch (JSONException ex) {
            // edited, to include @Arthur's comment
            // e.g. in case JSONArray is valid as well...
            try {
                new JSONArray(test);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }


    public void getThirdKLM(Context context) {

        String syrian = "http://geonode.state.gov/geoserver/wms/kml?layers=geonode%3ASyria_RefugeeSites_2016Jan21_HIU_DoS0&mode=download";
        String volunteers = "https://mapsengine.google.com/map/kml?mid=zddfRUtGScOc.kQBgTQcoV5FM&forcekml=1";

        storeKlm("volunteers", volunteers);
        storeKlm("syrian", syrian);

    }

    private boolean storeKlm(String fileName, String url){

        //Prevent an unnecesary download
        /*
        int fileLenght = getFileLength(url);
        if (!FileManager.getKMLFileStore(fileName + ".kml").exists() || MainActivity.DATA_STORAGE.getInt(fileName) != 0){
        */
        KmlDocument mKmlDocument = new KmlDocument();
        Boolean parsed = mKmlDocument.parseKMLUrl(url);
        if(parsed){
            if (mKmlDocument.mKmlRoot != null) {
                KmlFolder root = (KmlFolder) mKmlDocument.mKmlRoot;
                mKmlDocument.saveAsKML(FileManager.getKMLFileStore(fileName + ".kml"));
                //MainActivity.DATA_STORAGE.putInt(fileName, fileLenght);
                //Log.e("STORED", "FILE " + fileName + " DATA SIZE " + fileLenght);
                return true;
            }
        }
        return false;
        /*
        }

        return true;*/
    }

    private int getFileLength(String fileUrl) {
         int contentLegth = 0;
        try {
            URL url = new URL(fileUrl);
            HttpURLConnection conn;
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.getInputStream();
            contentLegth = conn.getContentLength();
            if (contentLegth == -1 || contentLegth == 0){
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("HEAD");
                conn.getInputStream();
                contentLegth = conn.getContentLength();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contentLegth;
    }
}
