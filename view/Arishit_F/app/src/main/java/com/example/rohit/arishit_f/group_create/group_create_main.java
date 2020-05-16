package com.example.rohit.arishit_f.group_create;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.rohit.arishit_f.MainActivity;
import com.example.rohit.arishit_f.R;
import com.example.rohit.arishit_f.dashboard.homepage_contact;
import com.example.rohit.arishit_f.dashboard.homepage_contactAdapter;
import com.example.rohit.arishit_f.dashboard.homepage_contact_list;
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

public class group_create_main extends AppCompatActivity {

    private ListView recyclerView;
    private group_create_adpater adpater;
    private EditText etsearch;
    public static ArrayList<group_create_contact> contact;
    public ArrayList<group_create_contact> sorted_contact;
    public static ArrayList<group_create_contact> checked_contact;
    private ImageButton proceed;
    int textlength=0;
    private IMyService iMyService;
    private static String sender_org_token = null;
    private String sender_user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_create_main);

        recyclerView = (ListView) findViewById(R.id.recycler_view);
        etsearch = (EditText)findViewById(R.id.create_group_searchBar);
        proceed = (ImageButton)findViewById(R.id.group_create_proceed);

        contact = new ArrayList<group_create_contact>();
        checked_contact = new ArrayList<group_create_contact>();
        sorted_contact = new ArrayList<group_create_contact>();


        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);
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
                                            group_create_contact c = new group_create_contact(name, url, Designation, 0, userid);
                                            contact.add(c);
                                            sorted_contact.add(c);
                                        }
                                 }
                                 group_create_adpater contactAdapter = new group_create_adpater(group_create_main.this, contact);
                                 recyclerView.setAdapter(contactAdapter);
                             } catch (JSONException e) {
                                 e.printStackTrace();
                             }

                         }

                         @Override
                         public void onFailure(Call<MessageResult> call, Throwable t) {
                             Toast.makeText(group_create_main.this, t.toString(), Toast.LENGTH_SHORT).show();
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
                sorted_contact.clear();
                for (int i = 0; i < contact.size(); i++) {
                    if (textlength <= contact.get(i).getName().length()) {
                        if (contact.get(i).getName().toLowerCase().trim().contains(
                                etsearch.getText().toString().toLowerCase().trim())) {
                            sorted_contact.add(contact.get(i));
                        }
                    }
                }

                adpater = new group_create_adpater(group_create_main.this, sorted_contact);
                recyclerView.setAdapter(adpater);
            }
        });

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(checked_contact.size()==0){
                    Toast.makeText(group_create_main.this, "Select at least one member", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    Intent i = new Intent(group_create_main.this,group_create_second_page.class);
                    i.putExtra("userID",sender_user_id);
                    startActivity(i);
                }

            }
        });

    }

}
