package com.example.todo.data;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class TodoItem implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public long id;
    public String content;
    public int type;
    public boolean created;
    public int periodUnit;
    public int periodValue;
    public int periodTimes;
    public int periodTimesLeft;
    public int dateAdded;
    public int dateDelete;
    public boolean remind;
    public int remindTime;

    public TodoItem(String content,
                    int type,
                    boolean createdByItem,
                    int periodUnit,
                    int periodValue,
                    int periodTimes,
                    int periodTimesLeft,
                    int dateAdded,
                    boolean remind,
                    int remindTime) {
        this.content = content;
        this.type = type;
        this.created = createdByItem;
        this.periodUnit = periodUnit;
        this.periodValue = periodValue;
        this.periodTimes = periodTimes;
        this.periodTimesLeft = periodTimesLeft;
        this.dateAdded = dateAdded;
        this.remind = remind;
        this.remindTime = remindTime;
    }

    public TodoItem(String content, int type, boolean created) {
        this.content = content;
        this.type = type;
        this.created = created;
        this.periodUnit = TodoItemTypeUtils.NON_PERIOD;
        this.periodValue = 0;
        this.periodTimes = 0;
        this.periodTimesLeft = 0;
        this.dateAdded = TimeUtils.getTodayToken();
        this.remind = false;
        this.remindTime = 0;
    }

    @Ignore
    public TodoItem(long id) {
        this.id = id;
    }
}
