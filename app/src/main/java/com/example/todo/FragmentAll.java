package com.example.todo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.todo.data.TypeList;

public class FragmentAll extends Fragment {
    private RadioGroup fragmentAllRadioGroup;
    private RadioButton fragmentAllRadioButtonAll;
    private RadioButton fragmentAllRadioButtonNormal;
    private RadioButton fragmentAllRadioButtonUrgent;
    private RadioButton fragmentAllRadioButtonDaily;
    private RadioButton fragmentAllRadioButtonCold;
    private View fragmentAllRadioIndicatorAll;
    private View fragmentAllRadioIndicatorNormal;
    private View fragmentAllRadioIndicatorUrgent;
    private View fragmentAllRadioIndicatorDaily;
    private View fragmentAllRadioIndicatorCold;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewAll = inflater.inflate(R.layout.fragment_all_todo, container, false);

        fragmentAllRadioGroup = viewAll.findViewById(R.id.fragment_all_radio_group);
        fragmentAllRadioButtonAll = viewAll.findViewById(R.id.fragment_radio_button_all);
        fragmentAllRadioButtonNormal = viewAll.findViewById(R.id.fragment_radio_button_normal);
        fragmentAllRadioButtonUrgent = viewAll.findViewById(R.id.fragment_radio_button_urgent);
        fragmentAllRadioButtonDaily = viewAll.findViewById(R.id.fragment_radio_button_daily);
        fragmentAllRadioButtonCold = viewAll.findViewById(R.id.fragment_radio_button_cold);
        fragmentAllRadioIndicatorAll = viewAll.findViewById(R.id.fragment_all_radio_indicator_all);
        fragmentAllRadioIndicatorNormal = viewAll.findViewById(R.id.fragment_all_radio_indicator_normal);
        fragmentAllRadioIndicatorUrgent = viewAll.findViewById(R.id.fragment_all_radio_indicator_urgent);
        fragmentAllRadioIndicatorDaily = viewAll.findViewById(R.id.fragment_all_radio_indicator_daily);
        fragmentAllRadioIndicatorCold = viewAll.findViewById(R.id.fragment_all_radio_indicator_cold);

        fragmentAllRadioButtonAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setIndicator(TypeList.TYPE_ALL);
            }
        });
        fragmentAllRadioButtonNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setIndicator(TypeList.TYPE_NORMAL);
            }
        });
        fragmentAllRadioButtonUrgent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setIndicator(TypeList.TYPE_URGENT);
            }
        });
        fragmentAllRadioButtonDaily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setIndicator(TypeList.TYPE_DAILY);
            }
        });
        fragmentAllRadioButtonCold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setIndicator(TypeList.TYPE_COLD);
            }
        });

        return viewAll;
    }

    private void setIndicator(int typeKey) {
//        fragmentAllRadioIndicatorAll;
//        fragmentAllRadioIndicatorNormal;
//        fragmentAllRadioIndicatorUrgent;
//        fragmentAllRadioIndicatorDaily;
//        fragmentAllRadioIndicatorCold;
        fragmentAllRadioIndicatorAll.setVisibility(View.INVISIBLE);
        fragmentAllRadioIndicatorNormal.setVisibility(View.INVISIBLE);
        fragmentAllRadioIndicatorUrgent.setVisibility(View.INVISIBLE);
        fragmentAllRadioIndicatorDaily.setVisibility(View.INVISIBLE);
        fragmentAllRadioIndicatorCold.setVisibility(View.INVISIBLE);
        switch (typeKey) {
            case TypeList.TYPE_ALL:
                fragmentAllRadioIndicatorAll.setVisibility(View.VISIBLE);
                break;
            case TypeList.TYPE_NORMAL:
                fragmentAllRadioIndicatorNormal.setVisibility(View.VISIBLE);
                break;
            case TypeList.TYPE_URGENT:
                fragmentAllRadioIndicatorUrgent.setVisibility(View.VISIBLE);
                break;
            case TypeList.TYPE_DAILY:
                fragmentAllRadioIndicatorDaily.setVisibility(View.VISIBLE);
                break;
            case TypeList.TYPE_COLD:
                fragmentAllRadioIndicatorCold.setVisibility(View.VISIBLE);
                break;
        }
    }


}





