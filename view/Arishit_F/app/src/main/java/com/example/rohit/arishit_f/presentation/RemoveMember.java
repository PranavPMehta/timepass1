package com.example.rohit.arishit_f.presentation;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ListView;


import com.example.rohit.arishit_f.presentation.tools.ActiveAdapter;
import com.example.rohit.arishit_f.presentation.tools.contacts;
import com.example.rohit.arishit_f.presentation.tools.removeadapter;
import com.example.rohit.arishit_f.R;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

import static com.example.rohit.arishit_f.presentation.Presentation.activecontact;


public class RemoveMember extends AppCompatActivity {

     public static final ArrayList<contacts> contact = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.presentation_activity_remove_member);

        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int height=dm.heightPixels;
        int width=dm.widthPixels;

        getWindow().setLayout((int)(width * 0.75),(int)(height * 0.59));

        final ListView listView = findViewById(R.id.removeListView);



        //final ArrayList<Contacts> contact = new ArrayList<>();
//        contact.add(new Contacts("Sourabh",R.mipmap.ic_launcher_round));
//        contact.add(new Contacts("Pranav",R.mipmap.ic_launcher_round));
//        contact.add(new Contacts("kanak",R.mipmap.ic_launcher_round));
//        contact.add(new Contacts("sushmita",R.mipmap.ic_launcher_round));
//        contact.add(new Contacts("onkar",R.mipmap.ic_launcher_round));
//        contact.add(new Contacts("Sourabh",R.mipmap.ic_launcher_round));
//        contact.add(new Contacts("Pranav",R.mipmap.ic_launcher_round));
//        contact.add(new Contacts("kanak",R.mipmap.ic_launcher_round));
//        contact.add(new Contacts("sushmita",R.mipmap.ic_launcher_round));
//        contact.add(new Contacts("onkar",R.mipmap.ic_launcher_round));
//        contact.add(new Contacts("Sourabh",R.mipmap.ic_launcher_round));
//        contact.add(new Contacts("Pranav",R.mipmap.ic_launcher_round));
//        contact.add(new Contacts("kanak",R.mipmap.ic_launcher_round));
//        contact.add(new Contacts("sushmita",R.mipmap.ic_launcher_round));
//        contact.add(new Contacts("onkar",R.mipmap.ic_launcher_round));
        final ActiveAdapter activeAdapter = new ActiveAdapter(this,activecontact);

        final removeadapter removeadap = new removeadapter(this,activecontact);
        runOnUiThread(new Runnable() {
            public void run() {
                removeadap.notifyDataSetChanged();
                activeAdapter.notifyDataSetChanged();

            }
        });
        listView.setAdapter(activeAdapter);

        listView.setAdapter(removeadap);

        activeAdapter.notifyDataSetChanged();
        removeadap.notifyDataSetChanged();


    }
}
