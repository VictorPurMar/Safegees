/**
 *   ShareDataController.java
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
import android.view.Gravity;
import android.widget.Toast;

import org.safegees.safegees.gui.view.MainActivity;
import org.safegees.safegees.gui.view.SplashActivity;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by victor on 8/2/16.
 */
public class ShareDataController {
    private static DownloadAndSendTasks dAndSTask = null;
    private static SendAddContactTask sTask = null;
    private Context context;
    private String userEmail;
    private String contactToAdd;


    public void run(Context context) {
        this.context = context;
        dAndSTask = new DownloadAndSendTasks(this.context);
        dAndSTask.execute((Void) null);
    }

    public void addContact(Context context, String userMail, String contactToAdd) {
        this.context = context;
        this.userEmail = userMail;
        this.contactToAdd = contactToAdd;
        sTask = new SendAddContactTask(this.context, this.userEmail, this.contactToAdd);
        sTask.execute((Void) null);
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class DownloadAndSendTasks extends AsyncTask<Void, Void, Boolean> {
        private Context context;


        DownloadAndSendTasks(Context context) {
            this.context = context;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            SafegeesConnectionManager scc = new SafegeesConnectionManager();

            //Get and store the contacts data from all the app users queque
            getAppUsersData(scc);

            //Send all the Add Contacts Queque fields
            sendAddContactsQueque(scc);

            //Get general data (POI)
            scc.getPointsOfInterest(this.context);

            // TODO: register the new account here.
            return true;
        }

        private void sendAddContactsQueque(SafegeesConnectionManager scc) {

            Map<String, String> adContactsMap = DataQuequesManager.getAddContactsMapQueque(this.context);
            Iterator it = adContactsMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                String userMail = (String) pair.getKey();
                String contactEmail = (String) pair.getValue();
                String userPassword = DataQuequesManager.getUserPassword(context, userMail);
                try {
                    if (scc.addNewContact(this.context, userMail, userPassword, contactEmail)) {
                        DataQuequesManager.removeContactToAddInQueque(this.context, userMail, contactEmail);
                    }
                } catch (Exception e) {
                    Log.e("GetContactsData", e.getMessage());
                }
                it.remove(); // avoids a ConcurrentModificationException
            }

        }


        private void getAppUsersData(SafegeesConnectionManager scc) {
            Map<String, String> appUsersMap = DataQuequesManager.getAppUsersMap(this.context);

            //Get and store contacts data from all the app users
            Iterator it = appUsersMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                String userMail = (String) pair.getKey();
                String userPassword = (String) pair.getValue();
                try {
                    scc.getContactsData(this.context, userMail, userPassword);
                } catch (Exception e) {
                    Log.e("GetContactsData", e.getMessage());
                }
                it.remove(); // avoids a ConcurrentModificationException
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {


            dAndSTask = null;
            if (success) {
                //If the download was on splash
                //When de download is finished, launch the app
                if (this.context.getClass().equals(SplashActivity.class)) {
                    SplashActivity splashActivity = (SplashActivity) this.context;
                    splashActivity.launchTheApp();
                } else if (this.context.getClass().equals(MainActivity.class)) {
                    MainActivity mainActivity = (MainActivity) this.context;
                    mainActivity.refreshMap();
                }
            } else {

            }
        }

        @Override
        protected void onCancelled() {
            dAndSTask = null;;
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class SendAddContactTask extends AsyncTask<Void, Void, Boolean> {
        private Context context;
        private String userEmail;
        private String contactToAdd;


        SendAddContactTask(Context context, String userEmail, String contactToAdd) {
            this.context = context;
            this.userEmail = userEmail;
            this.contactToAdd = contactToAdd;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            SafegeesConnectionManager scc = new SafegeesConnectionManager();
            String userPassword = DataQuequesManager.getUserPassword(context, this.userEmail);
            if (Connectivity.isNetworkAvaiable(this.context)) {
                return scc.addNewContact(this.context, userEmail, userPassword, contactToAdd);
            }else{
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {


            sTask = null;
            if (success) {
                Toast toast = Toast.makeText(context, "The contact was added correctly", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            } else {
                    if (!Connectivity.isNetworkAvaiable(this.context)) {
                        Toast toast = Toast.makeText(context, "No internet connection. The contact will be stored and sended when do you have internet and update the app", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        DataQuequesManager.putUserAndKeyInAddUserQueque(this.context, userEmail, contactToAdd);
                    }else{
                        Toast toast = Toast.makeText(context, "The contact isn't registered at Safegges and cant be added. ", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER , 0, 0);
                        toast.show();
                    }

            }
        }

        @Override
        protected void onCancelled() {
            sTask = null;;
        }


    }





}