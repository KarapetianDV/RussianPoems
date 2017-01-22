package com.example.davit.poems;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


class PoemsAdapter extends RecyclerView.Adapter<PoemsAdapter.ViewHolder> {

    private ArrayList<String> authorsList;

    public PoemsAdapter(ArrayList<String> list) {
        this.authorsList = list;
    }

    @Override
    public PoemsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PoemsAdapter.ViewHolder holder, int position) {
        holder.text1.setText(authorsList.get(position));
    }

    @Override
    public int getItemCount() {
        return authorsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView text1;


        public ViewHolder(View itemView) {
            super(itemView);
            text1 = (TextView) itemView.findViewById(R.id.text1);
        }
    }
}