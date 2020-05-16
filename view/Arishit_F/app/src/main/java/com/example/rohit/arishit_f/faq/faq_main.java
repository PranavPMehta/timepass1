package com.example.rohit.arishit_f.faq;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rohit.arishit_f.R;
import com.example.rohit.arishit_f.login.MessageResult;
import com.example.rohit.arishit_f.retrofit.IMyService;
import com.example.rohit.arishit_f.retrofit.RetrofitClient;
import com.example.rohit.arishit_f.splash.IntroScreen;
import com.thoughtbot.expandablerecyclerview.listeners.GroupExpandCollapseListener;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.example.rohit.arishit_f.R.layout.faq_question;
import static com.example.rohit.arishit_f.constants.Constants.SERVER_URI;

public class faq_main extends AppCompatActivity {


    private ImageView imageView;
    private RelativeLayout layout;
    private LinearLayout parent;
    private IMyService iMyService;
    private ArrayList<faq_quetion> quetions;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.faq_main);

        getSupportActionBar().setTitle("FAQs");


        recyclerView = findViewById(R.id.faq_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);

        fetch();

    }

    private void fetch() {

        Call<MessageResult> getFAQ = iMyService.getFAQ();
        getFAQ.enqueue(new Callback<MessageResult>() {

            @Override
            public void onResponse(Call<MessageResult> call, Response<MessageResult> response) {

                try {
                    JSONArray jj = new JSONArray(response.body().getMessage());
                    System.out.println(jj);

                    quetions = new ArrayList<>();

                    for (int j = 0; j < jj.length(); j++) {
                        JSONObject j1 = jj.getJSONObject(j);
                        System.out.println("j1" + j1);

                        int color;

                        if (j % 2 == 0) {
                            color = 1;
                        } else {
                            color = 0;
                        }


                        ArrayList<faq_answer> answers1 = new ArrayList<>();

                        String loc = j1.getString("url");
                        String urll=SERVER_URI+'/'+loc;
                        Log.v("loc",urll);


                        answers1.add(new faq_answer( j1.getString("description"), color,urll));

                        quetions.add(new faq_quetion(j1.getString("title"), answers1, color));

                    }

                    answer_adapter adapter = new answer_adapter(quetions);
                    recyclerView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<MessageResult> call, Throwable t) {
                Toast.makeText(faq_main.this, "Some Error Occured", Toast.LENGTH_SHORT).show();

            }
        });

    }
}