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
public class Friend extends PublicUser {

    private Date last_connection_date; //millis

    public Friend(String bio, String publicEmail, Date last_connection_date, LatLng position, String name, String phoneNumber, String surname, String avatar, String avatar_md5) {
        super(bio, publicEmail,name,phoneNumber,position,surname,avatar,avatar_md5);
        this.last_connection_date = last_connection_date;
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
}
