package org.safegees.safegees.gui.gui_utils;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.osmdroid.bonuspack.kml.KmlPoint;
import org.osmdroid.bonuspack.location.POI;
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.bonuspack.overlays.MarkerInfoWindow;
import org.osmdroid.views.MapView;
import org.safegees.safegees.R;

/**
 * Created by victor on 26/2/16.
 */
public class MapInfoWindow extends MarkerInfoWindow {
    //private POI mSelectedPoi;

    public MapInfoWindow(MapView mapView) {
        super(R.layout.map_info_window, mapView);
        Button btn = (Button)(mView.findViewById(org.safegees.safegees.R.id.bubble_moreinfo));
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Button clicked", Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onOpen(Object item) {
        super.onOpen(item);
        Marker marker = (Marker)item;
        KmlPoint selectedPoint = (KmlPoint)marker.getRelatedObject();
        //POI poi = new POI();

    }
}
