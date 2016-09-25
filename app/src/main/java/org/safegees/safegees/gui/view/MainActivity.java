/**
 *   MainActivity.java
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

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.safegees.safegees.R;
import org.safegees.safegees.model.Friend;
import org.safegees.safegees.model.PublicUser;
import org.safegees.safegees.util.Connectivity;
import org.safegees.safegees.util.DownloadPublicUsersImagesController;
import org.safegees.safegees.util.ImageController;
import org.safegees.safegees.util.MapFileManager;
import org.safegees.safegees.util.StorageDataManager;
import org.safegees.safegees.util.SafegeesDAO;
import org.safegees.safegees.util.ShareDataController;
import org.safegees.safegees.util.StoredDataQuequesManager;
import org.safegees.safegees.util.WebViewInfoWebDownloadController;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by victor on 25/12/15.
 */
    public class MainActivity extends AppCompatActivity {
    public static StorageDataManager DATA_STORAGE;
    private FileManagerTask fileManagerTask;
    private TextView adviceUser;

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_LOCATION = 2;


        @Override
        protected void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.splash_screen_layout);
            this.adviceUser = (TextView) findViewById(R.id.advice_user);

            /*
            //Habilite your gps
            final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
            if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
            */


            //Manain the splash screen on
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

            //Activate the stored data
            this.DATA_STORAGE = new StorageDataManager(this);

            /*
            boolean moovedAssetsZip = DATA_STORAGE.getBoolean("movedAssetsZip");
            if (!moovedAssetsZip){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                            if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);



                            } else {
                                DATA_STORAGE.putBoolean("movedAssetsZip", MapFileManager.buildAssetsMap(this));
                                moovedAssetsZip = DATA_STORAGE.getBoolean("movedAssetsZip");
                                startAfterPermissions(moovedAssetsZip);
                            }

                    }else{
                        DATA_STORAGE.putBoolean("movedAssetsZip", MapFileManager.buildAssetsMap(this));
                        moovedAssetsZip = DATA_STORAGE.getBoolean("movedAssetsZip");
                        startAfterPermissions(moovedAssetsZip);

                    }
                }else{
                    //Set the maps from assets to osmdroid folder
                    DATA_STORAGE.putBoolean("movedAssetsZip", MapFileManager.buildAssetsMap(this));
                    moovedAssetsZip = DATA_STORAGE.getBoolean("movedAssetsZip");
                    startAfterPermissions(moovedAssetsZip);
                }

            }else{
                startAfterPermissions(moovedAssetsZip);
            }


            */

            /*
            if( StoredDataQuequesManager.getAppUsersMap(this).size() == 0) {
                storageUserSelect();
            }else{
                FileManagerTask fmt = new FileManagerTask(this);
                fmt.execute();
            }
            */

            boolean moovedAssetsZip = DATA_STORAGE.getBoolean("movedAssetsZip");
            // Request permissions to support Android Marshmallow and above devices (api-23)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                checkPermissions();
            }else{
                startAfterPermissions();
            }
        }

    private void startAfterPermissions() {
        boolean moovedAssetsZip = DATA_STORAGE.getBoolean("movedAssetsZip");
            if (MapFileManager.isNewMapZip()) {
                adviceUser.setText(getResources().getString(R.string.splash_advice_unocmpresing));
                this.DATA_STORAGE.putBoolean("isNewMap", true);
                FileManagerTask fmt = new FileManagerTask(this);
                fmt.execute();
            } else {
                start();
            }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new android.app.AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("CANCEL", null)
                .create()
                .show();
    }

    private void start() {
        if(DATA_STORAGE.getString(getResources().getString(R.string.KEY_USER_MAIL)) != null && DATA_STORAGE.getString(getResources().getString(R.string.KEY_USER_MAIL)).length()>0){
            shareDataWithServer();
        }else{

            /* TEST
            if(DATA_STORAGE.getString(getResources().getString(R.string.KEY_USER_MAIL)) != null && DATA_STORAGE.getString(getResources().getString(R.string.KEY_USER_MAIL)).length()>0){
                launchMainActivity();
            }else{
                //Start the loggin for result
                Intent loginInt = new Intent(this, LoginActivity.class);
                startActivityForResult(loginInt, 1);
            }*/

            if (Connectivity.isNetworkAvaiable(this) || StoredDataQuequesManager.getAppUsersMap(this).size() != 0) {

                final MainActivity mainActivity = this;


                //Download data
                this.adviceUser.setText(getResources().getString(R.string.splash_advice_download_info_hub));

                //Test
                //Not here at final
                final WebView webView = (WebView) this.findViewById(R.id.webview_info_pre_cache);
                final ArrayList<String> infoWebUrls = WebViewInfoWebDownloadController.getInfoUrlsArrayList();
                webView.setWebViewClient(new WebViewClient() {

                    @Override
                    public void onPageFinished(WebView view, String url) {
                            if (infoWebUrls.size()>0){
                                String nextUrl = infoWebUrls.get(0);
                                infoWebUrls.remove(nextUrl);
                                webView.loadUrl(nextUrl);
                            }else{
                                //Only one time
                                DATA_STORAGE.putBoolean(mainActivity.getResources().getString(R.string.KEY_INFO_WEB), true);
                                //Start the loggin for result
                                Intent loginInt = new Intent(mainActivity, LoginActivity.class);
                                startActivityForResult(loginInt, 1);
                            }
                    }
                });


                String nextUrl = infoWebUrls.get(0);
                infoWebUrls.remove(nextUrl);
                webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
                webView.getSettings().setJavaScriptEnabled( true );
                webView.setWebChromeClient(new WebChromeClient());

                if (StoredDataQuequesManager.getAppUsersMap(mainActivity).size() == 0 && !DATA_STORAGE.getBoolean(getResources().getString(R.string.KEY_INFO_WEB))) {
                    webView.loadUrl(nextUrl);
                }else{
                    if(DATA_STORAGE.getString(getResources().getString(R.string.KEY_USER_MAIL)) != null && DATA_STORAGE.getString(getResources().getString(R.string.KEY_USER_MAIL)).length()>0){
                        launchMainActivity();
                    }else{
                        //Start the loggin for result
                        Intent loginInt = new Intent(mainActivity, LoginActivity.class);
                        startActivityForResult(loginInt, 1);
                    }
                }


            }else{
                setNoInternetAdvice(this);
            }
        }
    }

    public void setNoInternetAdvice(Context context){
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(getResources().getString(R.string.splash_advice_first_use_advice))
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

    public void launchMainActivity(){
        // Start the app
        Intent intent = new Intent(this, PrincipalMapActivity.class);

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
                start();
            }
        }
    }

    public void preLauncher(){
        adviceUser.setText(getResources().getString(R.string.splash_advice_initializing));
        launchMainActivity();
    }

    // START PERMISSION CHECK
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;

    // Request permissions to support Android Marshmallow and above devices  (api-23)
    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermissions() {
        List<String> permissions = new ArrayList<>();
        String message = "OSMDroid permissions:";
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            message += "\nStorage access to store map tiles.";
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            message += "\nLocation to show user location.";
        }
        if (!permissions.isEmpty()) {
            //Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            String[] params = permissions.toArray(new String[permissions.size()]);
            requestPermissions(params, REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
        }else{
            startAfterPermissions();

        }
    }

    // Request permissions to support Android Marshmallow and above devices. (api-23)
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<>();
                // Initial
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for ACCESS_FINE_LOCATION and WRITE_EXTERNAL_STORAGE
                Boolean location = perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
                Boolean storage = perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
                if (location && storage) {
                    // All Permissions Granted
                    //Toast.makeText(MainActivity.this, "All permissions granted", Toast.LENGTH_SHORT).show();
                    //Set the maps from assets to osmdroid folder
                    DATA_STORAGE.putBoolean("movedAssetsZip", MapFileManager.buildAssetsMap(this));
                    startAfterPermissions();
                } else if (location) {
                    setPermissionsAlertDialog( "Storage permission is required to store map tiles to reduce data usage and for offline usage.");
                } else if (storage) {
                    setPermissionsAlertDialog( "Location permission is required to show the user's location on map.");
                } else {
                    setPermissionsAlertDialog( "Storage permission is required to store map tiles to reduce data usage and for offline usage.\nLocation permission is required to show the user's location on map.");
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void setPermissionsAlertDialog(String message){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent i = getBaseContext().getPackageManager()
                                .getLaunchIntentForPackage(getBaseContext().getPackageName());
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        //finish();
                    }
                }).setNegativeButton("EXIT", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();


    }
    // END PERMISSION CHECK


    private void buildObjects() {
        SafegeesDAO sDao = SafegeesDAO.getInstance(this);
    }

    private void shareDataWithServer() {
        //Download data
        this.adviceUser.setText(getResources().getString(R.string.splash_advice_sharing_info));
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
            //Must change the ofline maps
            //return MapFileManager.checkAndUnZipTilesFile();
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {


            fileManagerTask = null;
            if (success) {
                //Toast toast = Toast.makeText(context, "Zip was added correctly", Toast.LENGTH_LONG);
                //toast.setGravity(Gravity.CENTER, 0, 0);
                //toast.show();
                start();
            } else {
                Toast toast = Toast.makeText(context, "Error", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }

        @Override
        protected void onCancelled() {
            fileManagerTask = null;
        }
    }


}
