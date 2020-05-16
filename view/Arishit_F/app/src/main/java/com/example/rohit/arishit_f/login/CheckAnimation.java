package com.example.rohit.arishit_f.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.rohit.arishit_f.login.chat.ChatPage;
import com.example.rohit.arishit_f.R;

import androidx.appcompat.app.AppCompatActivity;
import cdflynn.android.library.checkview.CheckView;

public class CheckAnimation extends AppCompatActivity {

    Button nxtbtn;

    static class Views {

        View button;
        CheckView check;

        Views(CheckAnimation activity) {
            check = (CheckView) activity.findViewById(R.id.check);
        }
    }

    private Views mViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_animation);
        mViews = new Views(this);
        mViews.check.check();

        nxtbtn = findViewById(R.id.next_button_fiu);

        nxtbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chat = new Intent(CheckAnimation.this, ChatPage.class);
                startActivity(chat);
            }
        });
    }
}