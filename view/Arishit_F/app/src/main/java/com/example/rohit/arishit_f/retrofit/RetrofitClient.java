package com.example.rohit.arishit_f.retrofit;

import android.content.Context;

import com.example.rohit.arishit_f.constants.Constants;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static com.example.rohit.arishit_f.retrofit.IMyService.IP;

public class RetrofitClient {
    private static Retrofit instance;
    private static final String BASE_URL = Constants.IP_ADDRESS+":5002";

    private static Retrofit retrofit;

    public static Retrofit getInstance(){
        if(instance==null)
            instance=new Retrofit.Builder()
                    .baseUrl(Constants.SERVER_URI)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        return instance;
    }
    public static Retrofit getRetrofitClient(Context context) {

        if (retrofit == null) {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}


