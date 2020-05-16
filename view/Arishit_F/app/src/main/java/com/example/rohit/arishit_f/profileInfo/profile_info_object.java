package com.example.rohit.arishit_f.profileInfo;

public class profile_info_object {

    private String mName;
    private int mImageResId;
    private int misAdmin=-1;

    public profile_info_object(String name,int ImageId,int isAdmin){
        mName=name;
        mImageResId=ImageId;
        misAdmin=isAdmin;
    }
    public profile_info_object(String name,int ImageId){
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
