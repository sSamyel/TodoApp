package com.todo.service;

import com.todo.model.Todo;
import com.todo.model.User;
import com.todo.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoService {

    @Autowired
    private TodoRepository todoRepository;

    public List<Todo> findAllByUser(User user) {
        return todoRepository.findByUser(user);
    }

    public List<Todo> findByUserAndCompleted(User user, boolean completed) {
        return todoRepository.findByUserAndCompleted(user, completed);
    }

    public Todo findByIdAndUser(Long id, User user) {
        return todoRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Задача не найдена или не принадлежит вам"));
    }

    public Todo create(Todo todo, User user) {
        todo.setUser(user);
        todo.setCompleted(false);
        return todoRepository.save(todo);
    }

    public Todo toggleComplete(Long id, User user) {
        Todo todo = findByIdAndUser(id, user);
        todo.setCompleted(!todo.isCompleted());
        return todoRepository.save(todo);
    }

    public void delete(Long id, User user) {
        Todo todo = findByIdAndUser(id, user);
        todoRepository.delete(todo);
    }
}