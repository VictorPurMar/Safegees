/**
 *   ProfileContactFragment.java
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
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.safegees.safegees.R;
import org.safegees.safegees.model.Friend;
import org.safegees.safegees.util.SafegeesDAO;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileContactFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileContactFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileContactFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String POSITION = "position";

    //Fields
    private EditText editName;      //Tag name
    private EditText editSurname;   //Tag surname
    private EditText editEmail;     //Tag email
    private EditText editPhone;     //Tag phone
    private EditText editTopic;     //Tag topic

    //Image selector
    private ImageView imageView;

    private int position;

    private OnFragmentInteractionListener mListener;

    public ProfileContactFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment ProfileUserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileContactFragment newInstance(int position) {
        ProfileContactFragment fragment = new ProfileContactFragment();
        Bundle args = new Bundle();
        args.putInt(POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            position = getArguments().getInt(POSITION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile_contact, container, false);

        ArrayList<Friend> friends = SafegeesDAO.getInstance(view.getContext()).getFriends();

        Friend friend = friends.get(position);

        ImageButton closeContact = (ImageButton) view.findViewById(R.id.close_contact);
        closeContact.setOnClickListener(this);

        imageView = (ImageView) view.findViewById(R.id.result);
        editName = (EditText) view.findViewById(R.id.editName);
        editSurname = (EditText) view.findViewById(R.id.editSurname);
        editEmail = (EditText) view.findViewById(R.id.editEmail);
        editPhone = (EditText) view.findViewById(R.id.editPhone);
        editTopic = (EditText) view.findViewById(R.id.editTopic);

        LinearLayout llName = (LinearLayout) view.findViewById(R.id.lay_name);
        LinearLayout llSurname = (LinearLayout) view.findViewById(R.id.lay_surname);
        LinearLayout llMail = (LinearLayout) view.findViewById(R.id.lay_mail);
        LinearLayout llPhone = (LinearLayout) view.findViewById(R.id.lay_phone);
        LinearLayout llBio = (LinearLayout) view.findViewById(R.id.lay_topic);

        //Show arrows to reveal user if there are more contacts
        LinearLayout arrowLeft = (LinearLayout) view.findViewById(R.id.contact_left);
        if(position == 0) arrowLeft.setVisibility(View.INVISIBLE);
        LinearLayout arrowRight = (LinearLayout) view.findViewById(R.id.contact_right);
        if(position == friends.size()-1) arrowRight.setVisibility(View.INVISIBLE);

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

        // Inflate the layout for this fragment
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

    @Override
    public void onClick(View v) {
        this.getActivity().onBackPressed();
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
        ProfileContactFragment mFrgment = new ProfileContactFragment();
        return mFrgment;
    }
}
