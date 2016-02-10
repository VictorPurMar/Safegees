/**
 *   SplashActivity.java
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

package org.safegees.safegees.gui.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.safegees.safegees.R;
import org.safegees.safegees.util.DataQuequesManager;
import org.safegees.safegees.util.Connectivity;
import org.safegees.safegees.util.DataStorageManager;
import org.safegees.safegees.util.SafegeesDAO;
import org.safegees.safegees.util.SafegeesDownloadDataManager;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by victor on 25/12/15.
 */
    public class SplashActivity extends AppCompatActivity {
    public static DataStorageManager DATA_STORAGE;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            DATA_STORAGE = new DataStorageManager(this);

            if(DATA_STORAGE.getString(getResources().getString(R.string.KEY_USER_MAIL)) != null && DATA_STORAGE.getString(getResources().getString(R.string.KEY_USER_MAIL)).length()>0){
                downloadData();
            }else{
                //Start the loggin for result
                Intent loginInt = new Intent(this, LoginActivity.class);
                startActivityForResult(loginInt, 1);
            }




        }

        public void launchMainActivity(){
            // Start the app
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();

        }

    @Override
    protected void onStart() {
        super.onStart();

    }

    //If check login is OK lauch the Main Activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){

                downloadData();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    public void launchTheApp(){
        buildObjects();
        launchMainActivity();
    }

    private void buildObjects() {
        SafegeesDAO sDao = SafegeesDAO.getInstance(this);
    }

    private void downloadData() {
        if (Connectivity.isNetworkAvaiable(this)){
            //Download data
            SafegeesDownloadDataManager sddm = new SafegeesDownloadDataManager();
            sddm.run(this);
        }

        //Show the log if no connection
        Map<String,String> appUsersMap = DataQuequesManager.getAppUsersMap(this);
        Iterator it = appUsersMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            String userMail =  (String) pair.getKey();
            String contactsData = SplashActivity.DATA_STORAGE.getString(getResources().getString(R.string.KEY_CONTACTS_DATA)+"_"+userMail);
            Log.i("CONTACTS_DATA", "User:" + userMail + "    Data:" + contactsData);
            it.remove(); // avoids a ConcurrentModificationException
        }
        String generalData = SplashActivity.DATA_STORAGE.getString(getResources().getString(R.string.KEY_POINTS_OF_INTEREST));
        Log.i("GENERAL_DATA", "Data:" + generalData);
    }
}
