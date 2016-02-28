package org.safegees.safegees.model;

import android.util.Log;

/**
 * Created by victor on 21/2/16.
 */
public class LatLng {
    private double latitude;
    private double longitude;

    public LatLng(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Ex 1.321:2.342
    public LatLng(String latlongString){
        try{
            String[] latLongArray = latlongString.split(",");
            this.latitude = Double.parseDouble(latLongArray[0]);
            this.longitude = Double.parseDouble(latLongArray[1]);
        }catch(Exception e){
            Log.e("MALFORMED LATLNG", e.getMessage().toString());
        }
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return latitude + "," + longitude;
    }

}
