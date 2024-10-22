import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom'; // Добавляем useNavigate
import './LoginForm.css';

function LoginForm({ onLogin }) {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const navigate = useNavigate(); // Инициализируем useNavigate

    const handleSubmit = async (e) => {
        e.preventDefault();
        const response = await fetch('http://localhost:8080/api/auth/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, password }), // Removed the admin flag here
        });

        if (response.ok) {
            const data = await response.json();
            const { token, isApproved } = data;  // Now receiving 'isAdmin' from the server


            localStorage.setItem('jwtToken', token); // Save the JWT token
            localStorage.setItem('username', username); // Save the username

            sessionStorage.setItem('jwtToken', token);
            sessionStorage.setItem('username', username);
            sessionStorage.setItem('isApproved', isApproved);

            onLogin(token);

            navigate('/book-creatures');
        } else {
            setErrorMessage('Не удалось войти. Проверьте логин и пароль.');
        }
    };


    return (
        <form onSubmit={handleSubmit}>
            <div>
                <label>Логин:</label>
                <input
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                />
            </div>
            <div>
                <label>Пароль:</label>
                <input
                    type="password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                />
            </div>
            {errorMessage && <p className="error-message">{errorMessage}</p>}
            <button type="submit">Войти</button>
            <p className="account-message">Нет аккаунта? <a href="/register">Зарегистрироваться</a></p>
        </form>
    );
}

export default LoginForm;
