package com.todo.servlets;

import com.todo.storage.TodoStorage;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/todos/delete")
public class TodoDeleteServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        TodoStorage storage = (TodoStorage) getServletContext().getAttribute("storage");

        String idParam = request.getParameter("id");
        if (idParam != null) {
            try {
                int id = Integer.parseInt(idParam);
                storage.delete(id);
            } catch (NumberFormatException e) {
                // игнорируем
            }
        }

        response.sendRedirect(request.getContextPath() + "/todos");
    }
}