package com.hit.zhou.scanmachine.ui.main.message;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hit.zhou.scanmachine.R;
import com.hit.zhou.scanmachine.common.MyMessage;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by zhou on 2018/11/18.
 */

public class MessageItemOneAdapter extends RecyclerView.Adapter<MessageItemOneAdapter.ViewHolder>{

    private ArrayList<MyMessage> list;

    public MessageItemOneAdapter(ArrayList<MyMessage> list){
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item1,parent,false);
        final ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Picasso.get().load(list.get(position).getMessageImageUrl())
                .placeholder(R.color.white)
                .error(R.color.white)
                .into(holder.messageImage);
        holder.name.setText(list.get(position).getMessageName());
        holder.content.setText(list.get(position).getMessageContent());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView messageImage;
        private TextView name;
        private TextView content;

        private ViewHolder(View itemView) {
            super(itemView);
            messageImage = itemView.findViewById(R.id.message_image);
            name = itemView.findViewById(R.id.message_name);
            content = itemView.findViewById(R.id.message_content);
        }
    }
}
