/**
 *   DataStorageManager.java
 *
 *   This class allows to store in Shared Preferences memory some kind of data.
 *   This was made to store String, int, boolean, and long values with a String key as identificator
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

package org.safegees.safegees.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class DataStorageManager {
    private SharedPreferences config;
    private SharedPreferences.Editor configEditor;

    public DataStorageManager(Activity _activity) {
        config =  _activity.getPreferences(Context.MODE_PRIVATE);
        configEditor = config.edit();
    }

    public void putString(String key, String value) {
        configEditor.putString(key, value);
        configEditor.commit();
    }

    public void putLong(String key, long value) {
        configEditor.putLong(key, value);
        configEditor.commit();
    }

    public void putInt(String key, int value) {
        configEditor.putInt(key, value);
        configEditor.commit();
    }

    public void putBoolean(String key, boolean value) {
        configEditor.putBoolean(key, value);
        configEditor.commit();
    }

    public String getString(String key) {
        return config.getString(key, "");
    }

    public long getLong(String key) {
        return config.getLong(key, 0);
    }

    public int getInt(String key) {
        return config.getInt(key, 0);
    }

    public boolean getBoolean(String key) {
        return config.getBoolean(key, false);
    }


    public boolean contains(String key) {
        return config.contains(key);
    }

    public void remove(String key) {
        configEditor.remove(key);
        configEditor.commit();
    }

    public void clearAll() {
        configEditor.clear();
        configEditor.commit();
    }

}
