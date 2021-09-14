package com.example.todo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.todo.data.TodoHelper;
import com.example.todo.data.TodoContract.TodoEntry;
import com.example.todo.data.TypeUtils;

import java.util.Calendar;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    public final static String LATEST_DATE_KEY = "date";

    private SQLiteDatabase todoReadDatabase;
    private View toolbarIndicatorNow;
    private View toolbarIndicatorAll;
    private FragmentManager mainFragmentManager;
    private FragmentNow fragmentNow;
    private FragmentAll fragmentAll;
    private GestureDetector gestureDetector;
    private ProgressBar progressBar;
    private TodoHelper helper;
    boolean isFragmentNow;
    private static final String TAG = "main";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        TextView toolbarTagNow = findViewById(R.id.main_toolbar_textview_now);
        TextView toolbarTagAll = findViewById(R.id.main_toolbar_textview_all);
        toolbarIndicatorNow = findViewById(R.id.main_toolbar_indicator_now);
        toolbarIndicatorAll = findViewById(R.id.main_toolbar_indicator_all);
        progressBar = findViewById(R.id.main_progress_bar);
        helper = new TodoHelper(this);
        todoReadDatabase = helper.getReadableDatabase();

        new CreateDailyItemTask().execute();

        fragmentNow = new FragmentNow(this, todoReadDatabase, new NowButtonClickedEvent());
        fragmentAll = new FragmentAll(this, todoReadDatabase);
        mainFragmentManager = getSupportFragmentManager();
        mainFragmentManager.beginTransaction()
                .add(R.id.main_activity_container, fragmentNow)
                .commit();
        isFragmentNow = true;
        Log.d(TAG, "onCreate: afterFragmentTransaction");
//        test only
        fragmentNow.getCursorData();

        toolbarTagNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragmentNow();
            }
        });
        toolbarTagAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragmentAll();
            }
        });

    }

    @SuppressLint("StaticFieldLeak")
    class CreateDailyItemTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            TodoHelper helper = new TodoHelper(MainActivity.this);
            SharedPreferences preferences = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
            int todayValue = getTodayToken();
            int latestDateValue = preferences.getInt(LATEST_DATE_KEY, todayValue);

            if (todayValue != latestDateValue) {
                SQLiteDatabase todoWriteDatabase = helper.getWritableDatabase();
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt(LATEST_DATE_KEY, todayValue).commit();

                todoWriteDatabase.delete(TodoEntry.TABLE_TODO,
                        TodoEntry.TODO_COLUMN_IS_CREATED_BY_DAILY + "=1",
                        null);

                Cursor cursor = todoReadDatabase.query(TodoEntry.TABLE_TODO,
                        null,
                        TodoEntry.TODO_COLUMN_TYPE + "=" + TypeUtils.TYPE_DAILY,
                        null,
                        null,
                        null,
                        null);
                ContentValues values = new ContentValues();
                while (!cursor.moveToNext()) {
                    String content = cursor.getString(cursor.getColumnIndex(TodoEntry.TODO_COLUMN_CONTENT));
                    int type = cursor.getInt(cursor.getColumnIndex(TodoEntry.TODO_COLUMN_TYPE));
                    values.put(TodoEntry.TODO_COLUMN_CONTENT, content);
                    values.put(TodoEntry.TODO_COLUMN_TYPE, type);
                    values.put(TodoEntry.TODO_COLUMN_IS_CREATED_BY_DAILY, 1);
                    todoWriteDatabase.insert(TodoEntry.TABLE_TODO, null, values);
                }
                cursor.close();
                todoWriteDatabase.close();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void setFragmentNow() {
        if (!isFragmentNow) {
            toolbarIndicatorNow.setVisibility(View.VISIBLE);
            toolbarIndicatorAll.setVisibility(View.INVISIBLE);
            mainFragmentManager.beginTransaction()
                    .replace(R.id.main_activity_container, fragmentNow)
                    .commit();
            isFragmentNow = true;
        }
    }

    private void setFragmentAll() {
        if (isFragmentNow) {
            toolbarIndicatorNow.setVisibility(View.INVISIBLE);
            toolbarIndicatorAll.setVisibility(View.VISIBLE);
            mainFragmentManager.beginTransaction()
                    .replace(R.id.main_activity_container, fragmentAll)
                    .commit();
            isFragmentNow = false;
        }
    }

    public int getTodayToken() {
        Calendar c = Calendar.getInstance();
        return c.get(1) * 10000 + (c.get(2) + 1) * 100 + c.get(5);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    class NowButtonClickedEvent implements NowAdapter.NowButtonClickedListener {
        @Override
        public Cursor onButtonDoneClicked(long id) {
            SQLiteDatabase todoWriteDatabase = helper.getWritableDatabase();
            todoWriteDatabase.delete(TodoEntry.TABLE_TODO, TodoEntry._ID + "=" + id, null);
            todoWriteDatabase.close();
            Log.d(TAG, "onButtonDoneClicked: after write before check");
            return fragmentNow.getCursorData();
        }

        @Override
        public Cursor onButtonToUrgentClicked(long id) {
            SQLiteDatabase todoWriteDatabase = helper.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(TodoEntry.TODO_COLUMN_TYPE, TypeUtils.TYPE_URGENT);
            todoWriteDatabase.update(TodoEntry.TABLE_TODO, cv, TodoEntry._ID + "=" + id, null);
            todoWriteDatabase.close();
            return fragmentNow.getCursorData();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }
}