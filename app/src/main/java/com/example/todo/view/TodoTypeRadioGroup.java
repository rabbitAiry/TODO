package com.example.todo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.todo.R;
import com.example.todo.TodoItemWrapper.TodoItemWrapper;
import com.example.todo.data.TodoItemTypeUtils;

import java.util.Arrays;

public class TodoTypeRadioGroup extends RadioGroup {
    private boolean showAllButton;
    private LinearLayout.LayoutParams radioButtonParams;
    private final Context context;
    private RadioButton[] buttons;

    public TodoTypeRadioGroup(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public TodoTypeRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TodoTypeRadioGroup, 0, 0);
        showAllButton = array.getBoolean(R.styleable.TodoTypeRadioGroup_showAllButton, false);
        init();
    }

    private void init() {
        initRadioButtonParams();
        addRadioButtons();
    }

    private void initRadioButtonParams() {
        radioButtonParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, dp2px(32));
        radioButtonParams.gravity = Gravity.CENTER;
        radioButtonParams.rightMargin = dp2px(4);
    }

    private void addRadioButtons() {
        TodoItemWrapper[] typeList = TodoItemTypeUtils.getTypeList();
        if (!showAllButton) typeList = Arrays.copyOfRange(typeList, 1, typeList.length);
        buttons = new RadioButton[typeList.length];
        for (int i = 0; i < typeList.length; i++) {
            RadioButton button = new RadioButton(context);
            button.setTextAppearance(R.style.TextAppearance_MaterialComponents_Subtitle1);
            button.setGravity(Gravity.CENTER);
            button.setTextColor(getResources().getColor(R.color.grey40, context.getTheme()));
            button.setText(TodoItemTypeUtils.getText(typeList[i].typeTextId, context));
            button.setId(typeList[i].typeId);
            buttons[i] = button;
            super.addView(button, i, radioButtonParams);
        }
        if (showAllButton)buttons[0].setChecked(true);
    }

    public void setButtonChecked(int typeId) {
        for (RadioButton curr : buttons) {
            if (curr.getId() == typeId) {
                curr.setChecked(true);
                break;
            }
        }
    }

    public void setButtonsDisabled(int exceptionTypeId) {
        for (RadioButton curr : buttons) {
            if (curr.getId() != exceptionTypeId) curr.setEnabled(false);
            else curr.setChecked(true);
        }
    }

    public TodoItemWrapper getType() {
        int typeId = getCheckedRadioButtonId();
        return TodoItemTypeUtils.getTypeWrapperById(typeId);
    }

    int dp2px(int dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
