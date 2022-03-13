package com.example.todo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.widget.RadioGroup;

import com.example.todo.adapter.TodoItemAdapter;
import com.example.todo.data.TodoItem;
import com.example.todo.data.TodoItemDataUtil;
import com.example.todo.data.TodoItemTypeUtils;
import com.example.todo.databinding.ActivityAllItemBinding;

import java.util.List;

public class ActivityAllItem extends AppCompatActivity {
    private TodoItemAdapter adapter;
    private int currType = TodoItemTypeUtils.TYPE_ALL;
    public static int REQUEST_EDIT = 1;
    public static int MESSAGE_UPDATE = 2;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if(msg.what == MESSAGE_UPDATE){
                List<TodoItem> list = (List<TodoItem>) msg.obj;
                adapter.refreshData(list);
            }
            return false;
        }
    });

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityAllItemBinding binding = ActivityAllItemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);    
            actionBar.setTitle(R.string.all_item);
        }

        binding.allItemList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TodoItemAdapter(null, this, new TodoItemAdapter.TodoListener() {
            @Override
            public void onItemLongClickListener(TodoItem item) {
                Intent intent = new Intent(ActivityAllItem.this, ActivityEditTodo.class);
                intent.putExtra(ActivityEditTodo.TAG_TODO_ITEM, item);
                intent.putExtra(ActivityEditTodo.MODE, ActivityEditTodo.MODE_EDIT);
                startActivityForResult(intent, REQUEST_EDIT);
            }

            @Override
            public void onDoneClickListener(TodoItem item) {
                // do nothing
            }

            @Override
            public void toUrgentClickListener(TodoItem item) {
                // do nothing
            }
        });
        binding.allItemList.setAdapter(adapter);
        updateList(currType);
        binding.fragmentAllRadioGroup.setOnCheckedChangeListener((group, checkedId) -> updateList(checkedId));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ActivityMain.RESULT_OK) {
            if (requestCode == REQUEST_EDIT) {
                updateList(currType);
            }
        }
    }

    private void updateList(int type){
        new Thread(() -> {
            TodoItemDataUtil dataUtil = TodoItemDataUtil.getInstance(this);
            Message message = Message.obtain(handler,
                    MESSAGE_UPDATE,
                    dataUtil.getSpecifiedTodoItem(type));
            handler.sendMessage(message);
        }).start();
    }
}