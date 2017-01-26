package com.example.davit.poems.data;

import android.provider.BaseColumns;

public final class PoemsAppContract {

    private PoemsAppContract() {
    }

    public static final class PoetsEntry implements BaseColumns {
        public static final String TABLE_NAME = "poets";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_LINK = "link";
    }
}
