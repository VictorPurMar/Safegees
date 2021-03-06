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
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

import org.safegees.safegees.R;
import org.safegees.safegees.gui.fragment.AddContactFragment;
import org.safegees.safegees.gui.fragment.ContactsFragment;
import org.safegees.safegees.gui.fragment.MapFragment;
import org.safegees.safegees.gui.fragment.InfoFragment;
import org.safegees.safegees.gui.fragment.ProfileContactFragment;
import org.safegees.safegees.gui.fragment.ProfileUserFragment;
import org.safegees.safegees.model.Friend;
import org.safegees.safegees.util.Connectivity;
import org.safegees.safegees.util.ImageController;
import org.safegees.safegees.util.NetworkStateReceiver;
import org.safegees.safegees.util.SafegeesDAO;
import org.safegees.safegees.util.ShareDataController;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class PrincipalMapActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,ProfileUserFragment.OnFragmentInteractionListener, InfoFragment.OnFragmentInteractionListener, ContactsFragment.OnFragmentInteractionListener, AddContactFragment.OnFragmentInteractionListener, ProfileContactFragment.OnFragmentInteractionListener , View.OnClickListener, MapFragment.OnFragmentInteractionListener{


    private MapFragment mapFragment;
    private ProfileUserFragment profileFragment;
    private InfoFragment infoFragment;
    private ContactsFragment contactsFragment;
    //private FloatingActionButton floatingUpdateButton;      //update map button
    private FloatingActionButton floatingAddContactButton;
    private static PrincipalMapActivity instance;               //Singleton
    //For image getting
    private static final int REQUEST_IMAGE_CODE = 1;
    private static final int REQUEST_CONTACTS_CODE = 2;
    private Bitmap bitmap;
    private View headerView;
    DrawerLayout drawer;                                        //Lateral menu
    boolean closeSession = false;

    //TopBar menu buttons
    private MenuItem menuUpdate;
    private MenuItem menuInvitations;

    private WebView webView;
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
        SafegeesDAO.getInstance(getApplicationContext());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        this.floatingAddContactButton = (FloatingActionButton) findViewById(R.id.fab_add_contact);
        this.floatingAddContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar snackbar = Snackbar.make(view, getResources().getString(R.string.snack_adding_contact), Snackbar.LENGTH_LONG)
                        .setAction("Action", null);
                View snackBarLayout = snackbar.getView();
                snackBarLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorCadaquesAccentDark));
                snackbar.show();
                //Stablish the action of contact add
                addContactPopup();
            }

        });
        this.floatingAddContactButton.hide();

        //this.floatingUpdateButton = (FloatingActionButton) findViewById(R.id.fab);
        /*
        this.floatingUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Updating data", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                PrincipalMapActivity.getInstance().update();
            }
        });
        */



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


        loadNavMenuProfile();

        instance = this;
    }

    /*
    public View getFloatingButton(){
        return this.floatingUpdateButton;
    }
    */

    private void loadNavMenuProfile() {

        bitmap = ImageController.getUserImageBitmap(this);
        if (bitmap!=null) {
            //View headerView = navigationView.findViewById(R.id.navigation_header_layout);
            ImageView userImageView = (ImageView) headerView.findViewById(R.id.nav_user_image);
            userImageView.setImageBitmap(bitmap);
        }

        try{
            TextView textName = (TextView) headerView.findViewById(R.id.nav_user_name);
            TextView textMail = (TextView) headerView.findViewById(R.id.nav_user_email);

            textName.setText(SafegeesDAO.getInstance(getBaseContext()).getPublicUser().getName()+" "+ SafegeesDAO.getInstance(getBaseContext()).getPublicUser().getSurname());
            textMail.setText(SafegeesDAO.getInstance(getBaseContext()).getPublicUser().getPublicEmail());

        }catch (Exception e){
            e.printStackTrace();
            Log.e("NAME AND MAIL ERROR", e.getMessage());
        }
    }



    //Starts the google api connecting to client
    @Override
    public void onStart() {
        super.onStart();
        if(Connectivity.isNetworkAvaiable(this)) {
            connectivityOn();
        }else{
            connectivityOff();
        }
        showInvitations();
        SafegeesDAO.refreshInstance(this);

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

            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

            //Set propper floating buttons
            this.floatingAddContactButton.hide();               //Add contact
            NetworkStateReceiver.setFloatingUpdateButton(this); //Update map
            mapFragment.onResume();
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Close app
        instance = null;
        SafegeesDAO.close();
        if(closeSession)closeSession();

    }

    // Top App Menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        this.menuUpdate = menu.findItem(R.id.menu_update);
        this.menuInvitations = menu.findItem(R.id.menu_invitations);

        showInvitations();


        return true;
    }

    public void showInvitations() {
        SafegeesDAO dao = SafegeesDAO.getInstance(getApplicationContext());
        ArrayList<Friend> friends = dao.getNonAutorisedFriends();
        if (friends.size()==0 && this.menuInvitations != null){
            this.menuInvitations.setVisible(false);
        }else if (this.menuInvitations != null){
            this.menuInvitations.setVisible(true);
        }
    }

    private void launchInvitationsDialog(final Context context, final ArrayList<Friend> friends){

        if (friends.size()>0){
            final Friend friend = friends.get(0);
            new AlertDialog.Builder(context)
                    .setTitle(getResources().getString(R.string.invitation_title))
                    .setMessage(getResources().getString(R.string.invitation_text) + "\n" + friend.getName() + " " + friend.getSurname() + "\n"+friend.getPublicEmail())
                    .setPositiveButton(context.getResources().getString(R.string.invitation_accept), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            String userEmail = MainActivity.DATA_STORAGE.getString(getApplicationContext().getString(R.string.KEY_USER_MAIL));
                            //Add the contact
                            ShareDataController sssdc = new ShareDataController();
                            sssdc.addContact(getApplicationContext(), userEmail, friend.getPublicEmail());
                            friends.remove(friend);

                            if (friends.size()>0){
                                launchInvitationsDialog(context,friends);
                            }else{
                                update();
                            }

                        }
                    })
                    .setNegativeButton(context.getResources().getString(R.string.invitation_cancel), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            deleteContact(context, friends);

                            friends.remove(friend);
                            if (friends.size()>0){
                                launchInvitationsDialog(context,friends);
                            }else{
                                update();
                            }
                        }
                    }).setNeutralButton(context.getResources().getString(R.string.invitation_next), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                        friends.remove(friend);
                        if (friends.size()>0){
                            launchInvitationsDialog(context,friends);
                        }
                }
            }).setIcon(R.mipmap.ic_launcher)
                    .show();
        }

    }

    private void deleteContact(Context context, ArrayList<Friend> friends) {
        String userEmail = MainActivity.DATA_STORAGE.getString(getApplicationContext().getString(R.string.KEY_USER_MAIL));
        ShareDataController sssdc = new ShareDataController();
        sssdc.deleteContact(context, userEmail, friends.get(0).getPublicEmail());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, PreferencesActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_close_session:
                closeSession = true;
                onDestroy();
                return true;
            case R.id.menu_invitations:
                SafegeesDAO dao = SafegeesDAO.getInstance(getApplicationContext());
                ArrayList<Friend> friends = (ArrayList<Friend>) dao.getNonAutorisedFriends().clone();
                launchInvitationsDialog(this,friends);
                return true;
            case R.id.menu_update:
                Snackbar snackbar = Snackbar.make(this.getMapFragment().getView(), getResources().getString(R.string.updating_data), Snackbar.LENGTH_LONG)
                        .setAction("Action", null);
                View snackBarLayout = snackbar.getView();
                snackBarLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorCadaquesAccentDark));
                snackbar.show();
                PrincipalMapActivity.getInstance().update();
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

        /*
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

        } else */ if (id == R.id.nav_contacts) {
            this.floatingAddContactButton.show();

            contactsFragment = (ContactsFragment) ContactsFragment.newInstance();
            //Fragment acFrag = getActiveFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            if (getSupportFragmentManager().getBackStackEntryCount() != 0){
                getSupportFragmentManager().popBackStack();
            }else{
                transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);

            }
            transaction.replace(R.id.map, contactsFragment, "contacts").addToBackStack("contacts");
            transaction.commit();

            mapFragment.onPause();

            this.connectivityOff();
        } else if (id == R.id.nav_map) {
            showInvitations();
            this.floatingAddContactButton.hide();
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
        } else if (id == R.id.nav_info) {
            this.floatingAddContactButton.hide();
            this.infoFragment = InfoFragment.newInstance();
            //Fragment acFrag = getActiveFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            if (getSupportFragmentManager().getBackStackEntryCount() != 0){
                getSupportFragmentManager().popBackStack();
            }else{
                transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);

            }
            transaction.replace(R.id.map, infoFragment, "news").addToBackStack("news");
            transaction.commit();

            mapFragment.onPause();

            this.connectivityOff();

        }/* else if (id == R.id.nav_add_people) {
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

        }*/

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

    public void showMapFragment(){
        this.menuUpdate.setVisible(false);
        if (this.menuInvitations != null) this.showInvitations();
        this.floatingAddContactButton.hide();
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

    }

    /**
     * Floating button show
     * Called by NetworkStateReceiver
     */
    public void connectivityOn(){
        if (this.menuUpdate != null)  this.menuUpdate.setVisible(true);
        //this.floatingUpdateButton.show();
        if (mapFragment != null) mapFragment.setMapViewDependingConnection();
    }

    /**
     * Floating button hide
     * Called by NetworkStateReceiver
     */
    public void connectivityOff(){
        if (this.menuUpdate != null)  this.menuUpdate.setVisible(false);
        //The floating button will be used to update content if exists internet connection
        //floatingUpdateButton.hide();
        if (mapFragment != null) mapFragment.setMapViewDependingConnection();
    }


    //---------------------------------
    // Private
    //---------------------------------

    /**
     * Updates the data
     * Download new data with ShareDataController this method calls PrincipalMapActivity build method when is finished
     */
    public void update(){

            if (Connectivity.isNetworkAvaiable(this)){
                //Download data
                ShareDataController sddm = new ShareDataController();
                sddm.run(this);

                //Refresh Map (The map is actually refreshed diectly by ShareDataController on success
                //mapFragment.refreshMap();
            }else{
                SafegeesDAO.refreshInstance(this);
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
        //Delete last position on map
        MainActivity.DATA_STORAGE.remove(getResources().getString(R.string.MAP_LAST_ZOOM));
        MainActivity.DATA_STORAGE.remove(getResources().getString(R.string.MAP_LAST_LON));
        MainActivity.DATA_STORAGE.remove(getResources().getString(R.string.MAP_LAST_LAT));
        //Restart application
        Intent i = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage(getBaseContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == REQUEST_IMAGE_CODE && resultCode == Activity.RESULT_OK){
            //Set selected image as bitmap
            bitmap = ImageController.buildBitmapFromData(this, data.getData());
            //Send User Image
            this.sendUserImage();
        }else if(requestCode == REQUEST_CONTACTS_CODE && resultCode == Activity.RESULT_OK){
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
                transaction.remove(getActiveFragment());
                getSupportFragmentManager().popBackStack();
                transaction.commitAllowingStateLoss();
                this.floatingAddContactButton.hide();
            }
            mapFragment.onResume();
            this.connectivityOn();
        }else if(requestCode == REQUEST_CONTACTS_CODE && resultCode == Activity.RESULT_CANCELED){
            //Refresh the contacts fragment adapter list view
            if (this.contactsFragment != null) {
                this.contactsFragment.refresh();

            }
            //this.onBackPressed();
        }

        if (data != null){
            //data != null when image data is received
            Log.e("DATA", data.getDataString());
            ProfileUserFragment myFragment = (ProfileUserFragment) getSupportFragmentManager().findFragmentByTag("profile");
            if (myFragment != null && myFragment.isVisible()) {
                Log.i("ProfileFragment", "Add poto");
                myFragment.setImageBitmap(bitmap);
                //Store in /images
                ImageController.storeUserImage(this);
                //Reload the header image
                loadNavMenuProfile();
            }
        }

        //super.onActivityResult(requestCode, resultCode, data);
    }



    public void pickImage(View View) {
        //pickImage(v);
        Log.i("Picked image", "true");
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, REQUEST_IMAGE_CODE);
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

    public void showProfile(View v){
        this.floatingAddContactButton.hide();
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

        //close lateral menu
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawers();
    }

    public MapFragment getMapFragment(){
        return mapFragment;
    }

    public void addContactPopup(){

        //Open add contact popup

        AlertDialog.Builder builder = new AlertDialog.Builder(PrincipalMapActivity.getInstance());
        // Get the layout inflater
        LayoutInflater inflater = PrincipalMapActivity.getInstance().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.profile_alert, null);
        builder.setView(dialogView);

        final EditText edt = (EditText) dialogView.findViewById(R.id.edit1);

        builder.setTitle("Add Contact");
        //builder.setMessage("Enter text below");
        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                String userEmail = MainActivity.DATA_STORAGE.getString(getApplicationContext().getString(R.string.KEY_USER_MAIL));
                //Add the contact
                ShareDataController sssdc = new ShareDataController();
                sssdc.addContact(getApplicationContext(), userEmail, edt.getText().toString());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        AlertDialog b = builder.create();
        b.show();
    }

    public void startContactActivityForResult(int position){
        Intent i = new Intent(this, ContactProfileActivity.class);
        i.putExtra("position", position);
        this.startActivityForResult(i,REQUEST_CONTACTS_CODE);
        this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void sendUserImage() {
        //Add the contact
        ShareDataController sssdc = new ShareDataController();
        sssdc.sendUserImageFile(getApplicationContext(), SafegeesDAO.getInstance(getApplicationContext()).getPublicUser().getPublicEmail());
    }



}
