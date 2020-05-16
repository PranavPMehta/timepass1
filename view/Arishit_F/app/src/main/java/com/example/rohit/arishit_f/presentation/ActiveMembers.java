package com.example.rohit.arishit_f.presentation;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import com.example.rohit.arishit_f.R;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class ActiveMembers extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_members);


        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int height=dm.heightPixels;
        int width=dm.widthPixels;

        getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT);

    }




}
