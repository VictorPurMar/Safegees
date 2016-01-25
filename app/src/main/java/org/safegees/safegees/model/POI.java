package org.safegees.safegees.model;

import com.google.android.gms.maps.model.LatLng;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by victor on 2/1/16.
 */
public class POI {

    private LatLng position;
    private String name;
    private String description;
    private String image_path;
    private Date post_date;
    private Date exiration_date;
    private String author;                       //Posibility to develope: all the orgs included a a list. With photo and bio.

    public POI(LatLng position, String name, String description, String image_path, Date post_date, Date exiration_date, String author) {
        this.position = position;
        this.name = name;
        this.description = description;
        this.image_path = image_path;
        this.post_date = post_date;
        this.exiration_date = exiration_date;
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getExiration_date() {
        return exiration_date;
    }

    public void setExiration_date(Date exiration_date) {
        this.exiration_date = exiration_date;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LatLng getPosition() {
        return position;
    }

    public void setPosition(LatLng position) {
        this.position = position;
    }

    public Date getPost_date() {
        return post_date;
    }

    public void setPost_date(Date post_date) {
        this.post_date = post_date;
    }
}

