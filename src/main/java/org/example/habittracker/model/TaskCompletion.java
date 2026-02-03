package org.example.habittracker.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "task_completions", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"task_id", "completion_date"})
})
public class TaskCompletion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @Column(name = "completion_date", nullable = false)
    private LocalDate completionDate;

    public TaskCompletion() {}

    public TaskCompletion(Task task, LocalDate completionDate) {
        this.task = task;
        this.completionDate = completionDate;
    }

    public Long getId() {
        return id;
    }

    public Task getTask() {
        return task;
    }

    public LocalDate getCompletionDate() {
        return completionDate;
    }
}
