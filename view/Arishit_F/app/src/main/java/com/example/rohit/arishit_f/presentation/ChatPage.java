package com.example.rohit.arishit_f.presentation;

import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.rohit.arishit_f.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ChatPage extends AppCompatActivity {
    private static final int SIGNUP_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_page);

        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int height=dm.heightPixels;
        int width=dm.widthPixels;
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().getAttributes().verticalMargin = (float) (0.18);
        getWindow().setLayout((width ),(int)(height * 0.45));

        ImageView attach = (ImageView) findViewById(R.id.btn_attach);
        attach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayPopupWindow(v);
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView title = findViewById(R.id.userText);
        title.setText("Agenda");
        title.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.chatpage_threedots,menu);
        return true;
    }

    private void displayPopupWindow(View view) {
        int[] loc_int = new int[2];
        PopupWindow popup = new PopupWindow(ChatPage.this);
        View layout = getLayoutInflater().inflate(R.layout.popupwindow_attach, null);
        popup.setContentView(layout);
        popup.setBackgroundDrawable(null);
        // Set content width and height
        popup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popup.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        // Closes the popup window when touch outside of it - when looses focus
        popup.setOutsideTouchable(true);
        popup.setFocusable(true);
        //popup.setBackgroundDrawable(new BitmapDrawable());
        // Show anchored to button
        view.getLocationOnScreen(loc_int);
        Rect location = new Rect();
        location.left = loc_int[0];
        location.top = loc_int[1];
        //location.bottom = location.top - view.getHeight();
        location.bottom = view.getBottom() + view.getHeight() + view.getTop() + 20;
        popup.showAtLocation(view, Gravity.BOTTOM, 0, location.bottom);
    }
}
