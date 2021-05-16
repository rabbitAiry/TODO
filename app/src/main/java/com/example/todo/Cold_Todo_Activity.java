package com.example.todo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.todo.data.TodoContract.TodoEntry;
import com.example.todo.data.TodoHelper;

public class Cold_Todo_Activity extends AppCompatActivity implements ColdTodoAdapter.ColdItemListener {
    private SQLiteDatabase dbRead;
    private SQLiteDatabase dbWrite;
    private ColdTodoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cold_todo);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TodoHelper helper = new TodoHelper(this);
        dbRead = helper.getReadableDatabase();
        dbWrite = helper.getWritableDatabase();
        Cursor cursor = getCursorCold();

        RecyclerView coldRecyclerView;
        adapter = new ColdTodoAdapter(this, cursor, this);

        coldRecyclerView = (RecyclerView) findViewById(R.id.recyclerView_cold_list);
        coldRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        coldRecyclerView.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                long id = (long) viewHolder.itemView.getTag();
                dbWrite.delete(TodoEntry.TABLE_NAME, TodoEntry._ID + "=" + id, null);
                adapter.swapCursor(getCursorCold());
            }
        }).attachToRecyclerView(coldRecyclerView);

    }

    private Cursor getCursorCold() {
        Cursor cursor = dbRead.query(TodoEntry.TABLE_NAME,
                null,
                TodoEntry.COLUMN_STATUS + "=" + TodoEntry.categoryCold,
                null,
                null,
                null,
                null);
        return cursor;
    }

    @Override
    public void onStatusChange(long id, int becoming) {
        ContentValues cv = new ContentValues();
        cv.put(TodoEntry.COLUMN_STATUS, becoming);
        dbWrite.update(TodoEntry.TABLE_NAME, cv, TodoEntry._ID + "=" + id, null);
        adapter.swapCursor(getCursorCold());
        setResult(RESULT_OK);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}