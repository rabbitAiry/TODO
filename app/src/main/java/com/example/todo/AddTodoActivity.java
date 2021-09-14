package com.example.todo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todo.data.TodoHelper;
import com.example.todo.data.TodoContract.TodoEntry;
import com.example.todo.data.TypeUtils;

public class AddTodoActivity extends AppCompatActivity {

    private RadioGroup radioGroupSelectCategory;
    private EditText editTextDescription;
    private TextView textViewSummit;
    private EditText editContent;
    private RadioGroup radioGroup;
    public static final int NOW_ADD_CODE = 1;
    public static final int RESULT_ADD_SUCCESSFULLY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo);
        Toolbar toolbar = findViewById(R.id.add_todo_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        radioGroup = findViewById(R.id.add_todo_radio_group);
        editContent = findViewById(R.id.add_todo_edit_content);
        TextView buttonSubmit = findViewById(R.id.add_todo_button_submit);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                todoSubmit();
            }
        });

        this.setResult(RESULT_ADD_SUCCESSFULLY);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_ENTER:
                todoSubmit();
            default:
                return super.onKeyUp(keyCode, event);
        }
    }

    private void todoSubmit() {
        String content = editContent.getText().toString();
        if (content.trim().length() == 0) {
            Toast.makeText(AddTodoActivity.this, "内容不应为空", Toast.LENGTH_SHORT).show();
            return;
        }
        int type = TypeUtils.TYPE_NORMAL;
        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.add_todo_button_normal:
                type = TypeUtils.TYPE_NORMAL;
                break;
            case R.id.add_todo_button_urgent:
                type = TypeUtils.TYPE_URGENT;
                break;
            case R.id.add_todo_button_daily:
                type = TypeUtils.TYPE_DAILY;
                break;
            case R.id.add_todo_button_cold:
                type = TypeUtils.TYPE_COLD;
                break;
        }
        TodoHelper helper = new TodoHelper(AddTodoActivity.this);
        SQLiteDatabase writeDatabase = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TodoEntry.TODO_COLUMN_CONTENT, content);
        cv.put(TodoEntry.TODO_COLUMN_TYPE, type);
        cv.put(TodoEntry.TODO_COLUMN_IS_CREATED_BY_DAILY, 0);
        writeDatabase.insert(TodoEntry.TABLE_TODO, null, cv);
        if (type == TypeUtils.TYPE_DAILY) {
            cv.put(TodoEntry.TODO_COLUMN_CONTENT, content);
            cv.put(TodoEntry.TODO_COLUMN_TYPE, TypeUtils.TYPE_NORMAL);
            cv.put(TodoEntry.TODO_COLUMN_IS_CREATED_BY_DAILY, 1);
            writeDatabase.insert(TodoEntry.TABLE_TODO, null, cv);
        }
        writeDatabase.close();
        if (type != TypeUtils.TYPE_COLD) setResult(RESULT_OK);
        finish();
    }
}