package com.hit.zhou.scanmachine.ui.main.home;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hit.zhou.scanmachine.R;
import com.hit.zhou.scanmachine.common.Machine;
import com.hit.zhou.scanmachine.ui.MachineDetailActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by zhou on 2018/11/14.
 */

public class MachineAdapter extends RecyclerView.Adapter<MachineAdapter.ViewHolder> {
    private ArrayList<Machine> machines;

    public MachineAdapter(ArrayList<Machine> machines){
        this.machines = machines;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.machine_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(parent.getContext(),MachineDetailActivity.class);
                parent.getContext().startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.machineName.setText(machines.get(position).getMachineName());
        holder.machineRank.setText(machines.get(position).getRank());
        holder.machineHighScore.setText(machines.get(position).getScore());
        //:TODO 图片加载
        Picasso.get().load(machines.get(position).getImageUrl())
                .error(R.color.white)
                .placeholder(R.color.white)
                .into(holder.machineImage);
    }

    @Override
    public int getItemCount() {
        return machines.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView machineImage;
        private TextView machineHighScore;
        private TextView machineRank;
        private TextView machineName;
        private ViewHolder(View itemView) {
            super(itemView);
            machineName = itemView.findViewById(R.id.machineName);
            machineImage = itemView.findViewById(R.id.machineImage);
            machineHighScore = itemView.findViewById(R.id.historyScore);
            machineRank = itemView.findViewById(R.id.rank);
        }
    }

}
