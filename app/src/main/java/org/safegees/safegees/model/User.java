/**
 *   User.java
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


import java.util.ArrayList;

/**
 * Created by victor on 2/1/16.
 */
public class User {
    private String name;
    private String surname;
    private String email;
    private String password;
    private String imagePath;
    private String phoneNumber;
    private String bio;
    private LatLng position;
    private ArrayList <Contact> contacts;
    private ArrayList <AllowedContact> allowedContacts;

    public User(ArrayList<AllowedContact> allowedContacts, String bio, ArrayList<Contact> contacts, String email, String imagePath, String name, String password, String phoneNumber, LatLng position, String surname) {
        this.allowedContacts = allowedContacts;
        this.bio = bio;
        this.contacts = contacts;
        this.email = email;
        this.imagePath = imagePath;
        this.name = name;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.position = position;
        this.surname = surname;
    }

    public ArrayList<AllowedContact> getAllowedContacts() {
        return allowedContacts;
    }

    public void setAllowedContacts(ArrayList<AllowedContact> allowedContacts) {
        this.allowedContacts = allowedContacts;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public ArrayList<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(ArrayList<Contact> contacts) {
        this.contacts = contacts;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

        User user = (User) o;

        return email.equals(user.email);

    }

    @Override
    public int hashCode() {
        return email.hashCode();
    }

    public String getAuth(){
        return this.name+":"+this.password;
    }
}
