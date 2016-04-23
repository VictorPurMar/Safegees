package org.safegees.safegees.gui.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;

import org.safegees.safegees.R;
import org.safegees.safegees.gui.fragment.ProfileContactFragment;
import org.safegees.safegees.model.Friend;
import org.safegees.safegees.util.SafegeesDAO;

import java.util.ArrayList;

public class ContactProfileActivity extends AppCompatActivity implements ProfileContactFragment.OnFragmentInteractionListener{

    private int position;           //Friend position in SafegeesDAO ArrayList<Friend> friends
    GestureDetector gestureScanner;

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

    }

    private void sendEmail() {
        ArrayList<Friend> friends = SafegeesDAO.getInstance(getBaseContext()).getFriends();
        Friend friend = friends.get(position);
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"+friend.getPublicEmail()));
        startActivity(intent);
    }

    private void showOnMap(){
        ArrayList<Friend> friends = SafegeesDAO.getInstance(getBaseContext()).getFriends();
        Friend friend = friends.get(position);
        PrincipalMapActivity.getInstance().getMapFragment().centerMapViewOnFriend(friend);
        //PrincipalMapActivity.getInstance().showMapFragment();
        setResult(RESULT_OK, null);
        finish();

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
        public Fragment getItem(int pos) {



            // getItem is called to instantiate the fragment for the given page.
            return ProfileContactFragment.newInstance(pos);
        }


        @Override
        public int getCount() {
            ArrayList<Friend> friends = SafegeesDAO.getInstance(getBaseContext()).getFriends();
            return friends.size();
        }

        @Override
        public CharSequence getPageTitle(int pos) {
            return "FRIENDS";
        }
    }
}
