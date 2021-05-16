package com.example.todo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.example.todo.data.TodoHelper;
import com.example.todo.data.TodoContract.TodoEntry;

public class MainActivity extends AppCompatActivity implements TodoListAdapter.TodoItemListener {
    private TextView addTextViewButton;
    private RecyclerView recyclerViewTodoList;
    private TodoListAdapter adapter;
    private SQLiteDatabase dbRead;
    private SQLiteDatabase dbWrite;
    public static final int TO_ACTIVITY_ADD_TODO = 1;
    public static final int TO_ACTIVITY_COLD_TODO = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TodoHelper helper = new TodoHelper(this);
        dbRead = helper.getReadableDatabase();
        dbWrite = helper.getWritableDatabase();
        Cursor cursor = getCursorMainActivity();

        recyclerViewTodoList = (RecyclerView) findViewById(R.id.recyclerView_todo_list);
        adapter = new TodoListAdapter(cursor, this, this);
        recyclerViewTodoList.setAdapter(adapter);
        recyclerViewTodoList.setLayoutManager(new LinearLayoutManager(this));

        addTextViewButton = (TextView) findViewById(R.id.textView_button_add_content);
        addTextViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Edit_Todo_Activity.class);
                startActivityForResult(intent,
                        TO_ACTIVITY_ADD_TODO,
                        ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            adapter.swapCursor(getCursorMainActivity());
        }
    }

    private Cursor getCursorMainActivity() {
        String selection = TodoEntry.COLUMN_STATUS + " IN(?,?)";
        String[] selectionArgs = {"0", "1"};

        Cursor cursor = dbRead.query(TodoEntry.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                TodoEntry.COLUMN_STATUS + " DESC");
        return cursor;
    }

    @Override
    public void onDoneClick(long id) {
        String selection = TodoEntry._ID + "=" + id;
        dbWrite.delete(TodoEntry.TABLE_NAME, selection, null);
        adapter.swapCursor(getCursorMainActivity());
    }

    @Override
    public void onStatusChangeClick(long id, int becoming) {
        ContentValues cv = new ContentValues();
        cv.put(TodoEntry.COLUMN_STATUS, becoming);
        String selection = TodoEntry._ID + "=" + id;
        dbWrite.update(TodoEntry.TABLE_NAME, cv, selection, null);
        adapter.swapCursor(getCursorMainActivity());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.menu_start_cold_Activity:
                Intent intent = new Intent(this, Cold_Todo_Activity.class);
                startActivityForResult(intent,
                        TO_ACTIVITY_COLD_TODO,
                        ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        }
        return super.onOptionsItemSelected(item);
    }
}