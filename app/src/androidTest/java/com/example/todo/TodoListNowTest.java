package com.example.todo;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.longClick;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class TodoListNowTest {

    @Rule
    public ActivityTestRule<ActivityMain> activityMainActivityTestRule =
            new ActivityTestRule<>(ActivityMain.class);

    @Test
    public void testTagButtonNowAndAll(){
        onView(withId(R.id.main_toolbar_textview_all)).perform(click());
        onView(withId(R.id.main_toolbar_textview_now)).perform(click());
    }

    @Test
    public void testAddAndDeleteNormalTodoItem(){
        onView(withId(R.id.main_toolbar_textview_now)).perform(click());
        onView(withId(R.id.fragment_now_button_add)).perform(click());
        onView(withId(R.id.add_todo_edit_content)).perform(typeText("eat "));
        onView(withId(R.id.add_todo_button_submit)).perform(click());
        onView(withId(R.id.fragment_now_list)).perform(RecyclerViewActions.actionOnItem(hasDescendant(withText("eat")),longClick()));
        onView(withId(R.id.edit_todo_button_delete)).perform(click());
        onView(withId(R.id.edit_todo_button_submit)).perform(click());
    }

    @Test
    public void testAddAndDeleteDailyTodoItem(){
        onView(withId(R.id.main_toolbar_textview_now)).perform(click());
        onView(withId(R.id.fragment_now_button_add)).perform(click());
        onView(withId(R.id.add_todo_button_daily)).perform(click());
        onView(withId(R.id.add_todo_edit_content)).perform(typeText("eat "));
        onView(withId(R.id.add_todo_button_submit)).perform(click());
        onView(withId(R.id.fragment_now_list)).perform(RecyclerViewActions.actionOnItem(hasDescendant(withText("eat")),longClick()));
        onView(withId(R.id.edit_todo_button_delete)).perform(click());
        onView(withId(R.id.edit_todo_button_submit)).perform(click());

        onView(withId(R.id.main_toolbar_textview_all)).perform(click());
        onView(withId(R.id.fragment_all_radio_button_all)).perform(click());
        onView(withId(R.id.fragment_all_list)).perform(RecyclerViewActions.actionOnItem(hasDescendant(withText("eat")),longClick()));
        onView(withId(R.id.edit_todo_button_delete)).perform(click());
        onView(withId(R.id.edit_todo_button_submit)).perform(click());
    }

}
