/**
 *   PrivateUser.java
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
public class PrivateUser extends Contact {
    private String privateEmail;
    private String password;
    private ArrayList <Friend> friends;
    private ArrayList <AllowedContact> allowedContacts;

    public PrivateUser(ArrayList<AllowedContact> allowedContacts, String bio, ArrayList<Friend> friends, String privateEmail, String imagePath, String name, String password, String phoneNumber, LatLng position, String publicEmail, String surname) {
        super (bio,publicEmail, imagePath, name,phoneNumber,position,surname);
        this.allowedContacts = allowedContacts;
        this.friends = friends;
        this.privateEmail = privateEmail;
        this.password = password;
    }

    public ArrayList<AllowedContact> getAllowedContacts() {
        return allowedContacts;
    }

    public void setAllowedContacts(ArrayList<AllowedContact> allowedContacts) {
        this.allowedContacts = allowedContacts;
    }

    public ArrayList<Friend> getFriends() {
        return friends;
    }

    public void setFriends(ArrayList<Friend> friends) {
        this.friends = friends;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPrivateEmail() {
        return privateEmail;
    }

    public void setPrivateEmail(String privateEmail) {
        this.privateEmail = privateEmail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PrivateUser privateUser = (PrivateUser) o;

        return privateEmail.equals(privateUser.privateEmail);

    }

    @Override
    public int hashCode() {
        return privateEmail.hashCode();
    }

    public String getAuth(){
        return this.getPrivateEmail()+":"+this.password;
    }
}
