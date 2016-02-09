package org.safegees.safegees.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;

import org.safegees.safegees.gui.view.MainActivity;

/**
 * Created by victor on 9/2/16.
 */
public class NetworkStateReceiver extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {
        Log.d("Network", "Network connectivity change");
        if(Connectivity.isNetworkAvaiable(context))MainActivity.getInstance().showUpdate();
        else MainActivity.getInstance().hideUpdate();
    }
}
