/**
 *   SafegeesDownloadDataManager.java
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

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.safegees.safegees.gui.view.MainActivity;
import org.safegees.safegees.gui.view.SplashActivity;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by victor on 8/2/16.
 */
public class SafegeesDownloadDataManager {
    private static DownloadTasks mAuthTask = null;
    private Context context;


    public void run(Context context){
        this.context = context;
        mAuthTask = new DownloadTasks(this.context);
        mAuthTask.execute((Void) null);

    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class DownloadTasks extends AsyncTask<Void, Void, Boolean> {
        private Context context;



        DownloadTasks(Context context) {
            this.context = context;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

                SafegeesConnectionManager scc = new SafegeesConnectionManager();
                Map<String,String> appUsersMap = DataQuequesManager.getAppUsersMap(this.context);

                //Get and store contacts data from all the app users
                Iterator it = appUsersMap.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry)it.next();
                    String userMail =  (String) pair.getKey();
                    String userPassword = (String) pair.getValue();
                     try {
                     scc.getContactsData(this.context, userMail, userPassword);
                     } catch (Exception e) {
                         Log.e("GetContactsData", e.getMessage());
                     }
                     it.remove(); // avoids a ConcurrentModificationException
                 }

                //Get general data (POI)
                scc.getPointsOfInterest(this.context);

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            if (success) {
                //If the download was on splash
                //When de download is finished, launch the app
                if(this.context.getClass().equals(SplashActivity.class)){
                    SplashActivity splashActivity =  (SplashActivity) this.context;
                    splashActivity.launchTheApp();
                }else if(this.context.getClass().equals(MainActivity.class)){
                    MainActivity mainActivity =  (MainActivity) this.context;
                    mainActivity.refrehMap();
                }
            } else {
                //When de download is finished, launch the app


            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }
}
