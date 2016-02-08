package org.safegees.safegees.util;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;

import org.safegees.safegees.R;

/**
 * Created by victor on 8/2/16.
 */
public class SafegeesDownloadDataManager {
    private static DownloadTasks mAuthTask = null;


    public void run(){
        mAuthTask = new DownloadTasks();
        mAuthTask.execute((Void) null);

    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class DownloadTasks extends AsyncTask<Void, Void, Boolean> {




        DownloadTasks() {}

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            boolean isRegistered = false;
            try {
                // Simulate network access.
                SafegeesConnectionManager scc = new SafegeesConnectionManager();
                scc.getContactsData();
                scc.getPointsOfInterest();
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
