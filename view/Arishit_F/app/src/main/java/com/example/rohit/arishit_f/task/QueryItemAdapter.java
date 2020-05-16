package com.example.rohit.arishit_f.task;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.rohit.arishit_f.R;

import java.util.ArrayList;

public class QueryItemAdapter extends ArrayAdapter<Query> {

    public static String Anstoquery;
    TextView Query,Answer;
    Button answer;
    public QueryItemAdapter(Activity context, ArrayList<Query> wordGroup){
        super(context,0,wordGroup);
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.querylistitems, parent, false);
        }


        Query Defaultcontact = getItem(position);

        Query = listItemView.findViewById(R.id.query);
        Answer= listItemView.findViewById(R.id.answer);
        answer = listItemView.findViewById(R.id.ansbutton);


        //Answer Storec
        answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Anstoquery = Answer.getText().toString();

            }
        });


        return listItemView;
    }

}
