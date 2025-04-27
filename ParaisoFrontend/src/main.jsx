import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import "./index.css";
import App from "./App.jsx";
import Login from "./render/login.jsx";
import VSMascota from "./render/visualizarMascota.jsx";
import AdoptarMascota from "./render/adoptarMascota.jsx";
import VisuProyecto from "./render/visualizarProyecto.jsx";
import Donaciones from "./render/verdonaciones.jsx";
import CrearAlcancia from "./render/crearAlcancia.jsx";

createRoot(document.getElementById("root")).render(
  <StrictMode>
    <App />
  </StrictMode>
);
