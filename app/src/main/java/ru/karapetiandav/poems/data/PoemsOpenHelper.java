package ru.karapetiandav.poems.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static android.content.Context.MODE_PRIVATE;


public class PoemsOpenHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "poems.db";
    private static final int DATABASE_VERSION = 1;

    private Context context;

    public PoemsOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        openDatabase();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PoemContract.PoemsEntry.TABLE_NAME);

        onCreate(db);
    }


    public SQLiteDatabase openDatabase() {
        File dbFile = context.getDatabasePath(DATABASE_NAME);

        if (!dbFile.exists()) {
            try {
                copyDataBase();
            } catch (IOException e) {
                throw new RuntimeException("Error creating source database", e);
            }
        }

        return SQLiteDatabase.openDatabase(dbFile.getPath(), null, SQLiteDatabase.OPEN_READONLY);
    }

    /**
     * This method will copy database from /assets directory to application
     * package /databases directory
     **/
    private void copyDataBase() throws IOException {
        try {
            InputStream mInputStream = context.getAssets().open(DATABASE_NAME);
            SQLiteDatabase checkDB = context.openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
            checkDB.close();

            File outFile = context.getDatabasePath(DATABASE_NAME);
            OutputStream mOutputStream = new FileOutputStream(outFile);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = mInputStream.read(buffer)) > 0) {
                mOutputStream.write(buffer, 0, length);
            }

            mOutputStream.flush();
            mOutputStream.close();
            mInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}