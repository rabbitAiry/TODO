package com.example.todo;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;

import com.example.todo.databinding.MainActivityBinding;

public class ActivityMain extends AppCompatActivity {
    private static final String TAG = "touch_event";
    private FragmentManager manager;
    private Fragment[] own = new Fragment[2];
    int fragmentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivityBinding binding = MainActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        initFragment();
        binding.mainContainer.setSwipeListener(toward -> {
            int index = fragmentIndex+toward;
            if(index>=own.length)index = own.length-1;
            if(index<0)index = 0;
            Log.d(TAG, "swapFragment: "+index);
            Fragment curr = own[index];
            manager.beginTransaction()
                    .replace(R.id.main_container, curr)
                    .commit();
        });
    }

    // fragmentNow 总是启动页面
    private void initFragment() {
        FragmentNow fragmentNow = new FragmentNow(this);
        FragmentMe fragmentMe = new FragmentMe();
        own[0] = fragmentNow;
        own[1] = fragmentMe;
        manager = getSupportFragmentManager();
        manager.beginTransaction()
                .add(R.id.main_container, fragmentNow)
                .commit();
    }
}