package com.example.rohit.arishit_f.splash;

public class ScreenItem {
    String Title,Description;
    int ScreenImg,Bgimg;


    public ScreenItem(String title, String description, int screenImg, int bgimg) {
        Title = title;
        Description = description;
        ScreenImg = screenImg;
        Bgimg = bgimg;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public void setScreenImg(int screenImg) {
        ScreenImg = screenImg;
    }

    public String getTitle() {
        return Title;
    }

    public String getDescription() {
        return Description;
    }

    public int getScreenImg() {
        return ScreenImg;
    }

    public int getBgimg() {
        return Bgimg;
    }


    public void setBgimg(int bgimg) {
        Bgimg = bgimg;
    }
}
