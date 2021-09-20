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
import android.view.Menu;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.todo.data.TodoHelper;
import com.example.todo.data.TodoContract.TodoEntry;
import com.example.todo.data.TypeUtils;

import java.util.Calendar;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements GestureSwap.GestureSwapListener {
    public final static String LATEST_DATE_KEY = "date";

    private SQLiteDatabase todoReadDatabase;
    private View toolbarIndicatorNow;
    private View toolbarIndicatorAll;
    private FragmentManager mainFragmentManager;
    private FragmentNow fragmentNow;
    private FragmentAll fragmentAll;
    private ProgressBar progressBar;
    private TodoHelper helper;
    private Animation alphaAnimation;
    boolean isFragmentNow;
    private static final String TAG = "MainActivity";

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
        alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(100);

        new CreateDailyItemTask().execute();

        fragmentNow = new FragmentNow(this, todoReadDatabase, new NowButtonClickedEvent());
        fragmentAll = new FragmentAll(this, todoReadDatabase);
        mainFragmentManager = getSupportFragmentManager();
        mainFragmentManager.beginTransaction()
                .add(R.id.main_activity_container, fragmentNow)
                .commit();
        isFragmentNow = true;

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

    @Override
    public void onFlingListener(int gestureKey) {
        switch (gestureKey) {
            case GestureSwap.SWIPE_LEFTWARD:
                setFragmentAll();
                break;
            case GestureSwap.SWIPE_RIGHTWARD:
                setFragmentNow();
                break;
        }
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
            int latestDateValue = preferences.getInt(LATEST_DATE_KEY, 0);

            if (todayValue > latestDateValue) {
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
                while (cursor.moveToNext()) {
                    String content = cursor.getString(cursor.getColumnIndex(TodoEntry.TODO_COLUMN_CONTENT));
                    values.put(TodoEntry.TODO_COLUMN_CONTENT, content);
                    values.put(TodoEntry.TODO_COLUMN_TYPE, TypeUtils.TYPE_NORMAL);
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
            mainFragmentManager.beginTransaction()
                    .replace(R.id.main_activity_container, fragmentNow)
                    .commit();
            isFragmentNow = true;
            toolbarIndicatorNow.setVisibility(View.VISIBLE);
            toolbarIndicatorAll.setVisibility(View.INVISIBLE);
            toolbarIndicatorNow.startAnimation(alphaAnimation);
        }
    }

    private void setFragmentAll() {
        if (isFragmentNow) {
            mainFragmentManager.beginTransaction()
                    .replace(R.id.main_activity_container, fragmentAll)
                    .commit();
            isFragmentNow = false;
            toolbarIndicatorNow.setVisibility(View.INVISIBLE);
            toolbarIndicatorAll.setVisibility(View.VISIBLE);
            toolbarIndicatorAll.startAnimation(alphaAnimation);
        }
    }

    public int getTodayToken() {
        Calendar c = Calendar.getInstance();
        return c.get(1) * 10000 + (c.get(2) + 1) * 100 + c.get(5);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
//        return true;
//    }

    class NowButtonClickedEvent implements NowAdapter.NowButtonClickedListener {
        @Override
        public Cursor onButtonDoneClicked(long id) {
            fragmentNow.getCursorData();
            SQLiteDatabase todoWriteDatabase = helper.getWritableDatabase();
            todoWriteDatabase.delete(TodoEntry.TABLE_TODO, TodoEntry._ID + "=" + id, null);
            return fragmentNow.getCursorData();
        }

        @Override
        public Cursor onButtonToUrgentClicked(long id) {
            SQLiteDatabase todoWriteDatabase = helper.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(TodoEntry.TODO_COLUMN_TYPE, TypeUtils.TYPE_URGENT);
            todoWriteDatabase.update(TodoEntry.TABLE_TODO, cv, TodoEntry._ID + "=" + id, null);
            return fragmentNow.getCursorData();
        }
    }
}