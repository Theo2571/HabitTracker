package org.example.habittracker.service;

import org.example.habittracker.dto.MonthlyStatsPoint;
import org.example.habittracker.dto.MonthlyStatsResponse;
import org.example.habittracker.dto.WeeklyStatsPoint;
import org.example.habittracker.dto.WeeklyStatsResponse;
import org.example.habittracker.model.User;
import org.example.habittracker.repository.TaskCompletionRepository;
import org.example.habittracker.repository.TaskRepository;
import org.example.habittracker.repository.UserRepository;
import org.example.habittracker.security.SecurityUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class StatsService {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final String[] DAY_NAMES = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};

    private final TaskRepository taskRepository;
    private final TaskCompletionRepository taskCompletionRepository;
    private final UserRepository userRepository;

    public StatsService(TaskRepository taskRepository,
                        TaskCompletionRepository taskCompletionRepository,
                        UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.taskCompletionRepository = taskCompletionRepository;
        this.userRepository = userRepository;
    }

    private User getCurrentUser() {
        String username = SecurityUtils.getCurrentUsername();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    /**
     * Недельная консистентность (контракт BACKEND_JAVA.md §3.1).
     * По каждому дню в [from, to] (включительно) — одна точка в data.
     * Если в день нет задач — запись с completion: 0.
     * completion = total === 0 ? 0 : round((completed / total) * 100).
     */
    public WeeklyStatsResponse getWeeklyStats(LocalDate from, LocalDate to) {
        User user = getCurrentUser();
        List<WeeklyStatsPoint> data = new ArrayList<>();
        if (from.isAfter(to)) {
            return new WeeklyStatsResponse(data);
        }
        for (LocalDate date = from; !date.isAfter(to); date = date.plusDays(1)) {
            long total = taskRepository.countByUserAndDate(user, date);
            long completed = taskCompletionRepository.countByUserAndDate(user, date);
            int completion = (total == 0) ? 0 : (int) Math.round(completed * 100.0 / total);
            String dayName = DAY_NAMES[date.getDayOfWeek().getValue() - 1];
            data.add(new WeeklyStatsPoint(dayName, date.format(DATE_FORMAT), Math.min(100, completion)));
        }
        return new WeeklyStatsResponse(data);
    }

    /**
     * Месячная статистика (контракт BACKEND_JAVA.md §3.2).
     * W1: дни 1–7, W2: 8–14, W3: 15–21, W4: 22–конец месяца.
     * count — суммарное количество выполненных задач за неделю.
     */
    public MonthlyStatsResponse getMonthlyStats(int year, int month) {
        User user = getCurrentUser();
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());
        List<MonthlyStatsPoint> data = new ArrayList<>();
        int day = 1;
        int weekNum = 1;
        while (day <= end.getDayOfMonth()) {
            int weekEnd = Math.min(day + 6, end.getDayOfMonth());
            LocalDate weekStartDate = LocalDate.of(year, month, day);
            LocalDate weekEndDate = LocalDate.of(year, month, weekEnd);
            long count = taskCompletionRepository.countByUserAndDateBetween(user, weekStartDate, weekEndDate);
            data.add(new MonthlyStatsPoint("W" + weekNum, (int) count));
            day = weekEnd + 1;
            weekNum++;
        }
        return new MonthlyStatsResponse(data);
    }
}
