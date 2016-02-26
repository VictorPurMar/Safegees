/**
 *   PrincipalMapActivity.java
 *
 *   This class is the base Activity interact activity in the application
 *   Contains Top and Lateral Menu as fragments
 *   And displays the navigation Map that is the main value of the app
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
 */


package org.safegees.safegees.gui.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import org.osmdroid.views.MapView;
import org.safegees.safegees.R;
import org.safegees.safegees.gui.fragment.AddContactFragment;
import org.safegees.safegees.gui.fragment.ContactsFragment;
import org.safegees.safegees.gui.fragment.MapFragment;
import org.safegees.safegees.gui.fragment.NewsFragment;
import org.safegees.safegees.gui.fragment.ProfileContactFragment;
import org.safegees.safegees.gui.fragment.ProfileUserFragment;
import org.safegees.safegees.gui.preferences.SettingsActivity;
import org.safegees.safegees.util.Connectivity;
import org.safegees.safegees.util.SafegeesDAO;
import org.safegees.safegees.util.ShareDataController;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class PrincipalMapActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,ProfileUserFragment.OnFragmentInteractionListener, NewsFragment.OnFragmentInteractionListener, ContactsFragment.OnFragmentInteractionListener, AddContactFragment.OnFragmentInteractionListener, ProfileContactFragment.OnFragmentInteractionListener , View.OnClickListener, MapFragment.OnFragmentInteractionListener{


    private MapFragment mapFragment;
    private ProfileUserFragment profileFragment;
    private FloatingActionButton floatingUpdateButton;
    private static PrincipalMapActivity instance;       //Singleton
    //For image getting
    private static final int REQUEST_CODE = 1;
    private Bitmap bitmap;
    private View headerView;

    //---------------------------------
    // Singleton
    //---------------------------------

    public static PrincipalMapActivity getInstance(){
        return instance;
    }

    //---------------------------------
    // Android Basic
    //---------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //init the object builder
        SafegeesDAO.getInstance(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.floatingUpdateButton = (FloatingActionButton) findViewById(R.id.fab);
        this.floatingUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Updating data", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                PrincipalMapActivity.getInstance().update();
            }
        });

        if(Connectivity.isNetworkAvaiable(this)) {
            connectivityOn();
        }else{
            connectivityOff();
        }

        //The drawer Layout is the Lateral menu
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Store the header view to update
        headerView = navigationView.inflateHeaderView(R.layout.nav_header_main);

        //Start the Map fragment
        mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        instance = this;
    }

    private void loadStoredImage() {
        try {

            String filename = getUserImageFileName();

            //View headerView = navigationView.findViewById(R.id.navigation_header_layout);
            ImageView userImageView = (ImageView) headerView.findViewById(R.id.nav_user_image);

            Log.e("IMAGE", userImageView.toString());

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            bitmap = BitmapFactory.decodeFile(filename, options);

            if(bitmap!=null) {
                userImageView.setImageBitmap(bitmap);
            }
        }catch (Exception e){
            e.printStackTrace();
            Log.e("IMAGE ERROR", e.getMessage());
        }
    }

    @NonNull
    private String getUserImageFileName() {
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + getApplicationContext().getPackageName()
                + "/images/");
        String filename = "USER_IMAGE_" + MainActivity.DATA_STORAGE.getString(getResources().getString(R.string.KEY_USER_MAIL)).replace("@","").replace(".","") + ".png";
        filename = mediaStorageDir.getAbsolutePath()+File.separator+filename;
        return filename;
    }

    //Starts the google api connecting to client
    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onStop() {
        super.onStop();


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

            mapFragment.onResume();

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        instance = null;
        /*
        long longitude = (long) mMap.getCameraPosition().target.longitude;
        long latitude = (long) mMap.getCameraPosition().target.latitude;
        MainActivity.DATA_STORAGE.putLong(this.getResources().getString(R.string));
        */
        SafegeesDAO.close();
    }

    // Top App Menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_close_session:
                closeSession();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    //---------------------------------
    // Override
    //---------------------------------


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            Fragment fg = ProfileUserFragment.newInstance();

            //Fragment acFrag = getActiveFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            if (getSupportFragmentManager().getBackStackEntryCount() != 0){
                getSupportFragmentManager().popBackStack();
            }else{
                transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);

            }
            transaction.replace(R.id.map, fg,  "profile").addToBackStack("profile");
            transaction.commit();
            //Set the imageBitMap
            this.profileFragment = (ProfileUserFragment) fg;
            profileFragment.setImageBitmap(bitmap);

            mapFragment.onPause();

            this.connectivityOff();

        } else if (id == R.id.nav_contacts) {

            Fragment fg = ContactsFragment.newInstance();
            //Fragment acFrag = getActiveFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            if (getSupportFragmentManager().getBackStackEntryCount() != 0){
                getSupportFragmentManager().popBackStack();
            }else{
                transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);

            }
            transaction.replace(R.id.map, fg, "contacts").addToBackStack("contacts");
            transaction.commit();

            mapFragment.onPause();

            this.connectivityOff();
        } else if (id == R.id.nav_map) {

            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
                transaction.remove(getActiveFragment());
                getSupportFragmentManager().popBackStack();
                transaction.commit();
                //super.onBackPressed();
            }

            mapFragment.onResume();

            this.connectivityOn();
        } else if (id == R.id.nav_news) {
            Fragment fg = NewsFragment.newInstance();
            //Fragment acFrag = getActiveFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            if (getSupportFragmentManager().getBackStackEntryCount() != 0){
                getSupportFragmentManager().popBackStack();
            }else{
                transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);

            }
            transaction.replace(R.id.map, fg, "news").addToBackStack("news");
            transaction.commit();

            mapFragment.onPause();

            this.connectivityOff();

        } else if (id == R.id.nav_add_people) {
            Fragment fg = AddContactFragment.newInstance();
            //Fragment acFrag = getActiveFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            if (getSupportFragmentManager().getBackStackEntryCount() != 0){
                getSupportFragmentManager().popBackStack();
            }else{
                transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);

            }
            transaction.replace(R.id.map, fg, "addPeople").addToBackStack("addPeople");
            transaction.commit();

            mapFragment.onPause();

            this.connectivityOff();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //---------------------------------
    // Public
    //---------------------------------

    //Fragment interface necesary method
    public void onFragmentInteraction(Uri uri){
        //you can leave it empty
    }


    //Fragment interface necesary method
    public Fragment getActiveFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            return null;
        }
        String tag = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName();
        return (Fragment) getSupportFragmentManager().findFragmentByTag(tag);
    }


    /**
     * Refresh the GoogleMap mMap
     * First rebuild the sDao where the objects where loaded
     * Second clears the map
     * Third build the mMap with the local Tiles
     * Fourth add Markers to the map
     */
    public void refreshMap(){
        /*
        //Rebuild objects in DAO
        SafegeesDAO.refreshInstance(this);
        //Clear the map
        this.mMap.clear();
        //Build mMap with local Tiles
        buildMap(this.mMap);
        //Add markers
        this.refreshPointsInMap();
        */
    }

    /**
     * Floating button show
     * Called by NetworkStateReceiver
     */
    public void connectivityOn(){
        this.floatingUpdateButton.show();
        if (mapFragment != null) mapFragment.setMapViewDependingConnection();
    }

    /**
     * Floating button hide
     * Called by NetworkStateReceiver
     */
    public void connectivityOff(){
        //The floating button will be used to update content if exists internet connection
        floatingUpdateButton.hide();
        if (mapFragment != null) mapFragment.setMapViewDependingConnection();
    }


    //---------------------------------
    // Private
    //---------------------------------

    /**
     * Updates the data
     * Download new data with ShareDataController this method calls PrincipalMapActivity build method when is finished
     */
    private void update(){
            if (Connectivity.isNetworkAvaiable(this)){
                //Download data
                ShareDataController sddm = new ShareDataController();
                sddm.run(this);

                //Refresh Map (The map is actually refreshed diectly by ShareDataController on success
                //mapFragment.refreshMap();
            }
    }


    /**
     * Clossed the session
     * Delete the active UserEmail and Password and relauch the app
     */
    private void closeSession() {
        //Delete user password and mail
        MainActivity.DATA_STORAGE.putString(getResources().getString(R.string.KEY_USER_PASSWORD), "");
        MainActivity.DATA_STORAGE.putString(getResources().getString(R.string.KEY_USER_MAIL), "");
        //Restart application
        Intent i = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage(getBaseContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }


    @NonNull
    private Bitmap getBitmap(int drawable) {
        Bitmap bitmap;
        int px = getResources().getDimensionPixelOffset(R.dimen.map_dot_marker_size);
        bitmap = Bitmap.createBitmap(px, px, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Drawable shape = getResources().getDrawable(drawable);
        shape.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
        shape.draw(canvas);
        return bitmap;
    }

    private Paint paintSurface() {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.MITER);
        paint.setStrokeCap(Paint.Cap.SQUARE);
        paint.setColor(Color.RED);
        paint.setStrokeWidth(16);
        paint.setAlpha(100);
        return paint;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK)
            bitmap = buildBitmapFromData(data.getData());
        Log.e("DATA", data.getDataString());
        ProfileUserFragment myFragment = (ProfileUserFragment) getSupportFragmentManager().findFragmentByTag("profile");
        if (myFragment != null && myFragment.isVisible()) {
            Log.i("ProfileFragment", "Add poto");
            myFragment.setImageBitmap(bitmap);
            //Store in /images
            storeUserImage();
            //Reload the header image
            loadStoredImage();

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void storeUserImage() {
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + getApplicationContext().getPackageName()
                + "/images");
        String filename = "USER_IMAGE_" + MainActivity.DATA_STORAGE.getString(getResources().getString(R.string.KEY_USER_MAIL)).replace("@","").replace(".","") + ".png";
        filename = mediaStorageDir.getAbsolutePath()+File.separator+filename;

        boolean success = true;
        if (!mediaStorageDir.exists()) {
            success = mediaStorageDir.mkdir();
        }
        if (success) {
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(filename);
                bitmap.compress(Bitmap.CompressFormat.PNG, 10, out); // bmp is your Bitmap instance

                // PNG is a lossless format, the compression factor (100) is ignored
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Log.e("Error", "File cant be created");
        }
    }

    public Bitmap buildBitmapFromData(Uri data){
        try {
            // We need to recyle unused bitmaps
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
                bitmap = null;
            }

            InputStream stream = getContentResolver().openInputStream(
                    data);
            bitmap = BitmapFactory.decodeStream(stream);
            stream.close();
                /*
                getSupportFragmentManager()
                imageView.setImageBitmap(bitmap);
                */
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        bitmap = fixBitmap(bitmap);
        return bitmap;
    }

    private Bitmap fixBitmap(Bitmap bitmap) {

        Matrix matrix = new Matrix();
        matrix.postRotate(0);
        //matrix.postRotate(90);


        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
        Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap , 0, 0, scaledBitmap .getWidth(), scaledBitmap .getHeight(), matrix, true);

        if (bitmap.getWidth() >= bitmap.getHeight()){

            bitmap = Bitmap.createBitmap(
                    rotatedBitmap,
                    rotatedBitmap.getWidth()/2 - rotatedBitmap.getHeight()/2,
                    0,
                    rotatedBitmap.getHeight(),
                    rotatedBitmap.getHeight()
            );

        }else{

            bitmap = Bitmap.createBitmap(
                    rotatedBitmap,
                    0,
                    rotatedBitmap.getHeight()/2 - rotatedBitmap.getWidth()/2,
                    rotatedBitmap.getWidth(),
                    rotatedBitmap.getWidth()
            );
        }


        //Rounded the bitmap
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        //final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = bitmap.getWidth() / 2;


        paint.setAntiAlias(true);
        //canvas.drawARGB(0, 0, 0, 0);
        //paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        //Scale squared
        output = Bitmap.createScaledBitmap(output, 300, 300, true);

        return output;

    }

    public void pickImage(View View) {
        //pickImage(v);
        Log.i("Picked image", "true");
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onClick(View v) {
        //pickImage(v);
        Log.i("Clicked", "true");

    }

    public void setProfileField(View v) {
        if (profileFragment != null){
            profileFragment.setProfileField(v);
        }
    }

    public MapFragment getMapFragment(){
        return mapFragment;
    }
}
