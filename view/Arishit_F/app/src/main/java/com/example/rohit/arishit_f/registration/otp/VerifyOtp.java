package com.example.rohit.arishit_f.registration.otp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rohit.arishit_f.login.LoginScreen;
import com.example.rohit.arishit_f.login.MessageResult;
import com.example.rohit.arishit_f.R;
import com.example.rohit.arishit_f.registration.RegisterPage;
import com.example.rohit.arishit_f.retrofit.IMyService;
import com.example.rohit.arishit_f.retrofit.RetrofitClient;
import com.rengwuxian.materialedittext.MaterialEditText;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class VerifyOtp extends AppCompatActivity {

    MaterialEditText editMobileOtp, editEmailOtp;
    Button verifyOtp;
    TextView remain;
    IMyService saveEncrypted;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);



        editMobileOtp = findViewById(R.id.mobileOTP);
        editEmailOtp = findViewById(R.id.emailOTP);
        verifyOtp = findViewById(R.id.otpNextButton);
        /*remain = (TextView) findViewById(R.id.timeremain);

        new CountDownTimer(7000, 1000) {
            public void onTick(long millisUntilFinished) {
                remain.setText("seconds remaining: " + millisUntilFinished / 1000);
                Toast.makeText( VerifyOtp.this,Long.toString(millisUntilFinished/1000),Toast.LENGTH_SHORT).show();
            }



            public void onFinish() {
                Toast.makeText(VerifyOtp.this, "Timeout!", Toast.LENGTH_SHORT).show();
                Intent otpIntent = new Intent(VerifyOtp.this, RegisterPage.class);
                startActivity(otpIntent);
                finish();
            }
        }.start();*/

        initialize();

        verifyOtp.setOnClickListener(new View.OnClickListener() {
            /*@Override
            public void onClick(View v) {
                String Mobileotp = editMobileOtp.getText().toString();
                String Emailotp = editEmailOtp.getText().toString();
                if (editMobileOtp.length() != 6 && editEmailOtp.length() != 6) {
                    Toast.makeText(VerifyOtp.this, "Please enter a valid OTP", Toast.LENGTH_SHORT).show();
                    return;
                }


                final Call<MessageResult> verifyOtp = saveEncrypted.verifyOtp(userId, Mobileotp, Emailotp);

                verifyOtp.enqueue(new Callback<MessageResult>() {
                    @Override
                    public void onResponse(Call<MessageResult> call, Response<MessageResult> response) {
                        String response_result = response.body().getResult();
                        Toast.makeText(VerifyOtp.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        if (response_result.equals("true")) {
                            Intent loginIntent = new Intent(VerifyOtp.this, LoginScreen.class);
                            startActivity(loginIntent);
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<MessageResult> call, Throwable t) {
                        Toast.makeText(VerifyOtp.this, "Error in Verifying OTP", Toast.LENGTH_SHORT).show();
                        t.printStackTrace();
                    }
                });
            }*/
            @Override
            public void onClick(View v) {
                String Mobileotp = editMobileOtp.getText().toString();
                Mobileotp="132456";
                String Emailotp = editEmailOtp.getText().toString();
                if (editEmailOtp.length() != 6) {
                    Toast.makeText(VerifyOtp.this, "Please enter a valid OTP", Toast.LENGTH_SHORT).show();
                    return;
                }


                final Call<MessageResult> verifyOtp = saveEncrypted.verifyOtp(userId, Mobileotp, Emailotp);

                verifyOtp.enqueue(new Callback<MessageResult>() {
                    @Override
                    public void onResponse(Call<MessageResult> call, Response<MessageResult> response) {
                        String response_result = response.body().getResult();
                        Toast.makeText(VerifyOtp.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        if (response_result.equals("true")) {
                            Intent loginIntent = new Intent(VerifyOtp.this, LoginScreen.class);
                            startActivity(loginIntent);
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<MessageResult> call, Throwable t) {
                        Toast.makeText(VerifyOtp.this, "Error in Verifying OTP", Toast.LENGTH_SHORT).show();
                        t.printStackTrace();
                    }
                });
            }
        });
    }

    public void initialize() {
        //Initialize retrofit
        Retrofit retrofit = RetrofitClient.getInstance();
        SharedPreferences sharedPreferences = getSharedPreferences("userDetails", MODE_PRIVATE);
        userId = sharedPreferences.getString("UID", "123");
        saveEncrypted = retrofit.create(IMyService.class);
    }
}
