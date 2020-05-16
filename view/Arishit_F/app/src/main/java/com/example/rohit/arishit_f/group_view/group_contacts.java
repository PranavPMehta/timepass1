package com.example.rohit.arishit_f.group_view;


public class group_contacts {
    private String mName;
    private String mPosition;
    private String mImageResId;
    private int misAdmin = -1;
    private String muserid;

    public group_contacts(String name, String Position, String ImageId, int isAdmin) {
        mName = name;
        mPosition = Position;
        mImageResId = ImageId;
        misAdmin = isAdmin;
    }

    public group_contacts(String name, String Position, String ImageId,String userid) {
        mName = name;
        mPosition = Position;
        mImageResId = ImageId;
        muserid = userid;
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


    public String getmPosition() {
        return mPosition;
    }

    public void setmPosition(String mPosition) {
        this.mPosition = mPosition;
    }

    public String getMuserid() {
        return muserid;
    }

    public void setMuserid(String muserid) {
        this.muserid = muserid;
    }
}