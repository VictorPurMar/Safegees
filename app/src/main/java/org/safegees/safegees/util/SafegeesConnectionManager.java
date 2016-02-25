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
import android.os.Environment;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.bonuspack.kml.KmlDocument;
import org.osmdroid.bonuspack.kml.KmlFolder;
import org.safegees.safegees.R;
import org.safegees.safegees.gui.view.MainActivity;

import java.io.File;
import java.util.HashMap;

/**
 * Created by victor on 30/1/16.
 */
public class SafegeesConnectionManager {

    //URL
    static String WEB_BASE = "https://safegees.appspot.com/v1/";
    static String GET_POSITION = "user/position/";
    static String GET_LOGIN = "user/login/";
    static String SET_POSITION = "user/position/";
    static String OWN_REGISTER = "user/";
    static String AUTHORIZE_USER = "user/authorized/";
    static String GET_POINTS_OF_INTEREST = "map";



    public void getPointsOfInterest(Context context){
        String url = WEB_BASE+GET_POINTS_OF_INTEREST;
        HttpUrlConnection httpUrlConnection = new HttpUrlConnection();
        String response = null;
        HashMap<String, String> mp = new HashMap<String, String>();
        //mp.put(TEST_USER_NAME, TEST_USER_PASSWORD);
        response = new HttpUrlConnection().performGetCall(context, url, mp, null);
        //Store the response in conf preferences with key : contacts_data_mailfromuser
        if (this.isJSONValid(response)) {
            //Store the response in conf preferences with key : contacts_data_mailfromuser
            MainActivity.DATA_STORAGE.putString(context.getResources().getString(R.string.KEY_POINTS_OF_INTEREST), response);
        }
    }

    public void getContactsData(Context context, String userEmail, String password){
        String url = WEB_BASE+GET_POSITION;
        HttpUrlConnection httpUrlConnection = new HttpUrlConnection();
        String response = null;
        HashMap<String, String> mp = new HashMap<String, String>();
        String auth = userEmail+":"+password;
        response = new HttpUrlConnection().performGetCall(context, url, mp, auth);

        if (this.isJSONValid(response)) {
            //Store the response in conf preferences with key : contacts_data_mailfromuser
            MainActivity.DATA_STORAGE.putString(context.getResources().getString(R.string.KEY_CONTACTS_DATA) + "_" + userEmail, response);
        }
    }

    public boolean updateUserPosition(Context context, String userEmail, String userPassword, String position){
        //Get user password and data from storage
        //String user = MainActivity.DATA_STORAGE.getString(context.getString(R.string.KEY_USER_MAIL));
        //String password = MainActivity.DATA_STORAGE.getString(context.getString(R.string.KEY_USER_PASSWORD));

        String url = WEB_BASE + SET_POSITION; HttpUrlConnection httpUrlConnection = new HttpUrlConnection();
        String response = null;
        HashMap<String, String> mp = new HashMap<String, String>();
        mp.put("position",position);
        String auth = userEmail+":"+userPassword;
        response = new HttpUrlConnection().performPostCall(context, url, mp, auth);
        if (response != null){
            Log.i("RESPONSE",response);
            return true;
        }else{
            Log.e("LOGGIN", "No logged");
            return false;
        }

    }

    public boolean checkLogging(Context context, String user, String password){
        String url = WEB_BASE+GET_LOGIN;
        HttpUrlConnection httpUrlConnection = new HttpUrlConnection();
        String response = null;
        HashMap<String, String> mp = new HashMap<String, String>();
        mp.put("email",user);
        mp.put("password", password);
        String auth = user+":"+password;
        response = new HttpUrlConnection().performPostCall(context, url, mp, auth);

        if (response != null){
            Log.i("RESPONSE",response);
            return true;
        }else{
            Log.e("LOGGIN","No logged");
            return false;
        }
    }

    public boolean userRegister(Context context, String user, String password){
        String url = WEB_BASE+OWN_REGISTER;
        HttpUrlConnection httpUrlConnection = new HttpUrlConnection();
        String response = null;
        HashMap<String, String> mp = new HashMap<String, String>();
        mp.put("email",user);
        mp.put("password", password);
        String auth = user+":"+password;
        response = new HttpUrlConnection().performPostCall(context, url, mp, null);

        if (response != null){
            Log.i("RESPONSE",response);
            return true;
        }else{
            Log.e("REGISTER","Not registered");
            return false;
        }
    }


    public boolean addNewContact(Context context, String userMail, String userPassword, String contactEmail){
        String url = WEB_BASE+AUTHORIZE_USER;
        //Get user password and data from storage
        //String user = MainActivity.DATA_STORAGE.getString(context.getString(R.string.KEY_USER_MAIL));
        //String password = MainActivity.DATA_STORAGE.getString(context.getString(R.string.KEY_USER_PASSWORD));

        HttpUrlConnection httpUrlConnection = new HttpUrlConnection();
        String response = null;
        HashMap<String, String> mp = new HashMap<String, String>();
        mp.put( context.getResources().getString(R.string.POST_KEY_BODY_AUTHORIZED_EMAIL),contactEmail);
        String auth = userMail+":"+userPassword;
        response = new HttpUrlConnection().performPostCall(context, url, mp, auth);

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
        String volunteers = "http://mapsengine.google.com/map/kml?mid=z7Jqrnq1J1aA.kFPvSonz3mK0&forcekml=1";

        storeKlm("volunteers", volunteers);
        storeKlm("syrian", syrian);

    }

    private boolean storeKlm(String fileName, String url){
        KmlDocument mKmlDocument = new KmlDocument();
        Boolean parsed = mKmlDocument.parseKMLUrl(url);
        if(parsed){
            if (mKmlDocument.mKmlRoot != null) {
                KmlFolder root = (KmlFolder) mKmlDocument.mKmlRoot;
                //15. Loading and saving of GeoJSON content
                mKmlDocument.saveAsKML(FileManager.getKMLFileStore(fileName + ".kml"));
                return true;
            }
        }
        return false;
    }
}
