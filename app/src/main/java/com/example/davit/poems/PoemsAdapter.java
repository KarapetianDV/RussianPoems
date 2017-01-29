package com.example.davit.poems;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

public class PoemsAdapter extends RecyclerView.Adapter<PoemsAdapter.ViewHolder> implements Filterable {

    private static final String TAG = PoemsAdapter.class.getSimpleName();

    private ArrayList<String> poemsNames;
    private String authorName;
    private ArrayList<String> filteredList;
    private RecyclerItemClickListener listener;
    private PoemsFilter poemsFilter;

    public PoemsAdapter(ArrayList<String> poemsNames, String authorName, RecyclerItemClickListener listener) {
        this.poemsNames = poemsNames;
        this.filteredList = poemsNames;
        this.authorName = authorName;
        this.listener = listener;

        getFilter();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.poems_recycler_item, parent, false);
        final ViewHolder viewHolder = new ViewHolder(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, viewHolder.getAdapterPosition());
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.poemName.setText(filteredList.get(position));
        holder.poemAuthor.setText(authorName);
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    @Override
    public Filter getFilter() {
        if (poemsFilter == null)
            poemsFilter = new PoemsFilter();

        return poemsFilter;
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

    private class PoemsFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {
                ArrayList<String> tempList = new ArrayList<>();

                for (String poet : poemsNames) {
                    if (poet.toLowerCase().contains(constraint.toString().toLowerCase()))
                        tempList.add(poet);
                }

                results.values = tempList;
                results.count = tempList.size();
            } else {
                results.values = poemsNames;
                results.count = poemsNames.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredList = (ArrayList<String>) results.values;
            notifyDataSetChanged();
        }
    }
}
