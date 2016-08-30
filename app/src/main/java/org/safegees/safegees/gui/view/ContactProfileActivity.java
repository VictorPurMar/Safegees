package org.safegees.safegees.gui.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;

import org.safegees.safegees.R;
import org.safegees.safegees.gui.fragment.ProfileContactFragment;
import org.safegees.safegees.model.Friend;
import org.safegees.safegees.util.SafegeesDAO;
import org.safegees.safegees.util.ShareDataController;
import org.safegees.safegees.util.StoredDataQuequesManager;

import java.util.ArrayList;

public class ContactProfileActivity extends AppCompatActivity implements ProfileContactFragment.OnFragmentInteractionListener {

    private int position;           //Friend position in SafegeesDAO ArrayList<Friend> friends
    private Menu menu;
    private MenuInflater inflater;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");


        Intent mIntent = getIntent();
        position = mIntent.getIntExtra("position", 0);


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(position);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int index) {
                position = index;

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }
        });



        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Mail", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                sendEmail();
            }
        });

        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Show", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                showOnMap();
            }
        });
        */

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.contact_profile_menu, this.menu);

        ArrayList<Friend> friends = SafegeesDAO.getInstance(getBaseContext()).getMutualFriends();
        Friend friend = friends.get(position);
        if (friend.getPhoneNumber() == null || friend.getPhoneNumber().equals("")){
            this.menu.findItem(R.id.menu_call).setVisible(false);
        }else{
            menu.findItem(R.id.menu_call).setVisible(true);
        }

        return true;
    }


    private void sendEmail() {
        ArrayList<Friend> friends = SafegeesDAO.getInstance(getBaseContext()).getMutualFriends();
        Friend friend = friends.get(position);
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + friend.getPublicEmail()));
        startActivity(intent);
    }

    private void callContact() {
        ArrayList<Friend> friends = SafegeesDAO.getInstance(getBaseContext()).getMutualFriends();
        Friend friend = friends.get(position);
        if (friend.getPhoneNumber() != null && !friend.getPhoneNumber().equals("")) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + friend.getPhoneNumber()));
            startActivity(intent);
        }
    }

    private void showOnMap(){
        ArrayList<Friend> friends = SafegeesDAO.getInstance(getBaseContext()).getMutualFriends();
        Friend friend = friends.get(position);
        PrincipalMapActivity.getInstance().getMapFragment().centerMapViewOnFriend(friend);
        //PrincipalMapActivity.getInstance().showMapFragment();
        setResult(RESULT_OK, null);
        finish();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.onBackPressed();
                break;
            case R.id.menu_call:
                callContact();
                break;
            case R.id.menu_email:
                sendEmail();
                break;
            case R.id.menu_show_on_map:
                showOnMap();
                break;
            case R.id.menu_delete_contact:
                deleteContact();
                break;
        }
        return true;
    }

    private void deleteContact() {

        //Delete contact with advice
        final ContactProfileActivity cpactivity = this;
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(this.getResources().getString(R.string.delete_contact_title))
                .setMessage(this.getResources().getString(R.string.delete_contact_advice_text))
                .setPositiveButton(this.getResources().getString(R.string.delete_contact_accept_button), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        SafegeesDAO dao = SafegeesDAO.getInstance(getBaseContext());

                        //Delete contact
                        ArrayList<Friend> friends = dao.getMutualFriends();
                        Friend friendToDelete = friends.get(position);

                        //Add to users for delete
                        StoredDataQuequesManager.putUserContactToDeleteInQueque(cpactivity, dao.getPublicUser().getPublicEmail(), friendToDelete.getPublicEmail());
                        dao.refreshInstance(getBaseContext());

                        //Add the contact
                        ShareDataController sssdc = new ShareDataController();
                        sssdc.deleteContact(cpactivity, SafegeesDAO.getInstance(cpactivity).getPublicUser().getPublicEmail(), friendToDelete.publicEmail);
                        cpactivity.onBackPressed();
                    }
                })
                .setNegativeButton(this.getResources().getString(R.string.delete_contact_cancel_button), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                });

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            Drawable icon = ContextCompat.getDrawable(getApplicationContext(), android.R.drawable.ic_dialog_alert).mutate();
            icon.setColorFilter(new ColorMatrixColorFilter(new float[] {
                    -1, 0, 0, 0, 255, // red = 255 - red
                    0, -1, 0, 0, 255, // green = 255 - green
                    0, 0, -1, 0, 255, // blue = 255 - blue
                    0, 0, 0, 1, 0     // alpha = alpha
            }));
            builder.setIcon(icon);
        } else {
            builder.setIconAttribute(android.R.attr.alertDialogIcon);
        }

        builder.show();



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //overridePendingTransition(R.anim.right_to_left,R.anim.left_to_right);
        this.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            this.getItem(position);
        }


        @Override
        public void startUpdate(ViewGroup container) {
            super.startUpdate(container);
            if (menu != null){
                ArrayList<Friend> friends = SafegeesDAO.getInstance(getBaseContext()).getMutualFriends();
                Friend friend = friends.get(position);
                if (friend.getPhoneNumber() == null || friend.getPhoneNumber().equals("")){
                    menu.findItem(R.id.menu_call).setVisible(false);
                }else{
                    menu.findItem(R.id.menu_call).setVisible(true);
                }
            }
        }



        @Override
        public Fragment getItem(int pos) {
            // getItem is called to instantiate the fragment for the given page.
            return ProfileContactFragment.newInstance(pos);
        }


        @Override
        public int getCount() {
            ArrayList<Friend> friends = SafegeesDAO.getInstance(getBaseContext()).getMutualFriends();
            return friends.size();
        }

        @Override
        public CharSequence getPageTitle(int pos) {
            return "FRIENDS";
        }
    }
}
