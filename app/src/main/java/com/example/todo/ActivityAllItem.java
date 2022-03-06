package com.example.todo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.todo.databinding.ActivityAllItemBinding;

public class ActivityAllItem extends AppCompatActivity {
    
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
        
//        initFragment();
    }

//    private void initFragment() {
//        FragmentAll fragmentAll = new FragmentAll(this);
//        FragmentManager manager = getSupportFragmentManager();
//        manager.beginTransaction()
//                .add(R.id.all_item_container, fragmentAll)
//                .commit();
//    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}