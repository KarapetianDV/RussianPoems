package com.example.davit.poems;

import android.database.Cursor;
import android.os.AsyncTask;

import com.example.davit.poems.data.DbHelper;

public class GetCursorTask extends AsyncTask<Object, Void, Cursor> {

    private static final String TAG = GetCursorTask.class.getSimpleName();
    DbHelper dbHelper;

    public GetCursorTask(DbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    @Override
    protected Cursor doInBackground(Object... params) {
        Cursor c = dbHelper.getReadableDatabase().query(
                (String) params[0], // table name
                (String[]) params[1], // columns
                (String) params[2], // selection (WHERE)
                (String[]) params[3], // selection args (WHERE)
                (String) params[4], // group by
                (String) params[5], // having
                (String) params[6] // order by
        );

        return c;
    }
}
