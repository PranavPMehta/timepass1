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

import com.example.rohit.arishit_f.login.LoginScreen;
import com.example.rohit.arishit_f.login.MessageResult;
import com.example.rohit.arishit_f.R;
import com.example.rohit.arishit_f.retrofit.IMyService;
import com.example.rohit.arishit_f.retrofit.RetrofitClient;
import com.rengwuxian.materialedittext.MaterialEditText;

public class ResetPassword extends AppCompatActivity {

    private MaterialEditText TxtNewPassword;
    private MaterialEditText TxtConfirmNewPassword;
    private Button setPassword;
    IMyService saveEncrypted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        TxtNewPassword = (MaterialEditText)findViewById(R.id.enterNewPassword);
        TxtConfirmNewPassword = (MaterialEditText)findViewById(R.id.reenterNewPassword);
        setPassword = (Button) findViewById(R.id.setPasswordButton);

        Retrofit retrofit = RetrofitClient.getInstance();
        saveEncrypted = retrofit.create(IMyService.class);

        setPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = TxtNewPassword.getText().toString();
                String cnfPassword = TxtConfirmNewPassword.getText().toString();
                String userId = getIntent().getExtras().getString("UID");
                if(password.equals(cnfPassword)){
                    final Call<MessageResult> updatePassword = saveEncrypted.resetPassword(userId,password);
                    updatePassword.enqueue(new Callback<MessageResult>() {
                        @Override
                        public void onResponse(Call<MessageResult> call, Response<MessageResult> response) {
                            String response_result = response.body().getResult();
                            Toast.makeText(ResetPassword.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            if (response_result.equals("true")) {
                                Intent loginIntent = new Intent(ResetPassword.this, LoginScreen.class);
                                startActivity(loginIntent);
                                finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<MessageResult> call, Throwable t) {
                            Toast.makeText(ResetPassword.this, "Unable to Update password", Toast.LENGTH_SHORT).show();
                            t.printStackTrace();
                        }
                    });
                }
                else{
                    Toast.makeText(ResetPassword.this, "Passwords did not Match", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
