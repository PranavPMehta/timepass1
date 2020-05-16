package com.example.rohit.arishit_f.dashboard;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rohit.arishit_f.R;

import com.example.rohit.arishit_f.faq.faq_main;
import com.example.rohit.arishit_f.login.LoginScreen;
import com.example.rohit.arishit_f.login.MessageResult;
import com.example.rohit.arishit_f.login.chat.ChatPage;
import com.example.rohit.arishit_f.profileInfo.notification_main;
import com.example.rohit.arishit_f.profileInfo.profile_info;
import com.example.rohit.arishit_f.profileInfo.setting_main;
import com.example.rohit.arishit_f.profileInfo.feedback_main;
import com.example.rohit.arishit_f.retrofit.IMyService;
import com.example.rohit.arishit_f.retrofit.RetrofitClient;
import com.example.rohit.arishit_f.task.Task;
import com.example.rohit.arishit_f.vault.vault_login;
import com.example.rohit.arishit_f.vault.vault_register;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;



public class ThreetabSlider extends AppCompatActivity {
    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
//    private TextView meme_text;
    androidx.appcompat.widget.Toolbar toolbar;
    public static String Username,ishoney;
    private TextView meme_text;
    private String user_id;
    private JSONObject jj;
    private Intent i;
    IMyService iMyService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.threetab_slider);

        SharedPreferences sharedPreferences = getSharedPreferences("userDetails", MODE_PRIVATE);

        ishoney=sharedPreferences.getString("ishoney","no");

        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);
        if(ishoney.equals("no"))
        {
            String bearerToken = sharedPreferences.getString("bearer_token","12345");
            System.out.println("Bearer token"+bearerToken);
            retrofit2.Call<MessageResult> call = iMyService.getMyDetails("Bearer "+bearerToken);
            call.enqueue(new Callback<MessageResult>() {
                @Override
                public void onResponse(retrofit2.Call<MessageResult> call, Response<MessageResult> response) {
                    System.out.println("Getmydetails"+response.body().getMessage());
                    try {
                        JSONObject info = new JSONObject(response.body().getMessage());
                        Username = info.getString("Username");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if(!response.body().getMessage().equals("error"))
                    {
                        retrofit2.Call<MessageResult> call1 = iMyService.checkLogin(Username);
                        call1.enqueue(new Callback<MessageResult>() {
                            @Override
                            public void onResponse(retrofit2.Call<MessageResult> call, Response<MessageResult> response) {
                                String res=response.body().getResult();
                                System.out.println("Response"+res);
                                if(res.equals("false"))
                                {
                                    System.out.println("Not Logged in ChatPage");
                                    Intent intent=new Intent(ThreetabSlider.this, LoginScreen.class);
                                    startActivity(intent);
                                }
                                System.out.println("Logged in ChatPage");
                            }

                            @Override
                            public void onFailure(retrofit2.Call<MessageResult> call, Throwable t) {
                                System.out.println("failure ondestroy");
                                System.out.println("Throw"+t);

                            }
                        });
                    }

                }

                @Override
                public void onFailure(retrofit2.Call<MessageResult> call, Throwable t) {
                }
            });
        }

        /*Call<MessageResult> call1 = iMyService.checkLogin(Username);
        call1.enqueue(new Callback<MessageResult>() {
            @Override
            public void onResponse(Call<MessageResult> call, Response<MessageResult> response) {
                String res=response.body().getResult();
                System.out.println("Response"+res);
                if(res.equals("false"))
                {
                    System.out.println("Not Logged in ThreeTabPage");
                    Intent intent=new Intent(ThreetabSlider.this, LoginScreen.class);
                    startActivity(intent);
                }
                System.out.println("Logged in ThreeTabPage");
            }

            @Override
            public void onFailure(Call<MessageResult> call, Throwable t) {
                System.out.println("failure ondestroy");
                System.out.println("Throw"+t);

            }
        });

        */
        Bundle bundle = new Bundle();
        bundle.putString("username", Username);
        FragmentOne fragobj1 = new FragmentOne();
        fragobj1.setArguments(bundle);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        meme_text=(TextView) findViewById(R.id.MeMe);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addFragment(fragobj1, "Chat");
        adapter.addFragment(new FragmentTwo(), "Meeting");
        adapter.addFragment(new Task(), "Task");
        adapter.addFragment(new FragmentThree(), "History");
        toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.chatpage_threedots);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.profile:
                        Intent pr = new Intent(ThreetabSlider.this, profile_info.class);
                        startActivity(pr);
                        return true;
                    case R.id.notification:
                        Intent nt = new Intent(ThreetabSlider.this, notification_main.class);
                        startActivity(nt);
                        return true;
                    case R.id.setting:
                        Intent st = new Intent(ThreetabSlider.this, setting_main.class);
                        startActivity(st);
                        return true;
                    case R.id.faq:
                        Intent faq = new Intent(ThreetabSlider.this, faq_main.class);
                        startActivity(faq);
                        return true;
                    case R.id.feedback:
                        Intent fb = new Intent(ThreetabSlider.this, feedback_main.class);
                        startActivity(fb);
                        return true;
                }
                return false;
            }
        });

        meme_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i=new Intent(getApplicationContext(), vault_login.class);
                startActivity(i);
                System.out.println("User ID Vault_login "+user_id);
                try{
                    String vault_password=jj.getString("vault_password");
                    if(!vault_password.isEmpty()){
                        /*i=new Intent(getApplicationContext(),vault_login.class);
                        startActivity(i);*/
                        InputMethodManager mgr=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        mgr.showSoftInput(meme_text,InputMethodManager.SHOW_IMPLICIT);

                    }

                }catch (Exception e){
                    i =new Intent(getApplicationContext(), vault_register.class);
                    startActivity(i);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Exit Application?");
        alertDialogBuilder
                .setMessage("Click yes to exit!")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                moveTaskToBack(true);
                                android.os.Process.killProcess(android.os.Process.myPid());
                                System.exit(1);
                            }
                        })

                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    /*@Override
    protected void onDestroy() {
        Retrofit retrofitClient = RetrofitClient.getInstance();
        IMyService iMyService1 = retrofitClient.create(IMyService.class);
        System.out.println("USername in Three Destroy"+Username);
        Call<MessageResult> call1 = iMyService1.loginSessionEnd(Username);
        call1.enqueue(new Callback<MessageResult>() {
            @Override
            public void onResponse(Call<MessageResult> call, Response<MessageResult> response) {
                String respons=response.body().getResult();
                System.out.println("response ondestroy");
            }

            @Override
            public void onFailure(Call<MessageResult> call, Throwable t) {
                System.out.println("failure ondestroy");
                System.out.println("Throw"+t);

            }
        });
        super.onDestroy();
    }*/
}

