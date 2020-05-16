package com.example.rohit.arishit_f.task;

public class Item {
    private String name;
    private Boolean value;

    public String getName() {
        return name;
    }

    public Item(String name, Boolean value) {
        this.name = name;
        this.value = value;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getValue() {
        return value;
    }

    public void setValue(Boolean value) {
        this.value = value;
    }
}