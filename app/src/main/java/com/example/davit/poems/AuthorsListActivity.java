package com.example.davit.poems;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.davit.poems.data.PoemContract;
import com.example.davit.poems.data.PoemsOpenHelper;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class AuthorsListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    public static final String AUTHOR_INTENT_URL_TAG = "AUTHOR_INTENT_URL";
    public static final String AUTHOR_INTENT_NAME_TAG = "AUTHOR_INTENT_NAME";
    private static final String TAG = AuthorsListActivity.class.getSimpleName();

    private AuthorsListAdapter authorsListAdapter;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_authors);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(
                mRecyclerView.getContext(),
                layoutManager.getOrientation()));

        ArrayList<String> authorsList = new ArrayList<>();

        try {
            Cursor cursor = new QueryForAuthorList().execute().get();
            int columnIndex = cursor.getColumnIndex(PoemContract.PoemsEntry.COLUMN_AUTHOR);
            while (cursor.moveToNext()) {
                authorsList.add(cursor.getString(columnIndex));
            }
            cursor.close();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        authorsListAdapter = new AuthorsListAdapter(authorsList, new RecyclerItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent intentToPoemsListActivity = new Intent(
                        AuthorsListActivity.this,
                        PoemsListActivity.class);

                intentToPoemsListActivity.putExtra(AUTHOR_INTENT_NAME_TAG, authorsListAdapter.getItem(position));
                Log.d(TAG, "onItemClick: " + authorsListAdapter.getItem(position));
                startActivity(intentToPoemsListActivity);
            }
        });

        mRecyclerView.setAdapter(authorsListAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.poets_activity_menu, menu);

        MenuItem searchMenuItem = menu.findItem(R.id.poetsSearch);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        authorsListAdapter.getFilter().filter(query);

        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        authorsListAdapter.getFilter().filter(newText);

        return true;
    }

    private class QueryForAuthorList extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Void... params) {
            PoemsOpenHelper helper = new PoemsOpenHelper(AuthorsListActivity.this);
            SQLiteDatabase db = helper.openDatabase();
            Cursor c = db.query(
                    PoemContract.PoemsEntry.TABLE_NAME,
                    new String[]{PoemContract.PoemsEntry.COLUMN_AUTHOR},
                    null,
                    null,
                    PoemContract.PoemsEntry.COLUMN_AUTHOR,
                    null,
                    null);

            return c;
        }
    }
}
