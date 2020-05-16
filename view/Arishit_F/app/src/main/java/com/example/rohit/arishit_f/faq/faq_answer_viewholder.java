package com.example.rohit.arishit_f.faq;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.rohit.arishit_f.R;
import com.squareup.picasso.Picasso;

import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class faq_answer_viewholder extends ChildViewHolder {

    private int flag_a=1;
    private RelativeLayout layout;
    private Context context;
    private ImageView mgif;
    private TextView mTextview_answer;
    private Bitmap bmp = null;
    private URL url = null;
    private ProgressBar progressBar;

    public faq_answer_viewholder(View itemView) {

        super(itemView);
        mTextview_answer = itemView.findViewById(R.id.faq_answer);
        mgif =(ImageView) itemView.findViewById(R.id.faq_answer_gif);
        layout = itemView.findViewById(R.id.ans_relative);
        context = itemView.getContext();
        progressBar = itemView.findViewById(R.id.progress);
    }

    public void bind(faq_answer answer){
        mTextview_answer.setText(answer.answer);
        Log.v("Ans bind",answer.getUrl());


           Glide.with(context)
                   .load(answer.getUrl())
                   .listener(new RequestListener<Drawable>() {
                       @Override
                       public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                           progressBar.setVisibility(View.GONE);
                           return false;
                       }

                       @Override
                       public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                           progressBar.setVisibility(View.GONE);
                           return false;
                       }
                   })
                   .into(mgif);

        layout.setElevation(20);
        if(answer.getColor()==1) {
            layout.setBackground(ContextCompat.getDrawable(context, R.drawable.rounded_faq_answer2));
            Log.v("","in ans flag==1");
            Log.v("falg_a",Integer.toString(flag_a));

        }else{
            layout.setBackground(ContextCompat.getDrawable(context, R.drawable.rounded_faq_answer));
            Log.v("","in ans flag==0");

        }

    }

}
