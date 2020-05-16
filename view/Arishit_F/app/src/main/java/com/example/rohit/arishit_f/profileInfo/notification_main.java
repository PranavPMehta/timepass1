package com.example.rohit.arishit_f.profileInfo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.example.rohit.arishit_f.MainActivity;
import com.example.rohit.arishit_f.R;
import com.example.rohit.arishit_f.login.MessageResult;
import com.example.rohit.arishit_f.retrofit.IMyService;
import com.example.rohit.arishit_f.retrofit.RetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import io.reactivex.functions.Consumer;

import static java.lang.Thread.sleep;


public class notification_main extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    Switch messageReceiptsSwitch;
    Switch newMessageSwitch;
    Switch meetingReminderSwitch;
    Switch taskAllocationSwitch;
    Switch appUpdatesSwitch;
    Button saveNotificationButton;

    Boolean notify_message_receipts = true;
    Boolean notify_new_message = true;
    Boolean notify_scheduled_meeting = true;
    Boolean notify_task_allocation = true;
    Boolean notify_app_update = true;

    // retrofit2 classes
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    IMyService iMyService;
    String user_id;

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_main);


        messageReceiptsSwitch = (Switch) findViewById(R.id.messageReceiptSwitch);
        newMessageSwitch = (Switch) findViewById(R.id.newMessageSwitch);
        meetingReminderSwitch = (Switch) findViewById(R.id.meetingReminderSwitch);
        taskAllocationSwitch = (Switch) findViewById(R.id.taskAllocationSwitch);
        appUpdatesSwitch = (Switch) findViewById(R.id.appUpdatesSwitch);

        messageReceiptsSwitch.setOnCheckedChangeListener( this);
        newMessageSwitch.setOnCheckedChangeListener( this);
        meetingReminderSwitch.setOnCheckedChangeListener( this);
        taskAllocationSwitch.setOnCheckedChangeListener( this);
        appUpdatesSwitch.setOnCheckedChangeListener( this);

        saveNotificationButton = (Button) findViewById(R.id.saveNotificationButton);

        //Init service
        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);

        SharedPreferences sharedPreferences = getSharedPreferences("userDetails", MODE_PRIVATE);
        String bearerToken = sharedPreferences.getString("bearer_token","12345");

        retrofit2.Call<MessageResult> call = iMyService.getMyDetails("Bearer "+bearerToken);
        call.enqueue(new Callback<MessageResult>() {
            @Override
            public void onResponse(retrofit2.Call<MessageResult> call, Response<MessageResult> response) {
                System.out.println(response.body().getMessage());
                JSONObject info = null;
                try {
                    info = new JSONObject(response.body().getMessage());
                    user_id = info.getString("user_id");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<MessageResult> call, Throwable t) {

            }
        });


        saveNotificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNotificationData(notify_message_receipts, notify_new_message, notify_scheduled_meeting, notify_task_allocation, notify_app_update,user_id);
            }
        });

    }

        private void saveNotificationData(Boolean notify_message_receipts, Boolean notify_new_message, Boolean notify_scheduled_meeting, Boolean notify_task_allocation, Boolean notify_app_update,String uid) {
            compositeDisposable.add(iMyService.saveData(notify_message_receipts, notify_new_message, notify_scheduled_meeting, notify_task_allocation, notify_app_update,uid)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String response) throws Exception {
                            JSONObject jsonObject = new JSONObject(response);
                            String message = jsonObject.getString("message");
                            String result = jsonObject.getString("result");
                            if(result.equals("true")) {
                                Toast.makeText(notification_main.this, "" + message, Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(notification_main.this, "" + message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
            );
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch(buttonView.getId()) {
                case R.id.messageReceiptSwitch: {
                    if(isChecked)
                        notify_message_receipts = true;
                    else
                        notify_message_receipts=false;
                    break;
                }
                case R.id.newMessageSwitch: {
                    if(isChecked)
                        notify_new_message = true;
                    else
                        notify_new_message = false;
                    break;
                }
                case R.id.meetingReminderSwitch: {
                    if(isChecked)
                        notify_scheduled_meeting = true;
                    else
                        notify_scheduled_meeting = false;
                    break;
                }
                case R.id.taskAllocationSwitch: {
                    if(isChecked)
                        notify_task_allocation = true;
                    else
                        notify_task_allocation = false;
                    break;
                }
                case R.id.appUpdatesSwitch: {
                    if(isChecked)
                        notify_app_update = true;
                    else
                        notify_app_update = false;
                    break;
                }
            }
        }

}

