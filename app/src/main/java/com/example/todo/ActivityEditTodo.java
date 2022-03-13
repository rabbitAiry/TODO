package com.example.todo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.todo.data.TodoItem;
import com.example.todo.data.TodoItemDataUtil;
import com.example.todo.data.TodoItemTypeUtils;
import com.example.todo.databinding.ActivityEditTodoBinding;
import com.example.todo.view.TypeIndicator;

public class ActivityEditTodo extends AppCompatActivity {
    public static String TAG_TODO_ITEM = "todo";
    public static final String MODE = "mode";
    public static final int MODE_ADD = 1;
    public static final int MODE_EDIT = 2;

    private ActivityEditTodoBinding binding;
    private TodoItemDataUtil dataUtil;
    private TodoItem curr;
    public boolean isAdd = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditTodoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        new Thread(() -> dataUtil = TodoItemDataUtil.getInstance(this)).start();
        Intent intent = getIntent();
        isAdd = intent.getIntExtra(MODE, MODE_ADD) == MODE_ADD;
        if (isAdd) initForAdd();
        else initForEdit(intent);
        // TODO: 周期设置 - 暂不可用
        binding.editTodoPeriodBackgroundMask.setVisibility(View.VISIBLE);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(isAdd ? R.string.add_todo : R.string.edit_todo);
        }
        binding.editTodoButtonSubmit.setOnClickListener(v -> handleSubmit());
        binding.editTodoTypeRadioGroup.setOnCheckedChangeListener((group, checkedId) -> onRadioButtonSelected(checkedId));

//        binding.editTodoPeriodStart.setOnClickListener(v -> {
//            DialogFragment fragment = new FragmentDatePicker(new DatePickerListener());
//            fragment.show(getSupportFragmentManager(), "datePicker");
//        });
//        binding.editTodoPeriodStart.setText(TimeUtils.getTodayText());
    }

    private void onRadioButtonSelected(int type) {
        int typeColor = TodoItemTypeUtils.getTypeColorByTypeId(type, this);
        binding.editTodoTypeBar.setBackground(new TypeIndicator(typeColor));
    }

    private void initForAdd() {
        onRadioButtonSelected(TodoItemTypeUtils.TYPE_NORMAL);
        binding.editTodoDeleteButton.setOnClickListener(v -> {
            Toast.makeText(ActivityEditTodo.this, "未保存", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void initForEdit(Intent intent) {
        curr = (TodoItem) intent.getSerializableExtra(TAG_TODO_ITEM);
        onRadioButtonSelected(curr.type);
        binding.editTodoContent.setText(curr.content);
        binding.editTodoDeleteButton.setOnClickListener(v -> {
            Toast.makeText(ActivityEditTodo.this, "已删除", Toast.LENGTH_SHORT).show();
            new Thread(() -> dataUtil.deleteTodoItem(curr)).start();
            setResult(RESULT_OK);
            finish();
        });
        if (curr.type == TodoItemTypeUtils.TYPE_PERIODIC) binding.editTodoTypeRadioGroup.setButtonsDisabled(curr.type);
        else binding.editTodoTypeRadioGroup.setButtonChecked(curr.type);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void handleSubmit() {
        String newContent = binding.editTodoContent.getText().toString();
        int newType = binding.editTodoTypeRadioGroup.getType().typeId;
        if (newContent.trim().length() == 0) {
            Toast.makeText(this, "内容不应为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (isAdd) {
            TodoItem item = new TodoItem(newContent, newType, false);
            new Thread(() -> dataUtil.insertTodo(item)).start();
            if (newType == TodoItemTypeUtils.TYPE_PERIODIC) {
                TodoItem todayItem = new TodoItem(newContent, TodoItemTypeUtils.TYPE_NORMAL, true);
                new Thread(() -> dataUtil.insertTodo(todayItem)).start();
            }
            if (newType != TodoItemTypeUtils.TYPE_LATER) setResult(RESULT_OK);
        } else {
            if (curr.type != newType && newType == TodoItemTypeUtils.TYPE_PERIODIC) {
                TodoItem todayItem = new TodoItem(newContent, TodoItemTypeUtils.TYPE_NORMAL, true);
                new Thread(() -> dataUtil.insertTodo(todayItem)).start();
            }
            curr.content = newContent;
            curr.type = newType;
            new Thread(() -> dataUtil.updateTodoItem(curr)).start();
            setResult(RESULT_OK);
        }
        finish();
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            handleSubmit();
        }
        return false;
    }
//    class DatePickerListener implements FragmentDatePicker.DatePickerListener{
//        @Override
//        public void onDateChosen(String date) {
//            binding.editTodoPeriodStart.setText(date);
//        }
//    }
}