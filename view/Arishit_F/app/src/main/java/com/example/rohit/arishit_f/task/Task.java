package com.example.rohit.arishit_f.task;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.rohit.arishit_f.login.MessageResult;
import com.example.rohit.arishit_f.R;
import com.example.rohit.arishit_f.retrofit.IMyService;
import com.example.rohit.arishit_f.retrofit.RetrofitClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Task extends Fragment {
    private static final int ADDMEETING_REQUEST = 1;
    public static int flag =0;
    List<ContactRetro> adslist;
    final static ArrayList<Item> users = new ArrayList<>();
    public static  final ArrayList<ListItems> addcontact = new ArrayList<>();
    String[] names ;
    ListView listView ;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task ,
                container, false);
        //FloatingActionButton fab = view.findViewById(R.id.fab);
        listView = view.findViewById(R.id.tasklistview);
        /*fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(), CreateTask.class));
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        /*
        Retrofit retrofit = RetrofitClient.getInstance();
        IMyService api = retrofit.create(IMyService.class);


        Call<List<ContactRetro>> call = api.getstatus("ar1");

        call.enqueue(new Callback<List<ContactRetro>>() {
            @Override
            public void onResponse(Call<List<ContactRetro>> call, Response<List<ContactRetro>> response) {
                adslist = response.body();
                //names = new String[adslist.size()];
                //Log.d("Size","adslist.size() : "+adslist.size());
                for (ContactRetro C : adslist) {
                    String temp = C.getUsername();
                    users.add(new Item ( temp,true));
                    Log.d("Fetched","Username : "+temp);
                    //contact.add(new Contacts(temp, R.drawable.ic_person_black_24dp));
                }
                //Toast.makeText(getActivity(), "Fetch Successful", Toast.LENGTH_SHORT).show();
            }


            @Override
            public void onFailure(Call<List<ContactRetro>> call, Throwable t) {
                Log.e("Throw",t.toString());
                Toast.makeText(getActivity(), "Some Error Occured", Toast.LENGTH_SHORT).show();

            }
        });
*/
        /*Call<MessageResult> call1 = api.getTask();

        call1.enqueue(new Callback<MessageResult>() {
            @Override
            public void onResponse(Call<MessageResult> call, Response<MessageResult> response) {
                Log.d("call1", "onResponse Call1: "+response.body().getMessage());
                addcontact.clear();
                try {
                    JSONArray jj = new JSONArray(response.body().getMessage());
                    for(int j=0;j<jj.length();j++) {
                        JSONObject j1 = jj.getJSONObject(j);
                        Log.d("JSON", "onResponse: JSON : " + j1.getString("for_whom"));
                        JSONArray jj1 = new JSONArray(j1.getString("for_whom"));
                        String result="";
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < jj1.length(); i++) {
                            JSONObject j11 = jj1.getJSONObject(i);
                            sb.append(j11.getString("user")).append(",");
                            Log.d("JSON", "onResponse: JSON : " + j11.getString("user"));
                        }
                        result = sb.deleteCharAt(sb.length() - 1).toString();
                        addcontact.add(new ListItems(result, j1.getString("title"), j1.getString("description"), "Manager"));
                        //ListItemsAdapter contactAdapter = new ListItemsAdapter(this,addcontact);
                        ListItemsAdapter contactAdapter = new ListItemsAdapter(getActivity(),addcontact);
                        contactAdapter.notifyDataSetChanged();
                        listView.setAdapter(contactAdapter);


                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view,
                                                    int position, long id) {

                                Intent intent = new Intent(getActivity(),TaskDesc.class);
                                Log.d("Pos", "onItemClick: "+position);
                                Log.d("Item", "onItemClick: "+addcontact.get(position));
                                ListItems temp = addcontact.get(position);
                                intent.putExtra("Title",temp.getTitle());
                                intent.putExtra("membernames",temp.getMembernames());
                                intent.putExtra("desc",temp.getDesc());

                                startActivity(intent);
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Toast.makeText(getContext(), "Call Successful", Toast.LENGTH_SHORT).show();
            }


            @Override
            public void onFailure(Call<MessageResult> call, Throwable t) {
                Log.e("Throw",t.toString());
                Toast.makeText(getContext(), "Some Error Occured", Toast.LENGTH_SHORT).show();

            }
        });
*/
        return view;
    }
/*
    @Override
    public void onResume() {
        super.onResume();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(Task.this).attach(Task.this).commit();

    }
    */

}