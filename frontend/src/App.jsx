import { useState, useEffect } from 'react';
import { getTodos, createTodo, toggleTodo, deleteTodo, logout as logoutApi } from './api';
import Login from './Login';
import Register from './Register';
import TodoForm from './TodoForm';
import TodoList from './TodoList';
import './App.css';

function App() {
    const [isAuthenticated, setIsAuthenticated] = useState(false);
    const [isRegistering, setIsRegistering] = useState(false);
    const [todos, setTodos] = useState([]);
    const [filter, setFilter] = useState('all');
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const token = localStorage.getItem('token');
        if (token) {
            setIsAuthenticated(true);
            loadTodos();
        } else {
            setLoading(false);
        }
    }, []);

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

    useEffect(() => {
        if (isAuthenticated) {
            loadTodos();
        }
    }, [filter, isAuthenticated]);

    const handleLogin = () => {
        setIsAuthenticated(true);
        setIsRegistering(false);
        loadTodos();
    };

    const handleRegisterSuccess = () => {
        setIsRegistering(false);
    };

    const handleLogout = () => {
        logoutApi();
        setIsAuthenticated(false);
        setTodos([]);
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

    if (!isAuthenticated) {
        if (isRegistering) {
            return (
                <Register
                    onRegister={handleRegisterSuccess}
                    onSwitchToLogin={() => setIsRegistering(false)}
                />
            );
        }
        return <Login onLogin={handleLogin} onSwitchToRegister={() => setIsRegistering(true)} />;
    }

    return (
        <div className="container">
            <h1>
                📋 Мои задачи
                <span style={{ fontSize: '14px', color: '#999', display: 'flex', gap: '10px', alignItems: 'center' }}>
                    <span>
                        Всего: <span style={{ fontWeight: 600, color: '#2d3748' }}>{todos.length}</span>
                    </span>
                    <button onClick={handleLogout} className="btn-logout">🚪 Выйти</button>
                </span>
            </h1>

            <TodoForm onAdd={handleAdd} />

            <div className="filters">
                <button className={filter === 'all' ? 'active' : ''} onClick={() => setFilter('all')}>Все</button>
                <button className={filter === 'active' ? 'active' : ''} onClick={() => setFilter('active')}>Активные</button>
                <button className={filter === 'completed' ? 'active' : ''} onClick={() => setFilter('completed')}>Выполненные</button>
            </div>

            {loading && <div className="loading">Загрузка...</div>}
            {error && <div className="error">{error}</div>}
            {!loading && !error && (
                <TodoList todos={todos} onToggle={handleToggle} onDelete={handleDelete} />
            )}
        </div>
    );
}

export default App;