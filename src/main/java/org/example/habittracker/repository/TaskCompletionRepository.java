package org.example.habittracker.repository;

import org.example.habittracker.model.Task;
import org.example.habittracker.model.TaskCompletion;
import org.example.habittracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TaskCompletionRepository extends JpaRepository<TaskCompletion, Long> {

    Optional<TaskCompletion> findByTaskAndCompletionDate(Task task, LocalDate completionDate);

    void deleteByTaskAndCompletionDate(Task task, LocalDate completionDate);

    @Query("SELECT tc.completionDate FROM TaskCompletion tc WHERE tc.task.user = :user AND tc.task.title = :title ORDER BY tc.completionDate DESC")
    List<LocalDate> findCompletionDatesByUserAndTitle(User user, String title);

    @Query("SELECT COUNT(tc) FROM TaskCompletion tc WHERE tc.task.user = :user AND tc.completionDate BETWEEN :from AND :to")
    long countByUserAndDateBetween(User user, LocalDate from, LocalDate to);

    @Query("SELECT COUNT(tc) FROM TaskCompletion tc WHERE tc.task.user = :user AND tc.completionDate = :date")
    long countByUserAndDate(User user, LocalDate date);
}
