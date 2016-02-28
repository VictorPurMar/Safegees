package org.safegees.safegees.model;

/**
 * Created by victor on 28/2/16.
 */
public class Contact {
    public String name;
    public String surname;
    public String publicEmail;
    public String imagePath;
    public String phoneNumber;
    public String bio;
    public LatLng position;

    public Contact(String bio, String publicEmail, String imagePath, String name, String phoneNumber, LatLng position, String surname) {
        this.bio = bio;
        this.publicEmail = publicEmail;
        this.imagePath = imagePath;
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

        Contact contact = (Contact) o;

        return publicEmail.equals(contact.publicEmail);

    }

    @Override
    public int hashCode() {
        return publicEmail.hashCode();
    }

}

