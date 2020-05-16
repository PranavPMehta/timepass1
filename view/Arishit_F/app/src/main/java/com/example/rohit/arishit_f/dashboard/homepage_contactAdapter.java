package com.example.rohit.arishit_f.dashboard;

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
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.rohit.arishit_f.R;
import com.example.rohit.arishit_f.group_create.group_create_second_page;

import java.util.ArrayList;

import static com.example.rohit.arishit_f.constants.Constants.IP_ADDRESS;

public class homepage_contactAdapter extends ArrayAdapter<homepage_contact> {
    public homepage_contactAdapter(Activity context, ArrayList<homepage_contact> wordGroup){
        super(context,0,wordGroup);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.contacts_item, parent, false);
        }

        // Get the {@link AndroidFlavor} object located at this position in the list
        homepage_contact Defaultcontact = getItem(position);

        // Find the TextView in the list_item.xml layout with the ID version_name
        TextView mContactName = (TextView) listItemView.findViewById(R.id.contactName);
        TextView mDesignation = (TextView) listItemView.findViewById(R.id.contactDesignation);
        // Get the version name from the current AndroidFlavor object and
        // set this text on the name TextView
        mContactName.setText(Defaultcontact.getname());

        mDesignation.setText(Defaultcontact.getmDesignation());

        ImageView mContactImage= (ImageView) listItemView.findViewById(R.id.contactImage);

        String templink = Defaultcontact.getImageResId();
        if(templink == null){
            mContactImage.setImageResource(R.drawable.profile_img_new);
        }else {
                Log.v("templink : ",templink.toString());
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
