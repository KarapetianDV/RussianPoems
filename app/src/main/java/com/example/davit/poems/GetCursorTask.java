package com.example.davit.poems;

import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

import com.example.davit.poems.data.DbHelper;

import static com.example.davit.poems.data.PoemsAppContract.PoetsEntry.COLUMN_NAME;

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

        Log.d(TAG, "doInBackground: " + c.getColumnName(c.getColumnIndex(COLUMN_NAME)));
        return c;
    }
}
