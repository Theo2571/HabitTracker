package org.example.habittracker.dto;

public class MonthlyStatsPoint {

    private String week;  // W1, W2, W3, W4
    private int count;    // completed habits count for that week

    public MonthlyStatsPoint(String week, int count) {
        this.week = week;
        this.count = count;
    }

    public String getWeek() {
        return week;
    }

    public int getCount() {
        return count;
    }
}
