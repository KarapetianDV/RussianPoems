package com.example.davit.poems;

import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.davit.poems.data.DbHelper;

import java.util.concurrent.ExecutionException;

import static com.example.davit.poems.data.PoemsAppContract.PoetsEntry.COLUMN_NAME;
import static com.example.davit.poems.data.PoemsAppContract.PoetsEntry.TABLE_NAME;

public class AuthorsActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private static final String TAG = "AuthorsActivity";
    private RecyclerView mRecyclerView;
    private DbHelper mDbHelper = new DbHelper(this);

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

        // Парсим и пишем в бд, данные про поэтов
        AuthorParserTask authorParserTask = new AuthorParserTask(mDbHelper);
        authorParserTask.execute();

        try {
            PoetsAdapter adapter = new PoetsAdapter(mDbHelper, new Object[]{
                    TABLE_NAME,
                    new String[]{COLUMN_NAME},
                    null,
                    null,
                    null,
                    null,
                    null
            });
            mRecyclerView.setAdapter(adapter);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        MenuItem searchMenuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
