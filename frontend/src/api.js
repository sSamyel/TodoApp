const API_URL = 'http://localhost:8080/api/todos';

export const getTodos = async (filter = 'all') => {
    const url = filter === 'all' ? API_URL : `${API_URL}?filter=${filter}`;
    const response = await fetch(url);
    if (!response.ok) throw new Error('Ошибка загрузки');
    return response.json();
};

export const createTodo = async (title, description) => {
    const response = await fetch(API_URL, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ title, description })
    });
    if (!response.ok) throw new Error('Ошибка создания');
    return response.json();
};

export const toggleTodo = async (id) => {
    const response = await fetch(`${API_URL}/${id}/toggle`, {
        method: 'PUT'
    });
    if (!response.ok) throw new Error('Ошибка обновления');
    return response.json();
};

export const deleteTodo = async (id) => {
    const response = await fetch(`${API_URL}/${id}`, {
        method: 'DELETE'
    });
    if (!response.ok) throw new Error('Ошибка удаления');
};