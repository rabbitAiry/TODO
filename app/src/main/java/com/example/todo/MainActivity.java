package com.example.todo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Space;
import android.widget.TextView;

import com.example.todo.data.TodoHelper;
import com.example.todo.data.TodoContract.TodoEntry;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private SQLiteDatabase todoReadDatabase;
    private SQLiteDatabase todoWriteDatabase;
    private View toolbarIndicatorNow;
    private View toolbarIndicatorAll;
    private FragmentManager mainFragmentManager;
    private FragmentNow fragmentNow;
    private FragmentAll fragmentAll;
    private GestureDetector gestureDetector;
    boolean isFragmentNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        TextView toolbarTagNow = findViewById(R.id.main_toolbar_textview_now);
        TextView toolbarTagAll = findViewById(R.id.main_toolbar_textview_all);
        toolbarIndicatorNow = findViewById(R.id.main_toolbar_indicator_now);
        toolbarIndicatorAll = findViewById(R.id.main_toolbar_indicator_all);

        fragmentNow = new FragmentNow();
        fragmentAll = new FragmentAll();
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

        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                float differ = e2.getX() - e1.getX();
                if (differ > 30) {
                    setFragmentNow();
                } else if (differ < -30) {
                    setFragmentAll();
                }
                return false;
            }
        });

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
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

    public int getTodayKey() {
        Calendar c = Calendar.getInstance();
        return c.get(1) * 10000 + (c.get(2) + 1) * 100 + c.get(5);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return true;
    }
}