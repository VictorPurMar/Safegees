/**
 *   AllowedContact.java
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

/**
 * Created by victor on 2/1/16.
 */
public class AllowedContact {

    private String email;
    private int positionConfidenciallity;

    public AllowedContact(String email, int positionConfidenciallity) {
        this.email = email;
        this.positionConfidenciallity = positionConfidenciallity;
    }

    public String getEmail() {
        return email;
    }

    public int getPositionConfidenciallity() {
        return positionConfidenciallity;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPositionConfidenciallity(int positionConfidenciallity) {
        this.positionConfidenciallity = positionConfidenciallity;
    }
}
