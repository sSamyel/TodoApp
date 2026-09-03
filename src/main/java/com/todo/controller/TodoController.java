package com.todo.controller;

import com.todo.model.Todo;
import com.todo.storage.TodoStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/todos")
public class TodoController {

    @Autowired
    private TodoStorage storage;

    @GetMapping
    public String listTodos(
            @RequestParam(value = "filter", required = false, defaultValue = "all")
            String filter,
            Model model) {

        List<Todo> todos;
        if ("active".equals(filter)) {
            todos = storage.findByStatus(false);
        } else if ("completed".equals(filter)) {
            todos = storage.findByStatus(true);
        } else {
            todos = storage.findAll();
        }

        model.addAttribute("todos", todos);
        model.addAttribute("filter", filter);
        model.addAttribute("totalCount", todos.size());

        return "todo-list";  // → templates/todo-list.html (Thymeleaf)
    }

    @PostMapping("/add")
    public String addTodo(
            @RequestParam("title") String title,
            @RequestParam(value = "description", required = false, defaultValue = "")
            String description) {

        if (title != null && !title.trim().isEmpty()) {
            storage.create(title.trim(), description.trim());
        }
        return "redirect:/todos";
    }

    @PostMapping("/toggle")
    public String toggleTodo(@RequestParam("id") int id) {
        storage.toggleComplete(id);
        return "redirect:/todos";
    }

    @PostMapping("/delete")
    public String deleteTodo(@RequestParam("id") int id) {
        storage.delete(id);
        return "redirect:/todos";
    }

    // Редирект с корня
    @GetMapping("/")
    public String redirectToTodos() {
        return "redirect:/todos";
    }
}