<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
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
        .todo-actions button {
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
        .badge {
            background: #667eea;
            color: white;
            padding: 2px 10px;
            border-radius: 12px;
            font-size: 12px;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>
            📋 Мои задачи
            <span style="font-size: 14px; color: #999;">
                Всего: ${totalCount}
            </span>
        </h1>

        <form action="${pageContext.request.contextPath}/todos/add" method="POST" class="add-form">
            <input type="text" name="title" placeholder="Название задачи" required>
            <input type="text" name="description" placeholder="Описание (необязательно)">
            <button type="submit">+ Добавить</button>
        </form>

        <div class="filters">
            <a href="${pageContext.request.contextPath}/todos" class="${filter == 'all' ? 'active' : ''}">Все</a>
            <a href="${pageContext.request.contextPath}/todos?filter=active" class="${filter == 'active' ? 'active' : ''}">Активные</a>
            <a href="${pageContext.request.contextPath}/todos?filter=completed" class="${filter == 'completed' ? 'active' : ''}">Выполненные</a>
        </div>

        <div class="todo-list">
            <c:choose>
                <c:when test="${empty todos}">
                    <div class="empty">🎉 Пока нет задач. Добавьте первую!</div>
                </c:when>
                <c:otherwise>
                    <c:forEach var="todo" items="${todos}">
                        <div class="todo-item ${todo.completed ? 'completed' : ''}">
                            <div class="todo-title">
                                ${todo.title}
                                <div style="font-size:12px;color:#999;">${todo.description}</div>
                            </div>
                            <div class="todo-date">
                                ${todo.formattedCreatedAt}
                            </div>
                            <div class="todo-actions">
                                <form action="${pageContext.request.contextPath}/todos/toggle" method="POST" style="display:inline;">
                                    <input type="hidden" name="id" value="${todo.id}">
                                    <button type="submit" class="btn-toggle ${todo.completed ? 'undo' : ''}">
                                        ${todo.completed ? '↩ Вернуть' : '✅ Выполнено'}
                                    </button>
                                </form>
                                <form action="${pageContext.request.contextPath}/todos/delete" method="POST" style="display:inline;">
                                    <input type="hidden" name="id" value="${todo.id}">
                                    <button type="submit" class="btn-delete" onclick="return confirm('Удалить задачу?')">🗑</button>
                                </form>
                            </div>
                        </div>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</body>
</html>