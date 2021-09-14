package com.example.todo;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo.data.TodoContract;
import com.example.todo.data.TodoHelper;
import com.example.todo.data.TypeUtils;

public class FragmentNow extends Fragment {
    private SQLiteDatabase todoReadDatabase;
    private final NowAdapter.NowButtonClickedListener listener;
    private final Context context;
    private NowAdapter adapter;
    private static final String TAG = "Now";

    public FragmentNow(Context context, SQLiteDatabase db, NowAdapter.NowButtonClickedListener listener) {
        this.context = context;
        this.listener = listener;
        todoReadDatabase = db;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewNow = inflater.inflate(R.layout.fragment_now_todo, container, false);
        RecyclerView recyclerViewNowList = viewNow.findViewById(R.id.recyclerView_now_list);
        TextView buttonAdd = viewNow.findViewById(R.id.button_add);
        Log.d(TAG, "onCreateView: after using db");

        adapter = new NowAdapter(context, getCursorData(), listener, new FragmentNow.AllTodoClickedEvent());
        recyclerViewNowList.setLayoutManager(new LinearLayoutManager(context));
        recyclerViewNowList.setAdapter(adapter);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), AddTodoActivity.class);
                startActivityForResult(i, AddTodoActivity.NOW_ADD_CODE);
            }
        });

        return viewNow;
    }

    public Cursor getCursorData() {
        Log.d(TAG, "getCursorData: ready to get" + todoReadDatabase);
        Cursor cursor1 = null;
        try {
            cursor1 = todoReadDatabase.query(TodoContract.TodoEntry.TABLE_TODO,
                    null,
                    TodoContract.TodoEntry.TODO_COLUMN_TYPE + " IN(?,?)",
                    new String[]{Integer.toString(TypeUtils.TYPE_URGENT), Integer.toString(TypeUtils.TYPE_NORMAL)},
                    null,
                    null,
                    TodoContract.TodoEntry.TODO_COLUMN_TYPE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cursor1;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == MainActivity.RESULT_OK) {
            if (requestCode == AddTodoActivity.NOW_ADD_CODE || requestCode == EditTodoActivity.NOW_EDIT_CODE) {
                adapter.swapCursor(getCursorData());
            }
        }
    }

    class AllTodoClickedEvent implements AllAdapter.LongPressedListener {
        @Override
        public void gotoEditActivity(Intent intent) {
            startActivityForResult(intent, EditTodoActivity.NOW_EDIT_CODE);
        }
    }
}
