package com.example.rohit.arishit_f.dashboard;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.rohit.arishit_f.R;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static com.example.rohit.arishit_f.constants.Constants.IP_ADDRESS;

public class ContactsAdapter extends ArrayAdapter<Contacts> {

    public ContactsAdapter(Activity context, ArrayList<Contacts> wordGroup){
        super(context,0,wordGroup);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.contacts_item, parent, false);
        }

        // Get the {@link AndroidFlavor} object located at this position in the list
        Contacts Defaultcontact = getItem(position);

        // Find the TextView in the list_item.xml layout with the ID version_name
        TextView mContactName = (TextView) listItemView.findViewById(R.id.contactName);
        TextView mDesignation = (TextView) listItemView.findViewById(R.id.contactDesignation);
        // Get the version name from the current AndroidFlavor object and
        // set this text on the name TextView
        mContactName.setText(Defaultcontact.getname());
        mDesignation.setText(Defaultcontact.getmDesignation());
//        String isOnline = Defaultcontact.getmIsOnline();
//        //getResources();
//        System.out.println("Printing Online or not : "+isOnline);


        //        if(isOnline.equals("true")) {
//            mContactName.setTextColor(ContextCompat.getColor(getContext(),R.color.green));
//        } else {
//            //mContactName.setTextColor(R.color.red_color_picker);
//            mContactName.setTextColor(ContextCompat.getColor(getContext(),R.color.red_color_picker));
//        }

//        System.out.println(" User name : "+ Defaultcontact.getmUserName()+ "   Is Group : "+ Defaultcontact.get_is_group());

        ImageView mContactImage= (ImageView) listItemView.findViewById(R.id.contactImage);

        String templink = Defaultcontact.getImageResId();
        System.out.println("Printing tempLink : "+templink);
        //Login using false crendentialsok
        //crash
        if(templink == null || templink == "1"){
            if (Defaultcontact.get_is_group())
                mContactImage.setImageResource(R.drawable.group_img_new);
            else {
                mContactImage.setImageResource(R.drawable.profile_img_new);
            }
        }else {
            Log.v("templink : ",templink.toString());
            try {
                if(Defaultcontact.get_is_group()){

                    Glide.with(getContext())
                            .load(IP_ADDRESS + ":3000" + templink)
                            .apply(
                                    new RequestOptions()
                                            .error(R.drawable.group_img_new)
                                            .centerCrop()
                            )
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    mContactImage.setImageResource(R.drawable.group_img_new);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    System.out.println(resource);
                                    return false;
                                }
                            })
                            .into(mContactImage);

                }else {

                Glide.with(getContext())
                        .load(IP_ADDRESS + ":3000" + templink)
                        .apply(
                                new RequestOptions()
                                        .error(R.drawable.profile_img_new)
                                        .centerCrop()
                        )
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                mContactImage.setImageResource(R.drawable.profile_img_new);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                System.out.println(resource);
                                return false;
                            }
                        })
                        .into(mContactImage);
                }

            }catch (Exception e){
                System.out.println(e);
            }
        }

        return listItemView;
    }

}
//apperaed