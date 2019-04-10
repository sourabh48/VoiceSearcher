package com.example.voicesearcher;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    Context context;
    List<DataFile> MainImageUploadInfoList;

    public RecyclerViewAdapter(Context context, List<DataFile> TempList) {
        this.MainImageUploadInfoList = TempList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DataFile Details = MainImageUploadInfoList.get(position);
        holder.PlaceNameTextView.setText(Details.getName());
        holder.PlaceAddressTextView.setText(Details.getAddress());
    }

    @Override
    public int getItemCount()
    {
        return MainImageUploadInfoList.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView PlaceNameTextView;
        public TextView PlaceAddressTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            PlaceNameTextView = (TextView) itemView.findViewById(R.id.vname);
            PlaceAddressTextView = (TextView) itemView.findViewById(R.id.address);
        }
    }
}