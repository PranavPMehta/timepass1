package com.example.rohit.arishit_f.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.example.rohit.arishit_f.presentation.Presentation;
import com.example.rohit.arishit_f.R;
import com.example.rohit.arishit_f.screenSharing.MainScreenSharing;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;

public class FragmentThree extends Fragment {
    private static final int ADDMEETING_REQUEST = 1;
    public static  final ArrayList<CallHistoryItems> history = new ArrayList<>();
    ListView listView ;
    public static Integer[] icons = {R.drawable.ic_person_black_24dp,R.drawable.ic_group_black_24dp};
    public static Integer[] callIcon = {R.drawable.ic_call_arishti_24dp,R.drawable.ic_videocam_arishti_24dp};
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_three,
                container, false);
        //Button button = (Button) view.findViewById(R.id.presentation_icon);
        //Button btnScreen = (Button)view.findViewById(R.id.btnScreenSharing);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                /*Intent intent = new Intent(getActivity(), MeetingCreate.class);
//                startActivityForResult(intent,ADDMEETING_REQUEST );// do something*/
//                Intent intent = new Intent(getActivity(), Presentation.class);
//                startActivity(intent );// do something
//            }
//        });

        //listView = view.findViewById(R.id.callHistoryList);


        //   history.add(new CallHistoryItems(icons[0],Caller + "called you ", "Time"));
        //TODO Add api call for history list
        /*history.add(new CallHistoryItems(icons[0], "Username Called You", "Time",callIcon[0]));
        history.add(new CallHistoryItems(icons[1], "Groupname Called You", "Time",callIcon[0]));
        history.add(new CallHistoryItems(icons[0], "Missed call from Username", "Time",callIcon[0]));
        history.add(new CallHistoryItems(icons[1], "Missed call from Groupname", "Time",callIcon[0]));
        history.add(new CallHistoryItems(icons[0], "Username called you", "Time",callIcon[1]));
        history.add(new CallHistoryItems(icons[1], "Groupname called you", "Time",callIcon[1]));
        history.add(new CallHistoryItems(icons[0], "Missed call from Username", "Time",callIcon[1]));
        history.add(new CallHistoryItems(icons[1], "Missed call from Groupname", "Time",callIcon[1]));
        history.add(new CallHistoryItems(icons[1], "Meeting Title", "Time",callIcon[0]));
        history.add(new CallHistoryItems(icons[1], "Missec Meeting Title", "Time",callIcon[0]));


        CallHistoryAdapter contactAdapter = new CallHistoryAdapter(getActivity(),history);
        contactAdapter.notifyDataSetChanged();
        listView.setAdapter(contactAdapter);*/
       /* btnScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainScreenSharing.class);
                startActivity(intent );// do something
            }
        });*/
        return view;
    }
}