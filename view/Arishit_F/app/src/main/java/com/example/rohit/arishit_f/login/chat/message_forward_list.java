package com.example.rohit.arishit_f.login.chat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.rohit.arishit_f.R;
import com.example.rohit.arishit_f.dashboard.Contacts;
import com.example.rohit.arishit_f.dashboard.ContactsAdapter;
import com.example.rohit.arishit_f.login.MessageResult;
import com.example.rohit.arishit_f.retrofit.IMyService;
import com.example.rohit.arishit_f.retrofit.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class message_forward_list extends AppCompatActivity {
    IMyService iMyService;
    private String message_to_be_forwarded;
    private String datatype;
    private boolean isForward;
    private static String sender_id = null;
    private static String sender_username = null;
    private ListView listView;
    private ArrayList<Contacts> contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_forward_list);
        try {
            message_to_be_forwarded = getIntent().getExtras().getString("message_to_be_forwarded");
            datatype = getIntent().getExtras().getString("data_type");
            isForward = true;
        }catch (Exception e){
            System.out.println(e);
            isForward = false;
        }
        listView = (ListView) findViewById(R.id.forwardContactListView);
        /*
        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);
        SharedPreferences sharedPreferences = getSharedPreferences("userDetails", MODE_PRIVATE);
        String sender_username = sharedPreferences.getString("s_id", "12345");
        Call<MessageResult> getUser = iMyService.getOrgList(sender_username);
        ListView listView = (ListView) findViewById(R.id.forwardContactListView);
        final ArrayList<Contacts> contact = new ArrayList<Contacts>();
        getUser.enqueue(new Callback<MessageResult>() {
            @Override
            public void onResponse(Call<MessageResult> call, Response<MessageResult> response) {
                try {
                    //contact.clear();
                    JSONArray jj = new JSONArray(response.body().getMessage());
                    System.out.println(jj);
                    for(int j=0;j<jj.length();j++) {
                        JSONObject j1 = jj.getJSONObject(j);
                        System.out.println("j1" + j1);
                        String name = "";
                        Log.d("Username..",sender_username);
                        if(j1.has("Username")){
                            name = j1.getString("Username");
                        }
                        else if(j1.has("group_name")){
                            name = j1.getString("group_name");
                        }
                        String ID = "";
                        boolean is_group_val = false;
                        if(j1.has("user_id")){
                            ID = j1.getString("user_id");
                        }
                        else if(j1.has("group_id")){
                            ID = j1.getString("group_id");
                            is_group_val = true;
                        }

                        String Designation = "";
                        if(j1.has("Designation")){
                            Designation = j1.getString("Designation");
                        }
                        String url = null;
                        if(j1.has("display_picture")){
                            JSONObject imageurl = new JSONObject(j1.getString("display_picture"));
                            if (!imageurl.getString("url").equals("null")) {
                                url = imageurl.getString("url");
                                Log.v("url", url);
                            }
                        }
                        if(!j1.has("group_name")){
                            if(!(j1.getString("Username").equals(sender_username)) && !j1.getString("user_id").equals(sender_id)) {
                                Contacts c = new Contacts(name, ID, Designation, url, is_group_val);
                                contact.add(c);
                            }
                        }
                        else{
                            Contacts c = new Contacts(name, ID, Designation, url, is_group_val);
                            contact.add(c);
                        }
                    }
                    ContactsAdapter contactAdapter =new ContactsAdapter(message_forward_list.this,contact);
                    listView.setAdapter(contactAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //contact.add(new Contacts("Rohit","roh1", R.drawable.ic_person_black_24dp));
            }

            @Override
            public void onFailure(Call<MessageResult> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error Loading contacts", Toast.LENGTH_SHORT).show();
                System.out.println(t);
            }
        });
*/
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
                    sender_id = j1.getString("user_id");
                    sender_username = j1.getString("Username");
                    contact = new ArrayList<Contacts>();
                    Call<MessageResult> getUser = iMyService.getUserList(sender_id);
                    getUser.enqueue(new Callback<MessageResult>() {
                        @Override
                        public void onResponse(Call<MessageResult> call, Response<MessageResult> response) {
                            try {
                                //contact.clear();
                                JSONArray jj = new JSONArray(response.body().getMessage());
                                System.out.println(jj);
                                for (int j = 0; j < jj.length(); j++) {
                                    JSONObject j1 = jj.getJSONObject(j);
                                    System.out.println("j1" + j1);
//                                    String name = j1.getString("Name");
//                                    String Username = j1.getString("Username");
//                                    String Designation = j1.getString("Designation");
                                    String name = "";
                                    Log.d("Username..",sender_username);
                                    if(j1.has("Username")){
                                        name = j1.getString("Username");
                                    }
                                    else if(j1.has("group_name")){
                                        name = j1.getString("group_name");
                                    }
                                    String ID = "";
                                    boolean is_group_val = false;
                                    if(j1.has("user_id")){
                                        ID = j1.getString("user_id");
                                    }
                                    else if(j1.has("group_id")){
                                        ID = j1.getString("group_id");
                                        is_group_val = true;
                                    }

                                    String Designation = "";
                                    if(j1.has("Designation")){
                                        Designation = j1.getString("Designation");
                                    }
                                    String url = null;
                                    if(j1.has("display_picture")){
                                        JSONObject imageurl = new JSONObject(j1.getString("display_picture"));
                                        if (!imageurl.getString("url").equals("null")) {
                                            url = imageurl.getString("url");
                                            Log.v("url", url);
                                        }
                                    }
                                    if(!j1.has("group_name")){
                                        if(!(j1.getString("Username").equals(sender_username)) && !j1.getString("user_id").equals(sender_id)) {
                                            Contacts c = new Contacts(name, ID, Designation, url, is_group_val);
                                            contact.add(c);
                                        }
                                    }
                                    else{
                                        Contacts c = new Contacts(name, ID, Designation, url, is_group_val);
                                        contact.add(c);
                                    }

                                }
                                ContactsAdapter contactAdapter = new ContactsAdapter(message_forward_list.this, contact);
                                listView.setAdapter(contactAdapter);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }


                        @Override
                        public void onFailure(Call<MessageResult> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), "Error Chat", Toast.LENGTH_SHORT).show();
                            System.out.println(t);
                        }
                    });
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<MessageResult> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error Fetching Chat List", Toast.LENGTH_SHORT).show();
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//                Intent intent = new Intent(message_forward_list.this, ChatPage.class);
                Contacts current_contact = contact.get(position);
                Intent intent;
                if(current_contact.get_is_group())
                    intent = new Intent(message_forward_list.this, GroupChat.class);
                else
                    intent = new Intent(message_forward_list.this, ChatPage.class);
                intent.putExtra("id",current_contact.getmID());
                intent.putExtra("name",current_contact.getname());
//                intent.putExtra("User",current_contact.getname());
//                intent.putExtra("UserName",current_contact.getmUserName());
                if(isForward) {
                    System.out.println("MEssage to be forward cl "+message_to_be_forwarded);
                    intent.putExtra("message_to_be_forwarded", message_to_be_forwarded);
                    intent.putExtra("data_type",datatype);
                }
                else{
                    Toast.makeText(message_forward_list.this, "Failed to Forward", Toast.LENGTH_SHORT).show();
                    finish();
                }
                //based on item add info to intent
                startActivity(intent);
                finish();
                //Toast.makeText(message_forward_list.this, "Username : "+current_contact.getmUserName(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
