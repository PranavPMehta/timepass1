package com.example.rohit.arishit_f.presentation.tools;


public class contacts {
    private String mName;
    private int mImageResId;
    private int misAdmin=-1;

    public contacts(String name, int ImageId, int isAdmin){
        mName=name;
        mImageResId=ImageId;
        misAdmin=isAdmin;
    }
    public contacts(String name, int ImageId){
        mName=name;
        mImageResId=ImageId;
    }
    public String getname(){
        return mName;
    }

    public int getIsAdmin(){
        return misAdmin;
    }

    public int getImageResId(){
        return mImageResId;
    }


}
