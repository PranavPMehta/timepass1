package com.example.rohit.arishit_f.vault;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rohit.arishit_f.R;
import com.example.rohit.arishit_f.login.MessageResult;
import com.example.rohit.arishit_f.retrofit.IMyService;
import com.example.rohit.arishit_f.retrofit.RetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class vault_login extends AppCompatActivity {

    private EditText etPassword;
    private Button btnVaultLogin;
    private String password;
    private String user_id,vault_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vault_login);
//ata vault madhe vault chata open kr display nahi hot okkk thamb
        etPassword=findViewById(R.id.etPassword);
        etPassword.setOnEditorActionListener(editorActionListener);
        IMyService iMyService;
        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);


        SharedPreferences sharedPreferences = getSharedPreferences("userDetails", MODE_PRIVATE);
        String bearerToken = sharedPreferences.getString("bearer_token","12345");

        retrofit2.Call<MessageResult> call = iMyService.getMyDetails("Bearer "+bearerToken);
        call.enqueue(new Callback<MessageResult>() {
            @Override
            public void onResponse(retrofit2.Call<MessageResult> call, Response<MessageResult> response) {
                System.out.println(response.body().getMessage());
                try {
                    JSONObject jj = new JSONObject(response.body().getMessage());
                    user_id = jj.getString("user_id");
                    vault_password=jj.getString("vault_password");

                    System.out.println("User ID Vault_login "+user_id);
                    try{
                            String vault_password=jj.getString("vault_password");
                    }catch (Exception e){
                        Intent i=new Intent(getApplicationContext(), vault_register.class);
                        startActivity(i);
                    }
                    Toast.makeText(getApplicationContext(),user_id,Toast.LENGTH_LONG).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(retrofit2.Call<MessageResult> call, Throwable t) {
            }
        });

        /*btnVaultLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),user_id,Toast.LENGTH_SHORT);
                password=etPassword.getText().toString();
                System.out.println("New asd etPassword:"+password);
                System.out.println("user_id:"+user_id);
                if(password.equals(vault_password)){
                    Intent i=new Intent(getApplicationContext(),vault_chat.class);
                    startActivity(i);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Wrong password", Toast.LENGTH_SHORT).show();
                }
            }
        });*/
    }

    private TextView.OnEditorActionListener editorActionListener=new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId== EditorInfo.IME_ACTION_SEND)
            {
                Toast.makeText(getApplicationContext(),user_id,Toast.LENGTH_SHORT);
                password=etPassword.getText().toString();
                System.out.println("New asd etPassword:"+password);
                System.out.println("user_id:"+user_id);
                if(password.equals(vault_password)){
                    Intent i=new Intent(getApplicationContext(), vault_chat.class);
                    startActivity(i);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Wrong password", Toast.LENGTH_SHORT).show();
                }
            }
            return false;
        }
    };
}
