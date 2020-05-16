package com.example.rohit.arishit_f.dashboard;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.rohit.arishit_f.R;

import java.util.ArrayList;

public class MeetingListAdapter extends ArrayAdapter<MeetingItems> {

    TextView agenda,organizer,date;
    public MeetingListAdapter(Activity context, ArrayList<MeetingItems> wordGroup){
        super(context,0,wordGroup);
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.meetinglistitemadapter, parent, false);
        }


        // Get the {@link AndroidFlavor} object located at this position in the list
        MeetingItems Defaultcontact = getItem(position);

        // Find the TextView in the list_item.xml layout with the ID version_name
       // Get the version name from the current AndroidFlavor object and
        // set this text on the name TextView
        agenda = listItemView.findViewById(R.id.meetingagenda);
        agenda.setText(Defaultcontact.getAganda());

        organizer = listItemView.findViewById(R.id.organizer);
        date = listItemView.findViewById(R.id.meetingtime);

        organizer.setText(Defaultcontact.getOrganizer());
        date.setText(Defaultcontact.getDate());
        return listItemView;
    }


}
