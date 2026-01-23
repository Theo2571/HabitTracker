package org.example.habittracker.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private boolean completed;

    @Column(name = "task_date")
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Task() {}

    public Task(String title, User user) {
        this.title = title;
        this.user = user;
        this.completed = false;
        this.date = LocalDate.now();
    }

    public Task(String title, User user, LocalDate date) {
        this.title = title;
        this.user = user;
        this.completed = false;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void toggleCompleted() {
        this.completed = !this.completed;
    }

    public User getUser() {
        return user;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
