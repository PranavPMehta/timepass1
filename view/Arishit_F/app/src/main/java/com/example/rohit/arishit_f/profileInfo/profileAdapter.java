package com.example.rohit.arishit_f.profileInfo;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rohit.arishit_f.R;

import java.util.ArrayList;

public class profileAdapter extends ArrayAdapter<profile_item> {

        public profileAdapter(Activity context, ArrayList<profile_item> wordGroup){
            super(context,0,wordGroup);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Check if the existing view is being reused, otherwise inflate the view
            View listItemView = convertView;
            if(listItemView == null) {
                listItemView = LayoutInflater.from(getContext()).inflate(
                        R.layout.profile_item, parent, false);
            }

            // Get the {@link AndroidFlavor} object located at this position in the list
            profile_item Defaultprofile = getItem(position);

            // Find the TextView in the list_item.xml layout with the ID version_name
            TextView mtitle = (TextView) listItemView.findViewById(R.id.p_i_title);
            // Get the version name from the current AndroidFlavor object and
            // set this text on the name TextView
            mtitle.setText(Defaultprofile.getMtitle());

            TextView mdescription = (TextView) listItemView.findViewById(R.id.p_i_description);
            // Get the version name from the current AndroidFlavor object and
            // set this text on the name TextView
            mdescription.setText(Defaultprofile.getMdescription());

            ImageView micon= (ImageView) listItemView.findViewById(R.id.profile_item_icon);
            micon.setImageResource(Defaultprofile.getmImageResId());

            return listItemView;
        }

    }


