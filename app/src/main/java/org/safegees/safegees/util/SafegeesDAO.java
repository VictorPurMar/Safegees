package org.safegees.safegees.util;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.safegees.safegees.R;
import org.safegees.safegees.gui.view.SplashActivity;
import org.safegees.safegees.model.Contact;
import org.safegees.safegees.model.POI;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by victor on 8/2/16.
 */
public class SafegeesDAO {

    private static SafegeesDAO instance;
    private Context context;
    private ArrayList<Contact> contacts;
    private ArrayList<POI> pois;

    public SafegeesDAO(Context context){
        this.pois = new ArrayList<POI>();
        this.contacts = new ArrayList<Contact>();
        this.context = context;
        this.run();
    }

    public static SafegeesDAO getInstance(Context context) {
        if(instance == null) {
            instance = new SafegeesDAO(context);
        }
        return instance;
    }


    private void run() {
        this.pois = this.getPoisArray();
    }

    private ArrayList<POI> getPoisArray() {

        String poisData = SplashActivity.DATA_STORAGE.getString(this.context.getString(R.string.KEY_POINTS_OF_INTEREST));
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

    public ArrayList<Contact> getContacts() {
        return contacts;
    }

    public ArrayList<POI> getPois() {
        return pois;
    }

}
