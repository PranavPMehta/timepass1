package com.example.rohit.arishit_f.group_create;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

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
import static com.example.rohit.arishit_f.group_create.group_create_main.checked_contact;

public class group_create_adpater extends ArrayAdapter<group_create_contact> {

    public group_create_adpater(Activity context, ArrayList<group_create_contact> wordGroup){
        super(context,0,wordGroup);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.group_create_item, parent, false);
        }

        // Get the {@link AndroidFlavor} object located at this position in the list
        group_create_contact Defaultcontact = getItem(position);

        // Find the TextView in the list_item.xml layout with the ID version_name
        TextView mContactName = (TextView) listItemView.findViewById(R.id.group_create_name);
        // Get the version name from the current AndroidFlavor object and
        // set this text on the name TextView
        mContactName.setText(Defaultcontact.getName());

        // Find the TextView in the list_item.xml layout with the ID version_name
        TextView mContactdesignation = (TextView) listItemView.findViewById(R.id.group_create_designation);
        // Get the version name from the current AndroidFlavor object and
        // set this text on the name TextView
        mContactdesignation.setText(Defaultcontact.getDesignation());


        ImageView mContactImage= (ImageView) listItemView.findViewById(R.id.group_create_image);
        String templink = Defaultcontact.getImage();
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

        CheckBox checkBox = (CheckBox) listItemView.findViewById(R.id.group_create_checkbox);

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Defaultcontact.getIschecked()==0) {
                    Defaultcontact.setIschecked(1);
                    checked_contact.add(Defaultcontact);
                }else{
                    Defaultcontact.setIschecked(0);
                    checked_contact.remove(Defaultcontact);
                }
            }
        });

        if(Defaultcontact.getIschecked() == 1){
            checkBox.setChecked(true);
        }else{
            checkBox.setChecked(false);
        }


        return listItemView;
    }
}
