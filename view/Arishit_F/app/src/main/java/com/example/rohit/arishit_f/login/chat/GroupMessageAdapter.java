package com.example.rohit.arishit_f.login.chat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rohit.arishit_f.R;
import com.example.rohit.arishit_f.login.MessageResult;
import com.example.rohit.arishit_f.login.chat.GoogleMapsActivity;
import com.example.rohit.arishit_f.retrofit.IMyService;
import com.example.rohit.arishit_f.retrofit.RetrofitClient;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class GroupMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int MESSAGE_RIGHT = 1;
    private static final int MESSAGE_LEFT = 2;
    public int flag;
    private List<groupMessage> mMessages;
    private int[] mUsernameColors;
    private Context context;
    private LayoutInflater layoutInflater;
    private GroupChat groupchat;
    private Retrofit retrofitClient = RetrofitClient.getInstance();
    private IMyService iMyService = retrofitClient.create(IMyService .class);

    public GroupMessageAdapter(List<groupMessage> messages) {
        mMessages = messages;
        //  mUsernameColors = context.getResources().getIntArray(R.array.username_colors);
    }

    public GroupMessageAdapter(List<groupMessage> messages, Context context) {
        mMessages = messages;
        this.context = context;
        System.out.println("Context00" + context);
        groupchat = (GroupChat) context;
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
            return new RightMessageViewHolder(view, groupchat);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        groupMessage message = mMessages.get(i);
        System.out.println("Datatype" + message.getTimestamp());
        System.out.println(getItemViewType(i));

        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
//                Toast.makeText(getContext(), i, Toast.LENGTH_SHORT).show();
                System.out.println("Long Click  ..  "+i);
                groupchat.onLongClickListner(view,i);
                return true;
            }
        });

        if (getItemViewType(i) == MESSAGE_LEFT) {

            ((LeftMessageViewHolder) viewHolder).setMessage(message.getUniqueId(),message.getMessage(), message.getDatatype(), message.getTimestamp(),message.isMispolled(),message.getMupvote(),message.getMdownvote(),message.getMneutral(),message.getSender_id());
            ((LeftMessageViewHolder) viewHolder).setLocation(message.getLocation());
            ((LeftMessageViewHolder) viewHolder).setImage(message.getImage());
        } else {
            ((RightMessageViewHolder) viewHolder).setMessage(message.getUniqueId(),message.getMessage(), message.getDatatype(), message.getTimestamp(),message.isMispolled(),message.getMupvote(),message.getMdownvote(),message.getMneutral());
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

        private ImageView mImageView,mlupvote,mldownvote,mlneutralvote;
        private TextView mMessageView, mLocationView, mTimeStamp,mlupcount,mldowncount,mlneutralcount,mSenderId;


        public LeftMessageViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.image);
            mMessageView = (TextView) itemView.findViewById(R.id.message);
            mTimeStamp = (TextView) itemView.findViewById(R.id.timer);

            mSenderId=(TextView)itemView.findViewById(R.id.sender_name);
            mlupvote = (ImageView) itemView.findViewById(R.id.left_upvote);
            mldownvote = (ImageView) itemView.findViewById(R.id.left_downvote);
            mlneutralvote = (ImageView) itemView.findViewById(R.id.left_neutralvote);
            mlupcount = (TextView) itemView.findViewById(R.id.left_upvotecount);
            mldowncount = (TextView) itemView.findViewById(R.id.left_downvotecount);
            mlneutralcount = (TextView) itemView.findViewById(R.id.left_neutralcount);
            mLocationView = (TextView) itemView.findViewById(R.id.locationStateCountry);
        }

        public void setMessage(String uniqueid,String message, String datatype, String time,Boolean lispolled,int lupvote,int ldownvote,int lneutral,String sender_id) {
            if (null == mMessageView) return;
            if (null == message) return;
            time = time.substring(0, 5);
            mTimeStamp.setText(time);
            System.out.println("In Message");
            mSenderId.setVisibility(View.VISIBLE);
            mSenderId.setText(sender_id);
            System.out.println("MEssage Left" + message);
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
                    groupchat.displayFile(message);
                }
            });

            Log.v("left ispolled",lispolled.toString());


            if(lispolled){
                mlupcount.setVisibility(View.VISIBLE);
                mlneutralcount.setVisibility(View.VISIBLE);
                mldowncount.setVisibility(View.VISIBLE);

                mlupcount.setText(Integer.toString(lupvote));
                mldowncount.setText(Integer.toString(ldownvote));
                mlneutralcount.setText(Integer.toString(lneutral));

                mlupvote.setVisibility(View.VISIBLE);
                mlupvote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        groupchat.setUpVote(uniqueid,mlupcount);
                    }
                });

                mldownvote.setVisibility(View.VISIBLE);
                mldownvote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        groupchat.setDownVote(uniqueid,mldowncount);
                    }
                });

                mlneutralvote.setVisibility(View.VISIBLE);
                mlneutralvote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        groupchat.setNeutralVote(uniqueid,mlneutralcount);
                    }
                });

            }

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
        public TextView mMessageView, mTimeStamp,mupcount,mdowncount,mneutralcount;
        GroupChat chatPage;
        private ImageView mImageView, unsentImage,mupvote,mdownvote,mneutralvote;
        private Toolbar toolbar;

        public RightMessageViewHolder(View itemView, GroupChat chatPage) {
            super(itemView);
            this.chatPage = chatPage;
            mImageView = (ImageView) itemView.findViewById(R.id.image);
            mTimeStamp = (TextView) itemView.findViewById(R.id.timer);
            unsentImage = (ImageView) itemView.findViewById(R.id.read_receipts);
            mMessageView = (TextView) itemView.findViewById(R.id.message);
            toolbar = (Toolbar) itemView.findViewById(R.id.toolbar);
            mupvote = (ImageView) itemView.findViewById(R.id.right_upvote);
            mdownvote = (ImageView) itemView.findViewById(R.id.right_downvote);
            mneutralvote = (ImageView) itemView.findViewById(R.id.right_neutralvote);
            mupcount = (TextView) itemView.findViewById(R.id.right_upvotecount);
            mdowncount = (TextView) itemView.findViewById(R.id.right_downvotecount);
            mneutralcount = (TextView) itemView.findViewById(R.id.right_neutralcount);
        }

        public void setMessage(String uniqueid,String message, String datatype, String time,Boolean rispolled,int rupvote,int rdownvote,int rneutral) {

            if (null == mMessageView) return;
            if (null == message) return;
            time = time.substring(0, 5);
            mMessageView.setText(message);
            mTimeStamp.setText(time);
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

            Log.v("r ispolled",rispolled.toString());

            if(rispolled){
                mupcount.setVisibility(View.VISIBLE);
                mdowncount.setVisibility(View.VISIBLE);
                mneutralcount.setVisibility(View.VISIBLE);

                mupcount.setText(Integer.toString(rupvote));
                mdowncount.setText(Integer.toString(rdownvote));
                mneutralcount.setText(Integer.toString(rneutral));

                mupvote.setVisibility(View.VISIBLE);
                mupvote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        groupchat.setUpVote(uniqueid,mupcount);

                    }
                });

                mdownvote.setVisibility(View.VISIBLE);
                mdownvote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        groupchat.setDownVote(uniqueid,mdowncount);
                    }
                });

                mneutralvote.setVisibility(View.VISIBLE);
                mneutralvote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        groupchat.setNeutralVote(uniqueid,mneutralcount);
                    }
                });

            }

            unsentImage.setVisibility(View.VISIBLE);
            System.out.println("msg_received" + chatPage.msg_received);
            System.out.println("msg_sent" + chatPage.msg_sent);

            if (chatPage.msg_received && chatPage.check_online)   //previous chat condition just need to add api to check wether message has isreceived variable true
            {
                System.out.println("IN");
                unsentImage.setVisibility(View.INVISIBLE);
                unsentImage.setBackgroundResource(R.drawable.ic_check_circle_green_24dp);
                unsentImage.setVisibility(View.VISIBLE);
            } else if (chatPage.msg_sent && chatPage.check_online)      //message sent when sender is online and message goes to remote database confirmation
            {
                unsentImage.setVisibility(View.INVISIBLE);
                unsentImage.setBackgroundResource(R.drawable.ic_check_circle_black_24dp);
                unsentImage.setVisibility(View.VISIBLE);
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