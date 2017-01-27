package com.example.davit.poems;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.davit.poems.data.DbHelper;

import java.util.concurrent.ExecutionException;

import static com.example.davit.poems.data.PoemsAppContract.PoetsEntry.COLUMN_NAME;
import static com.example.davit.poems.data.PoemsAppContract.PoetsEntry.TABLE_NAME;

public class AuthorsActivity extends AppCompatActivity {

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
}
