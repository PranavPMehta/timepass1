package com.example.rohit.arishit_f.vault;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rohit.arishit_f.R;
import com.example.rohit.arishit_f.login.chat.Message;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class vMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int MESSAGE_RIGHT = 1;
    private static final int MESSAGE_LEFT = 2;
    private List<Message> mMessages;
    private int[] mUsernameColors;
    private Context context;
    private LayoutInflater layoutInflater;
    private com.example.rohit.arishit_f.vault.vault_chat vault_chat;

    public vMessageAdapter(List<Message> messages) {
        mMessages = messages;
        //  mUsernameColors = context.getResources().getIntArray(R.array.username_colors);
    }

    public vMessageAdapter(List<Message> messages, Context context) {
        mMessages = messages;
        this.context = context;
        vault_chat = (com.example.rohit.arishit_f.vault.vault_chat) context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //  mUsernameColors = context.getResources().getIntArray(R.array.username_colors);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == MESSAGE_LEFT) { // for call layout
            view = LayoutInflater.from(context).inflate(R.layout.vault_chat_messagelayout, parent, false);
            return new LeftMessageViewHolder(view);

        } else { // for email layout
            view = LayoutInflater.from(context).inflate(R.layout.vault_chat_messagelayout, parent, false);
            return new RightMessageViewHolder(view, vault_chat);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        Message message = mMessages.get(i);
        System.out.println("LOGTIME"+message.getTimestamp());
        if (getItemViewType(i) == MESSAGE_LEFT) {
            ((LeftMessageViewHolder) viewHolder).setMessage(message.getMessage(), message.getUsername(), message.getTimestamp());
        } else {
            ((RightMessageViewHolder) viewHolder).setMessage(message.getMessage(), message.getUsername(), message.getTimestamp());
        }
    }
//vaultmessaage open kr directly
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

        private TextView mMessageView, mUsernameView, mTimestampView;

        public LeftMessageViewHolder(View itemView) {
            super(itemView);
            mMessageView = (TextView) itemView.findViewById(R.id.tvMessage);
            mUsernameView=(TextView)itemView.findViewById(R.id.tvUser);
            mTimestampView=(TextView)itemView.findViewById(R.id.tvTimeStamp);
        }

        public void setMessage(String message, String username, String timestamp) {
            if (null == mMessageView) return;
            if (null == message) return;
            mMessageView.setText(message);
            mUsernameView.setText(username);
            mTimestampView.setText(timestamp);
        }
    }

    class RightMessageViewHolder extends RecyclerView.ViewHolder {
        com.example.rohit.arishit_f.vault.vault_chat vault_chat;

        private TextView mMessageView, mUsernameView, mTimestampView;

        public RightMessageViewHolder(View itemView, com.example.rohit.arishit_f.vault.vault_chat vault_chat) {
            super(itemView);
            this.vault_chat = vault_chat;
            mMessageView = (TextView) itemView.findViewById(R.id.tvMessage);
            mUsernameView=(TextView)itemView.findViewById(R.id.tvUser);
            mTimestampView=(TextView)itemView.findViewById(R.id.tvTimeStamp);
        }

        public void setMessage(String message, String username, String timestamp) {
            if (null == mMessageView) return;
            if (null == message) return;
            mMessageView.setText(message);
            mUsernameView.setText(username);
            mTimestampView.setText(timestamp);
        }
    }
}