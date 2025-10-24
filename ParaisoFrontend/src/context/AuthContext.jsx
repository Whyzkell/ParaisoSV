import React, { createContext, useContext, useState } from "react";
import axios from "axios";

const AuthContext = createContext(null);
const API_URL = "http://localhost:8081"; // Ajusta tu URL del backend

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(() => {
    try {
      return JSON.parse(localStorage.getItem("user"));
    } catch {
      return null;
    }
  });

  const login = async (username, password) => {
    try {
      const response = await axios.post(`${API_URL}/auth/login`, {
        username,
        password,
      });
      const userData = response.data;

      localStorage.setItem("user", JSON.stringify(userData));
      setUser(userData);
      return userData;
    } catch (error) {
      console.error("Login failed", error);
      throw error;
    }
  };

  // ✅ ARREGLADO: Acepta { nombre, correo, password }
  const register = async ({ nombre, correo, password }) => {
    try {
      // Envía el payload que el backend espera
      const { data } = await axios.post(`${API_URL}/api/usuarios`, {
        nombre,
        correo,
        password,
      });
      // No guardamos token ni user aquí
      return data;
    } catch (error) {
      console.error("Register failed", error);
      throw error;
    }
  };

  const logout = () => {
    localStorage.removeItem("user");
    setUser(null);
  };

  const value = { user, login, register, logout, isAuthenticated: !!user };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

export const useAuth = () => {
  return useContext(AuthContext);
};
