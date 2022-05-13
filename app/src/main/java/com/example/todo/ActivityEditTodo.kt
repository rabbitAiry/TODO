package com.example.todo

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.todo.data.PeriodData
import com.example.todo.data.TodoItem
import com.example.todo.databinding.ActivityEditTodoBinding
import com.example.todo.utils.*
import com.example.todo.view.DateOrTimeSetListener
import com.example.todo.view.DatePickerFragment
import com.example.todo.view.TimePickerFragment
import com.example.todo.view.TypeIndicator

class ActivityEditTodo : AppCompatActivity() {

    companion object {
        const val TODO_OBJECT = "todo"
        const val mode = "mode"
        const val MODE_ADD = 1
        const val MODE_EDIT = 2
    }

    private lateinit var binding: ActivityEditTodoBinding
    private var curr: TodoItem? = null
    private var isAdd: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditTodoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        parseModeAndInit()
    }

    private fun onRadioButtonSelected(type: Int) {
        binding.editTodoTypeBar.background =
            TypeIndicator(TodoTypeUtil.getTypeColorByTypeId(type, this))
        if (type == TodoTypeUtil.TYPE_PERIODIC) {
            binding.editTodoPeriodBlock.visibility = View.VISIBLE
            binding.editTodoPeriodStart.text = TimeDataUtil.getTodayText()
        } else {
            binding.editTodoPeriodBlock.visibility = View.GONE
        }
    }

    private fun parseModeAndInit() {
        isAdd = intent.getIntExtra(mode, MODE_ADD) == MODE_ADD
        initView()
        if (!isAdd) initForModeEdit(intent)
    }

    private fun initView() {
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.title = resources.getText(if (isAdd) R.string.add_todo else R.string.edit_todo)
        }
        // type
        binding.editTodoTypeRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            onRadioButtonSelected(checkedId)
        }
        binding.editTodoTypeRadioGroup.setButtonChecked(TodoTypeUtil.TYPE_NORMAL)
        onRadioButtonSelected(TodoTypeUtil.TYPE_NORMAL)

        // period
        binding.editTodoPeriodRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            binding.editTodoPeriodUnit.text = TodoTypeUtil.getText(
                PeriodInfo.getPeriodInfo(getPeriodType(checkedId)).textId,
                this
            )
        }
        binding.editTodoPeriodDuration.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) getNum(
                binding.editTodoPeriodDuration.text.toString()
            )
        }
        binding.editTodoPeriodUnit.text = TodoTypeUtil.getText(PeriodInfo.PERIOD_DAY.textId, this)
        binding.editTodoPeriodStart.text = TimeDataUtil.getTodayText()
        binding.editTodoPeriodStart.setOnClickListener {
            val fragment = DatePickerFragment()
            fragment.setResultListener(object : DateOrTimeSetListener {
                override fun setResult(dateOrTimeText: String) {
                    binding.editTodoPeriodStart.text = dateOrTimeText
                    dateAddedValidCheck(TimeDataUtil.getDateToken(dateOrTimeText))
                }
            })
            fragment.show(supportFragmentManager, "datePicker")
        }
        binding.editTodoTimes.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) getNum(
                binding.editTodoTimes.text.toString()
            )
        }
        binding.editTodoPeriodTimesCheckbox.setOnCheckedChangeListener { _, isChecked ->
            binding.editTodoTimes.isEnabled = !isChecked
        }

        // remind
        binding.editTodoRemindTimeBlock.visibility = View.INVISIBLE
        binding.editTodoRemindTime.text = TimeDataUtil.getNowTimeText()
        binding.editTodoRemindTime.setOnClickListener {
            val fragment = TimePickerFragment()
            fragment.setResultListener(object : DateOrTimeSetListener {
                override fun setResult(dateOrTimeText: String) {
                    binding.editTodoRemindTime.text = dateOrTimeText
                }
            })
            fragment.show(supportFragmentManager, "timePicker")
        }
        binding.editTodoRemindCheckbox.setOnCheckedChangeListener { _, isChecked ->
            binding.editTodoRemindTimeBlock.visibility =
                if (isChecked) View.VISIBLE else View.INVISIBLE
        }
        binding.editTodoDeleteButton.setOnClickListener {
            Toast.makeText(this, R.string.not_saved, Toast.LENGTH_SHORT).show()
            finish()
        }
        binding.editTodoButtonSubmit.setOnClickListener { handleSubmit() }
    }

    private fun initForModeEdit(intent: Intent) {
        curr = intent.getSerializableExtra(TODO_OBJECT) as TodoItem
        curr?.let {
            binding.editTodoContent.setText(it.content)
            binding.editTodoTypeRadioGroup.setButtonChecked(it.type!!)
            onRadioButtonSelected(it.type!!)
            if (it.type == TodoTypeUtil.TYPE_PERIODIC) {
                binding.editTodoPeriodDuration.setText(it.periodValue.toString())
                binding.editTodoTimes.setText(it.periodTimes.toString())
                Toast.makeText(
                    this,
                    if (it.periodTimesLeft < 0) "无限次" else "还剩${it.periodTimesLeft}次",
                    Toast.LENGTH_SHORT
                ).show()
                binding.editTodoPeriodStart.text = TimeDataUtil.getDateText(it.dateAdded)
                binding.editTodoRemindTime.text = TimeDataUtil.getTimeText(it.remindTime)
            }
            binding.editTodoDeleteButton.setOnClickListener { _ ->
                Toast.makeText(this, R.string.deleted, Toast.LENGTH_SHORT).show()
                ThreadPoolUtil.submitTask { TodoDataUtil.deleteTodo(it, this) }
                setResult(RESULT_OK)
                finish()
            }
        }
    }

    private fun handleSubmit() {
        val content = binding.editTodoContent.text.toString()
        val type = binding.editTodoTypeRadioGroup.getType().typeId
        val periodUnit = getPeriodType(binding.editTodoPeriodRadioGroup.checkedRadioButtonId)
        val periodValue = getNum(binding.editTodoPeriodDuration.text.toString())
        val periodTimes =
            if (binding.editTodoPeriodTimesCheckbox.isChecked) -1 else getNum(binding.editTodoTimes.text.toString())
        val startTime = TimeDataUtil.getDateToken(binding.editTodoPeriodStart.text.toString())
        if (type == TodoTypeUtil.TYPE_PERIODIC && !submitValidCheck(
                content,
                periodValue,
                periodTimes,
                startTime
            )
        ) return

        val period = if (type == TodoTypeUtil.TYPE_PERIODIC) PeriodData(
            startTime,
            periodUnit,
            periodValue,
            periodTimes
        ) else null
        if (isAdd)
            TodoWrapper.getTypeWrapperById(type).handler!!.add(this, content, type, period)
        else {
            curr?.let {
                if ((periodUnit != it.periodUnit || periodValue != it.periodValue) && type == TodoTypeUtil.TYPE_PERIODIC) {
                    binding.editTodoPeriodStart.text = TimeDataUtil.getTodayText()
                    Toast.makeText(this, "周期变更后，起始日期会发生更改，请确认", Toast.LENGTH_SHORT).show()
                    return
                }
                TodoWrapper.getTypeWrapperById(type).handler!!
                    .update(this, curr!!, content, type, period)
            }
        }
        if (!isAdd || type != TodoTypeUtil.TYPE_LATER) setResult(RESULT_OK)
        finish()
    }

    private fun submitValidCheck(
        content: String,
        periodValue: Int,
        periodTimes: Int,
        startTime: Int
    ): Boolean {
        if (content.trim().isEmpty()) {
            Toast.makeText(this, R.string.content_should_not_be_empty, Toast.LENGTH_SHORT).show()
            return false
        }
        if (periodValue == 0 || periodTimes == 0) return false
        if (!dateAddedValidCheck(startTime)) return false
        return true
    }

    private fun dateAddedValidCheck(startToken: Int): Boolean {
        if (startToken < TimeDataUtil.getTodayToken()) {
            Toast.makeText(
                this@ActivityEditTodo,
                "请选择从今往后的日子！",
                Toast.LENGTH_SHORT
            ).show()
            return false
        }
        return true
    }

    private fun getPeriodType(type: Int): Int = when (type) {
        R.id.edit_todo_button_year -> TodoTypeUtil.PERIOD_YEAR
        R.id.edit_todo_button_month -> TodoTypeUtil.PERIOD_MONTH
        R.id.edit_todo_button_week -> TodoTypeUtil.PERIOD_WEEK
        else -> TodoTypeUtil.PERIOD_DAY
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            Toast.makeText(this, if (isAdd) "未保存" else "未保存更改", Toast.LENGTH_SHORT).show()
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * return:
     *  -1  无限次
     *  0   无效
     *  other 有效
     *
     */
    private fun getNum(s: String): Int {
        if (s.isBlank()) {
            Toast.makeText(
                this@ActivityEditTodo,
                R.string.content_should_not_be_empty,
                Toast.LENGTH_SHORT
            ).show()
            return 0
        } else {
            try {
                val num = s.trim().toInt()
                when {
                    num < 1 -> Toast.makeText(
                        this@ActivityEditTodo,
                        R.string.invalid_number,
                        Toast.LENGTH_SHORT
                    ).show()
                    num > 999 -> Toast.makeText(
                        this@ActivityEditTodo,
                        R.string.number_cannot_be_too_large,
                        Toast.LENGTH_SHORT
                    ).show()
                    else -> return num
                }
                return 0
            } catch (e: Exception) {
                Toast.makeText(this@ActivityEditTodo, R.string.invalid_number, Toast.LENGTH_SHORT)
                    .show()
                return 0
            }
        }
    }
}