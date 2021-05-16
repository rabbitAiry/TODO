package com.example.todo.data;

import android.provider.BaseColumns;

public class TodoContract {
    private TodoContract() {
    }

    public class TodoEntry implements BaseColumns {
        public static final String TABLE_NAME = "TODO";
        public static final String COLUMN_ITEM = "ITEM";
        public static final String COLUMN_STATUS = "STATUS";
        public static final int categoryNormal = 0;
        public static final int categoryUrgent = 1;
        public static final int categoryCold = 2;
    }
}
