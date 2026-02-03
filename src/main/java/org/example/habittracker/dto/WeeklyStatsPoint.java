package org.example.habittracker.dto;

public class WeeklyStatsPoint {

    private String day;   // Mon, Tue, ...
    private String date;  // YYYY-MM-DD
    private int completion; // 0-100

    public WeeklyStatsPoint(String day, String date, int completion) {
        this.day = day;
        this.date = date;
        this.completion = completion;
    }

    public String getDay() {
        return day;
    }

    public String getDate() {
        return date;
    }

    public int getCompletion() {
        return completion;
    }
}
