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
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import org.safegees.safegees.R;
import org.safegees.safegees.gui.fragment.ProfileContactFragment;
import org.safegees.safegees.model.Friend;
import org.safegees.safegees.util.SafegeesDAO;
import org.safegees.safegees.util.ShareDataController;
import org.safegees.safegees.util.StoredDataQuequesManager;

import java.util.ArrayList;

public class KmlPointViewActivity extends AppCompatActivity implements ProfileContactFragment.OnFragmentInteractionListener {

    private String title;           //Friend position in SafegeesDAO ArrayList<Friend> friends
    private String description;
    private Menu menu;
    private MenuInflater inflater;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kml_points);

        Intent mIntent = getIntent();
        title = mIntent.getStringExtra("title");
        description = mIntent.getStringExtra("description");
        Log.e("DESC",description);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(title);

        TextView wb = (TextView) findViewById(R.id.textKml);
        //patch
        description = description.replaceAll("<br>","");
        wb.setText(description);
        /*
        wb.setWebViewClient(new WebViewClient());    //the lines of code added
        wb.setWebChromeClient(new WebChromeClient()); //same as above

        WebSettings webSettings = wb.getSettings();
        webSettings.setJavaScriptEnabled(true);
        */

        //String html = "<html><head></head><body>"+description+"</body></html>";
        //wb.loadData(html, "text/html; charset=utf-8", "utf-8");


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.

        /*
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

        */


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
        inflater.inflate(R.menu.kml_point_menu, this.menu);

        /*
        ArrayList<Friend> friends = SafegeesDAO.getInstance(getBaseContext()).getMutualFriends();
        Friend friend = friends.get(position);
        if (friend.getPhoneNumber() == null || friend.getPhoneNumber().equals("")){
            this.menu.findItem(R.id.menu_call).setVisible(false);
        }else{
            menu.findItem(R.id.menu_call).setVisible(true);
        }

    */
        return true;
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.onBackPressed();
                break;
        }
        return true;
    }

}
