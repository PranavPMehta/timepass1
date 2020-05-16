package com.example.rohit.arishit_f.dashboard;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rohit.arishit_f.R;

import java.util.ArrayList;

public class CallHistoryAdapter extends ArrayAdapter<CallHistoryItems> {

    ImageView PerIcon,callIcon;
    TextView caller,time;

    public CallHistoryAdapter(Activity context, ArrayList<CallHistoryItems> wordGroup){
        super(context,0,wordGroup);
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.callhistoryadapter, parent, false);
        }


        // Get the {@link AndroidFlavor} object located at this position in the list
        CallHistoryItems Defaultcontact = getItem(position);

        // Find the TextView in the list_item.xml layout with the ID version_name
        // Get the version name from the current AndroidFlavor object and
        // set this text on the name TextView
        PerIcon = listItemView.findViewById(R.id.personIcon);
        callIcon = listItemView.findViewById(R.id.callicon);
   //     PerIcon.setImageResource(Defaultcontact.getPersonIcon());
        caller = listItemView.findViewById(R.id.caller);
        caller.setText(Defaultcontact.getUsername());
        callIcon.setImageResource(Defaultcontact.getCallIcon());
        PerIcon.setImageResource(Defaultcontact.getPersonIcon());
        time = listItemView.findViewById(R.id.calltime);

        time.setText(Defaultcontact.getTime());
        return listItemView;
    }



}
