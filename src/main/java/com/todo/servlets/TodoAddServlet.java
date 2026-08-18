package com.todo.servlets;

import com.todo.storage.TodoStorage;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/todos/add")
public class TodoAddServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        TodoStorage storage = (TodoStorage) getServletContext().getAttribute("storage");

        String title = request.getParameter("title");
        String description = request.getParameter("description");

        if (title != null && !title.trim().isEmpty()) {
            storage.create(title.trim(), description != null ? description.trim() : "");
        }

        response.sendRedirect(request.getContextPath() + "/todos");
    }
}