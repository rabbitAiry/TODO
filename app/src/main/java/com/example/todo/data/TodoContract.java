package com.example.todo.data;

import android.provider.BaseColumns;

public class TodoContract {
    private TodoContract() {
    }

    public static class TodoEntry implements BaseColumns {
        public static final String TABLE_TODO = "TODO";
        public static final String TODO_COLUMN_CONTENT = "CONTENT";
        public static final String TODO_COLUMN_TYPE = "TYPE";
        public static final String TODO_COLUMN_IS_CREATED_BY_DAILY = "IS_DAILY";
//        public static final String TODO_COLUMN_DATE = "DATE";
//        public static final String TODO_COLUMN_HAS_REMINDER = "HAS_REMINDER";
//        public static final String TODO_COLUMN_REMIND_TIME = "REMIND_TIME";
    }
}
