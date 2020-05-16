package com.example.rohit.arishit_f.registration.otp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.rohit.arishit_f.login.MessageResult;
import com.example.rohit.arishit_f.R;
import com.example.rohit.arishit_f.retrofit.IMyService;
import com.example.rohit.arishit_f.retrofit.RetrofitClient;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SendOtp extends AppCompatActivity {

    Button sendOtp;

    IMyService saveEncrypted;
    String userId = "123";
    String Mobile="";
    String Email="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_otp);

        sendOtp = findViewById(R.id.btn_send_otp);

        initialize();

        sendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Call<MessageResult> requestOtp = saveEncrypted.requestOtp(userId,Email,Mobile);

                requestOtp.enqueue(new Callback<MessageResult>() {
                    @Override
                    public void onResponse(Call<MessageResult> call, Response<MessageResult> response) {
                        Toast.makeText(SendOtp.this, response.body().getResult(), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SendOtp.this, VerifyOtp.class));
                        finish();
                    }

                    @Override
                    public void onFailure(Call<MessageResult> call, Throwable t) {
                        t.printStackTrace();
                        Toast.makeText(SendOtp.this, "Error in sending OTP!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public void initialize() {
        //Initialize retrofit
        Retrofit retrofit = RetrofitClient.getInstance();
        SharedPreferences sharedPreferences = getSharedPreferences("userDetails", MODE_PRIVATE);
        Mobile = sharedPreferences.getString("MOBILE","12345");
        Email = sharedPreferences.getString("EMAIL","a@a.com");
        userId = sharedPreferences.getString("UID","123");
        saveEncrypted = retrofit.create(IMyService.class);
    }
}
