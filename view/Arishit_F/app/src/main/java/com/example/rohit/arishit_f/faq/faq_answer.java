package com.example.rohit.arishit_f.faq;

import android.os.Parcel;
import android.os.Parcelable;

public class faq_answer implements Parcelable {

    public final String answer;
    private int color;
    private String url;


    protected faq_answer(String answer,int color,String url){
        this.answer = answer;
        this.color=color;
        this.url=url;
    }

    public static final Creator<faq_answer> CREATOR = new Creator<faq_answer>() {
        @Override
        public faq_answer createFromParcel(Parcel in) {
            return new faq_answer(" ",1,"");
        }

        @Override
        public faq_answer[] newArray(int size) {
            return new faq_answer[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
