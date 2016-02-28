/**
 *   Friend.java
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
public class Friend extends Contact{

    private Date last_connection_date; //millis

    public Friend(String bio, String publicEmail, String imagePath, Date last_connection_date, LatLng position, String name, String phoneNumber, String surname) {
        super(bio,publicEmail,imagePath,name,phoneNumber,position,surname);
        this.last_connection_date = last_connection_date;

    }

    public Date getLast_connection_date() {
        return last_connection_date;
    }

    public void setLast_connection_date(Date last_connection_date) {
        this.last_connection_date = last_connection_date;
    }

}
