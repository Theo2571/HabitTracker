package org.example.habittracker.controllers;

import org.example.habittracker.dto.MonthlyStatsResponse;
import org.example.habittracker.dto.WeeklyStatsResponse;
import org.example.habittracker.service.StatsService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/stats")
public class StatsController {

    private final StatsService statsService;

    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @GetMapping("/weekly")
    public WeeklyStatsResponse getWeekly(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        return statsService.getWeeklyStats(from, to);
    }

    @GetMapping("/monthly")
    public MonthlyStatsResponse getMonthly(
            @RequestParam int year,
            @RequestParam int month
    ) {
        return statsService.getMonthlyStats(year, month);
    }
}
