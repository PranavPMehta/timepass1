package com.example.rohit.arishit_f.login.chat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rohit.arishit_f.R;
import com.example.rohit.arishit_f.login.chat.GoogleMapsActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;


public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int MESSAGE_RIGHT = 1;
    private static final int MESSAGE_LEFT = 2;
    public int flag;
    private List<Message> mMessages;
    private int[] mUsernameColors;
    private Context context;
    private LayoutInflater layoutInflater;
    private ChatPage chatPage;

    public MessageAdapter(List<Message> messages) {
        mMessages = messages;
        //  mUsernameColors = context.getResources().getIntArray(R.array.username_colors);

    }

    public MessageAdapter(List<Message> messages, Context context) {
        mMessages = messages;
        this.context = context;
        System.out.println("Context00" + context);
        chatPage = (ChatPage) context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //  mUsernameColors = context.getResources().getIntArray(R.array.username_colors);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == MESSAGE_LEFT) { // for call layout
            view = LayoutInflater.from(context).inflate(R.layout.left_message, parent, false);
            return new LeftMessageViewHolder(view);

        } else { // for email layout
            view = LayoutInflater.from(context).inflate(R.layout.right_message, parent, false);
            return new RightMessageViewHolder(view, chatPage);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        Message message = mMessages.get(i);
        System.out.println("Datatype" + message.getDatatype());
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
//                Toast.makeText(getContext(), i, Toast.LENGTH_SHORT).show();
                System.out.println("Long Click  ..  "+i);
                chatPage.onLongClick(view,i,message.getDatatype());
                return true;
            }
        });
        if (getItemViewType(i) == MESSAGE_LEFT) {
            ((LeftMessageViewHolder) viewHolder).setMessage(message.getMessage(), message.getDatatype(), message.getTimestamp(),message.isImportant());
            ((LeftMessageViewHolder) viewHolder).setLocation(message.getLocation());
            ((LeftMessageViewHolder) viewHolder).setImage(message.getImage());
        } else {
            ((RightMessageViewHolder) viewHolder).setMessage(message.getMessage(), message.getDatatype(), message.getTimestamp(),message.isImportant());
            ((RightMessageViewHolder) viewHolder).setImage(message.getImage());
        }
    }

    //    @Override
