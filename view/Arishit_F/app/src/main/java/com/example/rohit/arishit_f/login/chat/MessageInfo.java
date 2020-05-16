package com.example.rohit.arishit_f.login.chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rohit.arishit_f.R;
import com.example.rohit.arishit_f.login.MessageResult;
import com.example.rohit.arishit_f.retrofit.IMyService;
import com.example.rohit.arishit_f.retrofit.RetrofitClient;

import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MessageInfo extends AppCompatActivity {
    private TextView txtSent,txtRecieve,txtLocation;
    private IMyService iMyService;
    private ImageView backBtn;
    String location,timestamp_sent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_info);
        Intent intent=getIntent();
        String ss=intent.getStringExtra("reference_id");
        System.out.println("SS"+ss);
        timestamp_sent=intent.getStringExtra("timestamp");
        location=intent.getStringExtra("location");
        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);

      //  retrofit2.Call<MessageResult> call1 = iMyService.getMessageInfo(ss);

        txtSent = findViewById(R.id.txtSentMsgInfo);
        txtRecieve = findViewById(R.id.txtReadMsgInfo);
        txtLocation = findViewById(R.id.txtLocationMsgInfo);
        backBtn = findViewById(R.id.msgInfoBackBtn);

        txtSent.setText(timestamp_sent);;
        txtLocation.setText(location);

   /*     call1.enqueue(new Callback<MessageResult>() {
            @Override
            public void onResponse(retrofit2.Call<MessageResult> call, Response<MessageResult> response) {

                try {
                    System.out.println(response.body().getMessage());
                    JSONObject jj = new JSONObject(response.body().getMessage());
                    System.out.println(jj);
                    //TODO Update the Read and sent timestamp
                    txtSent.setText(jj.getString("timestamp"));
                    txtRecieve.setText(jj.getString("timestamp"));
                    txtLocation.setText(jj.getString("location"));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<MessageResult> call, Throwable t) {

            }
        });
*/
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }
}
