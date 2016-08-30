package org.safegees.safegees.gui.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.kml.KmlDocument;
import org.osmdroid.bonuspack.kml.KmlFeature;
import org.osmdroid.bonuspack.kml.KmlLineString;
import org.osmdroid.bonuspack.kml.KmlPlacemark;
import org.osmdroid.bonuspack.kml.KmlPoint;
import org.osmdroid.bonuspack.kml.KmlPolygon;
import org.osmdroid.bonuspack.kml.Style;
import org.osmdroid.views.overlay.FolderOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polygon;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlay;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;
import org.safegees.safegees.R;
import org.safegees.safegees.gui.gui_utils.MapInfoWindow;
import org.safegees.safegees.gui.view.ContactProfileActivity;
import org.safegees.safegees.gui.view.MainActivity;
import org.safegees.safegees.gui.view.PrincipalMapActivity;
import org.safegees.safegees.model.Friend;
import org.safegees.safegees.model.LatLng;
import org.safegees.safegees.model.POI;
import org.safegees.safegees.util.Connectivity;
import org.safegees.safegees.util.FileManager;
import org.safegees.safegees.util.ImageController;
import org.safegees.safegees.util.SafegeesDAO;
import org.safegees.safegees.util.ShareDataController;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

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
    private KmlDocument mKmlDocument;
    private LatLng actualPosition;

    final private static int TILE_PX_SIZE = 512;

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

        mKmlDocument = new KmlDocument();

        mapView = (MapView) this.view.findViewById(R.id.mapview);
        mapView = (MapView) this.view.findViewById(R.id.mapview);
        mapView.setMultiTouchControls(true);
        mapView.setMinZoomLevel(2);

        //Set Map Controller
        mapViewController = mapView.getController();

        GeoPoint lastMapPosition = new GeoPoint(MainActivity.DATA_STORAGE.getDouble(getResources().getString(R.string.MAP_LAST_LAT)), MainActivity.DATA_STORAGE.getDouble(getResources().getString(R.string.MAP_LAST_LON)));
        if (lastMapPosition != null && (lastMapPosition.getLongitude() != 0 && lastMapPosition.getLatitude() != 0)){
            mapViewController.setCenter(lastMapPosition);
            mapViewController.setZoom(MainActivity.DATA_STORAGE.getInt(getResources().getString(R.string.MAP_LAST_ZOOM)));
        }else{
            mapViewController.setZoom(4);
            GeoPoint mediterrany = new GeoPoint(34.553127, 18.048012);
            mapViewController.setCenter(mediterrany);
        }
        setMapViewDependingConnection();
        refreshMap();
        return view;
    }

    @Override
    public void onDestroy() {
        MainActivity.DATA_STORAGE.putDouble(getResources().getString(R.string.MAP_LAST_LON), mapView.getMapCenter().getLongitude());
        MainActivity.DATA_STORAGE.putDouble(getResources().getString(R.string.MAP_LAST_LAT), mapView.getMapCenter().getLatitude());
        MainActivity.DATA_STORAGE.putInt(getResources().getString(R.string.MAP_LAST_ZOOM), mapView.getZoomLevel());
        super.onDestroy();
    }

    private void setInitialMapConfiguration() {


        // My Location Overlay
        myLocationOverlay = new MyLocationNewOverlay(mapView);
        myLocationOverlay.enableMyLocation(); // not on by default

        if (myLocationOverlay.getMyLocation() == null){
            myLocationOverlay.runOnFirstFix(new Runnable() {
                public void run() {
                    try {
                        mapViewController.animateTo(myLocationOverlay.getMyLocation());
                        //myLocationOverlay.enableFollowLocation();
                        myLocationOverlay.setPersonIcon(getBitmapFromDrawable(getResources().getDrawable(R.drawable.ic_user_position)));
                        mapView.getOverlays().add(myLocationOverlay);
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                        boolean mobileAllowed = prefs.getBoolean("pref_position_share", true);
                        if (Connectivity.isNetworkAvaiable(getContext()) && mobileAllowed) {
                            LatLng latLng = new LatLng(myLocationOverlay.getMyLocation().getLatitude(), myLocationOverlay.getMyLocation().getLongitude());
                            Log.i("POSITION", latLng.toString());
                            //Add the contact
                            ShareDataController sssdc = new ShareDataController();
                            sssdc.sendUserPosition(getContext(), SafegeesDAO.getInstance(getContext()).getPublicUser().getPublicEmail(), latLng);
                        }
                    }catch (Exception e){
                        //To do
                    }
                }
            });
        }else{
            mapViewController.animateTo(myLocationOverlay.getMyLocation());
            //myLocationOverlay.enableFollowLocation();
            myLocationOverlay.setPersonIcon(getBitmapFromDrawable(getResources().getDrawable(R.drawable.ic_user_position)));
            mapView.getOverlays().add(myLocationOverlay);
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
            boolean mobileAllowed = prefs.getBoolean("pref_position_share", true);
            if(Connectivity.isNetworkAvaiable(getContext()) && mobileAllowed) {
                LatLng latLng = new LatLng(myLocationOverlay.getMyLocation().getLatitude(), myLocationOverlay.getMyLocation().getLongitude());
                Log.i("POSITION", latLng.toString());
                //Add the contact
                ShareDataController sssdc = new ShareDataController();
                sssdc.sendUserPosition(getContext(), SafegeesDAO.getInstance(getContext()).getPublicUser().getPublicEmail(), latLng);
            }
        }

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
    }

    public void setMapViewDependingConnection() {

            if (Connectivity.isNetworkAvaiable(getContext())) {
                mapView.setTileSource(new XYTileSource("Mapnik",
                        2, 18, TILE_PX_SIZE, ".png", new String[]{
                        "http://a.tile.openstreetmap.org/",
                        "http://b.tile.openstreetmap.org/",
                        "http://c.tile.openstreetmap.org/"}));
                mapView.setUseDataConnection(true);

            } else {
                //String externalStorageDirectory =  MapFileManager.getUserStorageriority();
                //String destination = externalStorageDirectory + File.separator + "osmdroid" + File.separator + "tiles" + File.separator + "Mapnik"+File.separator;
                //mapView.setTileSource(new XYTileSource("Mapnik", 2, 18, 384, ".png", new String[]{}));
                mapView.setTileSource(new XYTileSource("Mapnik", 0, 18, TILE_PX_SIZE, ".png", new String[]{}));
                mapView.setUseDataConnection(false);

            }

    }


    public void refreshMap(){

        //Rebuild objects in sDAO
        this.sDAO = SafegeesDAO.refreshInstance(getContext());

        //Add markers
        this.refreshPointsInMap();

        //NEW TRY TO SEND THE POS
        try {
            /*
            mapViewController.animateTo(myLocationOverlay.getMyLocation());
            myLocationOverlay.disableFollowLocation();
            myLocationOverlay.setPersonIcon(getBitmapFromDrawable(getResources().getDrawable(R.drawable.ic_user_position)));

            mapView.getOverlays().add(myLocationOverlay);
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
            //boolean mobileAllowed = prefs.getBoolean("pref_position_share", false);
            if (Connectivity.isNetworkAvaiable(getContext())) {
                LatLng latLng = new LatLng(myLocationOverlay.getMyLocation().getLatitude(), myLocationOverlay.getMyLocation().getLatitude());
                Log.i("POSITION", latLng.toString());
                //Add the contact
                ShareDataController sssdc = new ShareDataController();
                sssdc.sendUserPosition(getContext(), SafegeesDAO.getInstance(getContext()).getPublicUser().getPublicEmail(), latLng);
            }
            */
        }catch (Exception e){
            Log.e("POS ERROR", e.getMessage());
        }
    }

    /**
     * Get the points from sDAO (SafeggeesDAO) and set the markers on Map
     */
    private void refreshPointsInMap() {


        setStyles();
        mapView.getOverlays().clear(); //Clear old items
        setInitialMapConfiguration();   //Set initial map overlays and


        File file = new File(FileManager.getFileStorePath("volunteers.kml", this.getContext()).getAbsolutePath());

        if (file.exists() && PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean("pref_external_pois_map", false)) {
            mKmlDocument.parseKMLFile(FileManager.getFileStorePath("volunteers.kml", this.getContext()));
            Drawable defaultMarker = getResources().getDrawable(R.drawable.ic_add_location_black_24dp);
            FolderOverlay campaments = getFolderOverlay(defaultMarker);
            mapView.getOverlays().add(campaments);
        }


        file = new File(FileManager.getFileStorePath("syrian.kml", this.getContext()).getAbsolutePath());
        if (file.exists() && PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean("pref_external_pois_map", false)) {
            mKmlDocument.parseKMLFile(FileManager.getFileStorePath("syrian.kml", this.getContext()));
            Drawable defaultMarker = getResources().getDrawable(R.drawable.ic_place_gray);
            FolderOverlay syrian = getFolderOverlay(defaultMarker);
            mapView.getOverlays().add(syrian);
        }

        if (this.sDAO != null) {

            ArrayList<OverlayItem> poiList = new ArrayList<OverlayItem>();
            ArrayList<OverlayItem> contactList = new ArrayList<OverlayItem>();

            Drawable poiDrawable = getResources().getDrawable(R.drawable.ic_default_safegees);
            ArrayList<POI> pois = this.sDAO.getPois();
            for (int i = 0; i < pois.size(); i++) {
                POI poi = pois.get(i);
                LatLng latLng = poi.getPosition();
                GeoPoint geopoint = new GeoPoint(latLng.getLatitude(), latLng.getLongitude());
                OverlayItem item = new OverlayItem(poi.getName() , poi.getDescription(), geopoint);
                item.setMarker(poiDrawable);
                poiList.add(item);
            }

            Drawable contactDrawable = getResources().getDrawable(R.drawable.ic_friend);

            ArrayList<Friend> friends = this.sDAO.getMutualFriends();
            if (friends != null) {
                for (int i = 0; i < friends.size(); i++) {
                    Friend friend = friends.get(i);
                    LatLng latLng = friend.getPosition();
                    GeoPoint geopoint = new GeoPoint(latLng.getLatitude(), latLng.getLongitude());
                    OverlayItem item = new OverlayItem(""+i,friend.getPublicEmail(), friend.getBio(), geopoint);
                    item.setMarker(contactDrawable);
                    contactList.add(item);
                }
            }


            TypedValue tv = new TypedValue();
            if (getActivity().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
                actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
            }

            ItemizedIconOverlay poiOverlay = new ItemizedIconOverlay(poiList, poiDrawable, new ItemizedIconOverlay.OnItemGestureListener() {
                @Override
                public boolean onItemSingleTapUp(int index, Object item) {
                    final OverlayItem overlay = (OverlayItem) item;
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
            }, getContext());


            ItemizedIconOverlay contactOverlay = new ItemizedIconOverlay(contactList, contactDrawable, new ItemizedIconOverlay.OnItemGestureListener() {
                @Override
                public boolean onItemSingleTapUp(int index, Object item) {
                    final OverlayItem overlay = (OverlayItem) item;
                    overlay.getTitle();
                    overlay.getSnippet();
                    /*
                    Toast toast = Toast.makeText(getContext(), overlay.getTitle() + " | " + overlay.getSnippet(), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP | Gravity.RIGHT, 5, actionBarHeight + 10);
                    toast.show();
                    */
                    /*
                    SpannableStringBuilder builder = new SpannableStringBuilder();
                    builder.setSpan(new ImageSpan(getActivity(), R.drawable.default_user_rounded), builder.length() - 1, builder.length(), 0);
                    builder.append(" ").append(overlay.getTitle());
                    Snackbar.make(PrincipalMapActivity.getInstance().getMapFragment().getView(), builder, Snackbar.LENGTH_LONG).setAction("MORE", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            int friendPosition = Integer.parseInt(overlay.getUid());
                            Intent i = new Intent(view.getContext(), ContactProfileActivity.class);
                            i.putExtra("position", friendPosition);
                            view.getContext().startActivity(i);
                        }
                    }).show();
                    */

                    SafegeesDAO DAO = SafegeesDAO.getInstance(getContext());
                    Friend friend = sDAO.getFriendWithEmail(overlay.getTitle());
                    Drawable dr = null;
                    try{
                        dr = new BitmapDrawable(ImageController.getContactImageBitmap(getContext(),friend.getPublicEmail()));
                    }catch (Exception e){}

                    Snackbar snackbar = Snackbar
                                //.make(PrincipalMapActivity.getInstance().getFloatingButton(), overlay.getTitle(), Snackbar.LENGTH_LONG)
                                .make(PrincipalMapActivity.getInstance().getMapFragment().getView(), friend.getName() + " " + friend.getSurname() +"\n\""+ friend.getBio()+"\"", Snackbar.LENGTH_LONG)
                                .setAction("MORE", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        int friendPosition = Integer.parseInt(overlay.getUid());
                                        Intent i = new Intent(view.getContext(), ContactProfileActivity.class);
                                        i.putExtra("position", friendPosition);
                                        view.getContext().startActivity(i);
                                    }
                                });



                        View snackBarLayout = snackbar.getView();
                        snackBarLayout.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorCadaquesAccentDark));
                        TextView textView = (TextView)snackBarLayout.findViewById(android.support.design.R.id.snackbar_text);
                        textView.setMaxLines(4); // Change your max lines
                        textView.setTextSize(15);
                        if (dr == null)dr = ContextCompat.getDrawable(getActivity().getApplicationContext(),R.drawable.default_user_rounded);
                        dr.setBounds(0,0,150,150);
                        //textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.default_user_rounded, 0, 0, 0);
                        textView.setCompoundDrawables(dr, null, null, null);
                        textView.setCompoundDrawablePadding(getResources().getDimensionPixelOffset(R.dimen.snackbar_contact_image));

                        snackbar.show();


                        return false;
                }

                @Override
                public boolean onItemLongPress(int index, Object item) {
                    return false;
                }
            }, getContext());



            mapView.getOverlays().add(contactOverlay);
            mapView.getOverlays().add(poiOverlay);

        }
        //KMLTask kml = new KMLTask(this.getContext());
        //kml.execute();
        //mapView.invalidate();



        mapView.invalidate();

    }

    private void setStyles() {

        Style style = new Style(getBitmapFromDrawable(getResources().getDrawable(R.drawable.ic_info)), 0x901010AA, 3.0f, 0x2010AA10);
        mKmlDocument.putStyle("location_info",style);

        style = new Style(getBitmapFromDrawable(getResources().getDrawable(R.drawable.ic_place_gray)), 0x901010AA, 3.0f, 0x2010AA10);
        mKmlDocument.putStyle("location_gray",style);

        style = new Style(getBitmapFromDrawable(getResources().getDrawable(R.drawable.ic_place_green)), 0x901010AA, 3.0f, 0x2010AA10);
        mKmlDocument.putStyle("location_green",style);

        style = new Style(getBitmapFromDrawable(getResources().getDrawable(R.drawable.ic_place_red)), 0x901010AA, 3.0f, 0x2010AA10);
        mKmlDocument.putStyle("location_red",style);

        style = new Style(getBitmapFromDrawable(getResources().getDrawable(R.drawable.ic_place_orange)), 0x901010AA, 3.0f, 0x2010AA10);
        mKmlDocument.putStyle("location_orange",style);

        style = new Style(getBitmapFromDrawable(getResources().getDrawable(R.drawable.ic_place_volunteers)), 0x901010AA, 3.0f, 0x2010AA10);
        mKmlDocument.putStyle("location_volunteers",style);
    }

    private FolderOverlay getFolderOverlay(Drawable defaultMarker) {

        Bitmap defaultBitmap = getBitmapFromDrawable(defaultMarker);

        //Bitmap defaultBitmap = ((BitmapDrawable) defaultMarker).getBitmap();
        Style defaultStyle = new Style(defaultBitmap, 0x901010AA, 3.0f, 0x20AA1010);
        //13.2 Advanced styling with Styler
        KmlFeature.Styler styler = new MyKmlStyler(defaultStyle);

        //FolderOverlay kmlOverlay = (FolderOverlay) mKmlDocument.mKmlRoot.buildOverlay(mapView, null, null, mKmlDocument);
        FolderOverlay kmlOverlay = (FolderOverlay) mKmlDocument.mKmlRoot.buildOverlay(mapView, null, styler, mKmlDocument);
        List<Overlay> overlays = kmlOverlay.getItems();
        FolderOverlay folder = (FolderOverlay)overlays.get(0);

        List<Overlay> capmList = kmlOverlay.getItems();
        for (int i = 0 ; i < capmList.size() ; i++){
            Overlay ov = capmList.get(i);
        }
        return folder;
    }

    @NonNull
    private Bitmap getBitmapFromDrawable(Drawable defaultMarker) {
        Bitmap defaultBitmap = Bitmap.createBitmap(defaultMarker.getIntrinsicWidth(),
                defaultMarker.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(defaultBitmap);
        defaultMarker.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        defaultMarker.draw(canvas);
        return defaultBitmap;
    }


    private File createFileFromInputStream(InputStream inputStream) {

        try{
            File f = new File("refugees.kml");
            OutputStream outputStream = new FileOutputStream(f);
            byte buffer[] = new byte[1024];
            int length = 0;

            while((length=inputStream.read(buffer)) > 0) {
                outputStream.write(buffer,0,length);
            }

            outputStream.close();
            inputStream.close();

            return f;
        }catch (IOException e) {
            //Logging exception
        }

        return null;
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

    //13.2 Loading KML content - Advanced styling with Styler
    class MyKmlStyler implements KmlFeature.Styler {
        Style mDefaultStyle;

        MyKmlStyler(Style defaultStyle) {
            mDefaultStyle = defaultStyle;
        }

        @Override
        public void onLineString(Polyline polyline, KmlPlacemark kmlPlacemark, KmlLineString kmlLineString) {
            //Custom styling:
            polyline.setColor(Color.GREEN);
            polyline.setWidth(Math.max(kmlLineString.mCoordinates.size() / 200.0f, 3.0f));
        }

        @Override
        public void onPolygon(Polygon polygon, KmlPlacemark kmlPlacemark, KmlPolygon kmlPolygon) {
            //Keeping default styling:
            kmlPolygon.applyDefaultStyling(polygon, mDefaultStyle, kmlPlacemark, mKmlDocument, mapView);
        }

        @Override
        public void onPoint(Marker marker, KmlPlacemark kmlPlacemark, KmlPoint kmlPoint) {
            //Styling based on ExtendedData properties:
            //if (kmlPlacemark.getExtendedData("maxspeed") != null)
            //    kmlPlacemark.mStyle = "maxspeed";
            //kmlPoint.applyDefaultStyling(marker, mDefaultStyle, kmlPlacemark, mKmlDocument, mapView);
            Drawable contactDrawable = getResources().getDrawable(R.drawable.ic_airline_seat_individual_suite_black_24dp);
            //kmlPlacemark.mStyle ="Estilo";
            try {
                //Log.e("MARKER subdescription", marker.getSubDescription() != null ?  marker.getSubDescription() : "none");
                //Log.e("MARKER infowindow", marker.getInfoWindow() != null ?  marker.getInfoWindow().toString() : "none");
                //Log.e("MARKER snippet", marker.getSnippet() != null ?  marker.getSnippet().toString() : "none");


                //Log.e("MARKER image", marker.getImage() != null ? marker.getImage().toString() : "none");
            }catch(Exception e){}

            if (kmlPlacemark.mStyle.equals("icon-503-F4EB37")){
                kmlPlacemark.mStyle = "location_green";
            }

            /*else if (kmlPlacemark.mStyle.equals("icon-503-4186F0")){
                kmlPlacemark.mStyle = "location_yellow";
            }
            */
            else if (kmlPlacemark.mStyle.equals("icon-503-DB4436")){
                kmlPlacemark.mStyle = "location_red";
            }else if(kmlPlacemark.mStyle.equals("icon-1255")) {
                kmlPlacemark.mStyle = "location_volunteers";
            }else if(kmlPlacemark.mStyle.equals("icon-503-4186F0")) {
                kmlPlacemark.mStyle = "location_info";
            }else {
                kmlPlacemark.mStyle = "location_gray";
            }

            //marker.getInfoWindow();
            marker.setInfoWindow(new MapInfoWindow(mapView));
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_TOP);

            kmlPoint.applyDefaultStyling(marker, null, kmlPlacemark, mKmlDocument, mapView);


        }

        @Override
        public void onFeature(Overlay overlay, KmlFeature kmlFeature) {
            //If nothing to do, do nothing.
        }
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


    public void centerMapViewOnFriend(Friend friend){
        this.mapViewController.setCenter(new GeoPoint(friend.getPosition().getLatitude(), friend.getPosition().getLongitude()));
        PrincipalMapActivity.getInstance().refreshMap();
    }


}
