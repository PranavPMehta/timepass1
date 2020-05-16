package com.example.rohit.arishit_f.login.chat;

import android.graphics.Bitmap;

/**
 * Created by sreejeshpillai on 10/05/15.
 */
public class Message {

    public static final int TYPE_MESSAGE = 0;
    public static final int TYPE_LOG = 1;
    public static final int TYPE_ACTION = 2;


    private int mType;
    public String mMessage;
    private Bitmap mImage;
    private String Sender;
    private String UniqueId;
    public String location;
    String datatype;
    String username,timestamp,sender_id;
    public boolean isSend;
    private boolean isSelected=false;
    private boolean isImportant;


    private Message(String mMessage, String loc,boolean isSend) {
        this.mMessage=mMessage;
        this.isSend=isSend;
        this.location = loc;
    }
private Message(String mMessage, String loc, boolean isSend,String username,String datatype,String timestamp,boolean imp) {
//    private Message(String mMessage,String username,String timestamp, String loc,boolean isSend,String datatype) {
        this.mMessage=mMessage;
        this.username=username;
        this.timestamp=timestamp;
        this.isSend=isSend;
        this.location = loc;
        this.datatype=datatype;
        this.isImportant=imp;
        System.out.println("In MMessage:"+mMessage);
    }

    private Message(String mMessage, String loc, boolean isSend,String username,String datatype,String timestamp,boolean imp,String sender_id) {
//    private Message(String mMessage,String username,String timestamp, String loc,boolean isSend,String datatype) {
        this.mMessage=mMessage;
        this.username=username;
        this.timestamp=timestamp;
        this.isSend=isSend;
        this.location = loc;
        this.datatype=datatype;
        this.isImportant=imp;
        this.sender_id=sender_id;
        System.out.println("In MMessage:"+mMessage);
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public boolean isImportant() {
        return isImportant;
    }

    public void setImportant(boolean important) {
        isImportant = important;
    }

    public void setSelected(boolean selected)
    {
        isSelected=selected;
    }

    public boolean isSelected(){
        return isSelected;
    }

    private Message(){}

    public int getType() {
        return mType;
    };

    public String getMessage() {
        return mMessage;
    };

    public String getLocation() { return location; }

    public void setLocation(String location) { this.location = location;  }

    public void setmMessage(String mMessage) {
        this.mMessage = mMessage;
    }

    public boolean isSend() {
        return isSend;
    }

    public void setSend(boolean send) {
        isSend = send;
    }

    public String getmMessage() {
        return mMessage;
    }

    public Bitmap getImage() {
        return mImage;
    };

    public String getUniqueId() {
        return UniqueId;
    }

    public void setUniqueId(String uniqueId) {
        UniqueId = uniqueId;
    }

    public String getSender(){
        return Sender;
    }
    public void setSender(String Sender){
        this.Sender=Sender;
    }

    public String getDatatype(){
        return datatype;
    }
    public void setDatatype(String Sender){
        this.datatype=datatype;
    }

    public String getUsername() {
        return username;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public static class Builder {
        private final int mType;
        private Bitmap mImage;
        private String locationStateCountry;
        private String mMessage;
        private boolean isSend;
        private String mUniqueId;
        String username,timestamp,datatype,sender_id;
        private boolean isImportant;

        public Builder(int type) {
            mType = type;
        }

        public Builder image(Bitmap image) {
            mImage = image;
            return this;
        }

        public Builder message(String message) {
            mMessage = message;
            return this;
        }

        public Builder datatype(String datatype) {
            this.datatype = datatype;
            return this;
        }

        public Builder isSend(boolean isSend) {
            this.isSend=isSend;
            return this;
        }

        public Builder isImportant(boolean important) {
            this.isImportant=important;
            return this;
        }

        public Builder location(String loc) {
            locationStateCountry = loc;
            return this;
        }

        public Builder username(String username) {
            this.username=username;
            return this;
        }

        public Builder uniqueId(String id) {
            mUniqueId = id;
            return this;
        }

        public Builder timestamp(String timestamp) {
            this.timestamp=timestamp;
            return this;
        }

        public Builder sender_id(String sender_id)
        {
            this.sender_id=sender_id;
            return this;
        }

        public Message build() {
            Message message = new Message();
            message.mType = mType;
            message.isSend=isSend;
            message.mImage = mImage;
            message.mMessage = mMessage;
            message.UniqueId = mUniqueId;
            message.location = locationStateCountry;
            message.timestamp=timestamp;
            message.username=username;
            message.datatype=datatype;
            message.sender_id=sender_id;
            return message;
        }
    }
}