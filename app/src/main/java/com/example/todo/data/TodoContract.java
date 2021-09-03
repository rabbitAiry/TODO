package com.example.todo.data;

import android.provider.BaseColumns;

public class TodoContract {
    private TodoContract() {
    }

    public class TodoEntry implements BaseColumns {
        public static final String TABLE_NAME = "TODO";
        public static final String COLUMN_NAME = "NAME";
        public static final String COLUMN_TYPE = "TYPE";
        public static final String COLUMN_IS_VISIBLE = "IsVISIBLE";
        public static final String COLUMN_LATEST_DATE = "LatestDATE";
        public static final String COLUMN_HAS_REMINDER = "HasREMINDER";
        public static final int CATEGORY_NORMAL = 1;
        public static final int CATEGORY_URGENT = 2;
        public static final int CATEGORY_DAILY = 3;
        public static final int CATEGORY_COLD = 5;
    }
}
