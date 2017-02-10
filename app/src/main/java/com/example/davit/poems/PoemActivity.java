package com.example.davit.poems;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.davit.poems.data.PoemContract;
import com.example.davit.poems.data.PoemsOpenHelper;

import java.util.concurrent.ExecutionException;

import static com.example.davit.poems.PoemsListActivity.TEXT_INTENT_AUTHOR_TAG;
import static com.example.davit.poems.PoemsListActivity.TEXT_INTENT_NAME_TAG;

public class PoemActivity extends AppCompatActivity {

    private static final String TAG = PoemActivity.class.getSimpleName();

    private String name;
    private String authorName;
    private String TEXT_SIZE_CONST = "TEXT_SIZE_CONST";

    private TextView poemText;
    private TextView authorText;

    private float poemTextSize = 14;

    private String text;

    private SharedPreferences preferences;

    private ShareActionProvider mShareActionProvider;

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

        try {
            Cursor cursor = new QueryForText().execute().get();
            int columnIndex = cursor.getColumnIndex(PoemContract.PoemsEntry.COLUMN_TEXT);
            cursor.moveToFirst();
            text = (cursor.getString(columnIndex));
            cursor.close();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        poemText = (TextView) findViewById(R.id.poemText);
        authorText = (TextView) findViewById(R.id.authorText);

        preferences = getPreferences(MODE_PRIVATE);
        initTextSize();

        poemText.setText(text);
        authorText.setText(authorName);
    }

    private void initTextSize() {
        if (preferences.contains(TEXT_SIZE_CONST)) {
            poemTextSize = preferences.getFloat(TEXT_SIZE_CONST, 14);
            poemText.setTextSize(TypedValue.COMPLEX_UNIT_SP, poemTextSize);
            Log.d(TAG, "initTextSize: poemTextSize есть в preferences и значение: " + poemTextSize);
        } else {
            SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
            editor.putFloat(TEXT_SIZE_CONST, 14);
            editor.apply();
            Log.d(TAG, "initTextSize: poemTextSize нет в preferences и мы пишем: " + poemTextSize);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.poem_activity_menu, menu);

        MenuItem item = menu.findItem(R.id.menu_item_share);

        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

        return true;
    }

    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }

        startActivity(Intent.createChooser(shareIntent, "Поделиться с помощью..."));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();

        switch (item.getItemId()) {
            case R.id.incText:
                poemText.setTextSize(TypedValue.COMPLEX_UNIT_SP, poemTextSize += 2);
                editor.putFloat(TEXT_SIZE_CONST, poemTextSize);
                editor.apply();

                Log.d(TAG, "onOptionsItemSelected: poemTextSize увеличили и записали - " + poemTextSize);

                return true;
            case R.id.decText:
                poemText.setTextSize(TypedValue.COMPLEX_UNIT_SP, poemTextSize -= 2);
                editor.putFloat(TEXT_SIZE_CONST, poemTextSize);
                editor.apply();

                Log.d(TAG, "onOptionsItemSelected: poemTextSize уменьшили и записали - " + poemTextSize);

                return true;

            case R.id.copyText:
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("текст стиха", text);
                clipboardManager.setPrimaryClip(clipData);

                Toast.makeText(this, "Текст скопирован в буфер обмена", Toast.LENGTH_SHORT).show();

                return true;

            case R.id.menu_item_share:
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, text + "\n\n" + authorName);
                intent.setType("text/plain");

                setShareIntent(intent);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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