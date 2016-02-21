/**
 *   Contact.java
 *
 *   Future class description
 *
 *
 *   Copyright (C) 2016  Victor Purcallas <vpurcallas@gmail.com>
 *
 *   Safegees is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Safegees is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with ARcowabungaproject.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.safegees.safegees.model;



import java.util.Date;

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
    private LatLng position;
    private Date last_connection_date; //millis

    public Contact(String bio, String email, String image_path, Date last_connection_date, LatLng position, String name, String phoneNumber, String surname) {
        this.bio = bio;
        this.email = email;
        this.image_path = image_path;
        this.last_connection_date = last_connection_date;
        this.position = position;
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

    public Date getLast_connection_date() {
        return last_connection_date;
    }

    public void setLast_connection_date(Date last_connection_date) {
        this.last_connection_date = last_connection_date;
    }

    public LatLng getPosition() {
        return position;
    }

    public void setPosition(LatLng location) {
        this.position = location;
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

        return email.equals(contact.email);

    }

    @Override
    public int hashCode() {
        return email.hashCode();
    }
}
