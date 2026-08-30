package com.todo.storage;

import com.todo.model.Todo;
import org.springframework.stereotype.Component;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

@Component  // ← теперь это не сам интерфейс, а его реализация
public class FileTodoStorage implements TodoStorage {

    private static final String STORAGE_FILE = "todos.txt";
    private Map<Integer, Todo> todos = new HashMap<>();
    private int nextId = 1;

    public FileTodoStorage() {
        loadFromFile();
    }

    @Override
    public List<Todo> findAll() {
        return new ArrayList<>(todos.values());
    }

    @Override
    public Todo findById(int id) {
        return todos.get(id);
    }

    @Override
    public Todo create(String title, String description) {
        Todo todo = new Todo(title, description);
        todos.put(todo.getId(), todo);
        saveToFile();
        return todo;
    }

    @Override
    public Todo toggleComplete(int id) {
        Todo todo = todos.get(id);
        if (todo != null) {
            todo.setCompleted(!todo.isCompleted());
            saveToFile();
        }
        return todo;
    }

    @Override
    public boolean delete(int id) {
        boolean removed = todos.remove(id) != null;
        if (removed) saveToFile();
        return removed;
    }

    @Override
    public List<Todo> findByStatus(boolean completed) {
        return todos.values().stream()
                .filter(t -> t.isCompleted() == completed)
                .collect(Collectors.toList());
    }

    private void saveToFile() {
        try {
            List<String> lines = todos.values().stream()
                    .map(Todo::toString)
                    .collect(Collectors.toList());
            Files.write(Paths.get(STORAGE_FILE), lines);
        } catch (IOException e) {
            System.err.println("Ошибка сохранения: " + e.getMessage());
        }
    }

    private void loadFromFile() {
        try {
            Path path = Paths.get(STORAGE_FILE);
            if (!Files.exists(path)) return;

            List<String> lines = Files.readAllLines(path);
            for (String line : lines) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split("\\|");
                if (parts.length >= 6) {
                    Todo todo = new Todo(
                            Integer.parseInt(parts[0]),
                            parts[1],
                            parts[2],
                            Boolean.parseBoolean(parts[3]),
                            parts[4],
                            parts[5]
                    );
                    todos.put(todo.getId(), todo);
                    if (todo.getId() >= nextId) nextId = todo.getId() + 1;
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка загрузки: " + e.getMessage());
        }
    }
}