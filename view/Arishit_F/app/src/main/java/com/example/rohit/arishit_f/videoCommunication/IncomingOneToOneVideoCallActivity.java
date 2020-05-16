package com.example.rohit.arishit_f.videoCommunication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.rohit.arishit_f.R;

//This activity is invoked when video call comes from other user
public class IncomingOneToOneVideoCallActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incoming_video_call);
    }
}
