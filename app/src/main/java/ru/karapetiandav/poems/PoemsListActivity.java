package ru.karapetiandav.poems;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
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

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import ru.karapetiandav.poems.data.PoemContract;
import ru.karapetiandav.poems.data.PoemsOpenHelper;

public class PoemsListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    public static final String TEXT_INTENT_NAME_TAG = "TEXT_INTENT_NAME";
    public static final String TEXT_INTENT_AUTHOR_TAG = "TEXT_INTENT_AUTHOR";
    private static final String TAG = PoemsListActivity.class.getSimpleName();

    private PoemsListAdapter poemsListAdapter;
    private String authorName;
    private RecyclerView poemsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poems_list);

        if (getIntent().getStringExtra(AuthorsListActivity.AUTHOR_INTENT_NAME_TAG) == null) {
            authorName = getPreferences(MODE_PRIVATE).getString(AuthorsListActivity.AUTHOR_INTENT_NAME_TAG, "");

            Log.d(TAG, "onCreate: Считали с SharedPreferences и теперь authorName - " + authorName);

            SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
            editor.remove(AuthorsListActivity.AUTHOR_INTENT_NAME_TAG);
            editor.apply();
        } else {
            authorName = getIntent().getStringExtra(AuthorsListActivity.AUTHOR_INTENT_NAME_TAG);
            Log.d(TAG, "onCreate: Получили с Intent и теперь authorName - " + authorName);
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        poemsRecyclerView = (RecyclerView) findViewById(R.id.poemsRecyclerView);
        poemsRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        poemsRecyclerView.setLayoutManager(layoutManager);
        poemsRecyclerView.addItemDecoration(new DividerItemDecoration(
                poemsRecyclerView.getContext(),
                layoutManager.getOrientation()
        ));

        ArrayList<String> poemsList = new ArrayList<>();

        try {
            Cursor cursor = new QueryForPoemsList().execute().get();
            int columnIndex = cursor.getColumnIndex(PoemContract.PoemsEntry.COLUMN_TITLE);
            while (cursor.moveToNext()) {
                poemsList.add(cursor.getString(columnIndex));
            }
            cursor.close();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        poemsListAdapter = new PoemsListAdapter(poemsList, authorName, new RecyclerItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent intentToPoemActivity = new Intent(PoemsListActivity.this, PoemActivity.class);
                intentToPoemActivity.putExtra(TEXT_INTENT_NAME_TAG,
                        poemsListAdapter.getItem(position)); // Имя произведения
                intentToPoemActivity.putExtra(TEXT_INTENT_AUTHOR_TAG, authorName); // Автор произведения
                startActivity(intentToPoemActivity);
            }
        });

        poemsRecyclerView.setAdapter(poemsListAdapter);
    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
        editor.putString(AuthorsListActivity.AUTHOR_INTENT_NAME_TAG, authorName);
        editor.apply();
        Log.d(TAG, "onStop: записали в SharedPreferences");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.poems_list_activity_menu, menu);

        MenuItem searchMenuItem = menu.findItem(R.id.poemsSearch);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        poemsListAdapter.getFilter().filter(query);

        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        poemsListAdapter.getFilter().filter(newText);

        return true;
    }

    private class QueryForPoemsList extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Void... params) {
            PoemsOpenHelper helper = new PoemsOpenHelper(PoemsListActivity.this);
            SQLiteDatabase db = helper.openDatabase();
            Cursor c = db.query(
                    PoemContract.PoemsEntry.TABLE_NAME,
                    new String[]{PoemContract.PoemsEntry.COLUMN_TITLE},
                    PoemContract.PoemsEntry.COLUMN_AUTHOR + " = '" + authorName + "'",
                    null,
                    PoemContract.PoemsEntry.COLUMN_TITLE,
                    null,
                    null);

            DatabaseUtils.dumpCursorToString(c);

            return c;
        }
    }
}
