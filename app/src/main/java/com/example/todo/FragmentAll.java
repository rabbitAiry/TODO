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
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo.data.TodoContract.TodoEntry;
import com.example.todo.data.TypeUtils;

public class FragmentAll extends Fragment {
    private Context context;
    private SQLiteDatabase todoReadDatabase;
    private AllAdapter adapter;
    //    private RadioGroup fragmentAllRadioGroup;
    private View fragmentAllRadioIndicatorAll;
    private View fragmentAllRadioIndicatorNormal;
    private View fragmentAllRadioIndicatorUrgent;
    private View fragmentAllRadioIndicatorDaily;
    private View fragmentAllRadioIndicatorCold;
    private static final String TAG = "fragmentAll";
    private static int typeKeyNow = TypeUtils.TYPE_ALL;

    public FragmentAll(Context context, SQLiteDatabase db) {
        this.context = context;
        todoReadDatabase = db;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewAll = inflater.inflate(R.layout.fragment_all_todo, container, false);

        RadioButton fragmentAllRadioButtonAll = viewAll.findViewById(R.id.fragment_radio_button_all);
        RadioButton fragmentAllRadioButtonNormal = viewAll.findViewById(R.id.fragment_radio_button_normal);
        RadioButton fragmentAllRadioButtonUrgent = viewAll.findViewById(R.id.fragment_radio_button_urgent);
        RadioButton fragmentAllRadioButtonDaily = viewAll.findViewById(R.id.fragment_radio_button_daily);
        RadioButton fragmentAllRadioButtonCold = viewAll.findViewById(R.id.fragment_radio_button_cold);
        fragmentAllRadioIndicatorAll = viewAll.findViewById(R.id.fragment_all_radio_indicator_all);
        fragmentAllRadioIndicatorNormal = viewAll.findViewById(R.id.fragment_all_radio_indicator_normal);
        fragmentAllRadioIndicatorUrgent = viewAll.findViewById(R.id.fragment_all_radio_indicator_urgent);
        fragmentAllRadioIndicatorDaily = viewAll.findViewById(R.id.fragment_all_radio_indicator_daily);
        fragmentAllRadioIndicatorCold = viewAll.findViewById(R.id.fragment_all_radio_indicator_cold);

        RecyclerView recyclerViewAllList = viewAll.findViewById(R.id.recyclerView_all_list);
        Cursor cursorAll = getCursorAll();
        fragmentAllRadioButtonAll.setChecked(true);
        adapter = new AllAdapter(context, cursorAll, new AllTodoClickedEvent());
        recyclerViewAllList.setAdapter(adapter);
        updateView(typeKeyNow);
        recyclerViewAllList.setLayoutManager(new LinearLayoutManager(context));

        fragmentAllRadioButtonAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateView(TypeUtils.TYPE_ALL);
            }
        });
        fragmentAllRadioButtonNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateView(TypeUtils.TYPE_NORMAL);
            }
        });
        fragmentAllRadioButtonUrgent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateView(TypeUtils.TYPE_URGENT);
            }
        });
        fragmentAllRadioButtonDaily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateView(TypeUtils.TYPE_DAILY);
            }
        });
        fragmentAllRadioButtonCold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateView(TypeUtils.TYPE_COLD);
            }
        });

        return viewAll;
    }

    private Cursor getCursorAll() {
        return todoReadDatabase.query(TodoEntry.TABLE_TODO,
                null,
                TodoEntry.TODO_COLUMN_IS_CREATED_BY_DAILY + "=0",
                null,
                null,
                null,
                TodoEntry.TODO_COLUMN_TYPE);
    }

    private void updateView(int typeKey) {
        Cursor cursor;
        fragmentAllRadioIndicatorAll.setVisibility(View.INVISIBLE);
        fragmentAllRadioIndicatorNormal.setVisibility(View.INVISIBLE);
        fragmentAllRadioIndicatorUrgent.setVisibility(View.INVISIBLE);
        fragmentAllRadioIndicatorDaily.setVisibility(View.INVISIBLE);
        fragmentAllRadioIndicatorCold.setVisibility(View.INVISIBLE);
        switch (typeKey) {
            case TypeUtils.TYPE_ALL:
                fragmentAllRadioIndicatorAll.setVisibility(View.VISIBLE);
                typeKeyNow = TypeUtils.TYPE_ALL;
                adapter.swapCursor(getCursorAll());
                return;
            case TypeUtils.TYPE_NORMAL:
                fragmentAllRadioIndicatorNormal.setVisibility(View.VISIBLE);
                typeKeyNow = TypeUtils.TYPE_NORMAL;
                break;
            case TypeUtils.TYPE_URGENT:
                fragmentAllRadioIndicatorUrgent.setVisibility(View.VISIBLE);
                typeKeyNow = TypeUtils.TYPE_URGENT;
                break;
            case TypeUtils.TYPE_DAILY:
                fragmentAllRadioIndicatorDaily.setVisibility(View.VISIBLE);
                typeKeyNow = TypeUtils.TYPE_DAILY;
                break;
            case TypeUtils.TYPE_COLD:
                fragmentAllRadioIndicatorCold.setVisibility(View.VISIBLE);
                typeKeyNow = TypeUtils.TYPE_COLD;
                break;
        }
        cursor = todoReadDatabase.query(TodoEntry.TABLE_TODO,
                null,
                TodoEntry.TODO_COLUMN_IS_CREATED_BY_DAILY + "=0 AND " + TodoEntry.TODO_COLUMN_TYPE + "=" + typeKey,
                null,
                null,
                null,
                null);
        adapter.swapCursor(cursor);
    }

    class AllTodoClickedEvent implements AllAdapter.LongPressedListener {
        @Override
        public void gotoEditActivity(Intent intent) {
            startActivityForResult(intent, EditTodoActivity.NOW_EDIT_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == MainActivity.RESULT_OK) {
            if (requestCode == EditTodoActivity.NOW_EDIT_CODE) {
                updateView(typeKeyNow);
            }
        }
    }
}





