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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import org.safegees.safegees.R;
import org.safegees.safegees.gui.view.PrincipalMapActivity;
import org.safegees.safegees.model.PublicUser;
import org.safegees.safegees.util.SafegeesDAO;
import org.safegees.safegees.util.ShareDataController;
import org.safegees.safegees.util.StoredDataQuequesManager;

import java.util.Map;

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
    private EditText editName;      //Tag name
    private EditText editSurname;   //Tag surname
    private EditText editEmail;     //Tag email
    private EditText editPhone;     //Tag phone
    private EditText editTopic;     //Tag topic

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
        editSurname = (EditText) view.findViewById(R.id.editSurname);
        editEmail = (EditText) view.findViewById(R.id.editEmail);
        editPhone = (EditText) view.findViewById(R.id.editPhone);
        editTopic = (EditText) view.findViewById(R.id.editTopic);

        SafegeesDAO sDAO = SafegeesDAO.getInstance(this.getContext());
        PublicUser pu = sDAO.getPublicUser();

        Map<String,String> publicUsers = StoredDataQuequesManager.getUserBasicDataMap(this.getContext());
        if (publicUsers != null){
            PublicUser puDynamic = PublicUser.getPublicUserFromJSON(publicUsers.get(pu.getPublicEmail()));
            if(puDynamic != null) pu = puDynamic;
        }


        if (pu != null) {
            this.editName.setText(pu.getName() != null ? pu.getName() : "");
            this.editSurname.setText(pu.getSurname() != null ? pu.getSurname() : "");
            this.editEmail.setText(pu.getPublicEmail() != null ? pu.getPublicEmail() : "");
            this.editPhone.setText(pu.getPhoneNumber() != null ? pu.getPhoneNumber() : "");
            this.editTopic.setText(pu.getBio() != null ? pu.getBio() : "");
        }



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

        if(v.getTag().equals("name")) {
            edt.setText(editName.getText());
            edt.setHint(editName.getHint());
            builder.setTitle("Name");
            //builder.setMessage("Enter text below");
            builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    editName.setText(edt.getText());
                    editName.setHint(edt.getHint());
                    ShareDataController sssdc = new ShareDataController();
                    PublicUser publicUser = new PublicUser(editTopic.getText().toString(),editEmail.getText().toString(), editName.getText().toString(),editPhone.getText().toString(),null,editSurname.getText().toString());
                    Log.i("SEND_UBD", PublicUser.getJSONStringFromPublicUser(publicUser));
                    sssdc.sendUserBasicData(getContext(), SafegeesDAO.getInstance(getContext()).getPublicUser().getPublicEmail(), publicUser);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                }
            });
            AlertDialog b = builder.create();
            b.show();
        }else if(v.getTag().equals("surname")) {
            edt.setText(editSurname.getText());
            edt.setHint(editSurname.getHint());
            builder.setTitle("Surname");
            builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    editSurname.setText(edt.getText());
                    editSurname.setHint(edt.getHint());
                    ShareDataController sssdc = new ShareDataController();
                    PublicUser publicUser = new PublicUser(editTopic.getText().toString(),editEmail.getText().toString(), editName.getText().toString(),editPhone.getText().toString(),null,editSurname.getText().toString());
                    Log.i("SEND_UBD", PublicUser.getJSONStringFromPublicUser(publicUser));
                    sssdc.sendUserBasicData(getContext(), SafegeesDAO.getInstance(getContext()).getPublicUser().getPublicEmail(), publicUser);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                }
            });
            AlertDialog b = builder.create();
            b.show();
        }else if(v.getTag().equals("email")) {
            edt.setText(editEmail.getText());
            edt.setHint(editEmail.getHint());
            builder.setTitle("Public email");
            builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    editEmail.setText(edt.getText());
                    editEmail.setHint(edt.getHint());
                    ShareDataController sssdc = new ShareDataController();
                    PublicUser publicUser = new PublicUser(editTopic.getText().toString(),editEmail.getText().toString(), editName.getText().toString(),editPhone.getText().toString(),null,editSurname.getText().toString());
                    Log.i("SEND_UBD", PublicUser.getJSONStringFromPublicUser(publicUser));
                    sssdc.sendUserBasicData(getContext(), SafegeesDAO.getInstance(getContext()).getPublicUser().getPublicEmail(), publicUser);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                }
            });
            AlertDialog b = builder.create();
            b.show();
        }else if(v.getTag().equals("phone")) {
            edt.setText(editPhone.getText());
            edt.setHint(editPhone.getHint());
            builder.setTitle("Surname");
            builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    editPhone.setText(edt.getText());
                    editPhone.setHint(edt.getHint());
                    ShareDataController sssdc = new ShareDataController();
                    PublicUser publicUser = new PublicUser(editTopic.getText().toString(),editEmail.getText().toString(), editName.getText().toString(),editPhone.getText().toString(),null,editSurname.getText().toString());
                    Log.i("SEND_UBD", PublicUser.getJSONStringFromPublicUser(publicUser));
                    sssdc.sendUserBasicData(getContext(), SafegeesDAO.getInstance(getContext()).getPublicUser().getPublicEmail(), publicUser);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                }
            });
            AlertDialog b = builder.create();
            b.show();
        }else if(v.getTag().equals("topic")) {
            edt.setText(editTopic.getText());
            edt.setHint(editTopic.getHint());
            builder.setTitle("Public message");
            builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    editTopic.setText(edt.getText());
                    editTopic.setHint(edt.getHint());
                    ShareDataController sssdc = new ShareDataController();
                    PublicUser publicUser = new PublicUser(editTopic.getText().toString(),editEmail.getText().toString(), editName.getText().toString(),editPhone.getText().toString(),null,editSurname.getText().toString());
                    Log.i("SEND_UBD", PublicUser.getJSONStringFromPublicUser(publicUser));
                    sssdc.sendUserBasicData(getContext(), SafegeesDAO.getInstance(getContext()).getPublicUser().getPublicEmail(), publicUser);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                }
            });
            AlertDialog b = builder.create();
            b.show();
        }



    }
}
