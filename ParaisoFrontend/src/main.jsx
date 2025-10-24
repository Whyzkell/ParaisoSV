// main.jsx (o index.js)
import React from 'react';
import ReactDOM from 'react-dom/client';
import { createBrowserRouter, RouterProvider } from 'react-router-dom';
import App from './App.jsx';
import {AuthProvider} from "../src/context/AuthContext.jsx"
import Login from './render/login.jsx';
import ProtectedRoute from './components/ProtectedRoute.jsx';
import Register from './render/register.jsx';

// 1. Define las rutas
const router = createBrowserRouter([
  {
    path: '/login',
    element: <Login />,
  },
  {
    path: '/',
    element: <App />, // App.jsx contendrá el layout/contenido principal
  },
  {
    path: '/register',
    element: < Register/>, // App.jsx contendrá el layout/contenido principal
  },
  {
    element: <ProtectedRoute />, // Este elemento aplica la protección
    children: [
      
    ],
  },
  // Opcional: Manejo de ruta no encontrada (404)
  {
    path: '*',
    element: <h1>404 - Página no encontrada</h1>,
  },
]);

// 2. Renderiza la aplicación con el proveedor de autenticación
ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <AuthProvider>
      <RouterProvider router={router} />
    </AuthProvider>
  </React.StrictMode>,
);