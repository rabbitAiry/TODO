package com.example.todo.utils

import com.example.todo.R

enum class TodoWrapper(
    var typeId: Int,
    var typeTextId: Int,
    var typeColorId: Int,
    var handler: NormalTodoSubmit?
) {
    AllTodoWrapper(TodoTypeUtil.TYPE_ALL, R.string.type_all, R.color.black, null),
    UrgentTodoWrapper(
        TodoTypeUtil.TYPE_URGENT,
        R.string.type_urgent,
        R.color.urgent,
        NormalTodoSubmit()
    ),
    PeriodicTodoWrapper(
        TodoTypeUtil.TYPE_PERIODIC,
        R.string.type_periodic,
        R.color.periodic,
        PeriodicTodoSubmit()
    ),
    NormalTodoWrapper(
        TodoTypeUtil.TYPE_NORMAL,
        R.string.type_normal,
        R.color.normal,
        NormalTodoSubmit()
    ),
    LaterTodoWrapper(
        TodoTypeUtil.TYPE_LATER,
        R.string.type_later,
        R.color.later,
        NormalTodoSubmit()
    );

    companion object {
        fun getTypeWrapperById(typeId: Int): TodoWrapper {
            val wrappers = values()
            for (wrapper in wrappers) {
                if (wrapper.typeId == typeId) return wrapper
            }
            return NormalTodoWrapper
        }
    }
}