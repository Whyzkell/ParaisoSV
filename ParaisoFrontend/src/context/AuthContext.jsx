// context/AuthContext.jsx
import React, { createContext, useContext, useState } from 'react';
import axios from 'axios';

const AuthContext = createContext(null);
const API_URL = "http://localhost:8081/api/auth"; // Ajusta tu URL del backend

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(JSON.parse(localStorage.getItem('user')));

  const login = async (username, password) => {
    try {
      const response = await axios.post(`${API_URL}/login`, { username, password });
      const userData = response.data;
      
      localStorage.setItem('user', JSON.stringify(userData));
      setUser(userData);
      return userData;

    } catch (error) {
      console.error("Login failed", error);
      throw error; 
    }
  };

  const logout = () => {
    localStorage.removeItem('user');
    setUser(null);
  };
  
  const value = { user, login, logout, isAuthenticated: !!user };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

export const useAuth = () => {
  return useContext(AuthContext);
};