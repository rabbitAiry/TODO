package com.example.todo.data;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class TimeUtils {
    // e.g: 20210101 = 2021/01/01
    public static int getTodayToken() {
        Calendar c = Calendar.getInstance();
        return c.get(1) * 10000 + (c.get(2) + 1) * 100 + c.get(5);
    }

    public static String getTodayText() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return dateFormat.format(c.getTime());
    }

    public static String getDayText(Calendar c){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return dateFormat.format(c.getTime());
    }

    public static int getDayToken(Calendar c) {
        return c.get(1) * 10000 + (c.get(2) + 1) * 100 + c.get(5);
    }

    public static Calendar parseTodayToken(int token){
        Calendar c = Calendar.getInstance();
        c.set(token/10000,token%10000/100-1,token%100);
        return c;
    }

    public static int addDate(int dayToken, int forwardDays){
        Calendar c = parseTodayToken(dayToken);
        c.add(Calendar.DATE, forwardDays);
        return getDayToken(c);
    }

    // e.g: 2200 = 22:00
    public static int getTimeToken(String s) {
        int hour = Integer.parseInt(s.substring(0,2));
        int minute = Integer.parseInt(s.substring(3,5));
        return hour*100+minute;
    }

    public static int getTimeToken(Calendar c){
        return c.get(Calendar.HOUR)*100 + c.get(Calendar.MINUTE);
    }

    public static int[] parseTimeToken(int token) {
        return new int[]{token/100, token%100};
    }
}