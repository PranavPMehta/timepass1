package com.example.rohit.arishit_f.dashboard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.rohit.arishit_f.R;
import com.example.rohit.arishit_f.group_create.group_create_adpater;
import com.example.rohit.arishit_f.group_create.group_create_contact;
import com.example.rohit.arishit_f.group_create.group_create_main;
import com.example.rohit.arishit_f.login.MessageResult;
import com.example.rohit.arishit_f.login.chat.ChatPage;
import com.example.rohit.arishit_f.presentation.tools.contacts;
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

public class homepage_contact_list extends AppCompatActivity {
    private ListView listView;
    private homepage_contactAdapter adpater;
    private EditText etsearch;
    public static ArrayList<homepage_contact> contact;
    public ArrayList<homepage_contact> sorted_contact;
    int textlength=0;
    private IMyService iMyService;
    private static String sender_org_token = null;
    public static String Username=null;
    private String sender_user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage_contact_list);
        Intent intent=getIntent();
        Username=intent.getStringExtra("username");
        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);

        listView = (ListView) findViewById(R.id.homepage_contact_list_listview);
        etsearch = (EditText)findViewById(R.id.homepage_contact_list_searchBar);
        contact = new ArrayList<homepage_contact>();
        sorted_contact = new ArrayList<homepage_contact>();

        SharedPreferences sharedPreferences = getSharedPreferences("userDetails", MODE_PRIVATE);
        String bearerToken = sharedPreferences.getString("bearer_token", "12345");
        retrofit2.Call<MessageResult> call = iMyService.getMyDetails("Bearer " + bearerToken);
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
                                        url = null;
                                    }
                                    if(!sender_user_id.equals(userid)) {
                                    homepage_contact c = new homepage_contact(name, Username, Designation, userid, url);
                                    contact.add(c);
                                    sorted_contact.add(c);

                                    }

                                }
                                homepage_contactAdapter contactAdapter = new homepage_contactAdapter(homepage_contact_list.this, contact);
                                listView.setAdapter(contactAdapter);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onFailure(Call<MessageResult> call, Throwable t) {
                            Toast.makeText(homepage_contact_list.this, t.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<MessageResult> call, Throwable t) {
                Toast.makeText(homepage_contact_list.this, "Error Fetching Chat List", Toast.LENGTH_SHORT).show();
            }
        });



        etsearch.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textlength = etsearch.getText().length();
                sorted_contact.clear();
                for (int i = 0; i < contact.size(); i++) {
                    if (textlength <= contact.get(i).getname().length()) {
                        if (contact.get(i).getname().toLowerCase().trim().contains(
                                etsearch.getText().toString().toLowerCase().trim())) {
                            sorted_contact.add(contact.get(i));
                        }
                    }
                }

                adpater = new homepage_contactAdapter(homepage_contact_list.this, sorted_contact);
                listView.setAdapter(adpater);
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(homepage_contact_list.this, ChatPage.class);
                homepage_contact current_contact = contact.get(i);
                intent.putExtra("name",current_contact.getname());
                intent.putExtra("id",current_contact.getMuserId());
                //based on item add info to intent
                startActivity(intent);
            }
        });

    }
}
