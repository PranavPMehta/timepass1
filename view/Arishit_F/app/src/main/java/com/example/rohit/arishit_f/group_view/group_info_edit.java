package com.example.rohit.arishit_f.group_view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.rohit.arishit_f.R;
import com.example.rohit.arishit_f.login.MessageResult;
import com.example.rohit.arishit_f.retrofit.IMyService;
import com.example.rohit.arishit_f.retrofit.RetrofitClient;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.example.rohit.arishit_f.constants.Constants.IP_ADDRESS;

public class group_info_edit extends AppCompatActivity {
    MaterialEditText change_name;
    MaterialEditText change_objective;
    IMyService iMyService;
    Button save;
    String group_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info_edit);
        change_name = (MaterialEditText) findViewById(R.id.group_edit_name);
        change_objective = (MaterialEditText)findViewById(R.id.group_edit_objective);
        save = (Button) findViewById(R.id.save_change);

        group_id = getIntent().getStringExtra("group_id");

        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);

        Call<MessageResult> getgrpinfo = iMyService.getGroupInfo(group_id);//write group_id here
        getgrpinfo.enqueue(new Callback<MessageResult>() {
            @Override
            public void onResponse(Call<MessageResult> call, Response<MessageResult> response) {
                Log.d("info", response.body().getMessage());
                try {
                    JSONArray infoarr = new JSONArray(response.body().getMessage());
                    JSONObject info = (JSONObject) infoarr.get(0);
                    System.out.println(info);
                    Log.v("name", info.getString("group_name"));
                    change_name.setText(info.getString("group_name"));
                    change_objective.setText(info.getString("objective"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<MessageResult> call, Throwable t) {
                Toast.makeText(group_info_edit.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                            if (TextUtils.isEmpty(change_name.getText().toString())) {
                                change_name.setError("Please enter the Name");
                                return;
                            }else if (TextUtils.isEmpty(change_objective.getText().toString())) {
                                change_objective.setError("Please enter the Objective");
                                return;
                            }else{

                                Call<MessageResult> changegrpinfo = iMyService.modifyGroupInfo(group_id,change_name.getText().toString(),change_objective.getText().toString());//write group_id here

                                changegrpinfo.enqueue(new Callback<MessageResult>() {
                                    @Override
                                    public void onResponse(Call<MessageResult> call, Response<MessageResult> response) {
                                        Log.d("modify group info", response.body().getMessage());
                                        Toast.makeText(group_info_edit.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onFailure(Call<MessageResult> call, Throwable t) {
                                        Toast.makeText(group_info_edit.this, t.toString(), Toast.LENGTH_SHORT).show();

                                    }
                                });
                            }
                Intent i = new Intent(group_info_edit.this,group_view.class);
                i.putExtra("group_id",group_id);

                startActivity(i);

            }
        });

    }
}
