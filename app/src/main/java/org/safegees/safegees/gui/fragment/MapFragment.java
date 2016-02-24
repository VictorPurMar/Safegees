package org.safegees.safegees.gui.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;
import org.safegees.safegees.R;
import org.safegees.safegees.model.Contact;
import org.safegees.safegees.model.LatLng;
import org.safegees.safegees.model.POI;
import org.safegees.safegees.util.Connectivity;
import org.safegees.safegees.util.MapFileManager;
import org.safegees.safegees.util.SafegeesDAO;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by victor on 21/2/16.
 */
public class MapFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    //List view (Recicler) implementation
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    //My Location
    private IMapController mapController;
    private MapView mapView;
    private SafegeesDAO sDAO;
    private ArrayList<OverlayItem> items;
    private MyLocationNewOverlay myLocationOverlay;
    private ItemizedOverlay<OverlayItem> mMyLocationOverlay;
   // private ArrayList<OverlayItem> poisList ;
    //private ArrayList<OverlayItem> contactList ;
    private IMapController mapViewController;
    //This fragment view
    private View view;

    private int actionBarHeight = 90;
    public MapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileUserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ContactsFragment newInstance(String param1, String param2) {
        ContactsFragment fragment = new ContactsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        this.view = inflater.inflate(R.layout.fragment_map, container, false);
        sDAO = SafegeesDAO.getInstance(getContext());

        mapView = (MapView) this.view.findViewById(R.id.mapview);
        //poisList = new ArrayList<OverlayItem>();
        //contactList = new ArrayList<OverlayItem>();



        mapView = (MapView) this.view.findViewById(R.id.mapview);

        mapView.setMultiTouchControls(true);
        mapView.setMinZoomLevel(2);


        //Set Map Controller
        mapViewController = mapView.getController();
        mapViewController.setZoom(4);
        GeoPoint mediterrany = new GeoPoint(34.553127, 18.048012);
        mapViewController.setCenter(mediterrany);

        // My Location Overlay
        myLocationOverlay = new MyLocationNewOverlay(getContext(), mapView);
        myLocationOverlay.enableMyLocation(); // not on by default
        myLocationOverlay.runOnFirstFix(new Runnable() {
            public void run() {
                mapViewController.animateTo(myLocationOverlay.getMyLocation());
            }
        });

        //Rotation Gesture Overlay
        RotationGestureOverlay mRotationGestureOverlay = new RotationGestureOverlay(getContext(), mapView);
        mRotationGestureOverlay.setEnabled(true);
        mapView.getOverlays().add(mRotationGestureOverlay);


        //Compass
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.FROYO) {
            CompassOverlay mCompassOverlay = new CompassOverlay(getContext(), new InternalCompassOrientationProvider(getContext()),
                    mapView);
            mCompassOverlay.enableCompass();
            mapView.getOverlays().add(mCompassOverlay);
        }

        setMapViewDependingConnection();

        refreshMap();

        //mapView.getOverlays().add(myLocationOverlay);
        //myLocationOverlay.enableFollowLocation();

        return view;
    }

    public void setMapViewDependingConnection() {

            if (Connectivity.isWifiConnection(getContext())) {
                mapView.setTileSource(new XYTileSource("Mapnik",
                        2, 18, 384, ".png", new String[]{
                        "http://a.tile.openstreetmap.org/",
                        "http://b.tile.openstreetmap.org/",
                        "http://c.tile.openstreetmap.org/"}));
                mapView.setUseDataConnection(true);

            } else {
                //String externalStorageDirectory =  MapFileManager.getUserStorageriority();
                //String destination = externalStorageDirectory + File.separator + "osmdroid" + File.separator + "tiles" + File.separator + "Mapnik"+File.separator;
                //mapView.setTileSource(new XYTileSource("Mapnik", 2, 18, 384, ".png", new String[]{}));
                mapView.setTileSource(new XYTileSource("Mapnik", 0, 18, 384, ".png", new String[]{}));
                mapView.setUseDataConnection(false);

            }

    }


    public void refreshMap(){

        //Rebuild objects in sDAO
        this.sDAO = SafegeesDAO.refreshInstance(getContext());

        //Clear the map
        //this.mapView.invalidate();
        //Build mMap with local Tiles
        //buildMap(this.mMap);
        //Add markers
        this.refreshPointsInMap();



    }

    /**
     * Get the points from sDAO (SafeggeesDAO) and set the markers on Map
     */
    private void refreshPointsInMap() {

        mapView.invalidate();
        if (this.sDAO != null) {
            ArrayList<OverlayItem> poisList = new ArrayList<OverlayItem>();
            ArrayList<OverlayItem> contactList = new ArrayList<OverlayItem>();
            Drawable poiDrawable = getResources().getDrawable(R.drawable.ic_add_location_black_24dp);
            ArrayList<POI> pois = this.sDAO.getPois();
            for (int i = 0; i < pois.size(); i++) {
                POI poi = pois.get(i);
                LatLng latLng = poi.getPosition();
                GeoPoint geopoint = new GeoPoint(latLng.getLatitude(), latLng.getLongitude());
                OverlayItem item = new OverlayItem(poi.getName(), poi.getDescription(), geopoint);
                item.setMarker(poiDrawable);
                poisList.add(item);
            }

            Drawable contactDrawable = getResources().getDrawable(R.drawable.ic_person_pin_circle_black_24dp);
            ArrayList<Contact> contacts = this.sDAO.getContacts();
            for (int i = 0; i < contacts.size(); i++) {
                Contact contact = contacts.get(i);
                LatLng latLng = contact.getPosition();
                GeoPoint geopoint = new GeoPoint(latLng.getLatitude(), latLng.getLongitude());
                OverlayItem item = new OverlayItem(contact.getName(), contact.getEmail(), geopoint);
                item.setMarker(contactDrawable);
                contactList.add(item);
            }


            TypedValue tv = new TypedValue();
            if (getActivity().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
                actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
            }

            ItemizedIconOverlay contactOverlay = new ItemizedIconOverlay(contactList, poiDrawable, new ItemizedIconOverlay.OnItemGestureListener() {
                @Override
                public boolean onItemSingleTapUp(int index, Object item) {
                    OverlayItem overlay = (OverlayItem) item;
                    overlay.getTitle();
                    overlay.getSnippet();
                    Toast toast = Toast.makeText(getContext(), overlay.getTitle() + " | " + overlay.getSnippet(), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP | Gravity.RIGHT, 5, actionBarHeight + 10);
                    toast.show();
                    return false;
                }

                @Override
                public boolean onItemLongPress(int index, Object item) {
                    return false;
                }
            }, new DefaultResourceProxyImpl(getContext()));

            ItemizedIconOverlay poiOverlay = new ItemizedIconOverlay(poisList, contactDrawable, new ItemizedIconOverlay.OnItemGestureListener() {
                @Override
                public boolean onItemSingleTapUp(int index, Object item) {
                    OverlayItem overlay = (OverlayItem) item;
                    overlay.getTitle();
                    overlay.getSnippet();
                    Toast toast = Toast.makeText(getContext(), overlay.getTitle() + " | " + overlay.getSnippet(), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP | Gravity.RIGHT, 5, actionBarHeight + 10);
                    toast.show();
                    return false;
                }

                @Override
                public boolean onItemLongPress(int index, Object item) {
                    return false;
                }
            }, new DefaultResourceProxyImpl(getContext()));

            mapView.getOverlays().add(contactOverlay);
            mapView.getOverlays().add(poiOverlay);

        }


    }




    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public static Fragment newInstance() {
        ContactsFragment mFrgment = new ContactsFragment();
        return mFrgment;
    }
}
