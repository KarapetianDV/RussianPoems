package com.example.davit.poems;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


class PoetsAdapter extends RecyclerView.Adapter<PoetsAdapter.ViewHolder> {

    private ArrayList<String> poetsNames;

    public PoetsAdapter(ArrayList<String> poetsNames) {
        this.poetsNames = poetsNames;
    }

    @Override
    public PoetsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PoetsAdapter.ViewHolder holder, int position) {
        holder.text1.setText(poetsNames.get(position));
    }

    @Override
    public int getItemCount() {
        return poetsNames.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView text1;

        public ViewHolder(View itemView) {
            super(itemView);
            text1 = (TextView) itemView.findViewById(R.id.text1);
        }
    }
}