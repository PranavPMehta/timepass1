package com.example.rohit.arishit_f.task;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.rohit.arishit_f.R;

import java.util.ArrayList;

public class ListItemsAdapter extends ArrayAdapter<ListItems> {

    TextView Manager,Title;
    public ListItemsAdapter(Activity context, ArrayList<ListItems> wordGroup){
        super(context,0,wordGroup);
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.listitemadapter, parent, false);
        }


        // Get the {@link AndroidFlavor} object located at this position in the list
        ListItems Defaultcontact = getItem(position);

        // Find the TextView in the list_item.xml layout with the ID version_name
        TextView Membernames =  listItemView.findViewById(R.id.memnames);
        // Get the version name from the current AndroidFlavor object and
        // set this text on the name TextView
        Membernames.setText(Defaultcontact.getMembernames());

        Manager = listItemView.findViewById(R.id.manager);
        Title= listItemView.findViewById(R.id.titlelist);

        Manager.setText("Manager");

        Title.setText(Defaultcontact.getTitle());
        return listItemView;
    }

}