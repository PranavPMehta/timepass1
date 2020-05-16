package com.example.rohit.arishit_f.vault;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rohit.arishit_f.R;
import com.example.rohit.arishit_f.login.MessageResult;
import com.example.rohit.arishit_f.retrofit.IMyService;
import com.example.rohit.arishit_f.retrofit.RetrofitClient;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class vault_register extends AppCompatActivity {

    private EditText etPassword,etConfirmPassword;
    private Button etSaveVault;
    private String bearerToken;
    private IMyService iMyService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vault_register);

        etPassword = findViewById(R.id.etVaultPassword);
        etSaveVault=findViewById(R.id.btnVaultSave);
        etConfirmPassword=findViewById(R.id.etConfirmVaultPassword);

        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);
        SharedPreferences sharedPreferences = getSharedPreferences("userDetails", MODE_PRIVATE);
        bearerToken = sharedPreferences.getString("bearer_token","12345");

        etSaveVault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = etPassword.getText().toString();
                String confirm_password=etConfirmPassword.getText().toString();
                if(password.equals(confirm_password)){
                    retrofit2.Call<MessageResult> call = iMyService.updateVaultPassword("Bearer "+bearerToken,password);
                    call.enqueue(new Callback<MessageResult>() {
                        @Override
                        public void onResponse(retrofit2.Call<MessageResult> call, Response<MessageResult> response) {
                            System.out.println(response.body().getMessage());
                            Toast.makeText(getApplicationContext(),response.body().getMessage(),Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(retrofit2.Call<MessageResult> call, Throwable t) {

                        }
                    });
                }
                else{
                    Toast.makeText(getApplicationContext(),"Password doesn't match",Toast.LENGTH_SHORT).show();
                }

            }
        });



    }
}
