// main.jsx
import React from "react";
import ReactDOM from "react-dom/client";
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import App from "./App.jsx";
import { AuthProvider } from "../src/context/AuthContext.jsx";
import Login from "./render/login.jsx";
import ProtectedRoute from "./components/ProtectedRoute.jsx";
import Register from "./render/register.jsx";
import VisuProyecto from "./render/visualizarProyecto.jsx";
import AdoptarMascota from "./render/adoptarMascota.jsx";
import DonarAlcancia from "./render/donarAlcancia.jsx";
import EnviarDonacion from "./render/enviarDonacion.jsx";
import VSMascota from "./render/visualizarMascota.jsx";
import CrearAlcancia from "./render/crearAlcancia.jsx";
import AgregarMascota from "./render/agregarMascosta.jsx";
import ActualizarActividad from "./render/actualizarActividades.jsx";
import Donaciones from "./render/verdonaciones.jsx";
import CrearActividad from "./render/CrearActividad.jsx";

// 1. Define las rutas
const router = createBrowserRouter([
  {
    path: "/login",
    element: <Login />,
  },
  {
    path: "/",
    element: <App />, // App.jsx contendrá el layout/contenido principal
  },
  {
    path: "/register",
    element: <Register />,
  },
  {
    path: "/visualizar-proyecto",
    element: <VisuProyecto />,
  },
  {
    path: "/adoptar-mascota",
    element: <AdoptarMascota />,
  },
  {
    path: "/donaralcancia",
    element: <DonarAlcancia />,
  },
  {
    path: "/enviadonacion",
    element: <EnviarDonacion />,
  },
  {
    // --- ESTA ES LA LÍNEA CORREGIDA ---
    path: "/visualizar-mascota/:id",
    element: <VSMascota />,
  },
  {
    path: "/crear-alcancia",
    element: <CrearAlcancia />,
  },
  {
    path: "/agregarmascota",
    element: <AgregarMascota />,
  },
  {
    path: "/actualizaractividad",
    element: <ActualizarActividad />,
  },
  {
    path: "/donaciones",
    element: <Donaciones />,
  },
  {
    element: <ProtectedRoute />, // Este elemento aplica la protección
    children: [],
  },
  // Opcional: Manejo de ruta no encontrada (404)
  {
    path: "*",
    element: <h1>404 - Página no encontrada</h1>,
  },
  {
    path: "/crear-actividad",
    element: <CrearActividad />,
  },
]);

// 2. Renderiza la aplicación con el proveedor de autenticación
ReactDOM.createRoot(document.getElementById("root")).render(
  <React.StrictMode>
    <AuthProvider>
      <RouterProvider router={router} />
    </AuthProvider>
  </React.StrictMode>
);
