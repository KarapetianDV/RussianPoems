package com.example.davit.poems;

import android.content.Intent;
import android.os.AsyncTask;
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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import static com.example.davit.poems.AuthorsActivity.AUTHOR_INTENT_NAME_TAG;
import static com.example.davit.poems.AuthorsActivity.AUTHOR_INTENT_URL_TAG;

public class PoemsListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private static final String TAG = PoemsListActivity.class.getSimpleName();
    PoemsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poems_list);

        Intent intentFromAuthorsActivity = getIntent();
        String authorUrl = "http://klassika.ru" +
                intentFromAuthorsActivity.getStringExtra(AUTHOR_INTENT_URL_TAG);
        String authorName = intentFromAuthorsActivity.getStringExtra(AUTHOR_INTENT_NAME_TAG);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerView poemsRecyclerView = (RecyclerView) findViewById(R.id.poemsRecyclerView);
        poemsRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        poemsRecyclerView.setLayoutManager(layoutManager);
        poemsRecyclerView.addItemDecoration(new DividerItemDecoration(
                poemsRecyclerView.getContext(),
                layoutManager.getOrientation()
        ));

        PoemsListParserTask poemsListParserTask = new PoemsListParserTask(authorUrl);

        HashMap<String, String> poemsMap = new HashMap<>();

        try {
            poemsMap = poemsListParserTask.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        ArrayList<String> poemsList = new ArrayList<>(poemsMap.keySet());

        mAdapter = new PoemsAdapter(poemsList, authorName, new RecyclerItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {

            }
        });
        poemsRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.poems_activity_menu, menu);

        MenuItem searchMenuItem = menu.findItem(R.id.poemsSearch);
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

    class PoemsListParserTask extends AsyncTask<Void, Void, HashMap<String, String>> {

        private Document doc;
        private String connectUrl;

        public PoemsListParserTask(String connectUrl) {
            this.connectUrl = connectUrl;
        }

        @Override
        protected HashMap<String, String> doInBackground(Void... params) {
            try {
                doc = Jsoup.connect(connectUrl).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Elements authorsEl = doc.select("li");
            HashMap<String, String> map = new HashMap<>();

            for (Element element : authorsEl) {
                map.put(element.text(), element.attr("href"));
            }

            return map;
        }
    }
}
