package com.example.rohit.arishit_f.presentation.tools;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.rohit.arishit_f.R;

import java.util.ArrayList;

public class ActiveAdapter extends ArrayAdapter<contacts> {

    public ActiveAdapter(Activity context, ArrayList<contacts> wordGroup){
        super(context,0,wordGroup);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.presentation_activelist, parent, false);
        }

        // Get the {@link AndroidFlavor} object located at this position in the list
        contacts Defaultcontact = getItem(position);

        // Find the TextView in the list_item.xml layout with the ID version_name
        TextView mContactName = (TextView) listItemView.findViewById(R.id.contactName);
        // Get the version name from the current AndroidFlavor object and
        // set this text on the name TextView
        mContactName.setText(Defaultcontact.getname());

        ImageView mContactImage = listItemView.findViewById(R.id.contactImage);
        mContactImage.setImageResource(Defaultcontact.getImageResId());

        notifyDataSetChanged();
        return listItemView;
    }
}
