package com.example.rohit.arishit_f.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.rohit.arishit_f.presentation.tools.ActiveAdapter;
import com.example.rohit.arishit_f.presentation.tools.PresentationTools;
import com.example.rohit.arishit_f.presentation.tools.ToolType;
import com.example.rohit.arishit_f.presentation.tools.contacts;
import com.example.rohit.arishit_f.R;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class Presentation extends AppCompatActivity implements PresentationTools.OnItemSelected, View.OnClickListener {


    private RecyclerView mRvTools;
    private PresentationTools mPresentation = new PresentationTools( this);
    private ConstraintLayout mRootView;
    private CardView cardView;
    private TextView active;
    private ListView listView;
    public  static final ArrayList<contacts> activecontact = new ArrayList<>();
    boolean alreadyExecuted = false;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.presentation_activity_presentation);

        mRvTools = findViewById(R.id.rvConstraintTools);
        mRootView = findViewById(R.id.rootView);
        active = findViewById(R.id.active_mem);
        cardView = findViewById(R.id.canvas);
        cardView.setOnClickListener(this);
        LinearLayoutManager llmTools = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRvTools.setLayoutManager(llmTools);
        mRvTools.setAdapter(mPresentation);

        if(!alreadyExecuted) {
            activelist();
            alreadyExecuted = true;
        }

        active.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Presentation.this,RemoveMember.class));
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    public void onToolSelected(ToolType toolType) {
        switch (toolType) {
            case ATTACH:
                startActivity(new Intent(Presentation.this,PDFViewer.class));
                break;
            case CHAT:
                startActivity(new Intent(Presentation.this, ChatPage.class));

        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.canvas:
                startActivity(new Intent(Presentation.this,EditImageActivity.class));
                break;
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.add:
                startActivity(new Intent(Presentation.this, PopupWindow.class));

        }


        return super.onOptionsItemSelected(item);
    }

    public void activelist(){

       // listView = findViewById(R.id.activeListView);

        activecontact.add(new contacts("Sourabh",R.mipmap.ic_launcher_round));
        activecontact.add(new contacts("Pranav",R.mipmap.ic_launcher_round));
        activecontact.add(new contacts("kanak",R.mipmap.ic_launcher_round));
        activecontact.add(new contacts("sushmita",R.mipmap.ic_launcher_round));
        activecontact.add(new contacts("onkar",R.mipmap.ic_launcher_round));
        activecontact.add(new contacts("Sourabh",R.mipmap.ic_launcher_round));
//        activecontact.add(new Contacts("Pranav",R.mipmap.ic_launcher_round));
//        activecontact.add(new Contacts("kanak",R.mipmap.ic_launcher_round));
//        activecontact.add(new Contacts("sushmita",R.mipmap.ic_launcher_round));
//        activecontact.add(new Contacts("onkar",R.mipmap.ic_launcher_round));
//        activecontact.add(new Contacts("Sourabh",R.mipmap.ic_launcher_round));
//        activecontact.add(new Contacts("Pranav",R.mipmap.ic_launcher_round));
//        activecontact.add(new Contacts("kanak",R.mipmap.ic_launcher_round));
//        activecontact.add(new Contacts("sushmita",R.mipmap.ic_launcher_round));
//        activecontact.add(new Contacts("onkar",R.mipmap.ic_launcher_round));
//
        final ActiveAdapter activeAdapter = new ActiveAdapter(this,activecontact);
//        listView.setAdapter(activeAdapter);
        activeAdapter.notifyDataSetChanged();
        runOnUiThread(new Runnable() {
            public void run() {
                activeAdapter.notifyDataSetChanged();
            }
        });
    }


}
