package com.example.rohit.arishit_f.task;

public class ListItems {
    String membernames,manager,title,desc;

    public ListItems(String membernames, String manager, String title) {
        this.membernames = membernames;
        this.manager = manager;
        this.title = title;
    }

    public ListItems(String membernames, String manager) {
        this.membernames = membernames;
        this.manager = manager;
    }

    public ListItems(String membernames, String manager, String title, String desc) {
        this.membernames = membernames;
        this.manager = manager;
        this.title = title;
        this.desc = desc;
    }

    public ListItems(String memberText) {
    }

    public String getMembernames() {
        return membernames;
    }

    public void setMembernames(String membernames) {
        this.membernames = membernames;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
