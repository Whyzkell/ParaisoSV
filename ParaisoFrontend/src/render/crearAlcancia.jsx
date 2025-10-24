import React, { useState } from "react";
import chancho from "../assets/chancho.png";
import { useNavigate } from "react-router-dom";
import NavnoCAdm from "./componentes/navCesionAdm.jsx";
import Footer from "./componentes/footer.jsx";
import { useAuth } from "../context/AuthContext.jsx"; // 1. Importar el hook de autenticación
import axios from "axios"; // 2. Importar axios

// Asumo que esta es la URL de tu backend (la vi en tu AuthContext)
const API_URL = "http://localhost:8081";

const CrearAlcancia = () => {
  const [descripcion, setDescripcion] = useState("");
  const [meta, setMeta] = useState("");
  const [submitting, setSubmitting] = useState(false); // Para deshabilitar el botón
  const navigate = useNavigate();
  const { user } = useAuth(); // 3. Obtener el estado del usuario (que tiene el token)

  const handleClick = () => {
    navigate("/");
  };

  // 4. Convertir handleCrear en una función asíncrona
  const handleCrear = async () => {
    // Validación simple
    if (!descripcion || !meta) {
      alert("Por favor, completa todos los campos.");
      return;
    }
    if (parseFloat(meta) <= 0) {
      alert("La meta debe ser un número mayor a cero.");
      return;
    }

    // Verificar si el usuario está autenticado
    if (!user || !user.token) {
      alert("No estás autenticado. Inicia sesión como Admin.");
      return;
    }

    setSubmitting(true);

    // Este es el body que tu DTO (AlcanciaCreateDTO) espera
    const body = {
      descr: descripcion,
      precioMeta: parseFloat(meta), // Asegurarse que sea un número
    };

    // Configuración de la petición con el token de autorización
    const config = {
      headers: {
        Authorization: `Bearer ${user.token}`,
      },
    };

    try {
      // 5. Hacer la llamada POST al backend
      await axios.post(`${API_URL}/api/alcancias`, body, config);

      alert("¡Alcancía creada exitosamente!");
      navigate("/"); // Redirigir al inicio después de crear
    } catch (error) {
      console.error("Error al crear la alcancía:", error);
      // Manejar errores comunes
      if (error.response?.status === 403) {
        alert(
          "Error: No tienes permisos de Administrador para crear alcancías."
        );
      } else if (error.response?.status === 401) {
        alert("Error: Tu sesión ha expirado. Vuelve a iniciar sesión.");
      } else {
        alert("Ocurrió un error inesperado al crear la alcancía.");
      }
    } finally {
      setSubmitting(false); // Reactivar el botón
    }
  };

  const handleCancelar = () => {
    setDescripcion("");
    setMeta("");
  };

  return (
    <>
      <NavnoCAdm />
      <div className="flex flex-col md:flex-row min-h-screen">
        {/* Lado Izquierdo */}

        <div className="w-full md:w-1/2 bg-[#e8e1cc] p-8">
          <h1 className="text-3xl font-bold text-gray-800 mb-8">
            Crear alcancía
          </h1>

          <div className="bg-gradient-to-br from-yellow-400 to-yellow-500 p-6 rounded-[60px] mb-6 transform rotate-[-2deg] mt-16">
            <label className="block text-2xl font-bold text-gray-800 mb-2">
              Descripción
            </label>
            <textarea
              value={descripcion}
              onChange={(e) => setDescripcion(e.target.value)}
              className="w-full h-80 p-4 rounded-md bg-[#f8efdb] focus:outline-none resize-none text-gray-700"
              placeholder="Describe tu alcancía..."
            ></textarea>
          </div>

          <div className="mb-4 mt-16">
            <label className="block text-2xl font-bold text-gray-800 mb-2">
              Meta monetaria
            </label>
            <input
              type="number"
              min="0.01" // Añadir validación HTML
              step="0.01" // Permitir decimales
              value={meta}
              onChange={(e) => setMeta(e.target.value)}
              className="w-full p-3 rounded-md bg-[#f8efdb] focus:outline-none text-gray-700"
              placeholder="$0.00"
            />
          </div>

          <div className="flex flex-col gap-4 mt-10">
            <button
              onClick={handleCrear}
              // 6. Deshabilitar el botón mientras se envía
              disabled={submitting}
              className="bg-orange-500 text-white font-bold py-3 rounded-md hover:bg-orange-600 transition duration-300 text-2xl disabled:opacity-50"
            >
              {/* Cambiar texto del botón al enviar */}
              {submitting ? "Creando..." : "Crear alcancía"}
            </button>
            <button
              onClick={handleCancelar}
              className="bg-cyan-700 text-white font-bold py-3 rounded-md hover:bg-cyan-800 transition duration-300 text-2xl"
            >
              Cancelar
            </button>

            <button
              onClick={handleClick}
              className="bg-red-600 text-white font-bold py-3 rounded-md hover:bg-red-800 transition duration-300 text-2xl"
            >
              Volver
            </button>
          </div>
        </div>

        {/* Lado Derecho */}
        <div className="w-full md:w-1/2 bg-red-600 flex items-center justify-center">
          <img src={chancho} alt="Alcancía" className="w-2/3 object-contain" />
        </div>
      </div>
      <Footer />
    </>
  );
};

export default CrearAlcancia;
