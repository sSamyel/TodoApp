package com.todo.servlets;

import com.todo.model.Todo;
import com.todo.storage.TodoStorage;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/todos")
public class TodoListServlet extends HttpServlet {

    private TodoStorage storage;

    @Override
    public void init() {
        storage = new TodoStorage();
        getServletContext().setAttribute("storage", storage);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String filter = request.getParameter("filter");
        if (filter == null) filter = "all";

        List<Todo> todos;
        if ("active".equals(filter)) {
            todos = storage.findByStatus(false);
        } else if ("completed".equals(filter)) {
            todos = storage.findByStatus(true);
        } else {
            todos = storage.findAll();
        }

        request.setAttribute("todos", todos);
        request.setAttribute("filter", filter);
        request.setAttribute("totalCount", todos.size());

        request.getRequestDispatcher("/WEB-INF/jsp/todo-list.jsp")
                .forward(request, response);
    }
}