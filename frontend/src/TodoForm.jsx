import { useState } from 'react';

function TodoForm({ onAdd }) {
    const [title, setTitle] = useState('');
    const [description, setDescription] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!title.trim()) return;
        await onAdd(title.trim(), description.trim());
        setTitle('');
        setDescription('');
    };

    return (
        <form onSubmit={handleSubmit} className="add-form">
            <input
                type="text"
                placeholder="Название задачи"
                value={title}
                onChange={(e) => setTitle(e.target.value)}
                required
            />
            <input
                type="text"
                placeholder="Описание (необязательно)"
                value={description}
                onChange={(e) => setDescription(e.target.value)}
            />
            <button type="submit">+ Добавить</button>
        </form>
    );
}

export default TodoForm;