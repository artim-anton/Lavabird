package com.artimanton.lavabird.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.artimanton.lavabird.R;
import com.artimanton.lavabird.model.NotifEntity;

import java.util.ArrayList;
import java.util.List;

public class NotifAdapter extends RecyclerView.Adapter<NotifAdapter.RecordViewHolder> {

    private List<NotifEntity> list;

    public NotifAdapter(List<NotifEntity> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public RecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecordViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item_notif, parent, false));
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull RecordViewHolder holder, int position) {
        final NotifEntity notifEntity = list.get(position);

        holder.etPack.setText(notifEntity.packages);
        holder.etText.setText(notifEntity.text);
        holder.etData.setText(notifEntity.date);
        holder.etTime.setText(notifEntity.time);

    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    class RecordViewHolder extends RecyclerView.ViewHolder{
        private TextView etPack, etText,  etTime, etData;

        private RecordViewHolder(View itemView) {
            super(itemView);
            etPack = itemView.findViewById(R.id.et_pack);
            etText = itemView.findViewById(R.id.et_text);
            etData = itemView.findViewById(R.id.et_date);
            etTime = itemView.findViewById(R.id.et_time);
        }
    }
}
