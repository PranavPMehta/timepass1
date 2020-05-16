package com.example.rohit.arishit_f.resetPassword;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.rohit.arishit_f.login.MessageResult;
import com.example.rohit.arishit_f.R;
import com.example.rohit.arishit_f.retrofit.IMyService;
import com.example.rohit.arishit_f.retrofit.RetrofitClient;
import com.rengwuxian.materialedittext.MaterialEditText;

public class ForgotPassword extends AppCompatActivity {
    private MaterialEditText UserId;
    private Button getOTP;
    IMyService saveEncrypted;
    String userId = "123";
    String Mobile="";
    String Email="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        UserId = (MaterialEditText)findViewById(R.id.forgot_uid);
        getOTP = (Button)findViewById(R.id.ForgotPasswordGetOTP);
        Retrofit retrofit = RetrofitClient.getInstance();
        saveEncrypted = retrofit.create(IMyService.class);


        getOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userId = UserId.getText().toString();
                Call<MessageResult> requestOtp = saveEncrypted.requestOtp(userId,Email,Mobile);

                requestOtp.enqueue(new Callback<MessageResult>() {
                    @Override
                    public void onResponse(Call<MessageResult> call, Response<MessageResult> response) {
                        Toast.makeText(ForgotPassword.this, response.body().getResult(), Toast.LENGTH_SHORT).show();
                        Intent intent =new Intent(ForgotPassword.this, OTPForgotPassword.class);
                        intent.putExtra("UID",userId);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onFailure(Call<MessageResult> call, Throwable t) {
                        t.printStackTrace();
                        Toast.makeText(ForgotPassword.this, "Error in sending OTP!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }

}
