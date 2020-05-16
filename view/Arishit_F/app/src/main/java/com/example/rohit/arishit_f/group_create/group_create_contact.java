package com.example.rohit.arishit_f.group_create;

public class group_create_contact {

        private String name;
        private String designation;
        private String image;
        private int ischecked;
        private String contact_id;

        public group_create_contact(String name, String image,String designation,int ischecked,String contact_id) {
            this.name = name;
            this.image = image;
            this.designation = designation;
            this.ischecked=ischecked;
            this.contact_id = contact_id;
        }


    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getIschecked() {
        return ischecked;
    }

    public void setIschecked(int ischecked) {
        this.ischecked = ischecked;
    }

    public String getContact_id() {
        return contact_id;
    }

    public void setContact_id(String contact_id) {
        this.contact_id = contact_id;
    }
}
