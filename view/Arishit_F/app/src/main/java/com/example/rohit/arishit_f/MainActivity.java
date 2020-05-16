package com.example.rohit.arishit_f;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.rohit.arishit_f.splash.IntroScreen;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, MyService.class);
        startService(intent);
        startActivity(new Intent(getApplicationContext(), IntroScreen.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        System.out.println("In Destroy Main Acivity");
        super.onDestroy();
    }
}
