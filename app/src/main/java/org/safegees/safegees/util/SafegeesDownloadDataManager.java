package org.safegees.safegees.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.safegees.safegees.R;
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

            boolean isRegistered = false;
            try {
                // Simulate network access.
                SafegeesConnectionManager scc = new SafegeesConnectionManager();
                Map<String,String> appUsersMap = AppUsersManager.getAppUsersMap(this.context);

                Iterator it = appUsersMap.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry)it.next();
                    String userMail =  (String) pair.getKey();
                    String userPassword = (String) pair.getValue();
                    scc.getContactsData(this.context, userMail, userPassword);
                    String contactsData = SplashActivity.DATA_STORAGE.getString(this.context.getResources().getString(R.string.KEY_CONTACTS_DATA)+"_"+userMail);
                    Log.i("CONTACTS_DATA", "User:" + userMail + "    Data:" + contactsData);
                    it.remove(); // avoids a ConcurrentModificationException
                }

                scc.getPointsOfInterest(this.context);
                String generalData = SplashActivity.DATA_STORAGE.getString(this.context.getResources().getString(R.string.KEY_POINTS_OF_INTEREST));
                Log.i("GENERAL_DATA", "Data:" + generalData);
            } catch (Exception e) {
                return false;
            }

            // TODO: register the new account here.
            return isRegistered;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            if (success) {
                /*
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
                */
            } else {

            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }
}