//    public void onBindViewHolder(ViewHolder viewHolder, int position) {
//        Message message = mMessages.get(position);
//        viewHolder.setMessage(message.getMessage());
//        viewHolder.setImage(message.getImage());
//
//    }
    public Context getContext() {
        return context;
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mMessages.get(position).isSend()) {
            return MESSAGE_RIGHT;
        } else
            return MESSAGE_LEFT;
    }

    public void removeData(ArrayList<Message> list) {
        for (Message model : list) {
            mMessages.remove(model);
        }
        notifyDataSetChanged();
    }

    class LeftMessageViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImageView,mImportantImageView;
        private TextView mMessageView, mLocationView, mTimeStamp;

        public LeftMessageViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.image);
            mMessageView = (TextView) itemView.findViewById(R.id.message);
            mTimeStamp = (TextView) itemView.findViewById(R.id.timer);
            mLocationView = (TextView) itemView.findViewById(R.id.locationStateCountry);
            mImportantImageView = (ImageView)itemView.findViewById(R.id.imgvImpMsgL);

        }

        public void setMessage(String message, String datatype, String time,Boolean isImportant) {
            if (null == mMessageView) return;
            if (null == message) return;
            System.out.println("In Left Message view"+message);  //now run and check
            time = time.substring(0, 5);
            mTimeStamp.setText(time);
            System.out.println("MEssage Left" + message);
            System.out.println("isImportant" + isImportant);
            if(isImportant){
                mImportantImageView.setVisibility(View.VISIBLE);
            }
            else{
                mImportantImageView.setVisibility(View.INVISIBLE);
            }

            if (datatype.equals("MESSAGE")) {
                mImageView.setVisibility(View.INVISIBLE);
            } else if (datatype.equals("FILES")) {
                mImageView.setBackgroundResource(R.drawable.ic_file);
                mImageView.setVisibility(View.VISIBLE);
            } else if (datatype.equals("VIDEO")) {
                mImageView.setBackgroundResource(R.drawable.ic_video);
                mImageView.setVisibility(View.VISIBLE);
            } else if (datatype.equals("IMAGE")) {
                mImageView.setBackgroundResource(R.drawable.ic_image);
                mImageView.setVisibility(View.VISIBLE);
            }
            mMessageView.setText(message);
            mMessageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chatPage.displayFile(message);
                }
            });
        }

        public void setLocation(String loc) {
            if (null == mLocationView) return;
            if (null == loc) return;
            mLocationView.setText(loc);
            mLocationView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent gmap = new Intent(context, GoogleMapsActivity.class);
                    gmap.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(gmap);
                }
            });
        }

        public void setImage(Bitmap bmp) {
            if (null == mImageView) return;
            if (null == bmp) return;
            System.out.println("Image Left");
            mImageView.setImageBitmap(bmp);

            bmp = null;
        }

        private int getUsernameColor(String username) {
            int hash = 7;
            for (int i = 0, len = username.length(); i < len; i++) {
                hash = username.codePointAt(i) + (hash << 5) - hash;
            }
            int index = Math.abs(hash % mUsernameColors.length);
            return mUsernameColors[index];
        }
    }

    public class RightMessageViewHolder extends RecyclerView.ViewHolder {
        public TextView mMessageView, mTimeStamp;
        ChatPage chatPage;
        private ImageView mImageView, unsentImage,mImportantImgV;
        private Toolbar toolbar;

        public RightMessageViewHolder(View itemView, ChatPage chatPage) {
            super(itemView);
            this.chatPage = chatPage;
            mImageView = (ImageView) itemView.findViewById(R.id.image);
            mTimeStamp = (TextView) itemView.findViewById(R.id.timer);
            unsentImage = (ImageView) itemView.findViewById(R.id.read_receipts);
            mMessageView = (TextView) itemView.findViewById(R.id.message);
            toolbar = (Toolbar) itemView.findViewById(R.id.toolbar);
            mImportantImgV = (ImageView)itemView.findViewById(R.id.imgvImpMsgR);
//            mMessageView.setOnLongClickListener(chatPage);
        }

        public void setMessage(String message, String datatype, String time,Boolean isImportant) {
            if (null == mMessageView) return;
            if (null == message) return;
            System.out.println("In Right Message view"+message);  //now run and check
            time = time.substring(0, 5);
            mMessageView.setText(message);
            mTimeStamp.setText(time);
            if(isImportant){
                mImportantImgV.setVisibility(View.VISIBLE);
            }else{
                mImportantImgV.setVisibility(View.INVISIBLE);
            }
            if (datatype.equals("MESSAGE")) {
                mImageView.setVisibility(View.INVISIBLE);
            } else if (datatype.equals("FILES")) {
                mImageView.setBackgroundResource(R.drawable.ic_file);
                mImageView.setVisibility(View.VISIBLE);
            } else if (datatype.equals("VIDEO")) {
                mImageView.setBackgroundResource(R.drawable.ic_video);
                mImageView.setVisibility(View.VISIBLE);
            } else if (datatype.equals("IMAGE")) {
                mImageView.setBackgroundResource(R.drawable.ic_image);
                mImageView.setVisibility(View.VISIBLE);
            }

            System.out.println("msg_received" + chatPage.msg_received);
            System.out.println("msg_sent" + chatPage.msg_sent);

            if (chatPage.msg_received && chatPage.check_online)   //previous chat condition just need to add api to check wether message has isreceived variable true
            {
                System.out.println("IN");
                unsentImage.setBackgroundResource(R.drawable.ic_check_circle_green_24dp);
                unsentImage.setVisibility(View.VISIBLE);
                System.out.println("DONE1");
            } else if (chatPage.msg_sent && chatPage.check_online)      //message sent when sender is online and message goes to remote database confirmation
            {
                unsentImage.setBackgroundResource(R.drawable.ic_check_circle_black_24dp);
                unsentImage.setVisibility(View.VISIBLE);
                System.out.println("DONE2");
            }else{
                unsentImage.setBackgroundResource(R.drawable.ic_access_time_black_24dp);
                unsentImage.setVisibility(View.VISIBLE);
                System.out.println("DONE3");
            }

            message = null;
        }

        public void setImage(Bitmap bmp) {
            if (null == mImageView) return;
            if (null == bmp) return;
            mImageView.setImageBitmap(bmp);
            bmp = null;
        }

        private int getUsernameColor(String username) {
            int hash = 7;
            for (int i = 0, len = username.length(); i < len; i++) {
                hash = username.codePointAt(i) + (hash << 5) - hash;
            }
            int index = Math.abs(hash % mUsernameColors.length);
            return mUsernameColors[index];
        }
    }
}