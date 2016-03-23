package org.safegees.safegees.model;

import android.util.Log;

import org.json.JSONObject;

/**
 * Created by victor on 28/2/16.
 */
public class PublicUser {
    public String name;
    public String surname;
    public String publicEmail;
    //public String imagePath;
    public String phoneNumber;
    public String bio;
    public LatLng position;

    public PublicUser(String bio, String publicEmail,  String name, String phoneNumber, LatLng position, String surname) {
        this.bio = bio;
        this.publicEmail = publicEmail;
        //this.imagePath = imagePath;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.position = position;
        this.surname = surname;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    /*
    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
    */

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LatLng getPosition() {
        return position;
    }

    public void setPosition(LatLng position) {
        this.position = position;
    }

    public String getPublicEmail() {
        return publicEmail;
    }

    public void setPublicEmail(String publicEmail) {
        this.publicEmail = publicEmail;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PublicUser publicUser = (PublicUser) o;

        return publicEmail.equals(publicUser.publicEmail);

    }

    @Override
    public int hashCode() {
        return publicEmail.hashCode();
    }

    public static PublicUser getPublicUserFromJSON(String privateUserJSON){
        PublicUser pu = null;
        try {
            JSONObject json = new JSONObject(privateUserJSON);
            String bio = json.getString("topic")!=null?json.getString("topic"):"";
            String publicEmail = json.getString("email")!=null?json.getString("public_email"):"";
            String name = json.getString("name")!=null?json.getString("name"):"";
            String surname = json.getString("surname")!=null?json.getString("surname"):"";
            String phoneNumber = json.getString("telephone")!=null?json.getString("telephone"):"";
            LatLng position = json.getString("position")!=null?new LatLng(json.getString("position")):null;

            //Null values because are not implemented on server yet
            pu = new PublicUser(bio,publicEmail,name,phoneNumber,position,surname);

        }catch(Exception e){
            Log.e("Caused:", e.getCause().toString());
        }
        return pu;
    }

    public static String getJSONStringFromPublicUser(PublicUser pu){
        String publicObjectStr = null;
        try {
            JSONObject json = new JSONObject();
            json.put("topic", pu.getBio());
            json.put("public_email", pu.getPublicEmail());
            json.put("name", pu.getName());
            json.put("surname", pu.getSurname());
            json.put("telephone", pu.getPhoneNumber());
            json.put("position", pu.getPosition().toString());
            publicObjectStr = json.toString();
        }catch(Exception e){
            Log.e("Caused:", e.getCause().toString());
        }
        return publicObjectStr;
    }

}

