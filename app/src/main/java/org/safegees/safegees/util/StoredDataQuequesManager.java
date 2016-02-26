/**
 *   StoredDataQuequesManager.java
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
import org.safegees.safegees.gui.view.MainActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by victor on 8/2/16.
 */
public class StoredDataQuequesManager {
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


    public static String getUserPassword(Context context, String userMail){
        Map<String, String> appUsersMap = StoredDataQuequesManager.getAppUsersMap(context);
        if (appUsersMap != null){
            Log.i("getUserPassword",userMail+" "+appUsersMap.get(userMail));
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

    public static boolean putUserAndKeyInAppUserQueque(Context context, String userMail, String password){
        String storedJSONStringKEY = context.getResources().getString(R.string.KEY_APP_USERS);
        String jsonResourceKey = context.getResources().getString(KEY_APP_USER_JSON);
        return putInStorageJSON(userMail, password, storedJSONStringKEY, jsonResourceKey, KEY_JSON_APP_USERS_TITLE, KEY_JSON_USER_EMAIL, KEY_JSON_USER_PASSWORD, true);
    }

    public static boolean putUserPositionInPositionsQueque(Context context, String userMail, String userPosition){
        String storedJSONStringKEY = context.getResources().getString(R.string.KEY_USER_POSITIONS);
        String jsonResourceKey = context.getResources().getString(KEY_USER_POSITION_JSON);
        return putInStorageJSON(userMail, userPosition, storedJSONStringKEY, jsonResourceKey, KEY_JSON_USER_POSITION_TITLE, KEY_JSON_USER_EMAIL, KEY_JSON_USER_POSITION, true);
    }

    public static boolean putUserAndKeyInAddUserQueque(Context context, String userMail, String emailForAdd){
        String storedJSONStringKEY = context.getResources().getString(R.string.KEY_ADD_USERS);
        String jsonResourceKey = context.getResources().getString(KEY_ADD_USER_JSON);
        return putInStorageJSON(userMail, emailForAdd, storedJSONStringKEY, jsonResourceKey, KEY_JSON_ADD_USERS_TITLE, KEY_JSON_USER_EMAIL, KEY_JSON_CONTACT_TO_ADD, false);
    }

    private static boolean putInStorageJSON(String userMail, String userValue, String storedJSONStringKEY, String jsonResourceKey, String jsonTitle_key, String jsonUserEmailKey, String jsonUserValueKey, boolean substituteStoredFieldByUserEmail) {
        boolean isAdded = false;
        String JSONStoredString = MainActivity.DATA_STORAGE.getString(storedJSONStringKEY);
        if (JSONStoredString != null && !JSONStoredString.equals("")){
            try {
                JSONObject json = new JSONObject(JSONStoredString);
                if(substituteStoredFieldByUserEmail){
                    removeFromStoredQuequeByValue(userMail, JSONStoredString,jsonResourceKey,jsonTitle_key,jsonUserEmailKey);
                    JSONStoredString = MainActivity.DATA_STORAGE.getString(storedJSONStringKEY);
                    json = new JSONObject(JSONStoredString);
                }
                JSONArray ja = json.getJSONArray(jsonTitle_key);
                JSONObject value = new JSONObject();
                value.put(jsonUserEmailKey, userMail);
                value.put(jsonUserValueKey, userValue);
                ja.put(value);
                json = new JSONObject();
                json.put(jsonTitle_key, ja);
                Log.i("JSON_OBJECT_STORED", "KEY:" + jsonResourceKey + " JSON:" + json.toString());
                MainActivity.DATA_STORAGE.putString(jsonResourceKey, json.toString());
                isAdded = true;

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            try {

                JSONObject value = new JSONObject();
                value.put(jsonUserEmailKey, userMail);
                value.put(jsonUserValueKey, userValue);
                JSONArray ja = new JSONArray();
                ja.put(value);
                JSONObject json = new JSONObject();
                json.put(jsonTitle_key, ja);
                Log.i("JSON_OBJECT_STORED", "KEY:" + jsonResourceKey + " JSON:" + json.toString());
                MainActivity.DATA_STORAGE.putString(jsonResourceKey, json.toString());
                isAdded = true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return isAdded;
    }

    public static boolean removeAppUserFromQueque(Context context, String userMail, String userPassword){
        String storedJSON = MainActivity.DATA_STORAGE.getString(context.getResources().getString(R.string.KEY_APP_USERS));
        String jsonResourceKey = context.getResources().getString(KEY_APP_USER_JSON);
        return removeFromStoredQuequeByTwoValues(userMail, userPassword, storedJSON, jsonResourceKey, KEY_JSON_APP_USERS_TITLE, KEY_JSON_USER_EMAIL, KEY_JSON_USER_PASSWORD);
    }

    public static boolean removeUserPositionInQueque(Context context, String userMail, String userPosition){
        String storedJSON = MainActivity.DATA_STORAGE.getString(context.getResources().getString(R.string.KEY_USER_POSITIONS));
        String jsonResourceKey = context.getResources().getString(KEY_USER_POSITION_JSON);
        return removeFromStoredQuequeByTwoValues(userMail, userPosition, storedJSON, jsonResourceKey, KEY_JSON_USER_POSITION_TITLE, KEY_JSON_USER_EMAIL, KEY_JSON_USER_POSITION);
    }

    public static boolean removeContactToAddInQueque(Context context, String userMail, String emailForAdd){
        String storedJSON = MainActivity.DATA_STORAGE.getString(context.getResources().getString(R.string.KEY_ADD_USERS));
        String jsonResourceKey = context.getResources().getString(KEY_ADD_USER_JSON);
        return removeFromStoredQuequeByTwoValues(userMail, emailForAdd, storedJSON, jsonResourceKey, KEY_JSON_ADD_USERS_TITLE, KEY_JSON_USER_EMAIL, KEY_JSON_CONTACT_TO_ADD);
    }

    private static boolean removeFromStoredQuequeByTwoValues(String userMail, String userValue, String storedJSON, String jsonResourceKey, String storedJSONTitle, String userMailJSONKey, String valueJSONKey) {
        boolean isRemoved = false;
        if (storedJSON != null && !storedJSON.equals("")){
            try {

                JSONObject json = new JSONObject(storedJSON);
                JSONArray ja = json.getJSONArray(storedJSONTitle);
                JSONArray newJa = new JSONArray();

                for(int i = 0; i<ja.length(); i++){
                    JSONObject value= ja.getJSONObject(i);
                    String uMail = value.getString(userMailJSONKey);
                    String uValue = value.getString(valueJSONKey);
                    if (!uMail.equals(userMail) && !uValue.equals(userValue)) newJa.put(value);
                }

                ja = newJa;
                json = new JSONObject();
                json.put(storedJSONTitle, ja);
                Log.i("JSON_OBJECT_REMOVE", "Removed user:" + userMail + " and contact:" + userValue + " with RESULT JSON:" + json.toString());
                MainActivity.DATA_STORAGE.putString(jsonResourceKey, json.toString());
                isRemoved =  true;

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return isRemoved;
    }

    private static boolean removeFromStoredQuequeByValue(String userValue, String storedJSON, String jsonResourceKey, String storedJSONTitle, String valueJSONKey) {
        boolean isRemoved = false;
        if (storedJSON != null && !storedJSON.equals("")){
            try {

                JSONObject json = new JSONObject(storedJSON);
                JSONArray ja = json.getJSONArray(storedJSONTitle);
                JSONArray newJa = new JSONArray();
                for(int i = 0; i<ja.length(); i++){
                    JSONObject value= ja.getJSONObject(i);
                    String uValue = value.getString(valueJSONKey);
                    if (!uValue.equals(userValue)) newJa.put(value);
                }
                ja = newJa;
                json = new JSONObject();
                json.put(storedJSONTitle, ja);
                Log.i("JSON_OBJECT_REMOVE", "Removed by value:" + userValue + " with RESULT JSON:" + json.toString());
                MainActivity.DATA_STORAGE.putString(jsonResourceKey, json.toString());
                isRemoved =  true;

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return isRemoved;
    }

    public static Map<String, String> getAppUsersMap(Context context){
        String dataStorageValue = MainActivity.DATA_STORAGE.getString(context.getResources().getString(R.string.KEY_APP_USERS));
        return getStoredMap(KEY_JSON_USER_EMAIL, KEY_JSON_USER_PASSWORD, KEY_JSON_APP_USERS_TITLE, dataStorageValue);
    }

    public static Map<String, String> getAddContactsMapQueque(Context context){
        String addContact = MainActivity.DATA_STORAGE.getString(context.getResources().getString(R.string.KEY_ADD_USERS));
        return getStoredMap(KEY_JSON_USER_EMAIL, KEY_JSON_CONTACT_TO_ADD,KEY_JSON_ADD_USERS_TITLE, addContact);
    }

    public static Map<String, String> getUserPositionsMap(Context context){
        String userPositions = MainActivity.DATA_STORAGE.getString(context.getResources().getString(R.string.KEY_USER_POSITIONS));
        return getStoredMap(KEY_JSON_USER_EMAIL, KEY_JSON_USER_POSITION, KEY_JSON_USER_POSITION_TITLE, userPositions);
    }


    private static Map<String, String> getStoredMap(String userMail, String userValueParammeter, String jsonTitle, String dataStorageValue) {
        Map<String, String> userMap =  new HashMap<String, String>();
        try{
            JSONObject json = new JSONObject(dataStorageValue);
            JSONArray ja = json.getJSONArray(jsonTitle);
            for(int i = 0 ; i < ja.length() ; i++){
                JSONObject value= ja.getJSONObject(i);
                String uMail = value.getString(userMail);
                String userValueParametter = value.getString(userValueParammeter);
                userMap.put(uMail, userValueParametter);
            }

        } catch (JSONException e) {
            //e.printStackTrace();
            Log.e("StoredDataQuequesManager", "getStoredMap" + " failed to parse a JSON " + jsonTitle);
        }
        Log.i("JSON_GET_MAP", "Title:"+jsonTitle+ "Map:"+ userMap.toString());
        return userMap;
    }
}
