package com.example.rohit.arishit_f.profileInfo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.rohit.arishit_f.R;
import com.example.rohit.arishit_f.dashboard.ContactsAdapter;
import com.example.rohit.arishit_f.dashboard.ThreetabSlider;
import com.example.rohit.arishit_f.login.MessageResult;
import com.example.rohit.arishit_f.retrofit.IMyService;
import com.example.rohit.arishit_f.retrofit.RetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.example.rohit.arishit_f.constants.Constants.IP_ADDRESS;

public class profile_info extends AppCompatActivity {

    private ImageView edit,company_logo;
    private IMyService iMyService;
    private TextView userName;
    private TextView userid;
    private TextView useremail;
    private TextView userdesignation;
    private TextView usermobile;
    private ImageView user_image;
    private ProgressBar progressBar;
    private TextView org;
    private TextView email;
    private TextView mobile;
    private String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_info);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        edit =(ImageView)findViewById(R.id.editpersonalinfo);
        userdesignation = (TextView) findViewById(R.id.user_designation);
        userid = (TextView)findViewById(R.id.user_id);
        userName = (TextView)findViewById(R.id.user_name);
        user_image = (ImageView)findViewById(R.id.profile_photo);
        progressBar = (ProgressBar)findViewById(R.id.progress_bar);
        org = (TextView)findViewById(R.id.org_Designation);
        email = (TextView)findViewById(R.id.email_Designation);
        mobile = (TextView)findViewById(R.id.mobile_Designation);
        company_logo = (ImageView)findViewById(R.id.applogo);

        company_logo.setImageResource(R.drawable.default_company);

        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent edit = new Intent(profile_info.this,profile_edit_page.class);
                edit.putExtra("uid",uid);
                startActivity(edit);
                finish();
            }
        });

        fetch();


    }

    private void fetch(){

        final ArrayList<profile_item> item = new ArrayList<profile_item>();


        SharedPreferences sharedPreferences = getSharedPreferences("userDetails", MODE_PRIVATE);
        String bearerToken = sharedPreferences.getString("bearer_token","12345");

        retrofit2.Call<MessageResult> call = iMyService.getMyDetails("Bearer "+bearerToken);
        call.enqueue(new Callback<MessageResult>() {
            @Override
            public void onResponse(retrofit2.Call<MessageResult> call, Response<MessageResult> response) {
                System.out.println(response.body().getMessage());
                JSONObject info = null;
                try {
                    info = new JSONObject(response.body().getMessage());
                    userName.setText(info.getString("Name"));
                    userid.setText(info.getString("user_org_id"));
                    userdesignation.setText(info.getString("Designation"));
                    uid = info.getString("user_id");
                    JSONObject tobj = new JSONObject(info.getString("display_picture"));
                    String turl = tobj.getString("url");
                    Log.v("turl:",turl);
                    if(!turl.equals("null")) {
                        progressBar.setVisibility(View.VISIBLE);
                        Glide.with(profile_info.this)
                                .load(IP_ADDRESS + ":3000" + turl)
                                .apply(
                                        new RequestOptions()
                                                .error(R.drawable.profile_img_new)
                                                .centerCrop()
                                )
                                .listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        progressBar.setVisibility(View.GONE);
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        progressBar.setVisibility(View.GONE);
                                        return false;
                                    }
                                })
                                .into(user_image);
                    }else{
                        user_image.setImageResource(R.drawable.profile_circle);
                        progressBar.setVisibility(View.INVISIBLE);

                    }

                    org.setText(info.getString("Organization"));
                    email.setText(info.getString("Email"));
                    mobile.setText(info.getString("Mobile"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<MessageResult> call, Throwable t) {

            }
        });

//        uid = s.getString("UserID"," ");
//        Log.d("uid",uid);
//        Call<MessageResult> getinfo = iMyService.getInfo(uid);
//
//        getinfo.enqueue(new Callback<MessageResult>() {
//            @Override
//            public void onResponse(Call<MessageResult> call, Response<MessageResult> response) {
//                Log.d("info",response.body().getMessage());
//                try {
//                    JSONObject info =new JSONObject(response.body().getMessage());
//                    System.out.println(info);
//                    userName.setText(info.getString("Name"));
//                    userid.setText(info.getString("user_id"));
//                    userdesignation.setText(info.getString("Designation"));
//                    item.add(new profile_item("Organization Name","MeMe", R.drawable.ic_supervisor_account_black_24dp));
//                    item.add(new profile_item("Email",info.getString("Email"), R.drawable.ic_email_black_24dp));
//                    item.add(new profile_item("Mobile",info.getString("Mobile"), R.drawable.ic_call_arishti_24dp));
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                profileAdapter profile =new profileAdapter(profile_info.this,item);
//                listView.setAdapter(profile);
//            }
//            @Override
//            public void onFailure(Call<MessageResult> call, Throwable t) {
//
//            }
//        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        fetch();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, ThreetabSlider.class));
    }
}
