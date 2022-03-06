package com.example.todo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.todo.data.TodoItem;
import com.example.todo.data.TodoItemDataUtil;
import com.example.todo.data.TodoItemUtils;
import com.example.todo.databinding.ActivityEditTodoBinding;

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

        dataUtil = TodoItemDataUtil.getInstance(this);
        Intent intent = getIntent();
        isAdd = intent.getIntExtra(MODE, MODE_ADD) == MODE_ADD;
        if(isAdd)initForAdd();
        else initForEdit(intent);
        // TODO: 周期设置 - 暂不可用
        binding.editTodoPeriodBackgroundMask.setVisibility(View.VISIBLE);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(isAdd? R.string.add_todo:R.string.edit_todo);
        }
        binding.editTodoButtonSubmit.setOnClickListener(v -> handleSubmit());
        binding.editTodoButtonNormal.setOnClickListener(v -> onRadioButtonClick(TodoItemUtils.TYPE_NORMAL));
        binding.editTodoButtonUrgent.setOnClickListener(v -> onRadioButtonClick(TodoItemUtils.TYPE_URGENT));
        binding.editTodoButtonPeriodic.setOnClickListener(v -> onRadioButtonClick(TodoItemUtils.TYPE_PERIODIC));
        binding.editTodoButtonLater.setOnClickListener(v -> onRadioButtonClick(TodoItemUtils.TYPE_LATER));

//        binding.editTodoPeriodStart.setOnClickListener(v -> {
//            DialogFragment fragment = new FragmentDatePicker(new DatePickerListener());
//            fragment.show(getSupportFragmentManager(), "datePicker");
//        });
//        binding.editTodoPeriodStart.setText(TimeUtils.getTodayText());
    }

    private void initForAdd() {
        binding.editTodoTypeBar.setBackgroundColor(TodoItemUtils.getTypeColor(TodoItemUtils.TYPE_NORMAL, this));
        binding.editTodoButtonNormal.setChecked(true);
        binding.editTodoDeleteButton.setOnClickListener(v -> {
            Toast.makeText(ActivityEditTodo.this, "未保存", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void initForEdit(Intent intent) {
        curr = (TodoItem) intent.getSerializableExtra(TAG_TODO_ITEM);
        binding.editTodoTypeBar.setBackgroundColor(TodoItemUtils.getTypeColor(curr.type, this));
        binding.editTodoContent.setText(curr.content);
        binding.editTodoDeleteButton.setOnClickListener(v -> {
            Toast.makeText(ActivityEditTodo.this, "已删除", Toast.LENGTH_SHORT).show();
            new Thread(() -> dataUtil.deleteTodoItem(curr)).start();
            setResult(RESULT_OK);
            finish();
        });
        // TODO: layout of period

        switch (curr.type) {
            case TodoItemUtils.TYPE_NORMAL:
                binding.editTodoButtonNormal.setChecked(true);
                break;
            case TodoItemUtils.TYPE_URGENT:
                binding.editTodoButtonUrgent.setChecked(true);
                break;
            case TodoItemUtils.TYPE_PERIODIC:
                binding.editTodoButtonPeriodic.setChecked(true);
                binding.editTodoButtonNormal.setEnabled(false);
                binding.editTodoButtonUrgent.setEnabled(false);
                binding.editTodoButtonLater.setEnabled(false);
                break;
            case TodoItemUtils.TYPE_LATER:
                binding.editTodoButtonLater.setChecked(true);
        }

    }

    private void onRadioButtonClick(int type){
        // TODO 允许设置周期的情况
        int typeColor;
        switch (type) {
            default:
            case TodoItemUtils.TYPE_NORMAL:
                typeColor = TodoItemUtils.getTypeColor(TodoItemUtils.TYPE_NORMAL, this);
                break;
            case TodoItemUtils.TYPE_URGENT:
                typeColor = TodoItemUtils.getTypeColor(TodoItemUtils.TYPE_URGENT, this);
                break;
            case TodoItemUtils.TYPE_PERIODIC:
                typeColor = TodoItemUtils.getTypeColor(TodoItemUtils.TYPE_PERIODIC, this);
                break;
            case TodoItemUtils.TYPE_LATER:
                typeColor = TodoItemUtils.getTypeColor(TodoItemUtils.TYPE_LATER, this);
        }
        binding.editTodoTypeBar.setBackgroundColor(typeColor);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("NonConstantResourceId")
    private int getType(){
        int type = TodoItemUtils.TYPE_NORMAL;
        switch (binding.editTodoTypeRadioGroup.getCheckedRadioButtonId()) {
            case R.id.edit_todo_button_normal:
                type = TodoItemUtils.TYPE_NORMAL;
                break;
            case R.id.edit_todo_button_urgent:
                type = TodoItemUtils.TYPE_URGENT;
                break;
            case R.id.edit_todo_button_periodic:
                type = TodoItemUtils.TYPE_PERIODIC;
                break;
            case R.id.edit_todo_button_later:
                type = TodoItemUtils.TYPE_LATER;
                break;
        }
        return type;
    }

    private void handleSubmit() {
        String newContent = binding.editTodoContent.getText().toString();
        int newType = getType();
        if (newContent.trim().length() == 0) {
            Toast.makeText(this, "内容不应为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if(isAdd){
            TodoItem item = new TodoItem(newContent, newType, false);
            new Thread(()-> dataUtil.insertTodo(item)).start();
            if (newType == TodoItemUtils.TYPE_PERIODIC) {
                TodoItem todayItem = new TodoItem(newContent, TodoItemUtils.TYPE_NORMAL, true);
                new Thread(()-> dataUtil.insertTodo(todayItem)).start();
            }
            if (newType != TodoItemUtils.TYPE_LATER) setResult(RESULT_OK);
        }else{
            if (curr.type != newType && newType == TodoItemUtils.TYPE_PERIODIC) {
                TodoItem todayItem = new TodoItem(newContent, TodoItemUtils.TYPE_NORMAL, true);
                new Thread(()-> dataUtil.insertTodo(todayItem)).start();
            }
            curr.content = newContent;
            curr.type = newType;
            new Thread(()-> dataUtil.updateTodoItem(curr)).start();
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