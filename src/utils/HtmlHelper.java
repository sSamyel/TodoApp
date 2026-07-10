package utils;

import model.Todo;
import java.util.List;

public class HtmlHelper
{
    public static String generateTodoListPage(List<Todo> todos, String filter)
    {
        // Статическая часть HTML с плейсхолдерами
        String htmlTemplate = """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Todo-менеджер</title>
                <style>
                    * { margin: 0; padding: 0; box-sizing: border-box; }
                    body { 
                        font-family: 'Segoe UI', Arial, sans-serif;
                        background: linear-gradient(135deg, #667eea 0%%, #764ba2 100%%);
                        min-height: 100vh;
                        padding: 20px;
                    }
                    .container {
                        max-width: 800px;
                        margin: 0 auto;
                        background: white;
                        padding: 30px;
                        border-radius: 15px;
                        box-shadow: 0 20px 60px rgba(0,0,0,0.3);
                    }
                    h1 { 
                        color: #333;
                        margin-bottom: 20px;
                        display: flex;
                        justify-content: space-between;
                        align-items: center;
                    }
                    .add-form {
                        display: flex;
                        gap: 10px;
                        margin-bottom: 30px;
                        flex-wrap: wrap;
                    }
                    .add-form input, .add-form button {
                        padding: 10px 15px;
                        border: 1px solid #ddd;
                        border-radius: 8px;
                        font-size: 14px;
                    }
                    .add-form input {
                        flex: 1;
                        min-width: 150px;
                    }
                    .add-form button {
                        background: #667eea;
                        color: white;
                        border: none;
                        cursor: pointer;
                        font-weight: bold;
                        transition: 0.3s;
                    }
                    .add-form button:hover { background: #5a67d8; }
                    .filters {
                        display: flex;
                        gap: 10px;
                        margin-bottom: 20px;
                        flex-wrap: wrap;
                    }
                    .filters a {
                        padding: 8px 16px;
                        background: #f0f0f0;
                        border-radius: 20px;
                        text-decoration: none;
                        color: #333;
                        transition: 0.3s;
                    }
                    .filters a:hover, .filters a.active {
                        background: #667eea;
                        color: white;
                    }
                    .todo-item {
                        display: flex;
                        align-items: center;
                        padding: 15px;
                        border-bottom: 1px solid #eee;
                        gap: 15px;
                        transition: 0.3s;
                    }
                    .todo-item:hover { background: #f8f9fa; }
                    .todo-item.completed .todo-title {
                        text-decoration: line-through;
                        color: #999;
                    }
                    .todo-item .todo-title {
                        flex: 1;
                        font-weight: 500;
                    }
                    .todo-item .todo-date {
                        font-size: 12px;
                        color: #999;
                    }
                    .todo-actions {
                        display: flex;
                        gap: 8px;
                    }
                    .todo-actions button, .todo-actions a {
                        padding: 5px 12px;
                        border: none;
                        border-radius: 5px;
                        cursor: pointer;
                        font-size: 12px;
                        text-decoration: none;
                        transition: 0.3s;
                    }
                    .btn-toggle {
                        background: #48bb78;
                        color: white;
                    }
                    .btn-toggle:hover { background: #38a169; }
                    .btn-toggle.undo {
                        background: #ed8936;
                    }
                    .btn-toggle.undo:hover { background: #dd6b20; }
                    .btn-delete {
                        background: #fc8181;
                        color: white;
                    }
                    .btn-delete:hover { background: #f56565; }
                    .empty {
                        text-align: center;
                        color: #999;
                        padding: 40px 0;
                    }
                </style>
            </head>
            <body>
                <div class="container">
                    <h1>
                        📋 Мои задачи
                        <span style="font-size: 14px; color: #999;">
                            Всего: %d
                        </span>
                    </h1>
                    
                    <!-- Форма добавления -->
                    <form action="/todos" method="POST" class="add-form">
                        <input type="text" name="title" placeholder="Название задачи" required>
                        <input type="text" name="description" placeholder="Описание (необязательно)">
                        <button type="submit">+ Добавить</button>
                    </form>
                    
                    <!-- Фильтры -->
                    <div class="filters">
                        <a href="/" class="%s">Все</a>
                        <a href="/?filter=active" class="%s">Активные</a>
                        <a href="/?filter=completed" class="%s">Выполненные</a>
                    </div>
                    
                    <!-- Список задач -->
                    <div class="todo-list">
                        %s
                    </div>
                </div>
            </body>
            </html>
        """;

        // Формируем классы для фильтров
        String allClass = "all".equals(filter) ? "active" : "";
        String activeClass = "active".equals(filter) ? "active" : "";
        String completedClass = "completed".equals(filter) ? "active" : "";

        // Формируем список задач
        String todoItems = generateTodoItems(todos);

        // Подставляем все в шаблон
        return String.format(htmlTemplate, todos.size(), allClass, activeClass, completedClass, todoItems);
    }

    private static String generateTodoItems(List<Todo> todos)
    {
        StringBuilder sb = new StringBuilder();

        if (todos.isEmpty())
        {
            sb.append("""
                        <div class="empty">🎉 Пока нет задач. Добавьте первую!</div>
                    """);
        }
        else
        {
            for (Todo todo : todos)
            {
                String todoItem = String.format("""
                            <div class="todo-item %s">
                                <div class="todo-title">%s
                                    <div style="font-size:12px;color:#999;">%s</div>
                                </div>
                                <div class="todo-date">
                                    %s
                                </div>
                                <div class="todo-actions">
                                    <form action="/todos/toggle?id=%d" method="POST" style="display:inline;">
                                        <button type="submit" class="btn-toggle %s">
                                            %s
                                        </button>
                                    </form>
                                    <form action="/todos/delete?id=%d" method="POST" style="display:inline;">
                                        <button type="submit" class="btn-delete" 
                                                onclick="return confirm('Удалить задачу?')">🗑</button>
                                    </form>
                                </div>
                            </div>
                        """,
                        todo.isCompleted() ? "completed" : "",
                        escapeHtml(todo.getTitle()),
                        escapeHtml(todo.getDescription()),
                        todo.getFormattedDate(todo.getCreatedAt()),
                        todo.getId(),
                        todo.isCompleted() ? "undo" : "",
                        todo.isCompleted() ? "↩ Вернуть" : "✅ Выполнено",
                        todo.getId()
                );
                sb.append(todoItem);
            }
        }

        return sb.toString();
    }

    private static String escapeHtml(String text)
    {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }
}