/**
 *   DataQuequesManager.java
 *
 *   This class manages the queques of the app.
 *   Transform the string data to standarized JSONS to store and manage the data.
 *   Also convert the jsons in Map class to be used easely by other classes.
 *
 *   Queque 1: App Users Queque
 *   This queque stores the user and password of all the acounts added to the app
 *   to allow to use the app also ofline. Simulating a close session and login activity
 *   with the stored passowrds
 *
 *   Queque 2: Add Contacts Queque
 *   This queque store the non sended add contacts, with the mail of the contact to add
 *   and with the email from the user that makes the request.
 *   This allows to store and use this queque when the internet is avaiable.
 *
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.safegees.safegees.R;
import org.safegees.safegees.gui.view.SplashActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by victor on 8/2/16.
 */
public class DataQuequesManager {
    private static int KEY_APP_USER_JSON = R.string.KEY_APP_USERS;
    private static int KEY_ADD_USER_JSON = R.string.KEY_ADD_USERS;
    private static int KEY_USER_POSITION_JSON = R.string.KEY_USER_POSITIONS;

    //JSON keys
    private static String KEY_JSON_ADD_USERS_TITLE = "addUsersJSON";
    private static String KEY_JSON_APP_USERS_TITLE = "appUsersJSON";
    private static String KEY_JSON_USER_POSITION_TITLE = "setUserPositionJSON";

    private static String KEY_JSON_USER_EMAIL = "userEmail";
    private static String KEY_JSON_CONTACT_TO_ADD = "contactToAdd";
    private static String KEY_JSON_USER_PASSWORD = "password";
    private static String KEY_JSON_USER_POSITION = "position";


