package com.example.davit.poems;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.davit.poems.data.PoemContract;
import com.example.davit.poems.data.PoemsOpenHelper;

import java.util.concurrent.ExecutionException;

import static com.example.davit.poems.PoemsListActivity.TEXT_INTENT_AUTHOR_TAG;
import static com.example.davit.poems.PoemsListActivity.TEXT_INTENT_NAME_TAG;

public class PoemActivity extends AppCompatActivity {

    private static final String TAG = PoemActivity.class.getSimpleName();

    private String name;
    private String authorName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poem);

        Intent intent = getIntent();
        authorName = intent.getStringExtra(TEXT_INTENT_AUTHOR_TAG);
        name = intent.getStringExtra(TEXT_INTENT_NAME_TAG);

        setTitle(name);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        String text = "";

        try {
            Cursor cursor = new QueryForText().execute().get();
            int columnIndex = cursor.getColumnIndex(PoemContract.PoemsEntry.COLUMN_TEXT);
            cursor.moveToFirst();
            text = (cursor.getString(columnIndex));
            cursor.close();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        TextView poemText = (TextView) findViewById(R.id.poemText);
        TextView authorText = (TextView) findViewById(R.id.authorText);

        poemText.setText(text);
        authorText.setText(authorName);
    }

    private class QueryForText extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Void... params) {
            PoemsOpenHelper helper = new PoemsOpenHelper(PoemActivity.this);
            SQLiteDatabase db = helper.openDatabase();

            String selection = PoemContract.PoemsEntry.COLUMN_AUTHOR +
                    " = '" + authorName + "' AND " + PoemContract.PoemsEntry.COLUMN_TITLE +
                    " = '" + name + "'";

            Cursor c = db.query(
                    PoemContract.PoemsEntry.TABLE_NAME,
                    new String[]{PoemContract.PoemsEntry.COLUMN_TEXT},
                    selection,
                    null,
                    null,
                    null,
                    null);

            return c;
        }
    }
}
