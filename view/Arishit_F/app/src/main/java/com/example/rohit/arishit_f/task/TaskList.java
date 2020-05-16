package com.example.rohit.arishit_f.task;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.rohit.arishit_f.R;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

import static com.example.rohit.arishit_f.task.MultiSelectionSpinner.taskmem;
import static com.example.rohit.arishit_f.task.Task.flag;


public class TaskList extends AppCompatActivity {

    String TitleText = "helcldmcc",DescText = "ddsvdvc",MemberText = "cccc";
    public static  final ArrayList<ListItems> addcontact = new ArrayList<>();
    ListView listView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

//        Intent main = new Intent();
//        int flag =  main.getIntExtra("flag",0);
//        Log.d("flag ",String.valueOf(flag));
        listView = findViewById(R.id.tasklist);
        Intent getdata = getIntent();

        TitleText = getdata.getStringExtra("title");
        DescText = getdata.getStringExtra("Description");
        MemberText = taskmem;

        Log.d("scgdc",TitleText+"   "+ DescText + "   "+MemberText);

        if(getdata!=null && flag == 1) {
            addcontact.add(new ListItems(MemberText, TitleText, DescText, "Manager"));
        }
        ListItemsAdapter contactAdapter = new ListItemsAdapter(this,addcontact);
        contactAdapter.notifyDataSetChanged();
        listView.setAdapter(contactAdapter);




        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                /*Intent intent = new Intent(TaskList.this,TaskDesc.class);
                intent.putExtra("Title",addcontact.get(position).title);
                intent.putExtra("membernames",addcontact.get(position).membernames);
                intent.putExtra("desc",addcontact.get(position).desc);

                startActivity(intent);*/
            }
        });

    }

}




