package com.example.todo;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class FragmentNow extends Fragment {
    private Cursor cursorNow;
    private TextView buttonAdd;
    public static final int FRAGMENT_NOW_REQUEST_CODE = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewNow = inflater.inflate(R.layout.fragment_now_todo, container, false);
        RecyclerView recyclerViewNowList = viewNow.findViewById(R.id.recyclerView_now_list);
        buttonAdd = viewNow.findViewById(R.id.button_add);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), AddTodoActivity.class);
                startActivityForResult(i, FRAGMENT_NOW_REQUEST_CODE);
            }
        });

        return viewNow;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}
