package org.safegees.safegees.gui.adapters;
import java.util.ArrayList;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.safegees.safegees.R;
import org.safegees.safegees.gui.view.PrincipalMapActivity;
import org.safegees.safegees.model.Friend;
import org.safegees.safegees.util.ImageController;

public class ContactAdaptet extends RecyclerView.Adapter<ContactAdaptet.ViewHolder> {
    private ArrayList<Friend> mDataset;
    private View view;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView txtHeader;
        public TextView txtFooter;
        public ImageView avatar;

        public ViewHolder(View v) {
            super(v);
            txtHeader = (TextView) v.findViewById(R.id.name_listview_field);
            txtFooter = (TextView) v.findViewById(R.id.email_listview_field);
            avatar = (ImageView) v.findViewById(R.id.icon);
        }
    }

    public void add(int position, Friend friend) {
        mDataset.add(position, friend);
        notifyItemInserted(position);
    }

    public void remove(Friend item) {
        int position = mDataset.indexOf(item);
        mDataset.remove(position);
        notifyItemRemoved(position);
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ContactAdaptet(ArrayList<Friend> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ContactAdaptet.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {
        // create a new view
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_listview_field_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final Friend friend = mDataset.get(position);
        //To change
        holder.txtHeader.setText(friend.getName() + " " + friend.getSurname());

        //Add image from friend
        if (friend.getPublicEmail() != null){
            Bitmap bitmap = ImageController.getContactImageBitmap(view.getContext(),friend.getPublicEmail());
            if (bitmap != null) holder.avatar.setImageBitmap(bitmap);
        }

        holder.itemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                PrincipalMapActivity.getInstance().startContactActivityForResult(position);
            }
        });

        holder.txtFooter.setText(mDataset.get(position).getPublicEmail());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}