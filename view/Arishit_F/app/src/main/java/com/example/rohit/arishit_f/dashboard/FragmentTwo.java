package com.example.rohit.arishit_f.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.example.rohit.arishit_f.R;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;

public class FragmentTwo extends Fragment {
    private static final int ADDMEETING_REQUEST = 1;

    public static  final ArrayList<MeetingItems> meetings = new ArrayList<>();
    ListView listView ;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_two,
                container, false);
        /*
        Button imageView = view.findViewById(R.id.addmeeting_icon);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Listen na here I just want to display coming soon Nothing else
            }
        });

         */


        /*listView = view.findViewById(R.id.meetingslist);
        //TODO Add api call for meetings List
        meetings.add(new MeetingItems("Agenda", "Organizer", "Time"));
        meetings.add(new MeetingItems("Agenda", "Organizer", "Time"));
        meetings.add(new MeetingItems("Agenda", "Organizer", "Time"));
        meetings.add(new MeetingItems("Agenda", "Organizer", "Time"));

        MeetingListAdapter contactAdapter = new MeetingListAdapter(getActivity(),meetings);
        contactAdapter.notifyDataSetChanged();
        listView.setAdapter(contactAdapter);*/


        return view;
    }
}