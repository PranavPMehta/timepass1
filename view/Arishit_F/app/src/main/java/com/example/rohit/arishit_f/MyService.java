package com.example.rohit.arishit_f;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.example.rohit.arishit_f.login.MessageResult;
import com.example.rohit.arishit_f.retrofit.IMyService;
import com.example.rohit.arishit_f.retrofit.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.example.rohit.arishit_f.login.LoginScreen.Username;

public class MyService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        System.out.println("onTaskRemoved called");
        Retrofit retrofitClient = RetrofitClient.getInstance();
        IMyService iMyService1 = retrofitClient.create(IMyService.class);
        System.out.println("USername in Myservice Destroy"+Username);
        Call<MessageResult> call1 = iMyService1.loginSessionEnd(Username);
        call1.enqueue(new Callback<MessageResult>() {
            @Override
            public void onResponse(Call<MessageResult> call, Response<MessageResult> response) {
                String respons=response.body().getResult();
                //System.out.println("response ondestroy");
            }

            @Override
            public void onFailure(Call<MessageResult> call, Throwable t) {
                System.out.println("failure ondestroy");
                System.out.println("Throw"+t);

            }
        });
        super.onTaskRemoved(rootIntent);
        //do something you want
        //stop service
        this.stopSelf();
    }
}

