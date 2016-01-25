package org.safegees.safegees.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by victor on 2/1/16.
 */
public class User {
    private String name;
    private String email;
    private String password;
    private String imagePath;
    private String bio;
    private LatLng position;
    private ArrayList <Contact> contacts;
    private ArrayList <AllowedContact> allowedContacts;

    public User(String name, String email, String password, String imagePath, String bio, LatLng position, ArrayList<Contact> contacts, ArrayList<AllowedContact> allowedContacts) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.imagePath = imagePath;
        this.bio = bio;
        this.position = position;
        this.contacts = contacts;
        this.allowedContacts = allowedContacts;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public LatLng getPosition() {
        return position;
    }

    public void setPosition(LatLng position) {
        this.position = position;
    }

    public ArrayList<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(ArrayList<Contact> contacts) {
        this.contacts = contacts;
    }

    public ArrayList<AllowedContact> getAllowedContacts() {
        return allowedContacts;
    }

    public void setAllowedContacts(ArrayList<AllowedContact> allowedContacts) {
        this.allowedContacts = allowedContacts;
    }
}
