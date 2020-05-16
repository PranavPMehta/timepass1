package com.example.rohit.arishit_f.resetPassword;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.rohit.arishit_f.login.MessageResult;
import com.example.rohit.arishit_f.R;
import com.example.rohit.arishit_f.retrofit.IMyService;
import com.example.rohit.arishit_f.retrofit.RetrofitClient;
import com.rengwuxian.materialedittext.MaterialEditText;

public class OTPForgotPassword extends AppCompatActivity {
    private Button next;
    private MaterialEditText editMobileOtp, editEmailOtp;
    IMyService saveEncrypted;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp__forgot__password);

        next = (Button)findViewById(R.id.ForgotOTPNextButton);
        editMobileOtp = findViewById(R.id.forgot_otp_mobile);
        editEmailOtp = findViewById(R.id.forgot_otp_email);

        Retrofit retrofit = RetrofitClient.getInstance();

        userId = getIntent().getExtras().getString("UID");
        Log.d("UID",userId);
        saveEncrypted = retrofit.create(IMyService.class);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Mobileotp = editMobileOtp.getText().toString();
                String Emailotp = editEmailOtp.getText().toString();
                if (editMobileOtp.length() != 6 && editEmailOtp.length() != 6) {
                    Toast.makeText(OTPForgotPassword.this, "Please enter a valid OTP", Toast.LENGTH_SHORT).show();
                    return;
                }


                final Call<MessageResult> verifyOtp = saveEncrypted.verifyOtp(userId, Mobileotp, Emailotp);

                verifyOtp.enqueue(new Callback<MessageResult>() {
                    @Override
                    public void onResponse(Call<MessageResult> call, Response<MessageResult> response) {
                        String response_result = response.body().getResult();
                        Toast.makeText(OTPForgotPassword.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        if (response_result.equals("true")) {
                            Intent loginIntent = new Intent(OTPForgotPassword.this, ResetPassword.class);
                            loginIntent.putExtra("UID",userId);
                            startActivity(loginIntent);
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<MessageResult> call, Throwable t) {
                        Toast.makeText(OTPForgotPassword.this, "Error in Verifying OTP", Toast.LENGTH_SHORT).show();
                        t.printStackTrace();
                    }
                });
            }
        });
    }
}
