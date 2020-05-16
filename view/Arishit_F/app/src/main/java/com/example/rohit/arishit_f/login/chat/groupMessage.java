package com.example.rohit.arishit_f.login.chat;

import android.graphics.Bitmap;

public class groupMessage {

    public static final int TYPE_MESSAGE = 0;
    public static final int TYPE_LOG = 1;
    public static final int TYPE_ACTION = 2;


    private int mType;
    public String mMessage;
    private Bitmap mImage;
    private String Sender;
    private String mUniqueId;
    public String location;
    String datatype;
    String username, timestamp,sender_id;
    public boolean isSend;
    private boolean isSelected = false;
    private boolean isImportant;

    private boolean mispolled;
    private int mupvote;
    private int mdownvote;
    private int mneutral;

    private groupMessage(String mMessage, String loc, boolean isSend, String username, boolean ispolled, int upvote, int downvote, int neutral) {
        this.mMessage = mMessage;
        this.isSend = isSend;
        this.location = loc;
        this.mispolled = ispolled;
        this.mdownvote = downvote;
        this.mupvote = upvote;
        this.mneutral = neutral;
    }

    private groupMessage(String mMessage, String loc, boolean isSend, String username, String datatype, String timestamp, boolean imp, boolean ispolled, int upvote, int downvote, int neutral, String UniqueId) {
//    private Message(String mMessage,String username,String timestamp, String loc,boolean isSend,String datatype) {
        this.mMessage = mMessage;
        this.username = username;
        this.timestamp = timestamp;
        this.isSend = isSend;
        this.location = loc;
        this.datatype = datatype;
        this.isImportant = imp;
        this.mispolled = ispolled;
        this.mdownvote = downvote;
        this.mupvote = upvote;
        this.mneutral = neutral;
        this.mUniqueId = UniqueId;
        System.out.println("In MMessage:" + mMessage);
    }

    public boolean isImportant() {
        return isImportant;
    }

    public void setImportant(boolean important) {
        isImportant = important;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    private groupMessage() {
    }

    public int getType() {
        return mType;
    }

    ;

    public String getMessage() {
        return mMessage;
    }

    ;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

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
    }

    ;

    public String getUniqueId() {
        return mUniqueId;
    }

    public void setUniqueId(String uniqueId) {
        mUniqueId = uniqueId;
    }

    public String getSender() {
        return Sender;
    }

    public void setSender(String Sender) {
        this.Sender = Sender;
    }

    public String getDatatype() {
        return datatype;
    }

    public void setDatatype(String Sender) {
        this.datatype = datatype;
    }

    public String getUsername() {
        return username;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public boolean isMispolled() {
        return mispolled;
    }

    public void setMispolled(boolean mispolled) {
        this.mispolled = mispolled;
    }

    public int getMupvote() {
        return mupvote;
    }

    public void setMupvote(int mupvote) {
        this.mupvote = mupvote;
    }

    public int getMdownvote() {
        return mdownvote;
    }

    public void setMdownvote(int mdownvote) {
        this.mdownvote = mdownvote;
    }

    public int getMneutral() {
        return mneutral;
    }
    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public void setMneutral(int mneutral) {
        this.mneutral = mneutral;
    }

    public static class Builder {
        private final int mType;
        private Bitmap mImage;
        private String locationStateCountry;
        private String mMessage;
        private boolean isSend;
        private String mUniqueId;
        String username, timestamp, datatype, sender_id;
        private boolean isImportant;
        private boolean mispolled;
        private int mupvote;
        private int mdownvote;
        private int mneutral;


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
            this.isSend = isSend;
            return this;
        }

        public Builder isImportant(boolean important) {
            this.isImportant = important;
            return this;
        }

        public Builder location(String loc) {
            locationStateCountry = loc;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder uniqueId(String id) {
            mUniqueId = id;
            return this;
        }

        public Builder timestamp(String timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder ispolled(boolean ispolled) {
            this.mispolled = ispolled;
            return this;
        }

        public Builder upvote(int upvote) {
            this.mupvote = upvote;
            return this;
        }

        public Builder downvote(int downvote) {
            this.mdownvote = downvote;
            return this;
        }

        public Builder neutral(int neutral) {
            this.mneutral = neutral;
            return this;
        }

        public Builder sender_id(String sender_id) {
            this.sender_id = sender_id;
            return this;
        }

        public groupMessage build() {
            groupMessage message = new groupMessage();
            message.mType = mType;
            message.isSend = isSend;
            message.mImage = mImage;
            message.mMessage = mMessage;
            message.mUniqueId = mUniqueId;
            message.location = locationStateCountry;
            message.timestamp = timestamp;
            message.username = username;
            message.datatype = datatype;
            message.mispolled = mispolled;
            message.mupvote = mupvote;
            message.mdownvote = mdownvote;
            message.mneutral = mneutral;
            message.sender_id = sender_id;
            return message;
        }

    }
}

