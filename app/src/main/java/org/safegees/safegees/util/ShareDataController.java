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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.safegees.safegees.R;
import org.safegees.safegees.gui.view.MainActivity;
import org.safegees.safegees.gui.view.PrincipalMapActivity;
import org.safegees.safegees.model.Friend;
import org.safegees.safegees.model.LatLng;
import org.safegees.safegees.model.PrivateUser;
import org.safegees.safegees.model.PublicUser;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by victor on 8/2/16.
 */
public class ShareDataController {
    private static DownloadAndSendTasks dAndSTask = null;
    private static SendAddContactTask addContactTask = null;
    private static SendUserPosition sendUPosTask = null;
    private static SendUserBasicData sendUserBasicData = null;
    private Context context;
    private String userEmail;
    private String contactToAdd;
    private LatLng latLng;
    private PublicUser publicUser;


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

    public void sendUserBasicData(Context context, String userMail, PublicUser publicUser) {
        this.context = context;
        this.userEmail = userMail;
        this.publicUser = publicUser;
        sendUserBasicData = new SendUserBasicData(this.context, this.userEmail, this.publicUser);
        sendUserBasicData.execute((Void) null);
    }

    public void sendUserImageFile(Context context, String userMail) {
        this.context = context;
        this.userEmail = userMail;
        UserImageSender sui = new UserImageSender(this.context, this.userEmail);
        sui.execute((Void) null);
    }

    public void getPublicUserImages(Context context, ArrayList<PublicUser> publicUsers){

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

                //To do
                //User public data if new
                sendUserDataQueque(scc);

                //Get and store the contacts data from all the app users queque
                getAppUsersData(scc);

                //Get general data (POI)
                scc.getPointsOfInterest(this.context);

                //Its done but unactive to first release
                //Get third partners klm to store as GeoJSON
                scc.getThirdKLM(this.context);

                //Send all the Add Contacts Queque fields
                sendAddContactsQueque(scc);

                //NOT IMPLEMENTED IN SERVER
                //Send all the Remove PublicUser Queque fields
                sendDeleteContactsQueque(scc);

                //Send all the PrivateUser Positions Queque
                //In send the basic user data the position will be sended
                sendUserPositionsQueque(scc);
                
                //Send the user profile image 
                sendUserProfileImage(scc);

                //Download the rest of images
                //It must be placed at final because it builds the DAO with the updated data
                downloadContactImages();

                return true;
            }

