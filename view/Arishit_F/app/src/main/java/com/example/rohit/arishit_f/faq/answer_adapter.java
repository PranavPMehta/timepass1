package com.example.rohit.arishit_f.faq;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;

import com.example.rohit.arishit_f.R;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class answer_adapter extends ExpandableRecyclerViewAdapter<faq_quetion_viewholder,faq_answer_viewholder> {
    private int flag_q=1;
    private int flag_a=1;
    public answer_adapter(List<? extends ExpandableGroup> groups) {
        super(groups);
    }

    @Override
    public faq_quetion_viewholder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.faq_question,parent,false);

        if(flag_q==1) {
            v.setBackground(ContextCompat.getDrawable(parent.getContext(), R.drawable.rounded_faq_question_initial2));
            Log.v("","in que flag 1 ");
            flag_q=0;
        }else{
            v.setBackground(ContextCompat.getDrawable(parent.getContext(), R.drawable.rounded_faq_question_initial));
            Log.v("","in que flag 2 ");

            flag_q=1;
        }

        ImageView imageView = v.findViewById(R.id.faq_question_arrow);

        imageView.setImageResource(R.drawable.ic_add_circle_outline_black_24dp);
        return new faq_quetion_viewholder(v);

    }


    @Override
    public faq_answer_viewholder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.faq_answer,parent,false);

        return new faq_answer_viewholder(v);
    }

    @Override
    public void onBindChildViewHolder(faq_answer_viewholder holder, int flatPosition, ExpandableGroup group, int childIndex) {

        final faq_answer answer = (faq_answer) group.getItems().get(childIndex);

        holder.bind(answer);
    }

    @Override
    public void onBindGroupViewHolder(faq_quetion_viewholder holder, int flatPosition, ExpandableGroup group) {
        final faq_quetion quetion = (faq_quetion) group ;
        holder.bind(quetion);

    }


}
