package com.example.rohit.arishit_f.presentation.tools;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.rohit.arishit_f.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PresentationTools extends RecyclerView.Adapter<PresentationTools.ViewHolder> {

    private List<ToolModel> mToolList = new ArrayList<>();
    private PresentationTools.OnItemSelected mOnItemSelected;



    public PresentationTools(PresentationTools.OnItemSelected onItemSelected) {
        mOnItemSelected = onItemSelected;
        mToolList.add(new PresentationTools.ToolModel("Mike", R.drawable.ic_keyboard_voice_black_24dp, ToolType.MIKE));
        mToolList.add(new PresentationTools.ToolModel("Chat", R.drawable.ic_chat_black_24dp, ToolType.CHAT));
        mToolList.add(new PresentationTools.ToolModel("Stop", R.drawable.ic_clear_black_24dp, ToolType.STOP));
        mToolList.add(new PresentationTools.ToolModel("Attach", R.drawable.ic_attach_file_black_24dp, ToolType.ATTACH));
        mToolList.add(new PresentationTools.ToolModel("Mute", R.drawable.ic_volume_off_black_24dp, ToolType.MUTE));
    }

    public interface OnItemSelected {
        void onToolSelected(ToolType toolType);
    }

    class ToolModel {
        private String mToolName;
        private int mToolIcon;
        private ToolType mToolType;

        ToolModel(String toolName, int toolIcon, ToolType toolType) {
            mToolName = toolName;
            mToolIcon = toolIcon;
            mToolType = toolType;
        }

    }
    @NonNull
    @Override
    public PresentationTools.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.presentation_row_editing_tools, parent, false);
        return new PresentationTools.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PresentationTools.ViewHolder holder, int position) {
        PresentationTools.ToolModel item = mToolList.get(position);
        holder.txtTool.setText(item.mToolName);
        holder.imgToolIcon.setImageResource(item.mToolIcon);
    }

    @Override
    public int getItemCount() {
        return mToolList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgToolIcon;
        TextView txtTool;

        ViewHolder(View itemView) {
            super(itemView);
            imgToolIcon = itemView.findViewById(R.id.imgToolIcon);
            txtTool = itemView.findViewById(R.id.txtTool);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemSelected.onToolSelected(mToolList.get(getLayoutPosition()).mToolType);
                }
            });
        }
    }


}
