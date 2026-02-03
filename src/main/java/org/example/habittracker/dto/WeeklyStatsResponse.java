package org.example.habittracker.dto;

import java.util.List;

public class WeeklyStatsResponse {

    private List<WeeklyStatsPoint> data;

    public WeeklyStatsResponse(List<WeeklyStatsPoint> data) {
        this.data = data;
    }

    public List<WeeklyStatsPoint> getData() {
        return data;
    }
}
