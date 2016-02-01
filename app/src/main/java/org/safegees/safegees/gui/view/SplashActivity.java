package org.safegees.safegees.gui.view;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import org.safegees.safegees.util.SafegeesConnectionController;

import java.io.IOException;

/**
 * Created by victor on 25/12/15.
 */
    public class SplashActivity extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);


            //Start the loggin for result
            Intent loginInt = new Intent(this, LoginActivity.class);
            startActivityForResult(loginInt, 1);



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
                launchMainActivity();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }
}
