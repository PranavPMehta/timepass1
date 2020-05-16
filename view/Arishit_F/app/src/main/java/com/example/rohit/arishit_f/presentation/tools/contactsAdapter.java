package com.example.rohit.arishit_f.presentation.tools;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.example.rohit.arishit_f.R;

import java.util.ArrayList;

import static com.example.rohit.arishit_f.presentation.PopupWindow.addcontact;
import static com.example.rohit.arishit_f.presentation.Presentation.activecontact;

public class contactsAdapter extends ArrayAdapter<contacts> {

    ImageView addmem,mike;
    public contactsAdapter(Activity context, ArrayList<contacts> wordGroup){
        super(context,0,wordGroup);
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.contacts_item, parent, false);
        }

        // Get the {@link AndroidFlavor} object located at this position in the list
        contacts Defaultcontact = getItem(position);

        // Find the TextView in the list_item.xml layout with the ID version_name
        TextView mContactName = (TextView) listItemView.findViewById(R.id.contactName);
        // Get the version name from the current AndroidFlavor object and
        // set this text on the name TextView
        mContactName.setText(Defaultcontact.getname());

        addmem = listItemView.findViewById(R.id.addmem);
        mike = listItemView.findViewById(R.id.mike);
        addmem.setImageResource(R.drawable.ic_person_add_black_24dp);
        mike.setImageResource(R.drawable.ic_keyboard_voice_black_24dp);
        ImageView mContactImage = listItemView.findViewById(R.id.contactImage);
        mContactImage.setImageResource(Defaultcontact.getImageResId());

        addmem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View parentRow = (View) v.getParent();
                ListView listView = (ListView) parentRow.getParent();
                final int pos = listView.getPositionForView(parentRow);
                activecontact.add(0,addcontact.get(pos));
                notifyDataSetChanged();
                Log.d("Image Pressed",String.valueOf(pos));
            }
        });

        return listItemView;
    }

}