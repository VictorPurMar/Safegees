/**
 *   SafegeesDAO.java
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
import org.safegees.safegees.gui.view.MainActivity;
import org.safegees.safegees.model.Friend;
import org.safegees.safegees.model.LatLng;
import org.safegees.safegees.model.POI;
import org.safegees.safegees.model.PrivateUser;
import org.safegees.safegees.model.PublicUser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by victor on 8/2/16.
 */
public class SafegeesDAO {

    private static SafegeesDAO instance;
    private Context context;
 //   private PrivateUser privateUser;
    private PublicUser publicUser;
    private ArrayList<Friend> allFriends;
    private ArrayList<String> authorisedFriends;
    private ArrayList<Friend> mutualFriends;
    private ArrayList<Friend> nonAuthorisedFriends;
    private ArrayList<POI> pois;

    public SafegeesDAO(Context context){
        this.pois = new ArrayList<POI>();
        this.allFriends = new ArrayList<Friend>();
        this.authorisedFriends = new ArrayList<String>();
        this.nonAuthorisedFriends = new ArrayList<Friend>();
        this.mutualFriends = new ArrayList<Friend>();
        this.context = context;
        this.run();
    }

    public static SafegeesDAO getInstance(Context context) {
        if(instance == null) {
            instance = new SafegeesDAO(context);
        }
        return instance;
    }

    public static SafegeesDAO refreshInstance(Context context){
        instance = null;
        instance = new SafegeesDAO(context);
        return instance;
    }


    private void run() {

        this.pois = this.getPoisArray();
        this.allFriends = this.getContactsArray();
        this.publicUser = this.getPublicUserObject();
        this.authorisedFriends = this.getAuthorisedFriendsObject();
        this.mutualFriends = this.getMutualFriendsObject();
        this.nonAuthorisedFriends = this.getNonAuthorisedFriends();
  //      this.privateUser = this.getPrivateUserObjet();

    }

    private ArrayList<Friend> getMutualFriendsObject() {
        mutualFriends = new ArrayList<Friend>();
        for (int i = 0; i < allFriends.size(); i++){
            Friend f = allFriends.get(i);
            if (this.authorisedFriends.contains(f.getPublicEmail())) mutualFriends.add(f);
        }
        return mutualFriends;
    }

    private ArrayList<Friend> getNonAuthorisedFriends() {
        nonAuthorisedFriends = new ArrayList<Friend>();
        for (int i = 0; i < allFriends.size(); i++){
            Friend f = allFriends.get(i);
            if (!this.authorisedFriends.contains(f.getPublicEmail())) nonAuthorisedFriends.add(f);
        }
        return nonAuthorisedFriends;
    }

