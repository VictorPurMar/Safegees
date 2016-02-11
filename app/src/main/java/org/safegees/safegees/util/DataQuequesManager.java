/**
 *   DataQuequesManager.java
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.safegees.safegees.R;
import org.safegees.safegees.gui.view.SplashActivity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by victor on 8/2/16.
 */
public class DataQuequesManager {
    private static String USER_KEY_SEPARATOR = ":::";
    private static String FIELD_SEPARATOR = ";;;";
    private static int KEY_APP_USER_DICTIONARY = R.string.KEY_APP_USERS;
    private static int KEY_ADD_USER_DICTIONARY = R.string.KEY_ADD_USERS;

    //JSON keys
    private static String KEY_JSON_ADD_USERS_TITLE = "addUsersJSON";
    private static String KEY_JSON_USER_EMAIL = "userEmail";
    private static String KEY_JSON_CONTACT_TO_ADD = "contactToAdd";


    public static boolean putUserAndKeyInAppUserQueque(Context context, String userMail, String password){
        Map<String, String> appUsersMap = DataQuequesManager.getAppUsersMap(context);
        if (appUsersMap != null){
            appUsersMap.put(userMail, password);
            String appUsersString = "";
            Iterator it = appUsersMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                appUsersString = appUsersString + pair.getKey() + USER_KEY_SEPARATOR + pair.getValue()+ FIELD_SEPARATOR;
                it.remove(); // avoids a ConcurrentModificationException
            }
            SplashActivity.DATA_STORAGE.putString(context.getResources().getString(KEY_APP_USER_DICTIONARY),appUsersString);

        }else{
            SplashActivity.DATA_STORAGE.putString(context.getResources().getString(KEY_APP_USER_DICTIONARY), userMail + USER_KEY_SEPARATOR + password + FIELD_SEPARATOR);
        }
        return true;
    }


    public static String getUserPassword(Context context, String userMail){
        Map<String, String> appUsersMap = new HashMap<String, String>();
        String appUsers = SplashActivity.DATA_STORAGE.getString(context.getResources().getString(KEY_APP_USER_DICTIONARY));
        if (appUsers != null){
            String[] appUsersArray = appUsers.split(FIELD_SEPARATOR);

            for (int i = 0 ; i < appUsersArray.length ; i++){
                String[] userAndkey = appUsersArray[i].split(USER_KEY_SEPARATOR);
                String uMail = userAndkey[0];
                String uPassword = userAndkey[1];
                appUsersMap.put(uMail, uPassword);
            }
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
        if (appUsers != null && !appUsers.equals("")) {
            String[] appUsersArray = appUsers.split(FIELD_SEPARATOR);
            for (int i = 0; i < appUsersArray.length; i++) {
                String[] userAndkey = appUsersArray[i].split(USER_KEY_SEPARATOR);
                String uMail = userAndkey[0];
                String uPassword = userAndkey[1];
                appUsersMap.put(uMail, uPassword);
            }
        }
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

                SplashActivity.DATA_STORAGE.putString(context.getResources().getString(KEY_ADD_USER_DICTIONARY), json.toString());

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

                Log.i("JSON_OBJECT_NEW", json.toString());

                SplashActivity.DATA_STORAGE.putString(context.getResources().getString(KEY_ADD_USER_DICTIONARY), json.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return true;
    }



    public static Map<String, String> getAddContactsMapQueque(Context context){
        Map<String, String> appUsersMap = new HashMap<String, String>();
        String appUsers = SplashActivity.DATA_STORAGE.getString(context.getResources().getString(R.string.KEY_APP_USERS));
        try {
            JSONObject json = new JSONObject(appUsers);
            JSONArray ja = json.getJSONArray(KEY_JSON_ADD_USERS_TITLE);
            for(int i = 0; i<ja.length(); i++){
                JSONObject value= ja.getJSONObject(i);
                String uMail = value.getString(KEY_JSON_USER_EMAIL);
                String uContactToAdd = value.getString(KEY_JSON_CONTACT_TO_ADD);
                appUsersMap.put(uMail, uContactToAdd);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return appUsersMap;
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

                SplashActivity.DATA_STORAGE.putString(context.getResources().getString(KEY_ADD_USER_DICTIONARY), json.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return true;
    }




}
