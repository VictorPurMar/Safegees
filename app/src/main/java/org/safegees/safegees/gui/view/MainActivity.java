package org.safegees.safegees.gui.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import org.safegees.safegees.R;
import org.safegees.safegees.gui.fragment.AddContactFragment;
import org.safegees.safegees.gui.fragment.ContactsFragment;
import org.safegees.safegees.gui.fragment.NewsFragment;
import org.safegees.safegees.gui.fragment.ProfileContactFragment;
import org.safegees.safegees.gui.fragment.ProfileUserFragment;
import org.safegees.safegees.maps.CustomMapTileProvider;
import org.safegees.safegees.util.SafegeesDAO;

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
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.TileOverlayOptions;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, GoogleMap.OnCameraChangeListener, GoogleMap.OnMapLoadedCallback, ProfileUserFragment.OnFragmentInteractionListener, NewsFragment.OnFragmentInteractionListener, ContactsFragment.OnFragmentInteractionListener, AddContactFragment.OnFragmentInteractionListener, ProfileContactFragment.OnFragmentInteractionListener  {


    static double LAT = -32;
    static double LON = 151;
    int ZOOM = 1;
    public static float MAX_ZOOM = 1F;
    SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private Marker mCurrLocation;



    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    public static void setMaxZoom(float maxZoom){
        if (MAX_ZOOM < maxZoom) MAX_ZOOM = maxZoom;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //The floating button will be used to update content if exists internet connection
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Future update data process", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

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

        Toast.makeText(this, SafegeesDAO.getInstance(this).getPois().toString(), Toast.LENGTH_LONG).show();

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

    @Override
    public void onMapLoaded() {
        Log.e("MAP LOADING", "Loading");
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            Fragment fg = ProfileUserFragment.newInstance();
            Fragment acFrag = getActiveFragment();
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
            Fragment acFrag = getActiveFragment();
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
            Fragment acFrag = getActiveFragment();
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
            Fragment acFrag = getActiveFragment();
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

    //Fix maximun zoom
    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        if (cameraPosition.zoom > MAX_ZOOM){
            CameraUpdate upd = CameraUpdateFactory.newLatLngZoom(new LatLng(cameraPosition.target.latitude, cameraPosition.target.longitude), MAX_ZOOM);
            mMap.moveCamera(upd);
        }
    }

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

        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
        //Set custom tiles
        mMap.addTileOverlay(new TileOverlayOptions().tileProvider(new CustomMapTileProvider(getResources().getAssets())));
        mMap.setOnCameraChangeListener(this);
        // map is a GoogleMap object


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            //mMap.getUiSettings().setMyLocationButtonEnabled(true);
        } else {
            Log.i("PERMISSION", "No User Location Enabled");
        }




        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }


    private void setUpMap() {
        CameraUpdate upd = CameraUpdateFactory.newLatLngZoom(new LatLng(LAT, LON), ZOOM);
        mMap.moveCamera(upd);
    }

    /**
     * Fixing tile's y index (reversing order)
     */
    private int fixYCoordinate(int y, int zoom) {
        int size = 1 << zoom; // size = 2^zoom
        return size - 1 - y;
    }

//    @Override
//    public void onLocationChanged(Location location) {
//
//        //remove previous current location marker and add new one at current position
//            if (mCurrLocation != null) {
//                mCurrLocation.remove();
//
//            }
//            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//            MarkerOptions markerOptions = new MarkerOptions();
//            markerOptions.position(latLng);
//            markerOptions.title("Current Position");
//            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
//            mCurrLocation = mMap.addMarker(markerOptions);
//
//            Toast.makeText(this, "Location Changed", Toast.LENGTH_SHORT).show();
//
//            //If you only need one location, unregister the listener
//            //LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
//
//    }

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

    public void onFragmentInteraction(Uri uri){
        //you can leave it empty
    }

    public Fragment getActiveFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            return null;
        }
        String tag = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName();
        return (Fragment) getSupportFragmentManager().findFragmentByTag(tag);
    }







}
