package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Todo
{
    private static int nextId = 1;

    private int id;
    private String title;
    private String description;
    private boolean completed;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Todo(String title, String description)
    {
        this.id = nextId++;
        this.title = title;
        this.description = description;
        this.completed = false;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Конструктор для загрузки из файла
    public Todo(int id, String title, String description, boolean completed, String createdAt, String updatedAt)
    {
        this.id = id;
        this.title = title;
        this.description = description;
        this.completed = completed;
        this.createdAt = LocalDateTime.parse(createdAt);
        this.updatedAt = LocalDateTime.parse(updatedAt);
        if (id >= nextId) nextId = id + 1;
    }

    // Геттеры и сеттеры
    public int getId() { return id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed)
    {
        this.completed = completed;
        this.updatedAt = LocalDateTime.now();
    }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public String getFormattedDate(LocalDateTime date)
    {
        return date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
    }

    @Override
    public String toString()
    {
        return String.format("%d|%s|%s|%b|%s|%s", id, title, description, completed, createdAt, updatedAt);
    }
}