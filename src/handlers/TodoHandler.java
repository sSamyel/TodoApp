package handlers;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import model.Todo;
import storage.TodoStorage;
import utils.HtmlHelper;
import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TodoHandler implements HttpHandler
{
    private final TodoStorage storage;

    public TodoHandler()
    {
        this.storage = new TodoStorage();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException
    {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String query = exchange.getRequestURI().getQuery();

        System.out.println("[" + method + "] " + path + (query != null ? "?" + query : ""));

        try
        {
            if ("GET".equals(method))
            {
                handleGet(exchange, path, query);
            }
            else if ("POST".equals(method))
            {
                handlePost(exchange, path, query);
            }
            else
            {
                exchange.sendResponseHeaders(405, -1); // Method Not Allowed
                exchange.getResponseBody().close();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();

            sendError(exchange, 500, "Внутренняя ошибка сервера");
        }
    }

    private void handleGet(HttpExchange exchange, String path, String query) throws IOException
    {
        if ("/".equals(path) || "/todos".equals(path))
        {
            // Показать список задач
            String filter = "all";

            if (query != null && query.startsWith("filter="))
            {
                filter = query.substring(7);
            }

            List<Todo> todos;

            if ("active".equals(filter))
            {
                todos = storage.findByStatus(false);
            }
            else if ("completed".equals(filter))
            {
                todos = storage.findByStatus(true);
            }
            else
            {
                todos = storage.findAll();
            }

            String html = HtmlHelper.generateTodoListPage(todos, filter);
            sendHtml(exchange, 200, html);

        }
        else
        {
            sendError(exchange, 404, "Страница не найдена");
        }
    }

    private void handlePost(HttpExchange exchange, String path, String query) throws IOException
    {
        String body = readBody(exchange);
        var params = parseParams(body);

        if ("/todos".equals(path))
        {
            // Добавление задачи
            String title = params.getOrDefault("title", "").trim();
            String description = params.getOrDefault("description", "").trim();

            if (title.isEmpty())
            {
                sendError(exchange, 400, "Название задачи не может быть пустым");
                return;
            }

            storage.create(title, description);
            redirect(exchange, "/");

        }
        else if (path.startsWith("/todos/toggle"))
        {
            // Переключить статус
            if (query == null || !query.startsWith("id="))
            {
                sendError(exchange, 400, "Не указан ID задачи");
                return;
            }

            try
            {
                int id = Integer.parseInt(query.substring(3));
                storage.toggleComplete(id);
                redirect(exchange, "/");
            }
            catch (NumberFormatException e)
            {
                sendError(exchange, 400, "Неверный ID задачи");
            }

        }
        else if (path.startsWith("/todos/delete"))
        {
            // Удалить задачу
            if (query == null || !query.startsWith("id="))
            {
                sendError(exchange, 400, "Не указан ID задачи");
                return;
            }

            try
            {
                int id = Integer.parseInt(query.substring(3));
                storage.delete(id);
                redirect(exchange, "/");

            }
            catch (NumberFormatException e)
            {
                sendError(exchange, 400, "Неверный ID задачи");
            }

        }
        else
        {
            sendError(exchange, 404, "Страница не найдена");
        }
    }

    // Вспомогательные методы

    private String readBody(HttpExchange exchange) throws IOException
    {
        InputStream is = exchange.getRequestBody();
        return new String(is.readAllBytes(), StandardCharsets.UTF_8);
    }

    private java.util.Map<String, String> parseParams(String body)
    {
        java.util.Map<String, String> params = new java.util.HashMap<>();
        if (body == null || body.isEmpty()) return params;

        try
        {
            for (String pair : body.split("&"))
            {
                String[] parts = pair.split("=");

                if (parts.length == 2)
                {
                    String key = URLDecoder.decode(parts[0], "UTF-8");
                    String value = URLDecoder.decode(parts[1], "UTF-8");
                    params.put(key, value);
                }
            }
        }
        catch (UnsupportedEncodingException e)
        {
            // ignore
        }

        return params;
    }

    private void sendHtml(HttpExchange exchange, int status, String html) throws IOException
    {
        byte[] bytes = html.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
        exchange.sendResponseHeaders(status, bytes.length);
        OutputStream os = exchange.getResponseBody();
        os.write(bytes);
        os.close();
    }

    private void sendError(HttpExchange exchange, int status, String message) throws IOException
    {
        String html = String.format("""
            <html>
            <head><title>Ошибка</title></head>
            <body>
                <h1>Ошибка %d</h1>
                <p>%s</p>
                <a href="/">На главную</a>
            </body>
            </html>
        """, status, message);

        sendHtml(exchange, status, html);
    }

    private void redirect(HttpExchange exchange, String location) throws IOException
    {
        exchange.getResponseHeaders().set("Location", location);
        exchange.sendResponseHeaders(302, -1);
        exchange.getResponseBody().close();
    }
}