package com.todo.storage;

import com.todo.model.Todo;
import java.util.List;

public interface TodoStorage {
    List<Todo> findAll();
    Todo findById(int id);
    Todo create(String title, String description);
    Todo toggleComplete(int id);
    boolean delete(int id);
    List<Todo> findByStatus(boolean completed);
}