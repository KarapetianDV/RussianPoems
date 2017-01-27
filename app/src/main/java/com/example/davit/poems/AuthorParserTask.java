package com.example.davit.poems;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;


class AuthorParserTask extends AsyncTask<Void, Void, HashMap<String, String>> {

    private static final String TAG = AuthorParserTask.class.getSimpleName();
    private Document doc;

    @Override
    protected HashMap<String, String> doInBackground(Void... params) {
        try {
            doc = Jsoup.connect("http://www.klassika.ru/stihi/").get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements authorsEl = doc.select("#margins").select("a");
        HashMap<String, String> map = new HashMap<>();

        // Последние 5 элементов это меню
        for(int i = 0; i < 105; i++) {
            Element element = authorsEl.get(i);
            if (element.text().length() > 1) {
                map.put(element.text(), element.attr("href"));
            }
        }

        return map;
    }
}