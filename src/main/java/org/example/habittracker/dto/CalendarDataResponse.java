package org.example.habittracker.dto;

import java.util.Map;
import java.util.List;

public class CalendarDataResponse {

    private Map<String, List<TaskResponse>> tasksByDate;

    public CalendarDataResponse(Map<String, List<TaskResponse>> tasksByDate) {
        this.tasksByDate = tasksByDate;
    }

    public Map<String, List<TaskResponse>> getTasksByDate() {
        return tasksByDate;
    }
}
