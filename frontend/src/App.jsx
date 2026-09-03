import { useState, useEffect } from 'react';
import { getTodos, createTodo, toggleTodo, deleteTodo } from './api';
import TodoForm from './TodoForm';
import TodoList from './TodoList';
import './App.css';

function App() {
    const [todos, setTodos] = useState([]);
    const [filter, setFilter] = useState('all');
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    // Загрузка задач при монтировании и изменении фильтра
    useEffect(() => {
        loadTodos();
    }, [filter]);

    const loadTodos = async () => {
        try {
            setLoading(true);
            const data = await getTodos(filter);
            setTodos(data);
            setError(null);
        } catch (err) {
            setError('Не удалось загрузить задачи');
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    const handleAdd = async (title, description) => {
        try {
            const newTodo = await createTodo(title, description);
            setTodos([...todos, newTodo]);
        } catch (err) {
            alert('Не удалось создать задачу');
        }
    };

    const handleToggle = async (id) => {
        try {
            const updated = await toggleTodo(id);
            setTodos(todos.map(t => t.id === id ? updated : t));
        } catch (err) {
            alert('Не удалось обновить задачу');
        }
    };

    const handleDelete = async (id) => {
        try {
            await deleteTodo(id);
            setTodos(todos.filter(t => t.id !== id));
        } catch (err) {
            alert('Не удалось удалить задачу');
        }
    };

    return (
        <div className="container">
            <h1>
                📋 Мои задачи
                <span style={{ fontSize: '14px', color: '#999' }}>
                    Всего: {todos.length}
                </span>
            </h1>

            <TodoForm onAdd={handleAdd} />

            <div className="filters">
                <button
                    className={filter === 'all' ? 'active' : ''}
                    onClick={() => setFilter('all')}
                >
                    Все
                </button>
                <button
                    className={filter === 'active' ? 'active' : ''}
                    onClick={() => setFilter('active')}
                >
                    Активные
                </button>
                <button
                    className={filter === 'completed' ? 'active' : ''}
                    onClick={() => setFilter('completed')}
                >
                    Выполненные
                </button>
            </div>

            {loading && <div className="loading">Загрузка...</div>}
            {error && <div className="error">{error}</div>}
            {!loading && !error && (
                <TodoList
                    todos={todos}
                    onToggle={handleToggle}
                    onDelete={handleDelete}
                />
            )}
        </div>
    );
}

export default App;