package com.example.rohit.arishit_f.task;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rohit.arishit_f.login.MessageResult;
import com.example.rohit.arishit_f.R;
import com.example.rohit.arishit_f.retrofit.IMyService;
import com.example.rohit.arishit_f.retrofit.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.example.rohit.arishit_f.task.MultiSelectionSpinner.finalll;

public class CreateTask extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    //private Spinner spinner;
    private TextView Filenm;
    private EditText title,desc;
    private Button create,file;
    private MultiSelectionSpinner myspinner;
    public static String getDirectoryPath;
    public  static String Title,Desc,Member;
    IMyService iMyService;
    private static final String[] paths = {"Member List", "item 2", "item 3"};
    List<ContactRetro> adslist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);


        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int height=dm.heightPixels;
        int width=dm.widthPixels;

        getWindow().setLayout((int)(width * 0.75),(int)(height * 0.59));


        // spinner = findViewById(R.id.spinner);
//        ArrayAdapter<String>adapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_spinner_item,paths);
//
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(adapter);
//        spinner.setOnItemSelectedListener(this);

        title = findViewById(R.id.titledit);
        desc = findViewById(R.id.descedit);
        create = findViewById(R.id.create);
        file = findViewById(R.id.button2);
        Filenm = findViewById(R.id.Filenm);
        myspinner = findViewById(R.id.items);

        ArrayList<Item> selectedItems = new ArrayList<>();
        final ArrayList<Item> users = Task.users;



        myspinner.setItems(users);

        myspinner.setSelection(selectedItems);

        selectedItems = myspinner.getSelectedItems();

        Log.d("Item " , String.valueOf(finalll.size()));
        for(int i=0;i<finalll.size();i++){
            Log.d("Item " ,finalll.get(i));
        }


        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Title = title.getText().toString();
                Desc = desc.getText().toString();

                Log.d("Item " , String.valueOf(finalll.size()));
                for(int i=0;i<finalll.size();i++){
                    Log.d("Item " ,finalll.get(i));
                }
                Retrofit retrofit = RetrofitClient.getInstance();
                iMyService = retrofit.create(IMyService.class);
                String for_mem = MultiSelectionSpinner.taskmem;
                Log.d("Contents : ",Title+" -- "+Desc+" -- "+MultiSelectionSpinner.taskmem);
                final Call<MessageResult> create_task = iMyService.createTask(for_mem,Title,Desc);
                create_task.enqueue(new Callback<MessageResult>() {
                    @Override
                    public void onResponse(Call<MessageResult> call, Response<MessageResult> response) {
                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<MessageResult> call, Throwable t) {
                        Toast.makeText(CreateTask.this, "Error Occured", Toast.LENGTH_SHORT).show();

                    }
                });

                Call<MessageResult> call1 = iMyService.getTask();

                call1.enqueue(new Callback<MessageResult>() {
                    @Override
                    public void onResponse(Call<MessageResult> call, Response<MessageResult> response) {
                        Log.d("call1", "onResponse Call1: "+response.body().getMessage());
                        try {
                            JSONArray jj = new JSONArray(response.body().getMessage());
                            for(int j=0;j<jj.length();j++) {
                                JSONObject j1 = jj.getJSONObject(j);
                                Log.d("JSON", "onResponse: JSON : " + j1.getString("for_whom"));
                                JSONArray jj1 = new JSONArray(j1.getString("for_whom"));
                                for (int i = 0; i < jj1.length(); i++) {
                                    JSONObject j11 = jj1.getJSONObject(i);
                                    Log.d("JSON", "onResponse: JSON : " + j11.getString("user"));
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        //Toast.makeText(getApplicationContext(), "Call Successful", Toast.LENGTH_SHORT).show();
                    }


                    @Override
                    public void onFailure(Call<MessageResult> call, Throwable t) {
                        Log.e("Throw",t.toString());
                        Toast.makeText(getApplicationContext(), "Some Error Occured", Toast.LENGTH_SHORT).show();

                    }
                });


                /*Log.d("crste",Title+"   "+ Desc + "   "+Member);
                created.putExtra("title",Title);
                created.putExtra("Description",Desc);
                created.putExtra("Member",Member);
                Task.flag = 1;
                startActivity(created);
                finish();*/
                Task.flag = 1;
                finish();
            }
        });

        file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(intent, 7);


            }
        });


    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l){
        Member = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub

        super.onActivityResult(7,7,data);
        switch(requestCode){

            case 7:

                if(resultCode==RESULT_OK){

                    String PathHolder = data.getData().getPath();
                    File file = new File(PathHolder);
                    getDirectoryPath = file.getName();
                    Filenm.setText(getDirectoryPath);

                    //Toast.makeText(CreateTask.this, getDirectoryPath, Toast.LENGTH_LONG).show();

                }
                break;

        }
    }
}
