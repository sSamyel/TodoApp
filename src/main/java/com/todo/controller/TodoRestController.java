package com.todo.controller;

import com.todo.model.Todo;
import com.todo.storage.TodoStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
public class TodoRestController {

    @Autowired
    private TodoStorage storage;

    // GET /api/todos — список всех задач
    @GetMapping
    public List<Todo> getAllTodos() {
        return storage.findAll();
    }

    // GET /api/todos?filter=active
    @GetMapping(params = "filter")
    public List<Todo> getFilteredTodos(@RequestParam String filter) {
        if ("active".equals(filter)) {
            return storage.findByStatus(false);
        } else if ("completed".equals(filter)) {
            return storage.findByStatus(true);
        }
        return storage.findAll();
    }

    // POST /api/todos — создать задачу
    @PostMapping
    public Todo createTodo(@RequestBody Todo todo) {
        return storage.create(todo.getTitle(), todo.getDescription());
    }

    // PUT /api/todos/{id}/toggle — переключить статус
    @PutMapping("/{id}/toggle")
    public Todo toggleTodo(@PathVariable int id) {
        return storage.toggleComplete(id);
    }

    // DELETE /api/todos/{id} — удалить задачу
    @DeleteMapping("/{id}")
    public void deleteTodo(@PathVariable int id) {
        storage.delete(id);
    }
}