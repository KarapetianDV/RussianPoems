package com.example.davit.poems;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;


class AuthorParserTask extends AsyncTask<Void, Void, ArrayList<String>> {

    private static final String TAG = "AuthorParserTask";
    private Document doc;
    private Context context;
    private RecyclerView recyclerView;

    public AuthorParserTask(Context context, RecyclerView mRecyclerView) {
        this.context = context;
        this.recyclerView = mRecyclerView;
    }

    @Override
    protected ArrayList<String> doInBackground(Void... params) {
        ArrayList<String> list = new ArrayList<>();
        try {
            doc = Jsoup.connect("http://www.klassika.ru/stihi/").get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements authorsEl = doc.select("#margins").select("a");

        // Последние 5 элементов это меню
        for(int i = 0; i < 105; i++) {
            Element element = authorsEl.get(i);
            if(element.text().length() > 1) list.add(element.text());
        }
        return list;
    }

    @Override
    protected void onPostExecute(ArrayList<String> strings) {
        super.onPostExecute(strings);
        PoemsAdapter adapter = new PoemsAdapter(strings);
        recyclerView.setAdapter(adapter);
        Toast.makeText(context, R.string.data_uploaded, Toast.LENGTH_SHORT).show();
    }
}
