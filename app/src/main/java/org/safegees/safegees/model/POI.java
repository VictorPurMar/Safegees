package org.safegees.safegees.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

/**
 * Created by victor on 2/1/16.
 */
public class POI {

    private LatLng position;
    private String name;
    private String description;
    private String imagePath;
    private Date postDate;
    private Date expirationDate;
    private String author;                       //Posibility to develope: all the orgs included a a list. With photo and bio.
    private String category;


    public POI(String author, String category, String description, Date expirationDate, String imagePath, String name, LatLng position, Date postDate) {
        this.author = author;
        this.category = category;
        this.description = description;
        this.expirationDate = expirationDate;
        this.imagePath = imagePath;
        this.name = name;
        this.position = position;
        this.postDate = postDate;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
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

    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }

    @Override
    public String toString() {
        return "POI{" +
                "author='" + author + '\'' +
                ", position=" + position +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", postDate=" + postDate +
                ", expirationDate=" + expirationDate +
                ", category='" + category + '\'' +
                '}';
    }
}

