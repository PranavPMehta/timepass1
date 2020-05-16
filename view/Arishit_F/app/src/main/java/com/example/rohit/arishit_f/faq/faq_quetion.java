package com.example.rohit.arishit_f.faq;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class faq_quetion extends ExpandableGroup<faq_answer> {

    private int color;


    public faq_quetion(String title, List<faq_answer> items,int color) {
        super(title, items);
        this.color=color;

    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
