package org.safegees.safegees.model;


import com.google.android.gms.maps.model.LatLng;

/**
 * Created by victor on 2/1/16.
 */
public class Contact {

    private String name;
    private String surname;
    private String phoneNumber;
    private String email;
    private String image_path;
    private String bio;
    private LatLng location;
    private double last_connection_date; //millis

    public Contact(String bio, String email, String image_path, double last_connection_date, LatLng location, String name, String phoneNumber, String surname) {
        this.bio = bio;
        this.email = email;
        this.image_path = image_path;
        this.last_connection_date = last_connection_date;
        this.location = location;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.surname = surname;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
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

    public double getLast_connection_date() {
        return last_connection_date;
    }

    public void setLast_connection_date(double last_connection_date) {
        this.last_connection_date = last_connection_date;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
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

        if (Double.compare(contact.last_connection_date, last_connection_date) != 0) return false;
        if (name != null ? !name.equals(contact.name) : contact.name != null) return false;
        if (surname != null ? !surname.equals(contact.surname) : contact.surname != null)
            return false;
        if (phoneNumber != null ? !phoneNumber.equals(contact.phoneNumber) : contact.phoneNumber != null)
            return false;
        if (!email.equals(contact.email)) return false;
        if (image_path != null ? !image_path.equals(contact.image_path) : contact.image_path != null)
            return false;
        if (bio != null ? !bio.equals(contact.bio) : contact.bio != null) return false;
        return !(location != null ? !location.equals(contact.location) : contact.location != null);

    }

    @Override
    public int hashCode() {
        return email.hashCode();
    }
}
