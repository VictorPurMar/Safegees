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
import org.safegees.safegees.gui.view.PrincipalMapActivity;
import org.safegees.safegees.model.LatLng;
import org.safegees.safegees.model.PrivateUser;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by victor on 8/2/16.
 */
public class ShareDataController {
    private static DownloadAndSendTasks dAndSTask = null;
    private static SendAddContactTask addContactTask = null;
    private static SendUserPosition sendUPosTask = null;
    private Context context;
    private String userEmail;
    private String contactToAdd;
    private LatLng latLng;


    public void run(Context context) {
        this.context = context;
        dAndSTask = new DownloadAndSendTasks(this.context);
        dAndSTask.execute((Void) null);
    }

    public void addContact(Context context, String userMail, String contactToAdd) {
        this.context = context;
        this.userEmail = userMail;
        this.contactToAdd = contactToAdd;
        addContactTask = new SendAddContactTask(this.context, this.userEmail, this.contactToAdd);
        addContactTask.execute((Void) null);
    }

    public void sendUserPosition(Context context, String userMail, LatLng latLng) {
        this.context = context;
        this.userEmail = userMail;
        this.latLng = latLng;
        sendUPosTask = new SendUserPosition(this.context, this.userEmail, this.latLng);
        sendUPosTask.execute((Void) null);
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

            if (Connectivity.isNetworkAvaiable(this.context)) {

                //Get general data (POI)
                scc.getPointsOfInterest(this.context);

                //Get third partners klm to store as GeoJSON
                scc.getThirdKLM(this.context);

                //Get and store the contacts data from all the app users queque
                getAppUsersData(scc);

                //Send all the Add Contacts Queque fields
                sendAddContactsQueque(scc);

                //send user info basic data
                //name, surname, 

                //Send all the PrivateUser Positions Queque

                //sendUserPositionsQueque(scc);

                return true;
            }

            return false;
        }

        private void sendUserPositionsQueque(SafegeesConnectionManager scc) {
            Map<String, String> userPositionsMap = StoredDataQuequesManager.getUserPositionsMap(this.context);
            Iterator it = userPositionsMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                String userMail = (String) pair.getKey();
                String userPosition = (String) pair.getValue();
                String userPassword = StoredDataQuequesManager.getUserPassword(context, userMail);
                try {
                    scc.updateUserPosition(this.context, userMail, userPassword, userPosition);
                } catch (Exception e) {
                    Log.e("UPDATE_USER_POSITION", e.getMessage());
                }
                it.remove(); // avoids a ConcurrentModificationException
            }
        }

        private void sendAddContactsQueque(SafegeesConnectionManager scc) {

            Map<String, String> adContactsMap = StoredDataQuequesManager.getAddContactsMapQueque(this.context);
            Iterator it = adContactsMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                String userMail = (String) pair.getKey();
                String contactEmail = (String) pair.getValue();
                String userPassword = StoredDataQuequesManager.getUserPassword(context, userMail);
                try {
                    if (scc.addNewContact(this.context, userMail, userPassword, contactEmail)) {
                        StoredDataQuequesManager.removeContactToAddInQueque(this.context, userMail, contactEmail);
                    }
                } catch (Exception e) {
                    Log.e("GetContactsData", e.getMessage());
                }
                it.remove(); // avoids a ConcurrentModificationException
            }

        }


        private void getAppUsersData(SafegeesConnectionManager scc) {
            Map<String, String> appUsersMap = StoredDataQuequesManager.getAppUsersMap(this.context);

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
                if (this.context.getClass().equals(MainActivity.class)) {
                    MainActivity mainActivity = (MainActivity) this.context;
                    mainActivity.launchTheApp();
                } else if (this.context.getClass().equals(PrincipalMapActivity.class)) {
                    PrincipalMapActivity principalMapActivity = (PrincipalMapActivity) this.context;
                    principalMapActivity.getMapFragment().refreshMap();
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
            String userPassword = StoredDataQuequesManager.getUserPassword(context, this.userEmail);
            if (Connectivity.isNetworkAvaiable(this.context)) {
                return scc.addNewContact(this.context, userEmail, userPassword, contactToAdd);
            }else{
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {


            addContactTask = null;
            if (success) {
                Toast toast = Toast.makeText(context, this.contactToAdd + " was added correctly", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            } else {
                    if (!Connectivity.isNetworkAvaiable(this.context)) {
                        Toast toast = Toast.makeText(context, "No internet connection. The contact "+ this.contactToAdd  +" will be stored and sended when do you have internet and update the app", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        StoredDataQuequesManager.putUserAndKeyInAddUserQueque(this.context, userEmail, contactToAdd);
                    }else{
                        Toast toast = Toast.makeText(context, "The contact "+ this.contactToAdd +" isn't registered at Safegges and cant be added. ", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER , 0, 0);
                        toast.show();
                    }

            }
        }

        @Override
        protected void onCancelled() {
            addContactTask = null;;
        }


    }


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class SendUserPosition extends AsyncTask<Void, Void, Boolean> {
        private Context context;
        private String userEmail;
        private LatLng latLng;


        public SendUserPosition(Context context, String userEmail, LatLng latLng) {
            this.context = context;
            this.userEmail = userEmail;
            this.latLng = latLng;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            SafegeesConnectionManager scc = new SafegeesConnectionManager();

            if (Connectivity.isNetworkAvaiable(this.context)) {
                String userPassword = StoredDataQuequesManager.getUserPassword(this.context, this.userEmail);
                String latLongString = this.latLng.toString();
                return (scc.updateUserPosition(this.context, this.userEmail, userPassword,latLongString));
            }
            return false;
        }


        @Override
        protected void onPostExecute(final Boolean success) {


            sendUPosTask = null;
            if (success) {
               Log.i("UPDATE_POSITION", "The position was updated correctly");
            } else {
                Log.i("UPDATE_POSITION", "The position wasn't updated correctly");
            }
        }

        @Override
        protected void onCancelled() {
            sendUPosTask = null;;
        }
    }





}