    private ArrayList<String> getAuthorisedFriendsObject() {
        this.authorisedFriends = new ArrayList<String>();
        String contactsData = MainActivity.DATA_STORAGE.getString(context.getResources().getString(R.string.KEY_AUTHORIZED) + "_" + MainActivity.DATA_STORAGE.getString(context.getResources().getString(R.string.KEY_USER_MAIL)));
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(contactsData);
            Log.i("JSON FRIENDS", jsonArray.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        if (jsonArray != null && jsonArray.length() > 0){
            for (int i = 0 ; i < jsonArray.length() ; i++){
                try {
                    String authorisedEmail = jsonArray.getString(i);
                    this.authorisedFriends.add(authorisedEmail);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
        return authorisedFriends;
    }

    private PrivateUser getPrivateUserObjet(){
        return new PrivateUser(null,this.publicUser.getBio(), this.mutualFriends, MainActivity.DATA_STORAGE.getString(context.getResources().getString(R.string.KEY_USER_MAIL)), null,this.publicUser.getName(),MainActivity.DATA_STORAGE.getString(context.getResources().getString(R.string.KEY_USER_PASSWORD)),this.publicUser.getPhoneNumber(), this.publicUser.getPosition(),this.publicUser.getSurname(), this.publicUser.getAvatar(), this.publicUser.getAvatar_md5());
    }

    private PublicUser getPublicUserObject() {
        Log.e("getPublicUserObject()", "Start method");
        String contactsData = MainActivity.DATA_STORAGE.getString(context.getResources().getString(R.string.KEY_USER_BASIC) + "_" + MainActivity.DATA_STORAGE.getString(context.getResources().getString(R.string.KEY_USER_MAIL)));
        JSONObject pubUserJSON = null;
        PublicUser pu = null;
        try{
            pubUserJSON = new JSONObject();
            try {
                pubUserJSON = new JSONObject(contactsData);
            }catch (Exception e){
                Log.e("NONE FRIENDS", e.getMessage());
            }
            pu = PublicUser.getPublicUserFromJSON(pubUserJSON.toString());
        }catch (Exception e){

        }

        return pu;
    }


    private ArrayList<Friend> getContactsArray() {
        this.mutualFriends = new ArrayList<Friend>();
        String contactsData = MainActivity.DATA_STORAGE.getString(context.getResources().getString(R.string.KEY_CONTACTS_DATA) + "_" + MainActivity.DATA_STORAGE.getString(context.getResources().getString(R.string.KEY_USER_MAIL)));
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(contactsData);
            Log.i("JSON CONTACTS", jsonArray.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        if (jsonArray != null && jsonArray.length() > 0){
            for (int i = 0 ; i < jsonArray.length() ; i++){
                try {
                    JSONObject json = (JSONObject) jsonArray.get(i);
                    String stringPosition = json.getString("position");
                    LatLng position = getLatLngFromStringPosition(stringPosition);
                    String dateLastPositionString = json.getString("date_last_position");
                    Date dateLastPosition = null;
                    if (dateLastPositionString != null && !dateLastPositionString.equals(""))dateLastPosition = getDateAddedFromStringDate(dateLastPositionString);
                    String name = json.getString("name");
                    String surname = json.getString("surname");
                    String phone = json.getString("telephone");
                    String avatar_url = json.getString("avatar");
                    String avatar_md5 = json.getString("avatar_md5");
                    //String email = json.getString("email");
                    String bio =null;
                    try {
                        bio = json.getString("topic");
                    }catch (Exception e){
                        Log.e("Error in topic", e.getMessage());
                    }
                    String publicEmail = json.getString("email");

                    //Null values because are not implemented on server yet
                    Friend friend = new Friend(bio,publicEmail,dateLastPosition, position, name, phone, surname,avatar_url,avatar_md5);
                    this.mutualFriends.add(friend);
                }catch(Exception e){
                    Log.e("Caused:", e.getCause().toString());
                }
            }

        }

        return this.mutualFriends;
    }

    private ArrayList<POI> getPoisArray() {
        this.pois = new ArrayList<POI>();
        String poisData = MainActivity.DATA_STORAGE.getString(this.context.getString(R.string.KEY_POINTS_OF_INTEREST));
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(poisData);
            Log.i("JSON POI", jsonArray.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        if (jsonArray != null && jsonArray.length() > 0){
            for (int i = 0 ; i < jsonArray.length() ; i++){
                try {
                    JSONObject json = (JSONObject) jsonArray.get(i);
                    String stringPosition = json.getString("position");
                    LatLng position = getLatLngFromStringPosition(stringPosition);
                    String dateAddedString = json.getString("date_added");
                    Date dateAdded = getDateAddedFromStringDate(dateAddedString);
                    String title = json.getString("title");
                    String description = json.getString("description");

                    //Null values because are not implemented on server yet
                    POI poi = new POI(null, null, description, null, null, title, position, dateAdded);
                    this.pois.add(poi);

                }catch(Exception e){
                    Log.e("Caused:", e.getCause().toString());
                }
            }

        }

        return this.pois;
    }

    private LatLng getLatLngFromStringPosition(String position){
        String[] latAndLongArray = position.split(",");
        double latitude = Double.parseDouble(latAndLongArray[0]);
        double longitude = Double.parseDouble(latAndLongArray[1]);
        return new LatLng(latitude,longitude);
    }

    private Date getDateAddedFromStringDate(String strignDate){
        String[] dayAndHourArray = strignDate.split("-");
        String[] yearMonthAndDayArray = dayAndHourArray[0].split("/");
        String[] hourMinuteAndSecondArray = dayAndHourArray[1].split(":");
        int year = Integer.parseInt(yearMonthAndDayArray[0]);
        int month = Integer.parseInt(yearMonthAndDayArray[1]);
        int day = Integer.parseInt(yearMonthAndDayArray[2]);
        int hour = Integer.parseInt(hourMinuteAndSecondArray[0]);
        int minute = Integer.parseInt(hourMinuteAndSecondArray[1]);
        int second = Integer.parseInt(hourMinuteAndSecondArray[2]);

        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        Date date = calendar.getTime();

        return date;
    }




    //Getters

    public ArrayList<Friend> getMutualFriends() {
        return mutualFriends;
    }

    public ArrayList<POI> getPois() {
        return pois;
    }


    public PublicUser getPublicUser() {
        return publicUser;
    }

    public ArrayList<Friend> getAllFriends() {
        return allFriends;
    }

    public ArrayList<Friend> getNonAutorisedFriends(){
        return nonAuthorisedFriends;
    }

    public ArrayList<String> getAuthorisedFriends() {
        return authorisedFriends;
    }





    public static void close(){
        instance = null;
    }
}
