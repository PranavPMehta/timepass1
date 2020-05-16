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
import com.example.rohit.arishit_f.group_view.group_add_member_adapter;
import com.example.rohit.arishit_f.group_view.group_view;
import com.example.rohit.arishit_f.group_view.remove_member_group;
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

import static com.example.rohit.arishit_f.group_view.group_view.contact;

public class group_add_member extends AppCompatActivity {
    private IMyService iMyService;
    private String group_id;
    private ListView listView;
    private group_add_member_adapter adpater;
    private EditText etsearch;
    int textlength=0;
    public static ArrayList<group_create_contact> contact_add;
    public ArrayList<group_create_contact> sorted_contact_add;
    public static ArrayList<group_create_contact> checked_contact_add;
    private JSONArray members;
    Button addButton;
    String sender_user_id;
    String sender_org_token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_add_member);

        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);

        group_id = getIntent().getStringExtra("group_id");

        addButton = (Button) findViewById(R.id.group_add_member_add);
        listView = (ListView) findViewById(R.id.list_view);
        etsearch = (EditText)findViewById(R.id.group_add_member_searchBar);
        contact_add = new ArrayList<group_create_contact>();
        checked_contact_add = new ArrayList<group_create_contact>();
        sorted_contact_add = new ArrayList<group_create_contact>();

        SharedPreferences sharedPreferences = getSharedPreferences("userDetails", MODE_PRIVATE);
        String bearerToken = sharedPreferences.getString("bearer_token", "12345");
        Call<MessageResult> call = iMyService.getMyDetails("Bearer " + bearerToken);
        call.enqueue(new Callback<MessageResult>() {
            @Override

            public void onResponse(retrofit2.Call<MessageResult> call, Response<MessageResult> response) {
                JSONObject j1 = null;
                try {
                        j1 = new JSONObject(response.body().getMessage());
                        sender_org_token = j1.getString("org_token");
                        sender_user_id = j1.getString("user_id");

                            Call<MessageResult> getorglist = iMyService.getOrgList(sender_org_token);
                            getorglist.enqueue(new Callback<MessageResult>() {
                                @Override
                                public void onResponse(Call<MessageResult> call, Response<MessageResult> response) {
                                    try {
                                        //contact.clear();
                                        JSONArray jj = new JSONArray(response.body().getMessage());
                                        System.out.println(jj);
                                        //to check if the all the group members found or not for efficient finding of non group member
                                        int group_member_size = contact.size();
                                        int members_found = 0; //count the members found who is in group
                                        for (int j = 0; j < jj.length(); j++) {
                                            JSONObject j1 = jj.getJSONObject(j);
                                            System.out.println("j1" + j1);
                                            String name = j1.getString("Name");
                                            String Username = j1.getString("Username");
                                            String Designation = j1.getString("Designation");
                                            String userid = j1.getString("user_id");
                                            String url;
                                            JSONObject imageurl = new JSONObject(j1.getString("display_picture"));
                                            if (!imageurl.getString("url").equals("null")) {
                                                url = imageurl.getString("url");
                                                Log.v("url", url);
                                            } else {
                                                url = "null";
                                            }
                                            Log.v("member found", Integer.toString(members_found));
                                            Log.v("group size",Integer.toString(group_member_size));
                                            if(members_found<group_member_size) {
                                                int present = 0; //flag for present user
                                                //checking the present user is present int the group is yes then skip else add in the list
                                                for (int i = 0; i < contact.size(); i++) {
                                                    if (userid.equals(contact.get(i).getMuserid())) {
                                                        present = 1;
                                                        members_found++;
                                                        Log.v("present" ,"1");
                                                    }
                                                }
                                                if (present == 0) {
                                                    Log.v("present","0");
                                                    contact_add.add(new group_create_contact(name, url, Designation, 0, userid));
                                                }
                                            }else{
                                                contact_add.add(new group_create_contact(name, url, Designation, 0, userid));
                                            }
                                        }


                                        group_add_member_adapter contactAdapter = new group_add_member_adapter(group_add_member.this, contact_add);
                                        listView.setAdapter(contactAdapter);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }

                                @Override
                                public void onFailure(Call<MessageResult> call, Throwable t) {
                                    Toast.makeText(group_add_member.this, t.toString(), Toast.LENGTH_SHORT).show();
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
                sorted_contact_add.clear();
                for (int i = 0; i < contact_add.size(); i++) {
                    if (textlength <= contact_add.get(i).getName().length()) {
                        if (contact_add.get(i).getName().toLowerCase().trim().contains(
                                etsearch.getText().toString().toLowerCase().trim())) {
                            sorted_contact_add.add(contact_add.get(i));
                        }
                    }
                }

                adpater = new group_add_member_adapter(group_add_member.this, sorted_contact_add);
                listView.setAdapter(adpater);
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checked_contact_add.size() == 0){
                    Toast.makeText(group_add_member.this, "Please select at least one member", Toast.LENGTH_SHORT).show();
                    return;
                }else{

                    members = new JSONArray();
                    System.out.println(checked_contact_add.size());
                    for(int i=0;i<checked_contact_add.size();i++){
                        JSONObject member = new JSONObject();
                        try {
                            member.put("user_id",checked_contact_add.get(i).getContact_id());
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                        members.put(member);
                    }

                    Call<MessageResult> addgrpmember = iMyService.addGroupMember(group_id,members);//write group_id here
                    addgrpmember.enqueue(new Callback<MessageResult>() {
                        @Override
                        public void onResponse(Call<MessageResult> call, Response<MessageResult> response) {
                            Toast.makeText(group_add_member.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            Intent i =new Intent(group_add_member.this, group_view.class);
                            i.putExtra("group_id",group_id);
                            startActivity(i);
                        }

                        @Override
                        public void onFailure(Call<MessageResult> call, Throwable t) {
                            Toast.makeText(group_add_member.this, t.toString(), Toast.LENGTH_SHORT).show();

                        }
                    });


                }
            }
        });


    }

}
