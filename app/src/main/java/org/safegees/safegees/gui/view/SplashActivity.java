package org.safegees.safegees.gui.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import org.safegees.safegees.R;
import org.safegees.safegees.util.DataStorageManager;
import org.safegees.safegees.util.SafegeesDownloadDataManager;

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


                launchMainActivity();
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


                SafegeesDownloadDataManager sddm = new SafegeesDownloadDataManager();
                sddm.run(this);

                launchMainActivity();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }
}
