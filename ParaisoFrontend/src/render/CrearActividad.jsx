import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import NavnoCAdm from "./componentes/navCesionAdm.jsx";
import Footer from "./componentes/footer.jsx";
import { useAuth } from "../context/AuthContext.jsx"; // Para obtener el token

const API_URL = "http://localhost:8081"; // URL del backend

const CrearActividad = () => {
  const navigate = useNavigate();
  const { user } = useAuth(); // Para verificar si es admin y obtener token

  // Estados para el formulario
  const [formData, setFormData] = useState({
    tit: "", // Coincide con ProyectosCreateDTO
    descr: "", // Coincide con ProyectosCreateDTO
  });
  const [imagenFile, setImagenFile] = useState(null); // Archivo de imagen
  const [imagenPreview, setImagenPreview] = useState(null); // Vista previa
  const [submitting, setSubmitting] = useState(false);

  // Manejar cambio en inputs de texto
  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  // Manejar cambio en input de archivo (imagen)
  const handleImagenChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      setImagenFile(file);
      setImagenPreview(URL.createObjectURL(file));
    } else {
      setImagenFile(null);
      setImagenPreview(null);
    }
  };

  // Manejar envío del formulario
  const handleSubmit = async (e) => {
    e.preventDefault();

    // Validaciones
    if (!formData.tit || !formData.descr) {
      alert("Por favor, completa el título y la descripción.");
      return;
    }
    if (!imagenFile) {
      // Tu backend requiere imagen obligatoria para crear proyectos
      alert("Por favor, sube una imagen para la actividad.");
      return;
    }
    if (!user || !user.token) {
      alert("No estás autenticado. Inicia sesión como Admin.");
      return;
    }
    // Podrías añadir una verificación explícita del rol si lo tienes en 'user'
    // if (user.role !== 'ADMIN') { ... }

    setSubmitting(true);

    try {
      // --- PASO 1: Subir la imagen ---
      const fileData = new FormData();
      fileData.append("file", imagenFile);

      const configImg = {
        headers: {
          "Content-Type": "multipart/form-data",
          Authorization: `Bearer ${user.token}`,
        },
      };

      const resImg = await axios.post(
        `${API_URL}/api/files`,
        fileData,
        configImg
      );
      const imageUrl = resImg.data.url;

      // --- PASO 2: Crear la actividad/proyecto ---
      const projectData = {
        tit: formData.tit,
        descr: formData.descr,
        img: imageUrl, // URL obtenida del paso 1
      };

      const configProject = {
        headers: {
          Authorization: `Bearer ${user.token}`,
        },
      };

      await axios.post(`${API_URL}/api/proyectos`, projectData, configProject);

      alert("¡Actividad creada exitosamente!");
      // Podrías navegar a la lista de proyectos o al inicio
      navigate("/");
    } catch (error) {
      console.error("Error al crear la actividad:", error);
      if (error.response?.status === 403) {
        alert("Error: No tienes permisos de Administrador.");
      } else if (error.response?.status === 401) {
        alert("Error: Tu sesión ha expirado. Vuelve a iniciar sesión.");
      } else {
        alert("Ocurrió un error inesperado al crear la actividad.");
      }
    } finally {
      setSubmitting(false);
    }
  };

  // Botón Cancelar
  const handleCancelar = () => {
    setFormData({ tit: "", descr: "" });
    setImagenFile(null);
    setImagenPreview(null);
  };

  return (
    <div className="min-h-screen bg-[#f5f5dc] font-sans">
      <NavnoCAdm /> {/* Asumimos que solo Admins llegan aquí */}
      <main className="max-w-4xl mx-auto py-16 px-6">
        <h1 className="text-4xl font-bold text-center text-gray-800 mb-10">
          Crear Nueva Actividad/Proyecto
        </h1>

        <form
          onSubmit={handleSubmit}
          className="bg-white p-8 rounded-lg shadow-lg space-y-6"
        >
          {/* Input Título */}
          <div>
            <label
              htmlFor="tit"
              className="block text-lg font-medium text-gray-700 mb-1"
            >
              Título
            </label>
            <input
              type="text"
              id="tit"
              name="tit"
              value={formData.tit}
              onChange={handleChange}
              placeholder="Ej: Campaña de Reforestación 2025"
              className="w-full px-4 py-2 border border-gray-300 rounded-md focus:ring-orange-500 focus:border-orange-500"
              required
            />
          </div>

          {/* Input Descripción */}
          <div>
            <label
              htmlFor="descr"
              className="block text-lg font-medium text-gray-700 mb-1"
            >
              Descripción
            </label>
            <textarea
              id="descr"
              name="descr"
              rows="4"
              value={formData.descr}
              onChange={handleChange}
              placeholder="Describe en qué consiste la actividad..."
              className="w-full px-4 py-2 border border-gray-300 rounded-md focus:ring-orange-500 focus:border-orange-500 resize-none"
              required
            ></textarea>
          </div>

          {/* Input Imagen */}
          <div>
            <label className="block text-lg font-medium text-gray-700 mb-2">
              Imagen de Portada (Obligatoria)
            </label>
            <div className="mt-1 flex justify-center px-6 pt-5 pb-6 border-2 border-gray-300 border-dashed rounded-md">
              <div className="space-y-1 text-center">
                {imagenPreview ? (
                  <img
                    src={imagenPreview}
                    alt="Vista previa"
                    className="mx-auto h-48 w-auto rounded-md"
                  />
                ) : (
                  <svg
                    className="mx-auto h-12 w-12 text-gray-400"
                    stroke="currentColor"
                    fill="none"
                    viewBox="0 0 48 48"
                    aria-hidden="true"
                  >
                    <path
                      d="M28 8H12a4 4 0 00-4 4v20m32-12v8m0 0v8a4 4 0 01-4 4H12a4 4 0 01-4-4v-4m32-4l-3.172-3.172a4 4 0 00-5.656 0L28 28M8 32l9.172-9.172a4 4 0 015.656 0L28 28m0 0l4 4m4-24h8m-4-4v8m-12 4h.02"
                      strokeWidth="2"
                      strokeLinecap="round"
                      strokeLinejoin="round"
                    />
                  </svg>
                )}
                <div className="flex text-sm text-gray-600 justify-center">
                  <label
                    htmlFor="imagen-upload"
                    className="relative cursor-pointer bg-white rounded-md font-medium text-orange-600 hover:text-orange-500 focus-within:outline-none focus-within:ring-2 focus-within:ring-offset-2 focus-within:ring-orange-500 px-1"
                  >
                    <span>Sube un archivo</span>
                    <input
                      id="imagen-upload"
                      name="imagen-upload"
                      type="file"
                      className="sr-only"
                      accept="image/*"
                      onChange={handleImagenChange}
                    />
                  </label>
                  <p className="pl-1">o arrastra y suelta</p>
                </div>
                <p className="text-xs text-gray-500">
                  PNG, JPG, WEBP hasta 10MB
                </p>
              </div>
            </div>
          </div>

          {/* Botones */}
          <div className="flex justify-end gap-4 pt-4">
            <button
              type="button"
              onClick={handleCancelar}
              disabled={submitting}
              className="bg-gray-200 text-gray-700 py-2 px-6 rounded-md font-semibold hover:bg-gray-300 transition disabled:opacity-50"
            >
              Cancelar
            </button>
            <button
              type="submit"
              disabled={submitting}
              className="bg-orange-500 text-white py-2 px-6 rounded-md font-semibold hover:bg-orange-600 transition disabled:opacity-50"
            >
              {submitting ? "Creando..." : "Crear Actividad"}
            </button>
          </div>
        </form>
      </main>
      <Footer />
    </div>
  );
};

export default CrearActividad;