            return false;
        }

        private void sendUserProfileImage(SafegeesConnectionManager scc) {
            Map<String, String> appUsersMap = StoredDataQuequesManager.getAppUsersMap(this.context);            Set<String> emailUsers = appUsersMap.keySet();
            Iterator it = emailUsers.iterator();
            while(it.hasNext()){
                String userEmail = (String) it.next();
                String userPassword = StoredDataQuequesManager.getUserPassword(this.context, userEmail);
                    //Try if exist
                    if(MainActivity.DATA_STORAGE.getBoolean(context.getResources().getString(R.string.KEY_USER_IMAGES_TO_UPLOAD) + userEmail)){
                        String filepath = ImageController.getUserImageFileNameByEmail(context, userEmail);
                        File file = new File(filepath);
                        if (scc.uploadProfileImage(context, userEmail, userPassword ,file)) MainActivity.DATA_STORAGE.putBoolean(context.getResources().getString(R.string.KEY_USER_IMAGES_TO_UPLOAD) + userEmail, false);
                    }
            }
        }

        //The image download needs the objects builded on DAO
        private void downloadContactImages() {
            //Build the DAO
            //It must placed after retrieve all the rest of the info
            SafegeesDAO dao = SafegeesDAO.getInstance(this.context);
            ArrayList<Friend> friends = dao.getMutualFriends();
            //Add friends images
            ArrayList<PublicUser> publicUsers = new ArrayList<PublicUser>();
            //Add the user image
            publicUsers.add(dao.getPublicUser());
            publicUsers.addAll(friends);
            DownloadImagesTask downloadImagesTask = new DownloadImagesTask(this.context,publicUsers);
            downloadImagesTask.execute();
        }


        private void sendUserPositionsQueque(SafegeesConnectionManager scc) {
            Map<String, String> userPositionsMap = StoredDataQuequesManager.getUserPositionsMap(this.context);
            if (userPositionsMap != null) {
                Iterator it = userPositionsMap.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry) it.next();
                    String userMail = (String) pair.getKey();
                    String userPosition = (String) pair.getValue();
                    String userPassword = StoredDataQuequesManager.getUserPassword(this.context, userEmail);
                    try {
                        if(scc.updateUserPosition(context, userMail, userPassword,userPosition))StoredDataQuequesManager.removeUserPositionInQueque(context,userMail,userPosition);
                    } catch (Exception e) {
                        Log.e("sendUserPositionsQueque", e.getMessage());
                    }
                    it.remove(); // avoids a ConcurrentModificationException
                }
            }
        }


        private void sendUserDataQueque(SafegeesConnectionManager scc) {
            Map<String, String> appUsersMap = StoredDataQuequesManager.getUserBasicDataMap(this.context);
            Iterator it = appUsersMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                String userMail = (String) pair.getKey();
                String userPassword = StoredDataQuequesManager.getUserPassword(context, userMail);
                String userDataStoredInQueque = (String) pair.getValue();
                try {
                    //String publicUserStr = this.context.getResources().getString(R.string.KEY_USER_BASIC)+ "_" + userMail;
                    String userJson = userDataStoredInQueque;
                    if (userJson!= null){
                        //NEW
                        //String userJson = MainActivity.DATA_STORAGE.getString(publicUserStr);
                        Log.e("USER_JSON", userJson);
                        //PublicUser pu = PublicUser.getPublicUserFromJSON(publicUserStr);
                        PublicUser pu = PublicUser.getPublicUserFromJSON(userJson);

                        if (pu != null){
                            PrivateUser pr = new PrivateUser(userMail, userPassword,pu);
                            if (pr != null)
                                if(scc.updateUserBasic(pr))StoredDataQuequesManager.removeUserBasicDataInQueque(context, userMail, userJson);
                        }
                    }
                    scc.getUserBasic(this.context, userMail, userPassword);
                } catch (Exception e) {
                    Log.e("sendUserDataQueque", e.getMessage());
                }
                it.remove(); // avoids a ConcurrentModificationException
            }
        }

        private void sendAddContactsQueque(SafegeesConnectionManager scc) {

            Map<String, String> adContactsMap = StoredDataQuequesManager.getAddContactsMapQueque(this.context);
            if (adContactsMap != null) {
                Iterator it = adContactsMap.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry) it.next();
                    String userMail = (String) pair.getKey();
                    String contactEmail = (String) pair.getValue();
                    String userPassword = StoredDataQuequesManager.getUserPassword(context, userMail);
                    try {
                        if (scc.addNewContact(userMail, userPassword, contactEmail)) {
                            StoredDataQuequesManager.removeContactToAddInQueque(this.context, userMail, contactEmail);
                        }
                    } catch (Exception e) {
                        Log.e("sendAddContactsQueque", e.getMessage());
                    }
                    it.remove(); // avoids a ConcurrentModificationException
                }
            }
        }

        ///////////////////////////
        //NOT IMPLEMENTED IN SERVER
        ///////////////////////////

        private void sendDeleteContactsQueque(SafegeesConnectionManager scc) {

            Map<String, String> adContactsMap = StoredDataQuequesManager.getDeleteContactsMapQueque(this.context);
            if (adContactsMap != null) {
                Iterator it = adContactsMap.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry) it.next();
                    String userMail = (String) pair.getKey();
                    String contactEmail = (String) pair.getValue();
                    String userPassword = StoredDataQuequesManager.getUserPassword(context, userMail);
                    try {
                        if (scc.deleteContact(userMail, userPassword, contactEmail)) {
                            StoredDataQuequesManager.removeContactToDeleteInQueque(this.context, userMail, contactEmail);
                        }
                    } catch (Exception e) {
                        Log.e("sendDeleteContactsQu", e.getMessage());
                    }
                    it.remove(); // avoids a ConcurrentModificationException
                }
            }
        }


        private void getAppUsersData(SafegeesConnectionManager scc) {


            Map<String, String> appUsersMap = StoredDataQuequesManager.getAppUsersMap(this.context);
            if (appUsersMap!= null) {
                //Get app Users Basic Data
                Iterator itBasicUserData = appUsersMap.entrySet().iterator();

                //NOT IMPLEMENTED ON SERVER YET

            while (itBasicUserData.hasNext()) {
                Map.Entry pair = (Map.Entry) itBasicUserData.next();
                String userMail = (String) pair.getKey();
                String userPassword = (String) pair.getValue();
                try {
                    scc.getUserBasic(this.context, userMail, userPassword);
                } catch (Exception e) {
                    Log.e("getAppUsersData", e.getMessage());
                }
                //it removes the a
                //itBasicUserData.remove();
            }



                //Get and store contacts data from all the app users
                Iterator itContacts = appUsersMap.entrySet().iterator();
                while (itContacts.hasNext()) {
                    Map.Entry pair = (Map.Entry) itContacts.next();
                    String userMail = (String) pair.getKey();
                    String userPassword = (String) pair.getValue();
                    try {
                        scc.getContactsData(this.context, userMail, userPassword);
                    } catch (Exception e) {
                        Log.e("GetContactsData", e.getMessage());
                    }
                    //itContacts.remove(); // avoids a ConcurrentModificationException
                }

                //Get and store the authorized by user contacts in a JSON simple array
                Iterator itAuthorisedByUsersContacts = appUsersMap.entrySet().iterator();
                while (itAuthorisedByUsersContacts.hasNext()) {
                    Map.Entry pair = (Map.Entry) itAuthorisedByUsersContacts.next();
                    String userMail = (String) pair.getKey();
                    String userPassword = (String) pair.getValue();
                    try {
                        scc.getAuthorizedByUserContacts(this.context, userMail, userPassword);
                    } catch (Exception e) {
                        Log.e("GetAuthorisedByUsersCon", e.getMessage());
                    }
                    itAuthorisedByUsersContacts.remove(); // avoids a ConcurrentModificationException
                }
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
                    mainActivity.preLauncher();
                } else if (this.context.getClass().equals(PrincipalMapActivity.class)) {
                    PrincipalMapActivity principalMapActivity = (PrincipalMapActivity) this.context;
                    principalMapActivity.getMapFragment().refreshMap();
                }
            } else {
                Log.i("NO_CONN_START", "Starting app with no conection");
                MainActivity mainActivity = (MainActivity) this.context;
                mainActivity.preLauncher();
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
                return scc.addNewContact(userEmail, userPassword, contactToAdd);
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
    public class UserImageSender extends AsyncTask<Void, Void, Boolean> {
        private Context context;
        private String userEmail;


        public UserImageSender(Context context, String userEmail) {
            this.context = context;
            this.userEmail = userEmail;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            SafegeesConnectionManager scc = new SafegeesConnectionManager();
            String userPassword = StoredDataQuequesManager.getUserPassword(this.context, this.userEmail);

            if (Connectivity.isNetworkAvaiable(this.context)) {
                String filepath = ImageController.getUserImageFileNameByEmail(context, userEmail);
                File file = new File(filepath);
                scc.uploadProfileImage(context, userEmail, userPassword ,file);
                return true;
            }
            return false;
        }


        @Override
        protected void onPostExecute(final Boolean success) {


            sendUPosTask = null;
            if (success) {
                Log.i("UPDATE_IMAGE", "The image was added correctly");
            } else {
                Log.i("UPDATE_IMAGE", "The image wasn't added correctly, added to shared preferences");
                //String latLongString = this.file.toString();
                //StoredDataQuequesManager.putUserPositionInPositionsQueque(this.context, userEmail, latLongString);
                //Log.i("UPDATE_POSITION", "The position wasn't updated correctly");
            }
            // !success because it was true when the image must be sended
            MainActivity.DATA_STORAGE.putBoolean(context.getResources().getString(R.string.KEY_USER_IMAGES_TO_UPLOAD) + userEmail,!success);
        }

        @Override
        protected void onCancelled() {
            sendUPosTask = null;;
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
                String latLongString = this.latLng.toString();
                StoredDataQuequesManager.putUserPositionInPositionsQueque(this.context, userEmail, latLongString);
                Log.i("UPDATE_POSITION", "The position wasn't updated correctly");
            }
        }

        @Override
        protected void onCancelled() {
            sendUPosTask = null;;
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class SendUserBasicData extends AsyncTask<Void, Void, Boolean> {
        private Context context;
        private String userEmail;
        private PublicUser publicUser;


        public SendUserBasicData(Context context, String userEmail, PublicUser publicUser) {
            this.context = context;
            this.userEmail = userEmail;
            this.publicUser = publicUser;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            SafegeesConnectionManager scc = new SafegeesConnectionManager();

            //While this alpha doesnt send the position on live, only on start
            /* To do
            if (Connectivity.isNetworkAvaiable(this.context)) {
                String userPassword = StoredDataQuequesManager.getUserPassword(this.context, this.userEmail);
                PrivateUser pu = new PrivateUser(userEmail,userPassword,publicUser);
                return (scc.updateUserBasic(pu));
            }
            */
            return false;
        }


        @Override
        protected void onPostExecute(final Boolean success) {


            sendUPosTask = null;
            if (success) {
                Log.i("SEND_BASIC_USER_DATA", "The basic user data was updated correctly");
            } else {
                String userPassword = StoredDataQuequesManager.getUserPassword(this.context, this.userEmail);
                PrivateUser pu = new PrivateUser(userEmail,userPassword,publicUser);
                StoredDataQuequesManager.putUserInBasicDataQueque(this.context, userEmail, PublicUser.getJSONStringFromPublicUser(pu));
                Log.i("SEND_BASIC_USER_DATA", "The basic user data wasn't updated correctly");
            }
        }

        @Override
        protected void onCancelled() {
            sendUPosTask = null;;
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class DownloadImagesTask extends AsyncTask<Void, Void, Boolean> {
        private Context context;
        private ArrayList<PublicUser> publicUsers;


        public DownloadImagesTask(Context context, ArrayList<PublicUser> publicUsers) {
            this.context = context;
            this.publicUsers = publicUsers;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: it will change the process. This one doesnt update the image. It will use the md5 key.
            File f = new File(ImageController.getUserImageFileNameByEmail(context, publicUsers.get(0).getPublicEmail()));
            if (!f.exists() && publicUsers.get(0).getAvatar() != null && !publicUsers.get(0).getAvatar().equals("")){
                return  downloadBitmap(publicUsers.get(0));
            }else{
                publicUsers.remove(publicUsers.get(0));
            }
            return false;
        }

        private Boolean downloadBitmap(PublicUser publicUser) {

            String url = publicUser.getAvatar();
            // initilize the default HTTP client object
            final DefaultHttpClient client = new DefaultHttpClient();

            //forming a HttoGet request
            final HttpGet getRequest = new HttpGet(url);
            try {

                HttpResponse response = client.execute(getRequest);

                //check 200 OK for success
                final int statusCode = response.getStatusLine().getStatusCode();

                if (statusCode != HttpStatus.SC_OK) {
                    Log.w("ImageDownloader", "Error " + statusCode +
                            " while retrieving bitmap from " + url);
                    return false;

                }

                final HttpEntity entity = response.getEntity();
                if (entity != null) {
                    InputStream inputStream = null;
                    try {
                        // getting contents from the stream
                        inputStream = entity.getContent();

                        // decoding stream data back into image Bitmap that android understands
                        final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        ImageController.storeUserBitmapWithEmail(context, bitmap, publicUser.getPublicEmail());
                        //Remove the downloaded friend from the list
                        publicUsers.remove(publicUser);
                        return true;
                    } finally {
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        entity.consumeContent();
                    }
                }
            } catch (Exception e) {
                // You Could provide a more explicit error message for IOException
                getRequest.abort();
                Log.e("ImageDownloader", "Something went wrong while" +
                        " retrieving bitmap from " + url + e.toString());
            }

            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (publicUsers.size()>0){
                DownloadImagesTask sendAddContactTask = new DownloadImagesTask(context, publicUsers);
                sendAddContactTask.execute();
            }
        }
    }





}
