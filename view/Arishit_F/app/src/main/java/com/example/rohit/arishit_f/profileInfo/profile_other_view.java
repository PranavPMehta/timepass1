package com.example.rohit.arishit_f.profileInfo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.rohit.arishit_f.R;
import com.example.rohit.arishit_f.login.MessageResult;
import com.example.rohit.arishit_f.retrofit.IMyService;
import com.example.rohit.arishit_f.retrofit.RetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class profile_other_view extends AppCompatActivity {

    private IMyService iMyService;
    private TextView userName;
    private TextView userid;
    private TextView useremail;
    private TextView userdesignation;
    private TextView usermobile;


    String Email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_other_view);

        userdesignation = (TextView) findViewById(R.id.user_designation);
        useremail = (TextView) findViewById(R.id.user_emailid);
        userid = (TextView)findViewById(R.id.user_id);
        usermobile = (TextView)findViewById(R.id.user_mobilenum);
        userName = (TextView)findViewById(R.id.user_name);

        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);

        Email="s"; //write user id
        Log.d("uid",Email);
        Call<MessageResult> getinfo = iMyService.getInfo(Email);

        getinfo.enqueue(new Callback<MessageResult>() {
            @Override
            public void onResponse(Call<MessageResult> call, Response<MessageResult> response) {
                Log.d("info",response.body().getMessage());
                try {
                    JSONObject info =new JSONObject(response.body().getMessage());
                    System.out.println(info);
                    userName.setText(info.getString("Name"));
                    userid.setText(info.getString("user_id"));
                    useremail.setText(info.getString("Email"));
                    userdesignation.setText(info.getString("Designation"));
                    usermobile.setText(info.getString("Mobile"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<MessageResult> call, Throwable t) {

            }
        });



    }
}
