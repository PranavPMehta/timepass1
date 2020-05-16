package com.example.rohit.arishit_f.task;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.example.rohit.arishit_f.R;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class TaskDesc extends AppCompatActivity {

    String TitleText,DescText,MemberText;
    TextView Title,Desc,Member,Filenm;
    ListView querlist;
    public static  final ArrayList<Query> Queries = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_desc);


        Title = findViewById(R.id.editText2);
        Member = findViewById(R.id.memberName);
        Desc = findViewById(R.id.descText);
        querlist = findViewById(R.id.querylist);
        Filenm = findViewById(R.id.filenm);

        Title.setText(getIntent().getExtras().getString("Title"));
        Member.setText(getIntent().getExtras().getString("membernames"));
        Desc.setText(getIntent().getExtras().getString("desc"));
        Filenm.setText("File");

        Queries.add(new Query(MemberText));
        Queries.add(new Query("Member nam"));
        Queries.add(new Query("Member name"));

        QueryItemAdapter contactAdapter = new QueryItemAdapter(TaskDesc.this,Queries);

        querlist.setAdapter(contactAdapter);


    }

}
