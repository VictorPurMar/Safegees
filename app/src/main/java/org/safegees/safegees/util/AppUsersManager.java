/**
 *   AppUsersManager.java
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

import org.safegees.safegees.R;
import org.safegees.safegees.gui.view.SplashActivity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by victor on 8/2/16.
 */
public class AppUsersManager {
    private static String USER_KEY_SEPARATOR = ":::";
    private static String USER_SEPARATOR = ";;;";

    public static boolean putUserAndKey(Context context, String userMail, String password){
        Map<String, String> appUsersMap = new HashMap<String, String>();
        String appUsers = SplashActivity.DATA_STORAGE.getString(context.getResources().getString(R.string.KEY_APP_USERS));
        if (appUsers != null && !appUsers.equals("")){
            String[] appUsersArray = appUsers.split(USER_SEPARATOR);

            for (int i = 0 ; i < appUsersArray.length ; i++){
                String[] userAndkey = appUsersArray[i].split(USER_KEY_SEPARATOR);
                String uMail = userAndkey[0];
                String uPassword = userAndkey[1];
                appUsersMap.put(uMail, uPassword);
            }
            appUsersMap.put(userMail, password);

            String appUsersString = "";
            Iterator it = appUsersMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                appUsersString = appUsersString + pair.getKey() + USER_KEY_SEPARATOR + pair.getValue()+USER_SEPARATOR;
                it.remove(); // avoids a ConcurrentModificationException
            }
            SplashActivity.DATA_STORAGE.putString(context.getResources().getString(R.string.KEY_APP_USERS),appUsersString);

        }else{
            SplashActivity.DATA_STORAGE.putString(context.getResources().getString(R.string.KEY_APP_USERS), userMail + USER_KEY_SEPARATOR + password + USER_SEPARATOR);
        }
        return true;
    }

    public static String getUserPassword(Context context, String userMail){
        Map<String, String> appUsersMap = new HashMap<String, String>();
        String appUsers = SplashActivity.DATA_STORAGE.getString(context.getResources().getString(R.string.KEY_APP_USERS));
        if (appUsers != null){
            String[] appUsersArray = appUsers.split(USER_SEPARATOR);

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
        String storedPassword = getUserPassword(context,userMail);
        if (userPassword.equals(storedPassword)){
            return true;
        }
        return false;
    }

    public static Map<String, String> getAppUsersMap(Context context){
        Map<String, String> appUsersMap = new HashMap<String, String>();
        String appUsers = SplashActivity.DATA_STORAGE.getString(context.getResources().getString(R.string.KEY_APP_USERS));
        if (appUsers != null && !appUsers.equals("")) {
            String[] appUsersArray = appUsers.split(USER_SEPARATOR);
            for (int i = 0; i < appUsersArray.length; i++) {
                String[] userAndkey = appUsersArray[i].split(USER_KEY_SEPARATOR);
                String uMail = userAndkey[0];
                String uPassword = userAndkey[1];
                appUsersMap.put(uMail, uPassword);
            }
        }
        return appUsersMap;
    }

}
