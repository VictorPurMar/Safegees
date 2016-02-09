/**
 *   MainActivity.java
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

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;

import org.safegees.safegees.R;
import org.safegees.safegees.gui.fragment.AddContactFragment;
import org.safegees.safegees.gui.fragment.ContactsFragment;
import org.safegees.safegees.gui.fragment.NewsFragment;
import org.safegees.safegees.gui.fragment.ProfileContactFragment;
import org.safegees.safegees.gui.fragment.ProfileUserFragment;
import org.safegees.safegees.maps.CustomMapTileProvider;
import org.safegees.safegees.model.Contact;
import org.safegees.safegees.model.POI;
import org.safegees.safegees.util.Connectivity;
import org.safegees.safegees.util.SafegeesDAO;
import org.safegees.safegees.util.SafegeesDownloadDataManager;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
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

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlayOptions;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, GoogleMap.OnCameraChangeListener, GoogleMap.OnMapLoadedCallback, ProfileUserFragment.OnFragmentInteractionListener, NewsFragment.OnFragmentInteractionListener, ContactsFragment.OnFragmentInteractionListener, AddContactFragment.OnFragmentInteractionListener, ProfileContactFragment.OnFragmentInteractionListener  {


    private SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private SafegeesDAO sDAO;
    private FloatingActionButton floatingUpdateButton;

    private static MainActivity instance;           //Singleton
    private static float MAX_ZOOM = 1F;             //This MaxZoom can change depending the max depth of stored tile Maps
    private static float INIT_ZOOM = 4.5F;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    //---------------------------------
    // Singleton
    //---------------------------------

    public static MainActivity getInstance(){
        return instance;
    }

    //---------------------------------
    // Android Basic
    //---------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sDAO = SafegeesDAO.getInstance(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.floatingUpdateButton = (FloatingActionButton) findViewById(R.id.fab);
        this.floatingUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Updating data", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                MainActivity.getInstance().update();
            }
        });

        if(Connectivity.isNetworkAvaiable(this)) {
            showUpdateFloatingButton();
        }else{
            hideUpdateFloatingButton();
        }

        //The drawer Layout is the Lateral menu
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Google Map
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        instance = this;
    }

    //Starts the google api connecting to client
    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Maps Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://org.safegees.safegees/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Maps Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://org.safegees.safegees/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
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


    //GOOGLE MAPS API
    @SuppressWarnings("StatementWithEmptyBody")
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
            transaction.replace(R.id.map, fg).addToBackStack("profile");
            transaction.commit();
            mapFragment.onPause();

        } else if (id == R.id.nav_contacts) {

            Fragment fg = ContactsFragment.newInstance();
            //Fragment acFrag = getActiveFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            if (getSupportFragmentManager().getBackStackEntryCount() != 0){
                getSupportFragmentManager().popBackStack();
            }else{
                transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);

            }
            transaction.replace(R.id.map, fg).addToBackStack("contacts");
            transaction.commit();
            mapFragment.onPause();

        } else if (id == R.id.nav_map) {

            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
                //transaction.remove(getActiveFragment());
                getSupportFragmentManager().popBackStack();
                transaction.commit();
                //super.onBackPressed();
            }
            mapFragment.onResume();

        } else if (id == R.id.nav_news) {
            Fragment fg = NewsFragment.newInstance();
            //Fragment acFrag = getActiveFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            if (getSupportFragmentManager().getBackStackEntryCount() != 0){
                getSupportFragmentManager().popBackStack();
            }else{
                transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);

            }
            transaction.replace(R.id.map, fg).addToBackStack("profile");
            transaction.commit();
            mapFragment.onPause();

        } else if (id == R.id.nav_add_people) {
            Fragment fg = AddContactFragment.newInstance();
            //Fragment acFrag = getActiveFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            if (getSupportFragmentManager().getBackStackEntryCount() != 0){
                getSupportFragmentManager().popBackStack();
            }else{
                transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);

            }
            transaction.replace(R.id.map, fg).addToBackStack("profile");
            transaction.commit();
            mapFragment.onPause();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //GOOGLE MAPS API
    /**
     * Check if MAX ZOOM is passed to fix it updating the camera
     * @param cameraPosition
     */
    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        if (cameraPosition.zoom > MAX_ZOOM){
            CameraUpdate upd = CameraUpdateFactory.newLatLngZoom(new LatLng(cameraPosition.target.latitude, cameraPosition.target.longitude), MAX_ZOOM);
            mMap.moveCamera(upd);
        }
    }

    //GOOGLE MAPS API
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        //Build the Map
        buildMap(googleMap);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            //Move the camera to the user's location and zoom in!
            //mMap.getUiSettings().setMyLocationButtonEnabled(true);
        } else {
            Log.i("PERMISSION", "No User Location Enabled");
        }
        //Add markers
        this.refreshPointsInMap();
        //Toast.makeText(this, SafegeesDAO.getInstance(this).getPois().toString(), Toast.LENGTH_LONG).show();

        //First time stablish the initial Zoom Level
        this.setUpMap();

    }
    //GOOGLE MAPS API
    @Override
    public void onMapLoaded() {
        //Log.i("MAP LOADING", "Loading");
    }

    //---------------------------------
    // Public
    //---------------------------------

    //Fragment interface necesary method
    public void onFragmentInteraction(Uri uri){
        //you can leave it empty
    }

    /**
    //Fragment interface necesary method
    public Fragment getActiveFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            return null;
        }
        String tag = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName();
        return (Fragment) getSupportFragmentManager().findFragmentByTag(tag);
    }
     **/

    /**
     * Refresh the GoogleMap mMap
     * First rebuild the sDao where the objects where loaded
     * Second clears the map
     * Third build the mMap with the local Tiles
     * Fourth add Markers to the map
     */
    public void refrehMap(){
        //Rebuild objects in DAO
        SafegeesDAO.refreshInstance(this);
        //Clear the map
        this.mMap.clear();
        //Build mMap with local Tiles
        buildMap(this.mMap);
        //Add markers
        this.refreshPointsInMap();
    }

    /**
     * Floating button show
     * Called by NetworkStateReceiver
     */
    public void showUpdateFloatingButton(){
        this.floatingUpdateButton.show();
    }

    /**
     * Floating button hide
     * Called by NetworkStateReceiver
     */
    public void hideUpdateFloatingButton(){
        //The floating button will be used to update content if exists internet connection
        floatingUpdateButton.hide();
    }

    /**
     * It will called by CustomMapTileProvider to stablish the Max Zoom depending the map depth avaiablility
     * @param maxZoom float with the max zoom depth
     */
    public static void setMaxZoom(float maxZoom){
        if (MAX_ZOOM < maxZoom) MAX_ZOOM = maxZoom;
    }



    //---------------------------------
    // Private
    //---------------------------------

    /**
     * Updates the data
     * Download new data with SafegeesDownloadDataManager this method calls MainActivity build method when is finished
     */
    private void update(){
            if (Connectivity.isNetworkAvaiable(this)){
                //Download data
                SafegeesDownloadDataManager sddm = new SafegeesDownloadDataManager();
                sddm.run(this);
            }

            /*
            //Only for DEVELOPE
            //Show the log if no connection
            Map<String,String> appUsersMap = AppUsersManager.getAppUsersMap(this);
            Iterator it = appUsersMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                String userMail =  (String) pair.getKey();
                String contactsData = SplashActivity.DATA_STORAGE.getString(getResources().getString(R.string.KEY_CONTACTS_DATA)+"_"+userMail);
                Log.i("CONTACTS_DATA", "User:" + userMail + "    Data:" + contactsData);
                it.remove(); // avoids a ConcurrentModificationException
            }
            String generalData = SplashActivity.DATA_STORAGE.getString(getResources().getString(R.string.KEY_POINTS_OF_INTEREST));
            Log.i("GENERAL_DATA", "Data:" + generalData);
            */
    }


    /**
     * Clossed the session
     * Delete the active UserEmail and Password and relauch the app
     */
    private void closeSession() {
        //Delete user password and mail
        SplashActivity.DATA_STORAGE.putString(getResources().getString(R.string.KEY_USER_PASSWORD), "");
        SplashActivity.DATA_STORAGE.putString(getResources().getString(R.string.KEY_USER_MAIL), "");
        //Restart application
        Intent i = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage(getBaseContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }

    /**
     * Build the Gooogle Map with local Tiles (Using CustomMapTileProvider)
     * @param googleMap
     */
    private void buildMap(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
        //Set custom tiles
        mMap.addTileOverlay(new TileOverlayOptions().tileProvider(new CustomMapTileProvider(getResources().getAssets())));
        mMap.setOnCameraChangeListener(this);
    }

    /**
     * Get the points from DAO (SafeggeesDAO) and set the markers on Map
     */
    private void refreshPointsInMap() {
        if (this.sDAO != null) {
            ArrayList<POI> pois = this.sDAO.getPois();
            for (int i = 0; i < pois.size(); i++) {
                POI poi = pois.get(i);

                int px = getResources().getDimensionPixelSize(R.dimen.map_dot_marker_size);
                Bitmap mDotMarkerBitmap = Bitmap.createBitmap(px, px, Bitmap.Config.ARGB_8888);
                Paint paint = new Paint();
                ColorFilter filter = new PorterDuffColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
                paint.setColorFilter(filter);
                Canvas canvas = new Canvas(mDotMarkerBitmap);
                canvas.drawBitmap(mDotMarkerBitmap, 0, 0, paint);
                Drawable shape = getResources().getDrawable(R.drawable.ic_add_location_black_24dp);
                shape.setBounds(0, 0, mDotMarkerBitmap.getWidth(), mDotMarkerBitmap.getHeight());
                shape.draw(canvas);

                this.mMap.addMarker(new MarkerOptions().position(poi.getPosition()).title(poi.getName()).snippet(poi.getDescription()).alpha(0.9f).icon(BitmapDescriptorFactory.fromBitmap(mDotMarkerBitmap)));
            }

            ArrayList<Contact> contacts = this.sDAO.getContacts();
            for (int i = 0; i < contacts.size(); i++) {
                Contact contact = contacts.get(i);

                int px = getResources().getDimensionPixelSize(R.dimen.map_dot_marker_size);
                Bitmap mDotMarkerBitmap = Bitmap.createBitmap(px, px, Bitmap.Config.ARGB_8888);


                Paint paint = new Paint();
                ColorFilter filter = new PorterDuffColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
                paint.setColorFilter(filter);

                Canvas canvas = new Canvas(mDotMarkerBitmap);
                canvas.drawBitmap(mDotMarkerBitmap, 0, 0, paint);
                Drawable shape = getResources().getDrawable(R.drawable.ic_person_pin_circle_black_24dp);
                shape.setBounds(0, 0, mDotMarkerBitmap.getWidth(), mDotMarkerBitmap.getHeight());
                shape.draw(canvas);


                if (contact.getPosition() != null && contact.getLast_connection_date() != null)
                    this.mMap.addMarker(new MarkerOptions().position(contact.getPosition()).title(contact.getEmail()).snippet(contact.getLast_connection_date().toString()).alpha(1f).icon(BitmapDescriptorFactory.fromBitmap(mDotMarkerBitmap)));
                else if (contact.getPosition() != null)
                    this.mMap.addMarker(new MarkerOptions().position(contact.getPosition()).title(contact.getEmail()).alpha(1f).icon(BitmapDescriptorFactory.fromBitmap(mDotMarkerBitmap)));
            }


        }
    }

    /**
     * Stablish the ZoomLevel as INIT_ZOOM and move Camera
     */
    private void setUpMap() {
        LatLng latLng = this.getUserLocation();
        if (latLng == null) {
            CameraUpdate upd = CameraUpdateFactory.newLatLngZoom(mMap.getCameraPosition().target, INIT_ZOOM);
            mMap.moveCamera(upd);
        }else{
            //Move the camera to user position with init zoom
            CameraUpdate upd = CameraUpdateFactory.newLatLngZoom(latLng, INIT_ZOOM);
            mMap.moveCamera(upd);
        }
    }

    private void changeBitmapColor(Bitmap sourceBitmap, ImageView image, int color) {

        Bitmap resultBitmap = Bitmap.createBitmap(sourceBitmap, 0, 0,
                sourceBitmap.getWidth() - 1, sourceBitmap.getHeight() - 1);
        Paint p = new Paint();
        ColorFilter filter = new LightingColorFilter(color, 1);
        p.setColorFilter(filter);
        image.setImageBitmap(resultBitmap);

        Canvas canvas = new Canvas(resultBitmap);
        canvas.drawBitmap(resultBitmap, 0, 0, p);
    }

    /**
     * Fixing tile's y index (reversing order)
     */
    /**
    private int fixYCoordinate(int y, int zoom) {
        int size = 1 << zoom; // size = 2^zoom
        return size - 1 - y;
    }
     */

    /**
     * Get Coarse Location if permission is granted
     * @return latLng LatLong with the position
     */
    private LatLng getUserLocation() {
        // Get the location manager
        LocationManager locationManager = (LocationManager)
                getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, false);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Location location = locationManager.getLastKnownLocation(bestProvider);
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            return latLng;
        }else if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Location location = locationManager.getLastKnownLocation(bestProvider);
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            return latLng;
        }
        return null;
    }
}
