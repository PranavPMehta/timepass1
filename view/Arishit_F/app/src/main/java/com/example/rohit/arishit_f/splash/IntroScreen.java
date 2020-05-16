package com.example.rohit.arishit_f.splash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.example.rohit.arishit_f.login.LoginScreen;
import com.example.rohit.arishit_f.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class IntroScreen extends AppCompatActivity {

    private ViewPager screenPager;
    IntroViewPagerAdapter introViewPagerAdapter;
    TabLayout tabIndicator;
    int position = 0;
    Button btnGetStarted;
    Animation btnAnim;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (restorePrefData()) {

            Intent mainActivity = new Intent(getApplicationContext(), LoginScreen.class);
            startActivity(mainActivity);
            finish();


        }
        //done
        //so did you try something from your side
        //yes I refered youtube videos,stack overflow,codedemon ok
        //everywhere when we want to add icon with menu we want to create root and submenu
        //is it necessary every time? No i have tried it earlier give me some time will let you know
        //ok fine I too will search

        setContentView(R.layout.activity_intro_screen);
        //getSupportActionBar().hide();


        btnGetStarted = findViewById(R.id.btn_get_started);
        tabIndicator = findViewById(R.id.tab_indicator);
        btnAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_animation);


        final List<ScreenItem> mList = new ArrayList<>();
        mList.add(new ScreenItem("MeMe",
                "Hypersecure Messaging Platform", R.drawable.undrawchatting,R.drawable.loginpageopac));
        mList.add(new ScreenItem("Trusted", "Building Trust , Enhancing Security", R.drawable.trusted,R.drawable.loginpageopac));
        mList.add(new ScreenItem("Reliable", "Connecting People Together", R.drawable.reliable,R.drawable.loginpageopac));
        mList.add(new ScreenItem("Secure", "Keeping your Data Securely", R.drawable.security,R.drawable.loginpageopac));

        screenPager = findViewById(R.id.screen_viewpager);
        introViewPagerAdapter = new IntroViewPagerAdapter(this, mList);
        screenPager.setPageTransformer(true, new ZoomOutPageTransformer());
        screenPager.setAdapter(introViewPagerAdapter);
        tabIndicator.setupWithViewPager(screenPager);

        screenPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // introViewPagerAdapter.animate();
            }

            @Override
            public void onPageSelected(int position) {
                //introViewPagerAdapter.animate();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        tabIndicator.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (tab.getPosition() == mList.size() - 1) {
                    loaddLastScreen();
                }else{
                    btnGetStarted.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainActivity = new Intent(getApplicationContext(), LoginScreen.class);
                startActivity(mainActivity);
                savePrefsData();
                finish();


            }
        });

    }

    private boolean restorePrefData() {


        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs", MODE_PRIVATE);
        Boolean isIntroActivityOpnendBefore = pref.getBoolean("isIntroOpnend", false);
        return isIntroActivityOpnendBefore;


    }

    private void savePrefsData() {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isIntroOpnend", true);
        editor.commit();


    }

    private void loaddLastScreen() {
        btnGetStarted.setVisibility(View.VISIBLE);
        tabIndicator.setVisibility(View.INVISIBLE);
        btnGetStarted.setAnimation(btnAnim);
    }
}
