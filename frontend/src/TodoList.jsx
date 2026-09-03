function TodoList({ todos, onToggle, onDelete }) {
    if (todos.length === 0) {
        return <div className="empty">🎉 Пока нет задач. Добавьте первую!</div>;
    }

    // Функция для правильного форматирования даты
    const formatDate = (dateValue) => {
        if (!dateValue) return 'Неизвестно';

        // Если дата пришла в виде массива [год, месяц, день, час, минута, секунда]
        if (Array.isArray(dateValue)) {
            const [year, month, day, hour, minute] = dateValue;
            // month в массиве начинается с 1, а в JS Date с 0, поэтому вычитаем 1
            const date = new Date(year, month - 1, day, hour || 0, minute || 0);
            return date.toLocaleDateString('ru-RU', {
                day: '2-digit',
                month: '2-digit',
                year: 'numeric',
                hour: '2-digit',
                minute: '2-digit'
            });
        }

        // Если дата пришла в виде строки
        if (typeof dateValue === 'string') {
            const date = new Date(dateValue);
            if (!isNaN(date.getTime())) {
                return date.toLocaleDateString('ru-RU', {
                    day: '2-digit',
                    month: '2-digit',
                    year: 'numeric',
                    hour: '2-digit',
                    minute: '2-digit'
                });
            }
        }

        return 'Неизвестно';
    };

    return (
        <div className="todo-list">
            {todos.map((todo) => (
                <div key={todo.id} className={`todo-item ${todo.completed ? 'completed' : ''}`}>
                    <div className="todo-title">
                        {todo.title}
                        <div style={{ fontSize: '12px', color: '#999' }}>{todo.description}</div>
                    </div>
                    <div className="todo-date">
                        {formatDate(todo.createdAt)}
                    </div>
                    <div className="todo-actions">
                        <button
                            className={`btn-toggle ${todo.completed ? 'undo' : ''}`}
                            onClick={() => onToggle(todo.id)}
                        >
                            {todo.completed ? '↩ Вернуть' : '✅ Выполнено'}
                        </button>
                        <button
                            className="btn-delete"
                            onClick={() => {
                                if (confirm('Удалить задачу?')) onDelete(todo.id);
                            }}
                        >
                            🗑
                        </button>
                    </div>
                </div>
            ))}
        </div>
    );
}

export default TodoList;