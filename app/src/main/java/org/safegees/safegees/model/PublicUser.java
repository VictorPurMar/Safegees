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
    public String phoneNumber;
    public String bio;
    public LatLng position;
    public String avatar;
    public String avatar_md5;


    public PublicUser(String bio, String publicEmail,  String name, String phoneNumber, LatLng position, String surname, String avatar, String avatar_md5) {
        this.bio = bio;
        this.publicEmail = publicEmail;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.position = position;
        this.surname = surname;
        this.avatar = avatar;
        this.avatar_md5 = avatar_md5;
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAvatar_md5() {
        return avatar_md5;
    }

    public void setAvatar_md5(String avatar_md5) {
        this.avatar_md5 = avatar_md5;
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
            String bio = json.getString("topic")!=null && !json.getString("topic").equals("null")?json.getString("topic"):"";
            String publicEmail = json.getString("email")!=null && !json.getString("email").equals("null")?json.getString("email"):"";
            String name = json.getString("name")!=null && !json.getString("name").equals("null")?json.getString("name"):"";
            String surname = json.getString("surname")!=null && !json.getString("surname").equals("null")?json.getString("surname"):"";
            String phoneNumber = json.getString("telephone")!=null && !json.getString("telephone").equals("null")?json.getString("telephone"):"";
            LatLng position = json.getString("position")!=null && !json.getString("position").equals("null") || !json.getString("position").equals("")?new LatLng(json.getString("position")):null;
            String avatar = json.getString("avatar")!=null && !json.getString("avatar").equals("null")?json.getString("avatar"):"";
            String avatar_md5 = json.getString("avatar_md5")!=null && !json.getString("avatar_md5").equals("null")?json.getString("avatar_md5"):"";


            //Null values because are not implemented on server yet
            pu = new PublicUser(bio,publicEmail,name,phoneNumber,position,surname,avatar,avatar_md5);

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
            json.put("email", pu.getPublicEmail());
            json.put("name", pu.getName());
            json.put("surname", pu.getSurname());
            json.put("telephone", pu.getPhoneNumber());
            json.put("position", pu.getPosition()!=null?pu.getPosition().toString():"");
            json.put("avatar", pu.getBio());
            json.put("avatar_md5", pu.getBio());
            publicObjectStr = json.toString();
        }catch(Exception e){
            Log.e("Caused:", e.getCause().toString());
        }
        return publicObjectStr;
    }

}

