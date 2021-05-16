package com.example.todo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todo.data.TodoHelper;
import com.example.todo.data.TodoContract.TodoEntry;

public class Edit_Todo_Activity extends AppCompatActivity {

    private RadioGroup radioGroupSelectCategory;
    private RadioButton radioButtonNormal;
    private RadioButton radioButtonUrgent;
    private RadioButton radioButtonCold;
    private EditText editTextDescription;
    private TextView textViewSummit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_todo);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        radioGroupSelectCategory = (RadioGroup) findViewById(R.id.radioGroup_select_category);
        radioButtonNormal = (RadioButton) findViewById(R.id.button_normal);
        radioButtonUrgent = (RadioButton) findViewById(R.id.button_urgent);
        radioButtonCold = (RadioButton) findViewById(R.id.button_cold);
        editTextDescription = (EditText) findViewById(R.id.editText_description);
        textViewSummit = (TextView) findViewById(R.id.textView_summit);

        textViewSummit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = editTextDescription.getText().toString();
                if (description.trim().length() == 0) {
                    Toast.makeText(getApplicationContext(), "内容不应为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                int category = 0;
                switch (radioGroupSelectCategory.getCheckedRadioButtonId()) {
                    case R.id.button_normal:
                        category = TodoEntry.categoryNormal;
                        break;
                    case R.id.button_urgent:
                        category = TodoEntry.categoryUrgent;
                        break;
                    case R.id.button_cold:
                        category = TodoEntry.categoryCold;
                        break;
                }
                TodoHelper todoHelper = new TodoHelper(getApplicationContext());
                SQLiteDatabase db = todoHelper.getWritableDatabase();
                ContentValues cv = new ContentValues();
                cv.put(TodoEntry.COLUMN_ITEM, description);
                cv.put(TodoEntry.COLUMN_STATUS, category);
                db.insert(TodoEntry.TABLE_NAME, null, cv);
                db.close();
                if (category != TodoEntry.categoryCold) setResult(RESULT_OK);
                finish();
            }
        });
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