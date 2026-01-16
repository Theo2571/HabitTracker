package org.example.habittracker.model;

import jakarta.persistence.*;

@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private boolean completed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Task() {}

    public Task(String title, User user) {
        this.title = title;
        this.user = user;
        this.completed = false;
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
}
