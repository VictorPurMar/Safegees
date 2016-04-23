package org.safegees.safegees.gui.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import org.safegees.safegees.R;
import org.safegees.safegees.gui.fragment.ProfileContactFragment;
import org.safegees.safegees.model.Friend;
import org.safegees.safegees.util.SafegeesDAO;

import java.util.ArrayList;

public class ContactProfileActivity extends AppCompatActivity implements ProfileContactFragment.OnFragmentInteractionListener{

    private int position;           //Friend position in SafegeesDAO ArrayList<Friend> friends
    /*
    private GestureDetector gestureDetector;
    View.OnTouchListener gestureListener;

    private int position;           //Friend position in SafegeesDAO ArrayList<Friend> friends

    //Fields
    private EditText editName;      //Tag name
    private EditText editSurname;   //Tag surname
    private EditText editEmail;     //Tag email
    private EditText editPhone;     //Tag phone
    private EditText editTopic;     //Tag topic

    //Image selector
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_profile);

        Intent mIntent = getIntent();
        position = mIntent.getIntExtra("position", 0);

        ArrayList<Friend> friends = SafegeesDAO.getInstance(this).getFriends();

        Friend friend = friends.get(position);


        imageView = (ImageView) findViewById(R.id.result);
        editName = (EditText) findViewById(R.id.editName);
        editSurname = (EditText) findViewById(R.id.editSurname);
        editEmail = (EditText) findViewById(R.id.editEmail);
        editPhone = (EditText) findViewById(R.id.editPhone);
        editTopic = (EditText) findViewById(R.id.editTopic);

        LinearLayout llName = (LinearLayout) findViewById(R.id.lay_name);
        LinearLayout llSurname = (LinearLayout) findViewById(R.id.lay_surname);
        LinearLayout llMail = (LinearLayout) findViewById(R.id.lay_mail);
        LinearLayout llPhone = (LinearLayout) findViewById(R.id.lay_phone);
        LinearLayout llBio = (LinearLayout) findViewById(R.id.lay_topic);

        if (friend != null) {
            this.editName.setText(friend.getName() != null ? friend.getName() : "");
            this.editSurname.setText(friend.getSurname() != null ? friend.getSurname() : "");
            this.editEmail.setText(friend.getPublicEmail() != null ? friend.getPublicEmail() : "");
            this.editPhone.setText(friend.getPhoneNumber() != null ? friend.getPhoneNumber() : "");
            this.editTopic.setText(friend.getBio() != null ? friend.getBio() : "");
        }

        if (friend.getName() == null ||friend.getName().equals("") ) llName.setVisibility(View.GONE);
        if (friend.getSurname() == null ||friend.getSurname().equals("")) llSurname.setVisibility(View.GONE);
        if (friend.getPublicEmail() == null ||friend.getPublicEmail().equals("")) llMail.setVisibility(View.GONE);
        if (friend.getPhoneNumber() == null ||friend.getPhoneNumber().equals("")) llPhone.setVisibility(View.GONE);
        if (friend.getBio() == null || friend.getBio().equals("")) llBio.setVisibility(View.GONE);



    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {

        return false;
    }

    */

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


        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    /**
     * A placeholder fragment containing a simple view.
     */
    /*
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
            /*
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
    /*
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
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
            // Show 3 total pages.
            ArrayList<Friend> friends = SafegeesDAO.getInstance(getBaseContext()).getFriends();
            return friends.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "SECTION";
        }
    }
}
