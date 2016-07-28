package org.safegees.safegees.gui.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;

import org.safegees.safegees.R;
import org.safegees.safegees.gui.preferences.SettingsActivity;

public class PreferencesActivity extends PreferenceActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_data_sync);


        //listener on changed sort order preference:
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        SharedPreferences.OnSharedPreferenceChangeListener prefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                Log.i("JOLA","Settings key changed: " + key);
                PrincipalMapActivity.getInstance().getMapFragment().setMapViewDependingConnection();
                PrincipalMapActivity.getInstance().getMapFragment().refreshMap();

            }
        };
        prefs.registerOnSharedPreferenceChangeListener(prefListener);
    }



}
