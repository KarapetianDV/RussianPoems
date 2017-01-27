package com.example.davit.poems;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import com.example.davit.poems.data.DbHelper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import static com.example.davit.poems.data.PoemsAppContract.PoetsEntry.COLUMN_LINK;
import static com.example.davit.poems.data.PoemsAppContract.PoetsEntry.COLUMN_NAME;
import static com.example.davit.poems.data.PoemsAppContract.PoetsEntry.TABLE_NAME;


class AuthorParserTask extends AsyncTask<Void, Void, Void> {

    private static final String TAG = "AuthorParserTask";
    private Document doc;
    private DbHelper dbHelper;

    public AuthorParserTask(DbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            doc = Jsoup.connect("http://www.klassika.ru/stihi/").get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements authorsEl = doc.select("#margins").select("a");
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Последние 5 элементов это меню
        for(int i = 0; i < 105; i++) {
            Element element = authorsEl.get(i);
            if (element.text().length() > 1) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(COLUMN_NAME, element.text());
                contentValues.put(COLUMN_LINK, element.attr("href"));

                db.insert(TABLE_NAME, null, contentValues);
            }
        }
        db.close();

        return null;
    }
}
