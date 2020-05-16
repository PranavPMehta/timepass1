package com.example.rohit.arishit_f.presentation;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ListView;


import com.example.rohit.arishit_f.presentation.tools.contacts;
import com.example.rohit.arishit_f.presentation.tools.contactsAdapter;
import com.example.rohit.arishit_f.R;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class PopupWindow extends AppCompatActivity {
    ListView listView ;
    String[] name = {"aa","bb","cc"};
    public static  final ArrayList<contacts> addcontact = new ArrayList<>();

    boolean alreadyExecuted = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.presentation_activity_popupwindow);
        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int height=dm.heightPixels;
        int width=dm.widthPixels;

        getWindow().setLayout((int)(width * 0.75),(int)(height * 0.59));

        listView = findViewById(R.id.popListView);

//        for(Integer i=0; i<3;i++ ) {
//            addcontact.add(new Contacts(name[i], R.mipmap.ic_launcher_round));
//        }

        addcontact.add(new contacts("aa",R.mipmap.ic_launcher_round));
        addcontact.add(new contacts("cc",R.mipmap.ic_launcher_round));
        addcontact.add(new contacts("bb",R.mipmap.ic_launcher_round));
        addcontact.add(new contacts("aa",R.mipmap.ic_launcher_round));
        addcontact.add(new contacts("cc",R.mipmap.ic_launcher_round));
        addcontact.add(new contacts("bb",R.mipmap.ic_launcher_round));
        addcontact.add(new contacts("aa",R.mipmap.ic_launcher_round));
        addcontact.add(new contacts("cc",R.mipmap.ic_launcher_round));
        addcontact.add(new contacts("bb",R.mipmap.ic_launcher_round));
        addcontact.add(new contacts("aa",R.mipmap.ic_launcher_round));
        addcontact.add(new contacts("cc",R.mipmap.ic_launcher_round));
        addcontact.add(new contacts("bb",R.mipmap.ic_launcher_round));

        alreadyExecuted = true;
            // Code to run once




        contactsAdapter contactAdapter = new contactsAdapter(this,addcontact);

        listView.setAdapter(contactAdapter);

    }
}