/**
 *   ProfileUserFragment.java
 *
 *   Future class description
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
 *
 */

package org.safegees.safegees.gui.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import org.safegees.safegees.R;
import org.safegees.safegees.gui.view.PrincipalMapActivity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileUserFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileUserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileUserFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    //Fields
    private EditText editName;      //Tag 1
    private EditText editSurname;   //Tag 2
    private EditText editEmail;     //Tag 3
    private EditText editPhone;     //Tag 4

    //Image selector
    private ImageView imageView;
    private Bitmap bitmap;

    public ProfileUserFragment() {
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
    public static ProfileUserFragment newInstance(String param1, String param2) {
        ProfileUserFragment fragment = new ProfileUserFragment();
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
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_user, container, false);
        imageView = (ImageView) view.findViewById(R.id.result);
        editName = (EditText) view.findViewById(R.id.editName);


        if (this.bitmap != null){
            this.imageView.setImageBitmap(this.bitmap);
        }

        //PrincipalMapActivity activity = (PrincipalMapActivity) getActivity();
        //imageView.setOnClickListener(activity);


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


    public void setImageBitmap(Bitmap bitmap){
        if (this.bitmap != null && !this.bitmap.isRecycled()) {
            this.bitmap.recycle();
            this.bitmap = null;
        }
        this.bitmap = bitmap;
        //Prevent
        //java.lang.RuntimeException: Canvas: trying to use a recycled bitmap android.graphics.Bitmap@38248746
        if (this.bitmap != null && this.imageView != null){
            this.imageView.setImageBitmap(this.bitmap);
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
        ProfileUserFragment mFrgment = new ProfileUserFragment();
        return mFrgment;
    }

    public void setProfileField(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(PrincipalMapActivity.getInstance());
        // Get the layout inflater
        LayoutInflater inflater = PrincipalMapActivity.getInstance().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.profile_alert, null);
        builder.setView(dialogView);

        final EditText edt = (EditText) dialogView.findViewById(R.id.edit1);

        if(Integer.parseInt((String)v.getTag())==1) {
            edt.setText(editName.getText());
            edt.setHint(editName.getHint());
            builder.setTitle("Name");
            builder.setMessage("Enter text below");
            builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    editName.setText(edt.getText());
                    editName.setHint(edt.getHint());
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                }
            });
        }
        AlertDialog b = builder.create();
        b.show();

    }
}
