package com.example.rohit.arishit_f.group_view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rohit.arishit_f.R;
import com.example.rohit.arishit_f.group_create.group_create_contact;
import com.example.rohit.arishit_f.login.MessageResult;
import com.example.rohit.arishit_f.retrofit.IMyService;
import com.example.rohit.arishit_f.retrofit.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class remove_member_group extends AppCompatActivity {
    private IMyService iMyService;
    private String group_id;
    private ListView recyclerView;
    private group_remove_member_adpater adpater;
    private EditText etsearch;
    int textlength=0;
    public static ArrayList<group_create_contact> contact_remove;
    public ArrayList<group_create_contact> sorted_contact_remove;
    public static ArrayList<group_create_contact> checked_contact_remove;
    private JSONArray members;
    Button deleteButton;
    String sender_user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_member_group);
        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);

        group_id = getIntent().getStringExtra("group_id");

        deleteButton = (Button) findViewById(R.id.group_remove_delete);
        recyclerView = (ListView) findViewById(R.id.recycler_view);
        etsearch = (EditText)findViewById(R.id.create_group_searchBar);
        contact_remove = new ArrayList<group_create_contact>();
        checked_contact_remove = new ArrayList<group_create_contact>();
        sorted_contact_remove = new ArrayList<group_create_contact>();


        SharedPreferences sharedPreferences = getSharedPreferences("userDetails", MODE_PRIVATE);
        String bearerToken = sharedPreferences.getString("bearer_token", "12345");
        Call<MessageResult> call = iMyService.getMyDetails("Bearer " + bearerToken);
        call.enqueue(new Callback<MessageResult>() {
            @Override

            public void onResponse(retrofit2.Call<MessageResult> call, Response<MessageResult> response) {
                JSONObject j1 = null;
                try {
                     j1 = new JSONObject(response.body().getMessage());
                     sender_user_id = j1.getString("user_id");

                                Call<MessageResult> getgrpinfo = iMyService.getGroupInfo(group_id);//write group_id here
                                getgrpinfo.enqueue(new Callback<MessageResult>() {
                                @Override
                                public void onResponse(Call<MessageResult> call, Response<MessageResult> response) {
                                    Log.d("info", response.body().getMessage());
                                    try {
                                        JSONArray infoarr = new JSONArray(response.body().getMessage());
                                        JSONObject info = (JSONObject)infoarr.get(0);
                                        String admin = info.getString("admin");

                                        JSONArray member = (JSONArray)infoarr.get(1);

                                        System.out.println(member);

                                        for (int i = 0; i < member.length(); i++) {
                                            JSONObject mem = member.getJSONObject(i);
                                            JSONObject display_pic = new JSONObject(mem.getString("display_picture"));
                                            if(!admin.equals(mem.getString("user_id")))
                                            contact_remove.add(new group_create_contact(mem.getString("Name"),display_pic.getString("url"), mem.getString("Designation"),0,mem.getString("user_id")));
                                        }
                                        group_remove_member_adpater contactAdapter = new group_remove_member_adpater(remove_member_group.this, contact_remove);
                                        recyclerView.setAdapter(contactAdapter);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onFailure(Call<MessageResult> call, Throwable t) {

                                }
                            });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<MessageResult> call, Throwable t) {

            }
        });


        etsearch.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textlength = etsearch.getText().length();
                sorted_contact_remove.clear();
                for (int i = 0; i < contact_remove.size(); i++) {
                    if (textlength <= contact_remove.get(i).getName().length()) {
                        if (contact_remove.get(i).getName().toLowerCase().trim().contains(
                                etsearch.getText().toString().toLowerCase().trim())) {
                            sorted_contact_remove.add(contact_remove.get(i));
                        }
                    }
                }

                adpater = new group_remove_member_adpater(remove_member_group.this, sorted_contact_remove);
                recyclerView.setAdapter(adpater);
            }
        });


        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checked_contact_remove.size()==0){
                    Toast.makeText(remove_member_group.this, "Select at least one member", Toast.LENGTH_SHORT).show();
                    return;
                }else {

                    members = new JSONArray();
                    System.out.println(checked_contact_remove.size());
                    for(int i=0;i<checked_contact_remove.size();i++){
                        JSONObject member = new JSONObject();
                        try {
                            member.put("user_id",checked_contact_remove.get(i).getContact_id());

                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                        members.put(member);
                    }

                    Call<MessageResult> removegrpmember = iMyService.removeGroupMember(group_id,members);//write group_id here
                    removegrpmember.enqueue(new Callback<MessageResult>() {
                        @Override
                        public void onResponse(Call<MessageResult> call, Response<MessageResult> response) {
                            Toast.makeText(remove_member_group.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            Intent i =new Intent(remove_member_group.this,group_view.class);
                            i.putExtra("group_id",group_id);
                            startActivity(i);
                        }

                        @Override
                        public void onFailure(Call<MessageResult> call, Throwable t) {
                            Toast.makeText(remove_member_group.this, t.toString(), Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }


        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent i =new Intent(remove_member_group.this, group_view.class);
        i.putExtra("group_id",group_id);
        startActivity(i);
    }
}
