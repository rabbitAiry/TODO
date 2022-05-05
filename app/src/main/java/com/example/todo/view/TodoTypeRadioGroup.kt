package com.example.todo.view

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import com.example.todo.R
import com.example.todo.utils.TodoTypeUtil
import com.example.todo.utils.TodoWrapper

class TodoTypeRadioGroup(context: Context, attrs: AttributeSet? = null) :
    RadioGroup(context, attrs) {
    private var showButtonAll: Boolean = false  // 是否展示按钮“全部”
    private lateinit var radioButtonParams: LinearLayout.LayoutParams
    private lateinit var buttons: Array<RadioButton?>

    init {
        attrs?.let {
            val array: TypedArray =
                context.theme.obtainStyledAttributes(it, R.styleable.TodoTypeRadioGroup, 0, 0)
            showButtonAll = array.getBoolean(R.styleable.TodoTypeRadioGroup_showAllButton, false)
        }
        initRadioButtonParams()
        addRadioButtons()
    }

    private fun initRadioButtonParams() {
        radioButtonParams =
            LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, dp2px(32))
        radioButtonParams.gravity = Gravity.CENTER
        radioButtonParams.rightMargin = dp2px(4)
    }

    private fun addRadioButtons() {
        val typeList: Array<TodoWrapper> = TodoWrapper.values()
        if (showButtonAll) {
            buttons = arrayOfNulls(typeList.size)
            for (i in typeList.indices) buttons[i] = buttonInit(typeList[i], i)
            buttons[0]!!.isChecked = true
        } else {
            buttons = arrayOfNulls(typeList.size - 1)
            for (i in 1 until typeList.size) buttons[i - 1] = buttonInit(typeList[i], i - 1)
        }
    }

    /**
     * RadioButton工厂方法
     */
    private fun buttonInit(wrapper: TodoWrapper, idx: Int): RadioButton {
        val button = RadioButton(context)
        button.setTextAppearance(R.style.TextAppearance_MaterialComponents_Subtitle1)
        button.gravity = Gravity.CENTER
        button.setTextColor(resources.getColor(R.color.grey40, context.theme))
        button.text = TodoTypeUtil.getText(wrapper.typeTextId, context)
        button.id = wrapper.typeId
        super.addView(button, idx, radioButtonParams)
        return button
    }

    fun setButtonChecked(typeId: Int) {
        for (item in buttons) {
            item?.let { if (it.id == typeId) it.isChecked = true }
        }
    }

    fun setButtonDisabledFor(expectedTypeId: Int) {
        for (item in buttons) {
            item?.let {
                if (it.id == expectedTypeId) {
                    it.isChecked = true
                } else {
                    it.isEnabled = false
                }
            }
        }
    }

    fun getType(): TodoWrapper = TodoWrapper.getTypeWrapperById(checkedRadioButtonId)

    private fun dp2px(value: Int): Int {
        val scale = context.resources.displayMetrics.density
        return (value * scale + 0.5f).toInt()
    }
}