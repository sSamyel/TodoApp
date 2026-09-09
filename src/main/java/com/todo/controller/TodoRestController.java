package com.todo.controller;

import com.todo.model.Todo;
import com.todo.model.User;
import com.todo.service.TodoService;
import com.todo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
public class TodoRestController {

    @Autowired
    private TodoService todoService;

    @Autowired
    private UserService userService;

    private User getCurrentUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userService.findByUsername(userDetails.getUsername());
    }

    @GetMapping
    public List<Todo> getAllTodos() {
        User currentUser = getCurrentUser();
        return todoService.findAllByUser(currentUser);
    }

    @GetMapping(params = "filter")
    public List<Todo> getFilteredTodos(@RequestParam String filter) {
        User currentUser = getCurrentUser();
        if ("active".equals(filter)) {
            return todoService.findByUserAndCompleted(currentUser, false);
        } else if ("completed".equals(filter)) {
            return todoService.findByUserAndCompleted(currentUser, true);
        }
        return todoService.findAllByUser(currentUser);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Todo createTodo(@Valid @RequestBody Todo todo) {
        User currentUser = getCurrentUser();
        return todoService.create(todo, currentUser);
    }

    @PutMapping("/{id}/toggle")
    public Todo toggleTodo(@PathVariable Long id) {
        User currentUser = getCurrentUser();
        return todoService.toggleComplete(id, currentUser);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTodo(@PathVariable Long id) {
        User currentUser = getCurrentUser();
        todoService.delete(id, currentUser);
    }
}