package com.example.davit.poems;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.davit.poems.data.DbHelper;

import java.util.concurrent.ExecutionException;

import static com.example.davit.poems.data.PoemsAppContract.PoetsEntry.COLUMN_NAME;


class PoetsAdapter extends RecyclerView.Adapter<PoetsAdapter.ViewHolder> {

    private Cursor poetsNames;

    public PoetsAdapter(DbHelper dbHelper, Object[] queryOptions) throws ExecutionException, InterruptedException {
        // скидываем в другой поток, для предотвращения тормозов UI
        this.poetsNames = new GetCursorTask(dbHelper).execute(queryOptions).get();
    }

    @Override
    public PoetsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PoetsAdapter.ViewHolder holder, int position) {
        poetsNames.moveToPosition(position);
        holder.text1.setText(poetsNames.getString(poetsNames.getColumnIndex(COLUMN_NAME)));
    }

    @Override
    public int getItemCount() {
        return poetsNames.getCount();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView text1;

        public ViewHolder(View itemView) {
            super(itemView);
            text1 = (TextView) itemView.findViewById(R.id.text1);
        }
    }
}