    public static boolean putUserAndKeyInAppUserQueque(Context context, String userMail, String password){

        String appUsers = SplashActivity.DATA_STORAGE.getString(context.getResources().getString(R.string.KEY_APP_USERS));
        if (appUsers != null && !appUsers.equals("")){
            try {

                JSONObject json = new JSONObject(appUsers);
                JSONArray ja = json.getJSONArray(KEY_JSON_APP_USERS_TITLE);
                JSONObject value = new JSONObject();
                value.put(KEY_JSON_USER_EMAIL, userMail);
                value.put(KEY_JSON_USER_PASSWORD, password);
                ja.put(value);
                json = new JSONObject();
                json.put(KEY_JSON_APP_USERS_TITLE, ja);
                Log.i("JSON_OBJECT_APP", json.toString());
                SplashActivity.DATA_STORAGE.putString(context.getResources().getString(KEY_APP_USER_JSON), json.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            try {

                JSONObject value = new JSONObject();
                value.put(KEY_JSON_USER_EMAIL, userMail);
                value.put(KEY_JSON_USER_PASSWORD, password);
                JSONArray ja = new JSONArray();
                ja.put(value);
                JSONObject json = new JSONObject();
                json.put(KEY_JSON_APP_USERS_TITLE, ja);
                Log.i("JSON_OBJECT_APP_NEW", json.toString());
                SplashActivity.DATA_STORAGE.putString(context.getResources().getString(KEY_APP_USER_JSON), json.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public static String getUserPassword(Context context, String userMail){
        Map<String, String> appUsersMap = DataQuequesManager.getAppUsersMap(context);
        if (appUsersMap != null){
            Log.i("getUserPassword",userMail+":::"+appUsersMap.get(userMail));
            return appUsersMap.get(userMail);
        }
        return null;
    }

    public static boolean isPasswordMatch(Context context, String userMail, String userPassword){
        String storedPassword = getUserPassword(context, userMail);
        if (userPassword.equals(storedPassword)){
            return true;
        }
        return false;
    }

    public static Map<String, String> getAppUsersMap(Context context){

        Map<String, String> appUsersMap = new HashMap<String, String>();
        String appUsers = SplashActivity.DATA_STORAGE.getString(context.getResources().getString(R.string.KEY_APP_USERS));
        try {
            JSONObject json = new JSONObject(appUsers);
            JSONArray ja = json.getJSONArray(KEY_JSON_APP_USERS_TITLE);
            for(int i = 0; i<ja.length(); i++){
                JSONObject value= ja.getJSONObject(i);
                String uMail = value.getString(KEY_JSON_USER_EMAIL);
                String uPassword = value.getString(KEY_JSON_USER_PASSWORD);
                appUsersMap.put(uMail, uPassword);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("JSON_APP_QUEQUE", appUsersMap.toString());
        return appUsersMap;

    }


    public static boolean putUserAndKeyInAddUserQueque(Context context, String userMail, String emailForAdd){
        String appUsers = SplashActivity.DATA_STORAGE.getString(context.getResources().getString(R.string.KEY_ADD_USERS));
        if (appUsers != null && !appUsers.equals("")){
            try {

                JSONObject json = new JSONObject(appUsers);
                JSONArray ja = json.getJSONArray(KEY_JSON_ADD_USERS_TITLE);
                JSONObject value = new JSONObject();
                value.put(KEY_JSON_USER_EMAIL, userMail);
                value.put(KEY_JSON_CONTACT_TO_ADD, emailForAdd);
                ja.put(value);

                Log.i("JSON_OBJECT_ADD", json.toString());

                SplashActivity.DATA_STORAGE.putString(context.getResources().getString(KEY_ADD_USER_JSON), json.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            try {

                JSONObject value = new JSONObject();
                value.put(KEY_JSON_USER_EMAIL, userMail);
                value.put(KEY_JSON_CONTACT_TO_ADD, emailForAdd);
                JSONArray ja = new JSONArray();
                ja.put(value);
                JSONObject json = new JSONObject();
                json.put(KEY_JSON_ADD_USERS_TITLE, ja);

                Log.i("JSON_OBJECT_ADD_NEW", json.toString());

                SplashActivity.DATA_STORAGE.putString(context.getResources().getString(KEY_ADD_USER_JSON), json.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public static Map<String, String> getAddContactsMapQueque(Context context){
        Map<String, String> addContactMap = new HashMap<String, String>();
        String addContact = SplashActivity.DATA_STORAGE.getString(context.getResources().getString(R.string.KEY_ADD_USERS));
        try {
            JSONObject json = new JSONObject(addContact);
            JSONArray ja = json.getJSONArray(KEY_JSON_ADD_USERS_TITLE);
            for(int i = 0; i<ja.length(); i++){
                JSONObject value= ja.getJSONObject(i);
                String uMail = value.getString(KEY_JSON_USER_EMAIL);
                String uContactToAdd = value.getString(KEY_JSON_CONTACT_TO_ADD);
                addContactMap.put(uMail, uContactToAdd);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("JSON_ADD_QUEQUE", addContactMap.toString());
        return addContactMap;
    }

    public static boolean removeContactToAddInQueque(Context context, String userMail, String emailForAdd){
        String appUsers = SplashActivity.DATA_STORAGE.getString(context.getResources().getString(R.string.KEY_ADD_USERS));
        if (appUsers != null && !appUsers.equals("")){
            try {

                JSONObject json = new JSONObject(appUsers);
                JSONArray ja = json.getJSONArray(KEY_JSON_ADD_USERS_TITLE);
                JSONArray newJa = new JSONArray();

                for(int i = 0; i<ja.length(); i++){
                    JSONObject value= ja.getJSONObject(i);
                    String uMail = value.getString(KEY_JSON_USER_EMAIL);
                    String uContactToAdd = value.getString(KEY_JSON_CONTACT_TO_ADD);
                    if (!uMail.equals(userMail) && !uContactToAdd.equals(emailForAdd)) newJa.put(value);
                }

                ja = newJa;

                Log.i("JSON_OBJECT_REMOVE","Removed user:"+userMail+" and contact:"+emailForAdd+ " with RESULT JSON:"+ json.toString());

                SplashActivity.DATA_STORAGE.putString(context.getResources().getString(KEY_ADD_USER_JSON), json.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public static boolean putUserPositionInPositionsQueque(Context context, String userMail, String userPosition){
        String userPositions = SplashActivity.DATA_STORAGE.getString(context.getResources().getString(R.string.KEY_USER_POSITIONS));
        if (userPositions != null && !userPositions.equals("")){
            try {

                JSONObject json = new JSONObject(userPositions);
                JSONArray ja = json.getJSONArray(KEY_JSON_USER_POSITION_TITLE);
                JSONObject value = new JSONObject();
                value.put(KEY_JSON_USER_EMAIL, userMail);
                value.put(KEY_JSON_USER_POSITION, userPosition);
                ja.put(value);

                Log.i("JSON_OBJECT_ADD", json.toString());

                SplashActivity.DATA_STORAGE.putString(context.getResources().getString(KEY_USER_POSITION_JSON), json.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            try {

                JSONObject value = new JSONObject();
                value.put(KEY_JSON_USER_EMAIL, userMail);
                value.put(KEY_JSON_USER_POSITION, userPosition);
                JSONArray ja = new JSONArray();
                ja.put(value);
                JSONObject json = new JSONObject();
                json.put(KEY_JSON_USER_POSITION_TITLE, ja);

                Log.i("JSON_OBJECT_ADD_NEW", json.toString());

                SplashActivity.DATA_STORAGE.putString(context.getResources().getString(KEY_USER_POSITION_JSON), json.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public static Map<String, String> getUserPositionsMap(Context context){
        Map<String, String> addContactMap = new HashMap<String, String>();
        String addContact = SplashActivity.DATA_STORAGE.getString(context.getResources().getString(R.string.KEY_USER_POSITIONS));
        try {
            JSONObject json = new JSONObject(addContact);
            JSONArray ja = json.getJSONArray(KEY_JSON_USER_POSITION_TITLE);
            for(int i = 0 ; i < ja.length() ; i++){
                JSONObject value= ja.getJSONObject(i);
                String uMail = value.getString(KEY_JSON_USER_EMAIL);
                String uContactToAdd = value.getString(KEY_JSON_USER_POSITION);
                addContactMap.put(uMail, uContactToAdd);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("JSON_ADD_QUEQUE", addContactMap.toString());
        return addContactMap;
    }
}
