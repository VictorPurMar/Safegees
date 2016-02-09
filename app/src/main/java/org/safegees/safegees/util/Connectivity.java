/**
 *   Connectivity.java
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

import android.net.ConnectivityManager;
import android.content.Context;
import android.net.NetworkInfo;

import java.io.IOException;
import java.net.InetAddress;

/**
 * Connectivity
 *
 * Dectect if device is connected to internet, and if this connection is trought wifi or mobile brand
 *
 * Created by victor on 28/1/16.
 */

public class Connectivity {
    private static int TIME_OUT = 10;
    private static String SAFEGEES_SERVER_URL = "http://safegees.appspot.com/";

    /**
     * Is network avaiable
     *
     * @param context (android)
     * @return true if the device has internet connection
     */
    public static boolean isNetworkAvaiable(Context context){
        ConnectivityManager cm;
        NetworkInfo info = null;
        try {
            cm = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
            info = cm.getActiveNetworkInfo();
        }catch(Exception e){
            e.printStackTrace();
        }
        if (info != null) return true;
        else return false;

    }

    /**
     * Maybe is not allways working.
     *
     * "This method first tries to use ICMP (ICMP ECHO REQUEST). When first step fails, a TCP connection on
     * port 7 (Echo) of the remote host is established." I have found that many Android devices don't support ICMP,
     * and many servers don't accept TCP connections over port 7. isReachable() then simply times out while the
     * server is reachable.
     */
    /**
     * Is SafegeesServer Avaiable
     *
     * @param context (android)
     * @return  true if the mobile can connect with the Safegees Server
     */
    public static boolean isSafegeesServerAvaiable(Context context){
        if (Connectivity.isNetworkAvaiable(context)){
            try {
                return InetAddress.getByName(SAFEGEES_SERVER_URL).isReachable(TIME_OUT);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    /**
     * Is Wifi Connection in the device
     *
     * @param context (android)
     * @return true if the device is conected trought wifi
     */
    public static boolean isWifiConnection(Context context){
        if (Connectivity.isNetworkAvaiable(context)) {
            ConnectivityManager cm;
            NetworkInfo info = null;
            try {
                cm = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
                info = cm.getActiveNetworkInfo();

            } catch (Exception e) {
                e.printStackTrace();
            }

            if (info != null) {
                return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_WIFI);
            }
        }
        return false;
    }

    /**
     * Is Mobile Connection, detects if the device is connected by mobile brand
     * @param context (android)
     * @return true if the device is connected by mobile brand.
     */
    public static boolean isMobileConnection(Context context){
        if (Connectivity.isNetworkAvaiable(context)) {
            ConnectivityManager cm;
            NetworkInfo info = null;
            try {
                cm = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
                info = cm.getActiveNetworkInfo();

            } catch (Exception e) {
                e.printStackTrace();
            }

            if (info != null) {
                return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_MOBILE);
            }
        }
        return false;
    }

}
