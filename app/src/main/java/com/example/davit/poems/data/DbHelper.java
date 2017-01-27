package com.example.davit.poems.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.davit.poems.data.PoemsAppContract.PoetsEntry.COLUMN_LINK;
import static com.example.davit.poems.data.PoemsAppContract.PoetsEntry.COLUMN_NAME;
import static com.example.davit.poems.data.PoemsAppContract.PoetsEntry.TABLE_NAME;
import static com.example.davit.poems.data.PoemsAppContract.PoetsEntry._ID;


public class DbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "poems.db";
    private static final int DATABASE_VERSION = 1;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_POETS_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT NOT NULL, " +
                COLUMN_LINK + " TEXT NOT NULL);";
        db.execSQL(SQL_CREATE_POETS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF IT EXISTS " + TABLE_NAME);

        onCreate(db);
    }
}
