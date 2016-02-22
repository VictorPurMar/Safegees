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
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Toast;

import org.osmdroid.tileprovider.constants.OpenStreetMapTileProviderConstants;
import org.safegees.safegees.R;
import org.safegees.safegees.util.Connectivity;
import org.safegees.safegees.util.MapFileManager;
import org.safegees.safegees.util.SafegeesConnectionManager;
import org.safegees.safegees.util.StorageDataManager;
import org.safegees.safegees.util.SafegeesDAO;
import org.safegees.safegees.util.ShareDataController;
import org.safegees.safegees.util.StoredDataQuequesManager;

import java.io.File;

/**
 * Created by victor on 25/12/15.
 */
    public class SplashActivity extends AppCompatActivity {
    public static StorageDataManager DATA_STORAGE;
    private FileManagerTask fileManagerTask;

        @Override
        protected void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.splash_screen_layout);

            //Manain the splash screen on
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

            //Activate the stored data
            DATA_STORAGE = new StorageDataManager(this);


            if( StoredDataQuequesManager.getAppUsersMap(this).size() == 0) {
                storageUserSelect();
            }else{
                FileManagerTask fmt = new FileManagerTask(this);
                fmt.execute();
            }
        }

    private void storageUserSelect() {

        //To do the storage selector
        //Is neccessary to compile the whole OSMProject and touch OpenStreetMapTileProviderConstants.java
        //Concretly this variable public static final File OSMDROID_PATH = new File("/mnt/sdcard/osmdroid");
        //
        //By this reason this development will stopped by now
        /*
        final FileManagerTask fmt = new FileManagerTask(this);
            if(MapFileManager.isSDCard(this)) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                DATA_STORAGE.putBoolean("Sdcard",true);
                                fmt.execute();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                DATA_STORAGE.putBoolean("Sdcard",false);
                                fmt.execute();
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Welcome to Safegees!\n\n\nWe detect that you have an external storage (sdcard1). Do you want use this external card to store the maps?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }else{
                DATA_STORAGE.putBoolean("Sdcard",false);
                fmt.execute();
            }
            */
        DATA_STORAGE.putBoolean("Sdcard",false);
        final FileManagerTask fmt = new FileManagerTask(this);
        fmt.execute();
    }

    private void start() {


        if(DATA_STORAGE.getString(getResources().getString(R.string.KEY_USER_MAIL)) != null && DATA_STORAGE.getString(getResources().getString(R.string.KEY_USER_MAIL)).length()>0){
            shareDataWithServer();
        }else{
            if (Connectivity.isNetworkAvaiable(this) || StoredDataQuequesManager.getAppUsersMap(this).size() != 0) {
                //Start the loggin for result
                Intent loginInt = new Intent(this, LoginActivity.class);
                startActivityForResult(loginInt, 1);
            }else{
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("You must  be connected to interet before the first use")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent i = getBaseContext().getPackageManager()
                                        .getLaunchIntentForPackage(getBaseContext().getPackageName());
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);
                                //finish();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

            }
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

                shareDataWithServer();
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

    private void shareDataWithServer() {
        //Download data
        ShareDataController sddm = new ShareDataController();
        sddm.run(this);
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class FileManagerTask extends AsyncTask<Void, Void, Boolean> {
        private Context context;



        FileManagerTask(Context context) {
            this.context = context;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            return (MapFileManager.mapExists(context));
        }

        @Override
        protected void onPostExecute(final Boolean success) {


            fileManagerTask = null;
            if (success) {
                Toast toast = Toast.makeText(context, "Zip was added correctly", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                start();
            } else {
                Toast toast = Toast.makeText(context, "Error", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }

        @Override
        protected void onCancelled() {
            fileManagerTask = null;;
        }


    }
}
