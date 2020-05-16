package com.example.rohit.arishit_f.dashboard;

public class homepage_contact {
    private String mName;
    private String mUserName;
    private String mDesignation;
    private String mIsOnline;
    private String mImageResId;
    private int misAdmin = -1;
    private String muserId=" ";

    //constructor with Every field
    public homepage_contact(String name,String UserName, String ImageId, int isAdmin,String IsOnline,String userId) {
        mName = name;
        mUserName = UserName;
        mImageResId = ImageId;
        misAdmin = isAdmin;
        mIsOnline = IsOnline;
        muserId = userId;
    }


    //constructor without Isonline field
    public homepage_contact(String name,String UserName,String Designation,String userId , String ImageId) {
        mName = name;
        mUserName = UserName;
        mDesignation = Designation;
        mImageResId = ImageId;
        muserId = userId;
    }

    public String getname() {
        return mName;
    }

    public int getIsAdmin() {
        return misAdmin;
    }

    public String getImageResId() {
        return mImageResId;
    }

    public String getmUserName() {
        return mUserName;
    }

    public String getmDesignation(){ return mDesignation; }

    public void setmUserName(String mUserName) {
        this.mUserName = mUserName;
    }
    public void setmDesignation(String mDesignation) {this.mDesignation = mDesignation;}

    public String getmIsOnline() {
        return mIsOnline;
    }

    public void setmIsOnline(String mIsOnline) {
        this.mIsOnline = mIsOnline;
    }

    public String getMuserId() {
        return muserId;
    }

    public void setMuserId(String muserId) {
        this.muserId = muserId;
    }
}
