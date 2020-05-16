package com.example.rohit.arishit_f.dashboard;


public class Contacts {
    private String mName;
    private String mDesignation;
    private String mImageResId;
    private String muserId;
    private String mUserName;
    private boolean is_group;

    public Contacts(String name, String ID, String Designation, String ImageId, boolean is_group_val) {
        mName = name;
        mDesignation = Designation;
        mImageResId = ImageId;
        muserId = ID;
        is_group = is_group_val;
    }

    public String getname() {
        return mName;
    }

    public String getImageResId() {
        return mImageResId;
    }

    public String getmUserName() {
        return mUserName;
    }

    public String getmID() {
        return muserId;
    }

    public String getmDesignation(){ return mDesignation; }

    public boolean get_is_group(){ return is_group;}

}