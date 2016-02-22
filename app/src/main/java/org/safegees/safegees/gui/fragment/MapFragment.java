package org.safegees.safegees.gui.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.views.MapView;
import org.safegees.safegees.R;
import org.safegees.safegees.gui.adapters.MyAdapter;
import org.safegees.safegees.gui.decoration.DividerItemDecoration;
import org.safegees.safegees.model.Contact;
import org.safegees.safegees.util.SafegeesDAO;

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


        /*
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        // Inflate the layout for this fragment


        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), null));

        SafegeesDAO sDAO = SafegeesDAO.getInstance(getActivity());
        ArrayList<Contact> contacts = sDAO.getContacts();

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        //mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(contacts);
        mRecyclerView.setAdapter(mAdapter);
        */

        View view = inflater.inflate(R.layout.fragment_map, container, false);
        MapView mapView = (MapView) view.findViewById(R.id.mapview);

        mapView.setTileSource(new XYTileSource("SafegeesMap", 2, 6, 256, ".png", new String[]{}));
        mapView.setMultiTouchControls(true);
        mapView.setMinZoomLevel(2);
        mapView.setUseDataConnection(false); //optional, but a good way to prevent loading from the network and test your zip loading.
        IMapController mapController = mapView.getController();
        mapController.setZoom(4);
        //setContentView(mapView);



        return view;
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
