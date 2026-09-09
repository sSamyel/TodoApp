const API_URL = '/api';

let authToken = localStorage.getItem('token');

export const setAuthToken = (token) => {
    authToken = token;
    if (token) {
        localStorage.setItem('token', token);
    } else {
        localStorage.removeItem('token');
    }
};

const getHeaders = () => {
    const headers = { 'Content-Type': 'application/json' };
    if (authToken) {
        headers['Authorization'] = `Bearer ${authToken}`;
    }
    return headers;
};

export const register = async (username, email, password) => {
    const response = await fetch(`${API_URL}/auth/register`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username, email, password })
    });
    if (!response.ok) {
        const error = await response.json();
        throw new Error(error.error || 'Registration failed');
    }
    return response.json();
};

export const login = async (username, password) => {
    const response = await fetch(`${API_URL}/auth/login`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username, password })
    });
    if (!response.ok) {
        const error = await response.json();
        throw new Error(error.error || 'Login failed');
    }
    const data = await response.json();
    setAuthToken(data.token);
    return data;
};

export const logout = () => {
    setAuthToken(null);
};

export const getTodos = async (filter = 'all') => {
    const url = filter === 'all' ? `${API_URL}/todos` : `${API_URL}/todos?filter=${filter}`;
    const response = await fetch(url, { headers: getHeaders() });
    if (!response.ok) throw new Error('Failed to fetch todos');
    return response.json();
};

export const createTodo = async (title, description) => {
    const response = await fetch(`${API_URL}/todos`, {
        method: 'POST',
        headers: getHeaders(),
        body: JSON.stringify({ title, description })
    });
    if (!response.ok) throw new Error('Failed to create todo');
    return response.json();
};

export const toggleTodo = async (id) => {
    const response = await fetch(`${API_URL}/todos/${id}/toggle`, {
        method: 'PUT',
        headers: getHeaders()
    });
    if (!response.ok) throw new Error('Failed to toggle todo');
    return response.json();
};

export const deleteTodo = async (id) => {
    const response = await fetch(`${API_URL}/todos/${id}`, {
        method: 'DELETE',
        headers: getHeaders()
    });
    if (!response.ok) throw new Error('Failed to delete todo');
};

export const updateTodo = async (id, title, description) => {
    const response = await fetch(`${API_URL}/todos/${id}`, {
        method: 'PUT',
        headers: getHeaders(),
        body: JSON.stringify({ title, description })
    });
    if (!response.ok) throw new Error('Failed to update todo');
    return response.json();
};