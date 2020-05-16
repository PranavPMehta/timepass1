package com.example.rohit.arishit_f.group_view;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.rohit.arishit_f.R;
import com.example.rohit.arishit_f.dashboard.Contacts;

import java.util.ArrayList;

import static com.example.rohit.arishit_f.constants.Constants.IP_ADDRESS;

public class group_contactsAdapter extends ArrayAdapter<group_contacts> {

    public group_contactsAdapter(Activity context, ArrayList<group_contacts> wordGroup){
        super(context,0,wordGroup);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.group_view_item, parent, false);
        }

        // Get the {@link AndroidFlavor} object located at this position in the list
        group_contacts Defaultcontact = getItem(position);

        // Find the TextView in the list_item.xml layout with the ID version_name
        TextView mContactName = (TextView) listItemView.findViewById(R.id.group_item_name);
        // Get the version name from the current AndroidFlavor object and
        // set this text on the name TextView
        mContactName.setText(Defaultcontact.getname());


        TextView mContactPosition = (TextView) listItemView.findViewById(R.id.group_item_position);
        mContactPosition.setText(Defaultcontact.getmPosition());


        ImageView mContactImage= (ImageView) listItemView.findViewById(R.id.group_item_image);
        String templink = Defaultcontact.getImageResId();
        if(templink.equals("null")){
            mContactImage.setImageResource(R.drawable.profile_img_new);
        }else {
            Log.v("templink : ",templink);
            Glide.with(getContext())
                    .load(IP_ADDRESS + ":3000" + templink)
                    .apply(
                            new RequestOptions()
                                    .error(R.drawable.profile_img_new)
                                    .centerCrop()
                    )
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(mContactImage);
        }


        return listItemView;
    }

}
