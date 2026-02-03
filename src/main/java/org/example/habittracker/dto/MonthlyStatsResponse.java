package org.example.habittracker.dto;

import java.util.List;

public class MonthlyStatsResponse {

    private List<MonthlyStatsPoint> data;

    public MonthlyStatsResponse(List<MonthlyStatsPoint> data) {
        this.data = data;
    }

    public List<MonthlyStatsPoint> getData() {
        return data;
    }
}
