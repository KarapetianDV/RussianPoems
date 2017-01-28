package com.example.davit.poems;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class PoemsAdapter extends RecyclerView.Adapter<PoemsAdapter.ViewHolder> {

    private static final String TAG = PoemsAdapter.class.getSimpleName();
    private ArrayList<String> poemsNames;
    private String authorName;
    private RecyclerItemClickListener listener;

    public PoemsAdapter(ArrayList<String> poemsNames, String authorName, RecyclerItemClickListener listener) {
        this.poemsNames = poemsNames;
        this.authorName = authorName;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.poems_recycler_item, parent, false);
        final ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.poemName.setText(poemsNames.get(position));
        holder.poemAuthor.setText(authorName);
    }

    @Override
    public int getItemCount() {
        return poemsNames.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView poemName;
        TextView poemAuthor;

        public ViewHolder(View itemView) {
            super(itemView);
            poemName = (TextView) itemView.findViewById(R.id.poemName);
            poemAuthor = (TextView) itemView.findViewById(R.id.poemAuthor);
        }
    }
}
