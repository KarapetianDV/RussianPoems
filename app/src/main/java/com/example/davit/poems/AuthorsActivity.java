package com.example.davit.poems;

import android.content.Intent;
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
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class AuthorsActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    public static final String AUTHOR_INTENT_URL_TAG = "AUTHOR_INTENT_URL";
    public static final String AUTHOR_INTENT_NAME_TAG = "AUTHOR_INTENT_NAME";
    private static final String TAG = AuthorsActivity.class.getSimpleName();
    PoetsAdapter mAdapter;
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

        HashMap<String, String> map = new HashMap<>();

        try {
            map = new AuthorParserTask().execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        final ArrayList<String> list = new ArrayList<>();
        list.addAll(map.keySet());
        final HashMap<String, String> finalMap = map;

        mAdapter = new PoetsAdapter(list, new RecyclerItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Toast.makeText(AuthorsActivity.this, "OnClick", Toast.LENGTH_SHORT).show();
                Intent intentToPoemsListActivity = new Intent(AuthorsActivity.this, PoemsListActivity.class);
                intentToPoemsListActivity.putExtra(AUTHOR_INTENT_URL_TAG,
                        finalMap.get(((TextView) v.findViewById(R.id.text1)).getText().toString())
                );
                intentToPoemsListActivity.putExtra(AUTHOR_INTENT_NAME_TAG, list.get(position));
                startActivity(intentToPoemsListActivity);
            }
        });

        mRecyclerView.setAdapter(mAdapter);
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
        mAdapter.getFilter().filter(query);

        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mAdapter.getFilter().filter(newText);

        return true;
    }
}
