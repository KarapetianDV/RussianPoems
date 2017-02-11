package ru.karapetiandav.poems.data;

import android.provider.BaseColumns;

public final class PoemContract {

    public PoemContract() {
    }

    public static final class PoemsEntry implements BaseColumns {
        public final static String TABLE_NAME = "poems";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_AUTHOR = "author";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_TEXT = "text";
    }
}
