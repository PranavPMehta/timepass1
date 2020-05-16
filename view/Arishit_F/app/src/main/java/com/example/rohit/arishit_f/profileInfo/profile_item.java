package com.example.rohit.arishit_f.profileInfo;

public class profile_item {
        private String mtitle;
        private String mdescription;
        private int mImageResId;

    public profile_item(String mtitle, String mdescription, int mImageResId) {
        this.mtitle = mtitle;
        this.mdescription = mdescription;
        this.mImageResId = mImageResId;
    }

    public String getMtitle() {
        return mtitle;
    }

    public void setMtitle(String mtitle) {
        this.mtitle = mtitle;
    }

    public String getMdescription() {
        return mdescription;
    }

    public void setMdescription(String mdescription) {
        this.mdescription = mdescription;
    }

    public int getmImageResId() {
        return mImageResId;
    }

    public void setmImageResId(int mImageResId) {
        this.mImageResId = mImageResId;
    }
}
