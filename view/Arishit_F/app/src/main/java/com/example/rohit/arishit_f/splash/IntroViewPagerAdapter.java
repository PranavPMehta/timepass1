package com.example.rohit.arishit_f.splash;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rohit.arishit_f.R;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

public class IntroViewPagerAdapter extends PagerAdapter {
    Context mContext ;
    List<ScreenItem> mListScreen;
    //AnimatorSet set;
    public IntroViewPagerAdapter(Context mContext, List<ScreenItem> mListScreen) {
        this.mContext = mContext;
        this.mListScreen = mListScreen;
    }

    public ImageView imgSlide;
    public TextView title,description,developed;
    public LayoutInflater inflater;
    public View layoutScreen;

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        instantiate();
        title.setText(mListScreen.get(position).getTitle());
        description.setText(mListScreen.get(position).getDescription());
        imgSlide.setImageResource(mListScreen.get(position).getScreenImg());
        layoutScreen.setBackgroundResource(mListScreen.get(position).getBgimg());
        developed.setVisibility(View.INVISIBLE);

        if(position==0) {
            developed.setVisibility(View.VISIBLE);
            //animate();

        }
        container.addView(layoutScreen);
        return layoutScreen;
    }


    @Override
    public int getCount() {
        return mListScreen.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        container.removeView((View)object);
    }

    public void instantiate(){
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutScreen = inflater.inflate(R.layout.layout_screen,null);
        imgSlide = layoutScreen.findViewById(R.id.intro_img);
        title = layoutScreen.findViewById(R.id.intro_title);
        description = layoutScreen.findViewById(R.id.intro_description);
        developed = layoutScreen.findViewById(R.id.developed_by);

    }

//    public void animate(){
//        set = (AnimatorSet) AnimatorInflater.loadAnimator(layoutScreen.getContext(), R.animator.flip);
//        set.setTarget(imgSlide);
//        set.start();
//
//    }

}
