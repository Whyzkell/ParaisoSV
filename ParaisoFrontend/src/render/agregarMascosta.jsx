import React, { useState } from "react";
import NavnoCAdm from "./componentes/navCesionAdm.jsx";
import Footer from "./componentes/footer.jsx";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext.jsx"; // 1. Importar Auth
import axios from "axios"; // 2. Importar Axios

const API_URL = "http://localhost:8081"; // La URL de tu backend

const AgregarMascota = () => {
  const navigate = useNavigate();
  const { user } = useAuth(); // 3. Obtener el usuario (para el token)

  // 4. Estado para los campos del formulario (basado en PerroCreateDTO)
  const [formData, setFormData] = useState({
    nombre: "",
    raza: "",
    edad: "",
    descr: "",
  });

  // 5. Estados separados para el archivo y la vista previa
  const [imagenFile, setImagenFile] = useState(null); // El archivo a subir
  const [imagenPreview, setImagenPreview] = useState(null); // La URL para mostrar
  const [submitting, setSubmitting] = useState(false);

  // 6. Manejador para el archivo de imagen
  const handleImagenChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      setImagenFile(file); // Guardamos el archivo
      setImagenPreview(URL.createObjectURL(file)); // Creamos la vista previa
    } else {
      setImagenFile(null);
      setImagenPreview(null);
    }
  };

  // 7. Manejador para los campos de texto
  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  // 8. Lógica para cancelar
  const handleCancelar = () => {
    setFormData({ nombre: "", raza: "", edad: "", descr: "" });
    setImagenFile(null);
    setImagenPreview(null);
  };

  // 9. Lógica de envío (el proceso de 2 pasos)
  const handleSubmit = async (e) => {
    e.preventDefault();

    // Validaciones
    if (!imagenFile) {
      alert("Por favor, sube una foto de la mascota.");
      return;
    }
    if (
      !formData.nombre ||
      !formData.raza ||
      !formData.edad ||
      !formData.descr
    ) {
      alert("Por favor, completa todos los campos.");
      return;
    }
    if (!user || !user.token) {
      alert("No estás autenticado. Inicia sesión como Admin.");
      return;
    }

    setSubmitting(true);

    try {
      // --- PASO 1: Subir la imagen ---
      const fileData = new FormData();
      fileData.append("file", imagenFile); // "file" es la 'key' que espera tu FileController

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
      const imageUrl = resImg.data.url; // Obtenemos la URL de la imagen subida

      // --- PASO 2: Crear la mascota con la URL ---
      const petData = {
        nombre: formData.nombre,
        raza: formData.raza,
        edad: parseInt(formData.edad), // El DTO espera un número
        descr: formData.descr,
        img: imageUrl, // Le pasamos la URL del Paso 1
      };

      const configPet = {
        headers: {
          Authorization: `Bearer ${user.token}`,
        },
      };

      await axios.post(`${API_URL}/api/perros`, petData, configPet);

      alert("¡Mascota publicada exitosamente!");
      navigate("/"); // O a la página de visualizar mascotas
    } catch (error) {
      console.error("Error al agregar mascota:", error);
      if (error.response?.status === 403) {
        alert("Error: No tienes permisos de Administrador.");
      } else if (error.response?.status === 401) {
        alert("Error: Tu sesión ha expirado. Vuelve a iniciar sesión.");
      } else {
        alert("Ocurrió un error inesperado al publicar.");
      }
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div className="min-h-screen bg-[#f5f5dc] font-sans">
      <NavnoCAdm />

      <main className="max-w-5xl py-24 mx-auto mt-10 flex flex-col lg:flex-row items-center justify-center gap-10">
        {/* Área de imagen */}
        <div className="w-full lg:w-1/2 flex items-center justify-center border-2 border-dashed border-gray-400 rounded-3xl h-64 bg-[#fdfdf5]">
          <label
            htmlFor="imagen"
            className="cursor-pointer w-full h-full flex flex-col items-center justify-center text-gray-400"
          >
            {imagenPreview ? ( // <--- Usamos la vista previa
              <img
                src={imagenPreview}
                alt="Mascota"
                className="object-cover h-full w-full rounded-3xl"
              />
            ) : (
              <>
                <svg
                  className="w-12 h-12 mb-2"
                  fill="none"
                  stroke="currentColor"
                  strokeWidth="2"
                  viewBox="0 0 24 24"
                >
                  <path d="M3 16l4-4a4 4 0 015.656 0L21 4M3 16v5h5M21 4v5h-5" />
                </svg>
                <p>Sube una foto de tu mascota</p>
              </>
            )}
            <input
              id="imagen"
              type="file"
              accept="image/*"
              onChange={handleImagenChange}
              className="hidden"
            />
          </label>
        </div>

        {/* Formulario (Campos corregidos) */}
        <div className="relative transform -skew-y-2 border border-black rounded-3xl bg-[#F5F0DC] p-6 w-full lg:w-1/2">
          {/* 10. Conectar el formulario */}
          <form
            className="transform skew-y-2 space-y-6"
            onSubmit={handleSubmit}
          >
            {/* Campo Nombre */}
            <div>
              <label className="block text-gray-700 font-medium mb-1">
                Nombre
              </label>
              <input
                type="text"
                name="nombre"
                value={formData.nombre}
                onChange={handleChange}
                placeholder="Ingrese nombre"
                className="w-full bg-transparent border-b-2 border-gray-400 focus:border-orange-500 focus:outline-none py-1"
              />
            </div>

            {/* Campo Raza (Corregido) */}
            <div>
              <label className="block text-gray-700 font-medium mb-1">
                Raza
              </label>
              <input
                type="text"
                name="raza"
                value={formData.raza}
                onChange={handleChange}
                placeholder="Ingrese raza"
                className="w-full bg-transparent border-b-2 border-gray-400 focus:border-orange-500 focus:outline-none py-1"
              />
            </div>

            {/* Campo Edad */}
            <div>
              <label className="block text-gray-700 font-medium mb-1">
                Edad (años)
              </label>
              <input
                type="number"
                name="edad"
                value={formData.edad}
                onChange={handleChange}
                placeholder="Ingrese edad"
                className="w-full bg-transparent border-b-2 border-gray-400 focus:border-orange-500 focus:outline-none py-1"
              />
            </div>

            {/* Campo Descripción (Corregido) */}
            <div>
              <label className="block text-gray-700 font-medium mb-1">
                Descripción
              </label>
              <textarea
                name="descr"
                value={formData.descr}
                onChange={handleChange}
                placeholder="Describa el caso de la mascota..."
                className="w-full bg-transparent border-b-2 border-gray-400 focus:border-orange-500 focus:outline-none py-1 h-20 resize-none"
              />
            </div>

            <div className="pt-4 flex gap-4">
              <button
                type="submit"
                disabled={submitting}
                className="w-full bg-orange-500 text-white py-2 rounded-md font-semibold hover:bg-orange-600 transition disabled:opacity-50"
              >
                {submitting ? "Publicando..." : "Publicar"}
              </button>
              <button
                type="button"
                onClick={handleCancelar}
                disabled={submitting}
                className="w-full bg-[#007B8A] text-white py-2 rounded-md font-semibold hover:bg-[#006b75] transition disabled:opacity-50"
              >
                Cancelar
              </button>
            </div>
          </form>
        </div>
      </main>
      <Footer />
    </div>
  );
};

export default AgregarMascota;
