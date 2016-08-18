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

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
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

import org.safegees.safegees.R;
import org.safegees.safegees.model.Friend;
import org.safegees.safegees.util.Connectivity;
import org.safegees.safegees.util.DownloadContactImagesController;
import org.safegees.safegees.util.MapFileManager;
import org.safegees.safegees.util.StorageDataManager;
import org.safegees.safegees.util.SafegeesDAO;
import org.safegees.safegees.util.ShareDataController;
import org.safegees.safegees.util.StoredDataQuequesManager;
import org.safegees.safegees.util.WebViewInfoWebDownloadController;

import java.io.FilenameFilter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by victor on 25/12/15.
 */
    public class MainActivity extends AppCompatActivity {
    public static StorageDataManager DATA_STORAGE;
    private FileManagerTask fileManagerTask;
    private TextView adviceUser;

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

            boolean moovedAssetsZip = DATA_STORAGE.getBoolean("movedAssetsZip");
            if (!moovedAssetsZip){
                //Set the maps from assets to osmdroid folder
                DATA_STORAGE.putBoolean("movedAssetsZip", MapFileManager.buildAssetsMap(this));
                moovedAssetsZip = DATA_STORAGE.getBoolean("movedAssetsZip");
            }


            if (moovedAssetsZip) {
                if (MapFileManager.isNewMapZip()) {
                    adviceUser.setText(getResources().getString(R.string.splash_advice_unocmpresing));
                    this.DATA_STORAGE.putBoolean("isNewMap", true);
                    FileManagerTask fmt = new FileManagerTask(this);
                    fmt.execute();

                } else {
                    start();
                }
            }else{
                Log.e("MainActivity", "The assets wasnt dont mooved");
            }

            /*
            if( StoredDataQuequesManager.getAppUsersMap(this).size() == 0) {
                storageUserSelect();
            }else{
                FileManagerTask fmt = new FileManagerTask(this);
                fmt.execute();
            }
            */
        }

    /*
    private void storageUserSelect() {

        //To do the storage selector
        //Is neccessary to compile the whole OSMProject and touch OpenStreetMapTileProviderConstants.java
        //Concretly this variable public static final File OSMDROID_PATH = new File("/mnt/sdcard/osmdroid");
        //
        //By this reason this development will stopped by now

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

        DATA_STORAGE.putBoolean("Sdcard",false);
        final FileManagerTask fmt = new FileManagerTask(this);
        fmt.execute();
    }
    */

    private void start() {
        if(DATA_STORAGE.getString(getResources().getString(R.string.KEY_USER_MAIL)) != null && DATA_STORAGE.getString(getResources().getString(R.string.KEY_USER_MAIL)).length()>0){
            shareDataWithServer();
        }else{

            if(DATA_STORAGE.getString(getResources().getString(R.string.KEY_USER_MAIL)) != null && DATA_STORAGE.getString(getResources().getString(R.string.KEY_USER_MAIL)).length()>0){
                launchMainActivity();
            }else{
                //Start the loggin for result
                Intent loginInt = new Intent(this, LoginActivity.class);
                startActivityForResult(loginInt, 1);
            }


            if (Connectivity.isNetworkAvaiable(this) || StoredDataQuequesManager.getAppUsersMap(this).size() != 0) {
                /*
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

                if (StoredDataQuequesManager.getAppUsersMap(mainActivity).size() == 0) {
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
                */

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
            }
        }
    }

    public void launchTheApp(){
        adviceUser.setText(getResources().getString(R.string.splash_advice_initializing));
        buildObjects();
        downloadContactImages();
        launchMainActivity();
    }

    private void downloadContactImages() {
        SafegeesDAO dao = SafegeesDAO.getInstance(this);
        ArrayList<Friend> friends = dao.getFriends();
        DownloadContactImagesController imageDownloader = new DownloadContactImagesController(this,friends);
        imageDownloader.run();
    }

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
            return MapFileManager.checkAndUnZipTilesFile();
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
            fileManagerTask = null;;
        }


    }
}
