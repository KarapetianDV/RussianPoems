package com.example.davit.poems;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;


class PoetsAdapter extends RecyclerView.Adapter<PoetsAdapter.ViewHolder> implements Filterable {

    private ArrayList<String> poetsNames;
    private ArrayList<String> filteredList;
    private PoetsFilter poetsFilter;

    public PoetsAdapter(ArrayList<String> poetsNames) {
        this.poetsNames = poetsNames;
        this.filteredList = poetsNames;

        getFilter();
    }

    @Override
    public PoetsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PoetsAdapter.ViewHolder holder, int position) {
        holder.text1.setText(filteredList.get(position));
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    @Override
    public Filter getFilter() {
        if (poetsFilter == null)
            poetsFilter = new PoetsFilter();

        return poetsFilter;
    }

    private class PoetsFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {
                ArrayList<String> tempList = new ArrayList<>();

                for (String poet : poetsNames) {
                    if (poet.toLowerCase().contains(constraint.toString().toLowerCase()))
                        tempList.add(poet);
                }

                results.values = tempList;
                results.count = tempList.size();
            } else {
                results.values = poetsNames;
                results.count = poetsNames.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredList = (ArrayList<String>) results.values;
            notifyDataSetChanged();
        }
    }

    private class PoetsFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {
                ArrayList<String> tempList = new ArrayList<>();

                for (String poet : poetsNames) {
                    if (poet.toLowerCase().contains(constraint.toString().toLowerCase()))
                        tempList.add(poet);
                }

                results.values = tempList;
                results.count = tempList.size();
            } else {
                results.values = poetsNames;
                results.count = poetsNames.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredList = (ArrayList<String>) results.values;
            notifyDataSetChanged();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView text1;

        public ViewHolder(View itemView) {
            super(itemView);
            text1 = (TextView) itemView.findViewById(R.id.text1);
        }
    }
}