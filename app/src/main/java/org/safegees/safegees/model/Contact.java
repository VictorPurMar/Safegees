package org.safegees.safegees.model;


import com.google.android.gms.maps.model.LatLng;

/**
 * Created by victor on 2/1/16.
 */
public class Contact {

    private String name;
    private String email;
    private String image_path;
    private String bio;
    private LatLng location;
    private double last_connection; //millis

    public Contact(String name, double last_connection, LatLng location, String bio, String image_path, String email) {
        this.name = name;
        this.last_connection = last_connection;
        this.location = location;
        this.bio = bio;
        this.image_path = image_path;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public double getLast_connection() {
        return last_connection;
    }

    public void setLast_connection(double last_connection) {
        this.last_connection = last_connection;
    }
}
