package com.example.todo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.todo.data.TodoItemDataUtil;
import com.example.todo.data.TodoItem;
import com.example.todo.databinding.FragmentNowTodoBinding;
import com.example.todo.adapter.TodoItemAdapter;
import com.example.todo.adapter.TodoNowAdapter;

import java.util.List;

public class FragmentNow extends Fragment{
    private static final String TAG = "ToUrgent";
    private FragmentNowTodoBinding binding;
    private final Context context;
    private TodoNowAdapter adapter;
    private TodoItemDataUtil todoDataUtil;
    public static int REQUEST_EDIT = 1;

    public FragmentNow(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentNowTodoBinding.inflate(inflater, container, false);
        View viewNow = binding.getRoot();

        inflateList();
        binding.fragmentNowButtonAdd.setOnClickListener(v -> {
            Intent intent = new Intent(context, ActivityEditTodo.class);
            intent.putExtra(ActivityEditTodo.MODE, ActivityEditTodo.MODE_ADD);
            startActivityForResult(intent, REQUEST_EDIT);
        });

        return viewNow;
    }

    private void inflateList() {
        new Thread(() -> {
            todoDataUtil = TodoItemDataUtil.getInstance(context);
            adapter = new TodoNowAdapter(todoDataUtil.getNowTodoItem(), context, new TodoItemAdapter.TodoListener() {
                @Override
                public void onItemLongClickListener(TodoItem item) {
                    Intent intent = new Intent(context, ActivityEditTodo.class);
                    intent.putExtra(ActivityEditTodo.TAG_TODO_ITEM, item);
                    intent.putExtra(ActivityEditTodo.MODE, ActivityEditTodo.MODE_EDIT);
                    startActivityForResult(intent, REQUEST_EDIT);
                }

                @Override
                public void onDoneClickListener(TodoItem item) {
                    new Thread(() -> todoDataUtil.deleteTodoItem(item)).start();
                }

                @Override
                public void toUrgentClickListener(TodoItem item) {
                    new Thread(() -> {
                        Log.d(TAG, "toUrgentClickListener: ");
                        todoDataUtil.updateTodoItem(item);
                        List<TodoItem> list = todoDataUtil.getNowTodoItem();
                        getActivity().runOnUiThread(() -> adapter.refreshData(list));
                    }).start();
                }
            });
            getActivity().runOnUiThread(() -> {
                binding.fragmentNowList.setLayoutManager(new LinearLayoutManager(context));
                binding.fragmentNowList.setAdapter(adapter);
            });
        }).start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ActivityMain.RESULT_OK) {
            if (requestCode == REQUEST_EDIT) {
                new Thread(() -> {
                    List<TodoItem> list = todoDataUtil.getNowTodoItem();
                    getActivity().runOnUiThread(() -> adapter.refreshData(list));
                }).start();
            }
        }
    }
}
