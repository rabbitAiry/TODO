package com.example.todo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todo.data.TodoContract.TodoEntry;
import com.example.todo.data.TodoHelper;
import com.example.todo.data.TypeUtils;

public class EditTodoActivity extends AppCompatActivity {
    private EditText editContent;
    private RadioGroup radioGroupEdit;
    private String contentOrigin;
    public static final int NOW_EDIT_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_todo);
        Toolbar toolbar = findViewById(R.id.edit_todo_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        long id = intent.getLongExtra(AllAdapter.ITEM_ID, 0L);
        int type = intent.getIntExtra(AllAdapter.ITEM_TYPE, TypeUtils.TYPE_NORMAL);
        contentOrigin = intent.getStringExtra(AllAdapter.ITEM_CONTENT);

        editContent = findViewById(R.id.edit_todo_content);
        View viewIndicator = findViewById(R.id.edit_todo_indicator);
        radioGroupEdit = findViewById(R.id.edit_todo_radio_group);
        TextView buttonSubmit = findViewById(R.id.edit_todo_button_submit);
        RadioButton editTodoButtonNormal = findViewById(R.id.edit_todo_button_normal);
        RadioButton editTodoButtonUrgent = findViewById(R.id.edit_todo_button_urgent);
        RadioButton editTodoButtonDaily = findViewById(R.id.edit_todo_button_daily);
        RadioButton editTodoButtonCold = findViewById(R.id.edit_todo_button_cold);
        RadioButton editTodoButtonDelete = findViewById(R.id.edit_todo_button_delete);
        switch (type) {
            case TypeUtils.TYPE_NORMAL:
                editTodoButtonNormal.setChecked(true);
                break;
            case TypeUtils.TYPE_URGENT:
                editTodoButtonUrgent.setChecked(true);
                break;
            case TypeUtils.TYPE_DAILY:
                editTodoButtonDaily.setChecked(true);
                editTodoButtonNormal.setEnabled(false);
                editTodoButtonUrgent.setEnabled(false);
                editTodoButtonCold.setEnabled(false);
                break;
            case TypeUtils.TYPE_COLD:
                editTodoButtonCold.setChecked(true);
        }

        viewIndicator.setBackgroundColor(TypeUtils.getTypeColor(type, this));
        editContent.setText(contentOrigin);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                todoSubmit(type, id);
            }
        });
    }

    /**
     * on response to action when user clicked the "←" button in the toolbar
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * update the content of todo_item, or simply delete it
     *
     * @param type
     * @param id
     */
    private void todoSubmit(int type, long id) {
        int towardsType = TypeUtils.TYPE_NORMAL;
        switch (radioGroupEdit.getCheckedRadioButtonId()) {
            case R.id.edit_todo_button_normal:
                towardsType = TypeUtils.TYPE_NORMAL;
                break;
            case R.id.edit_todo_button_urgent:
                towardsType = TypeUtils.TYPE_URGENT;
                break;
            case R.id.edit_todo_button_daily:
                towardsType = TypeUtils.TYPE_DAILY;
                break;
            case R.id.edit_todo_button_cold:
                towardsType = TypeUtils.TYPE_COLD;
                break;
            case R.id.edit_todo_button_delete:
                towardsType = TypeUtils.TYPE_DELETE;
        }
        String content = editContent.getText().toString();
        if (content.trim().length() == 0 && towardsType != TypeUtils.TYPE_DELETE) {
            Toast.makeText(EditTodoActivity.this, "内容不应为空", Toast.LENGTH_SHORT).show();
            return;
        }
        TodoHelper helper = new TodoHelper(EditTodoActivity.this);
        SQLiteDatabase writeDatabase = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        boolean isUpdateNeeded = false;
        if (!content.equals(contentOrigin)) {
            cv.put(TodoEntry.TODO_COLUMN_CONTENT, content);
            isUpdateNeeded = true;
        }
        if (towardsType == TypeUtils.TYPE_DELETE) {
            writeDatabase.delete(TodoEntry.TABLE_TODO, TodoEntry._ID + "=" + id, null);
            setResult(RESULT_OK);
        } else if (type != towardsType) {
            cv.put(TodoEntry.TODO_COLUMN_TYPE, towardsType);
            if (towardsType == TypeUtils.TYPE_DAILY) {
                ContentValues cvDaily = new ContentValues();
                cvDaily.put(TodoEntry.TODO_COLUMN_CONTENT, content);
                cvDaily.put(TodoEntry.TODO_COLUMN_TYPE, TypeUtils.TYPE_NORMAL);
                cvDaily.put(TodoEntry.TODO_COLUMN_IS_CREATED_BY_DAILY, 1);
                writeDatabase.insert(TodoEntry.TABLE_TODO, null, cvDaily);
            }
            isUpdateNeeded = true;
        }
        if (isUpdateNeeded) {
            writeDatabase.update(TodoEntry.TABLE_TODO, cv, TodoEntry._ID + "=" + id, null);
            setResult(RESULT_OK);
        }
        writeDatabase.close();
        finish();
    }
}