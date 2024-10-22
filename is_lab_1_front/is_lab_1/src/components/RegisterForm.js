import React, { useState } from 'react';
import './RegistrationForm.css'

function RegisterForm() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [isAdmin, setIsAdmin] = useState(false);
    const [passwordError, setPasswordError] = useState('');
    const [errorMessage, setErrorMessage] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();
        setPasswordError('');
        setErrorMessage('');

        if (password.length < 8) {
            setPasswordError('Пароль должен быть минимум из 8 символов.');
            return;
        }

        // Send isAdmin in the userData object
        const userData = { username, password, isAdmin };
        console.log(userData);

        try {
            const response = await fetch('http://localhost:8080/api/auth/register', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(userData),
            });

            if (response.ok) {
                alert('Регистрация успешна! Ожидайте одобрения.');
                setUsername('');
                setPassword('');
                setIsAdmin( false);
            } else {
                const errorData = await response.json();
                setErrorMessage(errorData.message || 'Ошибка регистрации.');
            }
        } catch (error) {
            console.error('Ошибка при регистрации:', error);
            setErrorMessage('Ошибка при регистрации.');
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <div>
                <label>Логин:</label>
                <input value={username} onChange={(e) => setUsername(e.target.value)} />
            </div>
            <div>
                <label>Пароль:</label>
                <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} />
                {passwordError && <p className="error-message">{passwordError}</p>}
            </div>
            <div className="checkbox-container">
                <label>
                    <input type="checkbox" checked={isAdmin} onChange={(e) => setIsAdmin(e.target.checked)} />
                    Стать админом
                </label>
            </div>
            {errorMessage && <p className="error-message">{errorMessage}</p>}
            <button type="submit">Зарегистрироваться</button>

            <p className="account-message">Уже есть аккаунт? <a href="/login">Войти</a></p>
        </form>
    );
}

export default RegisterForm;
