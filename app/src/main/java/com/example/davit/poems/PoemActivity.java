package com.example.davit.poems;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static com.example.davit.poems.PoemsListActivity.TEXT_INTENT_AUTHOR_TAG;
import static com.example.davit.poems.PoemsListActivity.TEXT_INTENT_NAME_TAG;
import static com.example.davit.poems.PoemsListActivity.TEXT_INTENT_URL_TAG;

public class PoemActivity extends AppCompatActivity {

    private static final String TAG = PoemActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poem);

        Intent intent = getIntent();
        String URL = intent.getStringExtra(TEXT_INTENT_URL_TAG);
        String authorName = intent.getStringExtra(TEXT_INTENT_AUTHOR_TAG);
        String name = intent.getStringExtra(TEXT_INTENT_NAME_TAG);
        Log.d(TAG, "onCreate: " + name + " - " + authorName + " - " + URL);

        PoemTask task = new PoemTask();
        String text = new String();
        try {
            text = task.execute("http://klassika.ru" + URL).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        TextView textView = (TextView) findViewById(R.id.poemText);
        textView.setText(text);
    }

    private class PoemTask extends AsyncTask<String, Void, String> {

        Document doc;
        String result;

        @Override
        protected String doInBackground(String... params) {
            try {
                doc = Jsoup.connect(params[0]).get();
                Log.d(TAG, "doInBackground: " + params[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Elements poemTextElements = doc.select("pre");
            result = poemTextElements.text();
            return result;
        }
    }
}
