package com.example.rohit.arishit_f.faq;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.rohit.arishit_f.R;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

public class faq_quetion_viewholder extends GroupViewHolder {
    private TextView mTextview_quetion;
    private ImageView imageView;
    private RelativeLayout layout;
    private Context context;
    private  int flag_color;

    public faq_quetion_viewholder(View itemView) {
        super(itemView);
        mTextview_quetion=itemView.findViewById(R.id.faq_quetion);
        imageView = itemView.findViewById(R.id.faq_question_arrow);
        layout = itemView.findViewById(R.id.que_relative);
        context = itemView.getContext();
    }


    public void bind(faq_quetion quetion){
        mTextview_quetion.setText(quetion.getTitle());
        flag_color=quetion.getColor();

    }

    @Override
    public void expand() {
        imageView.setImageResource(R.drawable.ic_remove_circle_outline_black_24dp);
        if(flag_color==1){
            layout.setBackground(ContextCompat.getDrawable(context, R.drawable.rounded_faq_question2));

        }else{
            layout.setBackground(ContextCompat.getDrawable(context, R.drawable.rounded_faq_question));
        }
        layout.setElevation(20);
    }

    @Override
    public void collapse() {

        imageView.setImageResource(R.drawable.ic_add_circle_outline_black_24dp);

        if(flag_color==1){
            layout.setBackground(ContextCompat.getDrawable(context, R.drawable.rounded_faq_question_initial2));

        }else{
            layout.setBackground(ContextCompat.getDrawable(context, R.drawable.rounded_faq_question_initial));

        }
        layout.setElevation(0);

    }
}
