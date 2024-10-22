import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Route, Routes, Navigate } from 'react-router-dom';
import BookCreatureForm from './components/BookCreatureForm';
import LoginForm from './components/LoginForm';
import RegisterForm from './components/RegisterForm';
import UserApprovalList from './components/UserApprovalList';
import {
    deleteBookCreature, updateBookCreature, createBookCreature, getBookCreatures, getBookCreaturesWithFilters
} from './services/api';
import Content from './components/Content/Content';
import SimpleRow from './components/SimpleRow/SimpleRow';
import BookCreatureList from "./components/BookCreatureList/BookCreatureList";
import Button from "./components/Button/Button";
import SpecialActions from "./components/SpecialActions";


function App() {
    const [bookCreatures, setBookCreatures] = useState([]);
    const [selectedCreatures, setSelectedCreatures] = useState([]);
    const [nameFilter, setNameFilter] = useState('');
    const [ageFilter, setAgeFilter] = useState('');
    const [token, setToken] = useState(null);
    const [activePanel, setActivePanel] = useState('create');
    const [selectedCreature, setSelectedCreature] = useState(null);
    const [username, setUsername] = useState(localStorage.getItem('username'));
    const isApproved = sessionStorage.getItem('isApproved') === 'true'; // Retrieve isApproved from sessionStorage



    useEffect(() => {
        const storedUsername = sessionStorage.getItem('username');
        if (storedUsername) {
            setUsername(storedUsername);
        }
    }, []);


    useEffect(() => {
        if (token) {
            loadBookCreatures();
        }
    }, [token]);

    useEffect(() => {
        if (token) {
            loadBookCreaturesWithFilters();
        }
    }, [nameFilter, ageFilter, token]);

    const loadBookCreaturesWithFilters = async () => {
        const data = await getBookCreaturesWithFilters(token, nameFilter, ageFilter);
        setBookCreatures(data);
    };

    const loadBookCreatures = async () => {
        const data = await getBookCreatures(token);
        setBookCreatures(data);
    };

    const handleCreate = async (creature) => {
        await createBookCreature(creature, token);
        loadBookCreatures();
        setSelectedCreature(null);
        setActivePanel('create');
    };

    const handleUpdate = async (id, updatedCreature) => {
        await updateBookCreature(id, updatedCreature); // Call without the token parameter
        loadBookCreatures();
    };

    const handleDelete = async (id) => {
        await deleteBookCreature(id, token);
        loadBookCreatures();
    };

    const PrivateRoute = ({ children }) => {
        return token ? children : <Navigate to="/login" />;
    };

    const handleSelectCreatureForUpdate = (creature) => {
        setSelectedCreature(creature);
        setActivePanel('update');
    };

    const renderUserApprovalButton = () => {
        if (isApproved) {
            return <Button onClick={() => setActivePanel('approval')} label="User Approvals" />;
        }
        return null;
    };

    // Add a button for "Special Actions"
    const renderSpecialActionsButton = () => {
        return (
            <Button onClick={() => setActivePanel('special-actions')} label="Special Actions" />
        );
    };

    const handleChangeUser = () => {
        setToken(null); // Reset token to log out
        sessionStorage.clear(); // Clear session storage
    };


    return (
        <Router>
            <div className="App">
                {token && (
                    <header className="welcome-header">
                        <h2>Добро пожаловать, {username}!</h2>
                    </header>
                )}

                <Routes>
                    <Route
                        path="/book-creatures"
                        element={
                            <PrivateRoute>
                                <Content className="content--fullscreen-height">
                                    <SimpleRow>
                                        <Button onClick={() => setActivePanel('create')} label="Create Creature"/>
                                        <Button onClick={() => setActivePanel('update')} label="Creature's list"/>
                                        {renderUserApprovalButton()}
                                        {renderSpecialActionsButton()}
                                        <Button onClick={handleChangeUser} label="Сменить пользователя" />

                                    </SimpleRow>

                                    {/* Conditional Rendering of Panels */}
                                    {activePanel === 'create' ? (
                                        <BookCreatureForm onCreate={handleCreate}/>
                                    ) : activePanel === 'update' ? (
                                        <BookCreatureList
                                            creatures={bookCreatures}
                                            onUpdate={handleUpdate}
                                            onDelete={handleDelete}
                                            setSelectedCreatures={setSelectedCreatures}
                                            onSelect={handleSelectCreatureForUpdate}
                                        />
                                    ) : activePanel === 'special-actions' ? (
                                        <SpecialActions token={token}/>
                                    ) : (
                                        <UserApprovalList token={token}/>
                                    )}

                                </Content>
                            </PrivateRoute>
                        }
                    />
                    <Route path="/login" element={<LoginForm onLogin={setToken}/>}/>
                    <Route path="/register" element={<RegisterForm/>}/>
                    <Route path="/" element={<Navigate to="/login"/>}/>
                </Routes>
            </div>
        </Router>
    );
}

export default App;

