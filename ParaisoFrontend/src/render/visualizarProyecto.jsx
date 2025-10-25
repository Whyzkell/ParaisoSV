import React, { useState, useEffect } from "react";
import axios from "axios"; // 1. Importar axios
import { useNavigate } from "react-router-dom";
import NavnoCAdm from "./componentes/navCesionAdm.jsx"; // Admin
import NavnoCesion from "./componentes/navNocesion.jsx"; // Invitado
import NavCesionCli from "./componentes/navCesionCli.jsx"; // Cliente
import Footer from "./componentes/footer";
import useNavKind from "../hooks/useNavKind.js";
import { X } from "lucide-react"; // Icono para cerrar el modal

const API_URL = "http://localhost:8081"; // URL de tu backend

// --- Componente Modal (puedes moverlo a un archivo separado si prefieres) ---
const ProyectoModal = ({ proyecto, onClose }) => {
  if (!proyecto) return null; // No renderizar si no hay proyecto seleccionado

  return (
    // Overlay oscuro semi-transparente
    <div
      className="fixed inset-0 bg-black bg-opacity-70 flex items-center justify-center z-50 p-4"
      onClick={onClose} // Cerrar al hacer clic fuera del contenido
    >
      {/* Contenedor del contenido del modal */}
      <div
        className="bg-white rounded-lg shadow-xl max-w-3xl w-full max-h-[90vh] overflow-y-auto relative"
        onClick={(e) => e.stopPropagation()} // Evitar que el clic dentro cierre el modal
      >
        {/* Botón de cerrar */}
        <button
          onClick={onClose}
          className="absolute top-3 right-3 text-gray-500 hover:text-gray-800 p-1 bg-white rounded-full z-10"
          aria-label="Cerrar modal"
        >
          <X size={24} />
        </button>

        {/* Imagen del proyecto */}
        <img
          src={proyecto.img}
          alt={proyecto.tit}
          className="w-full h-64 object-cover rounded-t-lg" // Altura fija para la imagen
        />

        {/* Contenido de texto */}
        <div className="p-6">
          <h2 className="text-3xl font-bold text-gray-800 mb-4">
            {proyecto.tit}
          </h2>
          {/* Formatear descripción para mostrar saltos de línea si los hubiera */}
          <p className="text-gray-700 whitespace-pre-wrap">{proyecto.descr}</p>
        </div>
      </div>
    </div>
  );
};

// --- Componente Principal de la Página ---
export default function VisuProyecto() {
  const navKind = useNavKind();
  const navigate = useNavigate();

  // 2. Estados para proyectos, carga, error y modal
  const [proyectos, setProyectos] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [selectedProyecto, setSelectedProyecto] = useState(null); // Proyecto para el modal

  // 3. Cargar proyectos al montar el componente
  useEffect(() => {
    const fetchProyectos = async () => {
      try {
        setLoading(true);
        const response = await axios.get(`${API_URL}/api/proyectos`);
        setProyectos(response.data);
        setError(null);
      } catch (err) {
        console.error("Error cargando proyectos:", err);
        setError("No se pudieron cargar los proyectos.");
      } finally {
        setLoading(false);
      }
    };
    fetchProyectos();
  }, []); // [] = ejecutar solo una vez

  // 4. Funciones para abrir y cerrar el modal
  const openModal = (proyecto) => {
    setSelectedProyecto(proyecto);
  };

  const closeModal = () => {
    setSelectedProyecto(null);
  };

  return (
    <div className="bg-gray-100 min-h-screen">
      {/* NAV dinámico por rol */}
      {navKind === "admin" ? (
        <NavnoCAdm />
      ) : navKind === "client" ? (
        <NavCesionCli />
      ) : (
        <NavnoCesion />
      )}

      {/* Encabezado Opcional (puedes adaptar esto o quitarlo) */}
      <header className="bg-teal-600 text-white text-center py-12 px-4">
        <h1 className="text-4xl font-bold mb-2">Nuestros Proyectos</h1>
        <p className="text-lg opacity-90 max-w-2xl mx-auto">
          Conoce las iniciativas que impulsamos para hacer una diferencia. Tu
          apoyo es fundamental.
        </p>
      </header>

      {/* 5. Galería de Proyectos */}
      <section className="max-w-7xl mx-auto py-10 px-6">
        {/* Renderizado Condicional */}
        {loading && (
          <p className="text-center text-xl text-gray-600">
            Cargando proyectos...
          </p>
        )}
        {error && (
          <p className="text-center text-xl text-red-600 bg-red-100 p-4 rounded-md">
            {error}
          </p>
        )}

        {!loading && !error && proyectos.length === 0 && (
          <p className="text-center text-xl text-gray-600">
            No hay proyectos para mostrar en este momento.
          </p>
        )}

        {!loading && !error && proyectos.length > 0 && (
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-8">
            {/* Mapear proyectos a tarjetas */}
            {proyectos.map((proyecto) => (
              <div
                key={proyecto.id}
                className="bg-white rounded-lg shadow-lg overflow-hidden cursor-pointer transform hover:scale-105 transition-transform duration-300"
                onClick={() => openModal(proyecto)} // Abrir modal al hacer clic
              >
                <img
                  src={proyecto.img}
                  alt={proyecto.tit}
                  className="w-full h-48 object-cover" // Imagen de la tarjeta
                />
                <div className="p-4">
                  <h3 className="text-lg font-semibold text-gray-800 truncate">
                    {proyecto.tit}
                  </h3>
                  {/* Opcional: mostrar un extracto de la descripción */}
                  {/* <p className="text-sm text-gray-600 mt-1 line-clamp-2">{proyecto.descr}</p> */}
                </div>
              </div>
            ))}
          </div>
        )}
      </section>

      <Footer />

      {/* 6. Renderizar el Modal (solo si hay un proyecto seleccionado) */}
      <ProyectoModal proyecto={selectedProyecto} onClose={closeModal} />
    </div>
  );
}
