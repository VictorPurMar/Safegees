package org.safegees.safegees.gui.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by victor on 25/12/15.
 */
    public class SplashActivity extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
}
