/**
 *   NetworkStateReceiver.java
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

package org.safegees.safegees.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.safegees.safegees.gui.view.MainActivity;

/**
 * Created by victor on 9/2/16.
 */
public class NetworkStateReceiver extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {
        Log.d("Network", "Network connectivity change");
        if(Connectivity.isNetworkAvaiable(context)) {
            if (MainActivity.getInstance() != null) MainActivity.getInstance().showUpdateFloatingButton();
        }
        else {
            if (MainActivity.getInstance() != null)MainActivity.getInstance().hideUpdateFloatingButton();
        }
    }
}
