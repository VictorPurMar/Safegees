package org.safegees.safegees.gui.gui_utils;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.infowindow.MarkerInfoWindow;
import org.safegees.safegees.R;
import org.safegees.safegees.gui.view.ContactProfileActivity;
import org.safegees.safegees.gui.view.KmlPointViewActivity;
import org.safegees.safegees.gui.view.PrincipalMapActivity;
import org.safegees.safegees.util.ImageController;

/**
 * Created by victor on 26/2/16.
 */
public class MapInfoWindow extends MarkerInfoWindow  {
    //private POI mSelectedPoi;

    public MapInfoWindow(MapView mapView) {
        super(R.layout.map_info_window, mapView);
        /*
        Button btn = (Button)(mView.findViewById(org.safegees.safegees.R.id.bubble_moreinfo));
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Button clicked", Toast.LENGTH_LONG).show();
            }
        });*/

    }

    @Override
    public void onOpen(Object item) {
        //super.onOpen(item);

        Marker marker = (Marker)item;


        LinearLayout layout = (LinearLayout) mView.findViewById(R.id.bubble_layout);
        Button btnMoreInfo = (Button) mView.findViewById(R.id.bubble_moreinfo);
        TextView txtTitle = (TextView) mView.findViewById(R.id.bubble_title);
        TextView txtDescription = (TextView) mView.findViewById(R.id.bubble_description);
        TextView txtSubdescription = (TextView) mView.findViewById(R.id.bubble_subdescription);
        txtDescription.setVisibility(View.GONE);
        txtSubdescription.setVisibility(View.GONE);
        btnMoreInfo.setVisibility(View.GONE);

        txtTitle.setText(marker.getTitle());
        txtDescription.setText("");
        txtSubdescription.setText("");
        layout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Override Marker's onClick behaviour here
            }
        });


        //KmlPoint selectedPoint = (KmlPoint)marker.getRelatedObject();

        //Close on click map
        for(int i=0; i<mMapView.getOverlays().size(); ++i){
            Overlay o = mMapView.getOverlays().get(i);
            if(o instanceof Marker){
                Marker m = (Marker) o;
                if(m!=marker)
                    m.closeInfoWindow();
            }
        }

        //Auto close marker
        Handler myHandler = new CloseAfterTimer();
        Message m = new Message();
        m.obj = marker;//passing a parameter here
        myHandler.sendMessageDelayed(m, 3000);

        Drawable dr = marker.getImage();

        final Marker mFinal = marker;

        Snackbar snackbar = Snackbar
                //.make(PrincipalMapActivity.getInstance().getFloatingButton(), overlay.getTitle(), Snackbar.LENGTH_LONG)
                .make(PrincipalMapActivity.getInstance().getMapFragment().getView(), marker.getTitle()  +"\n"+ marker.getSubDescription(), Snackbar.LENGTH_LONG)
                .setAction(getMapView().getContext().getResources().getString(R.string.more), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        Intent i = new Intent(view.getContext(), KmlPointViewActivity.class);
                        i.putExtra("title", mFinal.getTitle());
                        i.putExtra("description", mFinal.getSubDescription());
                        view.getContext().startActivity(i);

                    }
                });

        View snackBarLayout = snackbar.getView();
        snackBarLayout.setBackgroundColor(ContextCompat.getColor(mMapView.getContext(), R.color.colorCadaquesAccentDark));
        TextView textView = (TextView)snackBarLayout.findViewById(android.support.design.R.id.snackbar_text);
        textView.setMaxLines(4); // Change your max lines
        textView.setTextSize(12);
        //if (dr == null)dr = ContextCompat.getDrawable(getActivity().getApplicationContext(),R.drawable.default_user_rounded);
        dr.setBounds(0,0,150,150);
        //textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.default_user_rounded, 0, 0, 0);
        textView.setCompoundDrawables(dr, null, null, null);
        textView.setCompoundDrawablePadding(mMapView.getContext().getResources().getDimensionPixelOffset(R.dimen.snackbar_contact_image));
                        /*
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                            int layoutDirection =  TextUtils.getLayoutDirectionFromLocale(Locale.getDefault());
                            if (layoutDirection == View.TEXT_DIRECTION_RTL) {
                                textView.setTextDirection(View.TEXT_DIRECTION_LTR);
                                textView.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
                            }
                        }*/
        snackbar.show();

    }

    class CloseAfterTimer extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Marker m = (Marker) msg.obj;
            m.closeInfoWindow();
        }
    }


}
