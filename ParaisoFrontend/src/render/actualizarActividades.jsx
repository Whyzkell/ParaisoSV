import React, { useState, useEffect } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import NavnoCAdm from "./componentes/navCesionAdm.jsx";
import Footer from "./componentes/footer.jsx";
import { useAuth } from "../context/AuthContext.jsx"; // Para autenticación

const API_URL = "http://localhost:8081"; // URL del backend

const ActualizarActividad = () => {
  const navigate = useNavigate();
  const { user } = useAuth(); // Obtener usuario y token

  // Estados
  const [proyectos, setProyectos] = useState([]); // Lista de todas las actividades
  const [selectedProyecto, setSelectedProyecto] = useState(null); // Actividad seleccionada para editar
  const [formData, setFormData] = useState({ tit: "", descr: "" }); // Datos del formulario
  const [imagenFile, setImagenFile] = useState(null); // Nuevo archivo de imagen
  const [imagenPreview, setImagenPreview] = useState(null); // Vista previa (URL)
  const [loadingList, setLoadingList] = useState(true); // Carga de la lista
  const [listError, setListError] = useState(null); // Error al cargar lista
  const [submitting, setSubmitting] = useState(false); // Enviando formulario

  // Cargar todos los proyectos al inicio
  const fetchProyectos = async () => {
    try {
      setLoadingList(true);
      const response = await axios.get(`${API_URL}/api/proyectos`);
      setProyectos(response.data);
      setListError(null);
    } catch (err) {
      console.error("Error cargando proyectos:", err);
      setListError("No se pudieron cargar las actividades.");
    } finally {
      setLoadingList(false);
    }
  };

  useEffect(() => {
    // Verificar si es Admin antes de cargar
    // IMPORTANTE: Asegúrate que tu `AuthContext` guarde el rol del usuario en `user.role`
    // Si no, esta verificación fallará. Comenta o ajusta si es necesario.
    if (!user || !user.token /* || user.role !== 'ADMIN' */) {
      setListError("Acceso denegado. Solo Administradores pueden editar.");
      setLoadingList(false);
      // Podrías redirigir: navigate('/login');
      return;
    }
    fetchProyectos();
  }, [user]); // Depende de 'user' para tener el token y rol

  // Seleccionar un proyecto para editar
  const handleSelectProyecto = (proyecto) => {
    setSelectedProyecto(proyecto);
    setFormData({ tit: proyecto.tit, descr: proyecto.descr });
    setImagenPreview(proyecto.img); // Mostrar imagen actual
    setImagenFile(null); // Limpiar selección de archivo nuevo
  };

  // Manejar cambios en el formulario de texto
  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  // Manejar cambio de imagen
  const handleImagenChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      setImagenFile(file);
      setImagenPreview(URL.createObjectURL(file)); // Vista previa de la nueva imagen
    }
  };

  // Enviar la actualización
  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!selectedProyecto || !user || !user.token) {
      alert(
        "Selecciona una actividad y asegúrate de estar logueado como Admin."
      );
      return;
    }
    // Simple validación para no enviar campos vacíos si antes no lo estaban
    if (!formData.tit || !formData.descr) {
      alert("El título y la descripción no pueden estar vacíos.");
      return;
    }

    setSubmitting(true);
    let finalImageUrl = selectedProyecto.img; // Usar imagen actual por defecto

    try {
      // PASO 1 (Opcional): Subir nueva imagen si existe
      if (imagenFile) {
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
        finalImageUrl = resImg.data.url; // Usar la nueva URL
      }

      // PASO 2: Construir payload SOLO con los campos que cambiaron
      const updateData = {};
      if (formData.tit !== selectedProyecto.tit) {
        updateData.tit = formData.tit;
      }
      if (formData.descr !== selectedProyecto.descr) {
        updateData.descr = formData.descr;
      }
      if (finalImageUrl !== selectedProyecto.img) {
        updateData.img = finalImageUrl;
      }

      // Solo hacer PUT si hay algo que cambiar
      if (Object.keys(updateData).length > 0) {
        const configProject = {
          headers: { Authorization: `Bearer ${user.token}` },
        };
        await axios.put(
          `${API_URL}/api/proyectos/${selectedProyecto.id}`,
          updateData,
          configProject
        );
      } else {
        alert("No se detectaron cambios para guardar.");
        setSubmitting(false); // Reactivar botón si no hubo cambios
        return; // Salir si no hay nada que enviar
      }

      alert("¡Actividad actualizada exitosamente!");
      // Limpiar y refrescar
      handleCancelar(); // Limpia el formulario y la selección
      fetchProyectos(); // Vuelve a cargar la lista actualizada
    } catch (error) {
      console.error("Error al actualizar la actividad:", error);
      if (error.response?.status === 403) {
        alert("Error: No tienes permisos de Administrador para esta acción.");
      } else if (error.response?.status === 401) {
        alert("Error: Tu sesión ha expirado. Vuelve a iniciar sesión.");
      } else if (error.response?.status === 404) {
        alert("Error: La actividad que intentas editar ya no existe.");
        fetchProyectos(); // Refrescar lista por si acaso
        handleCancelar();
      } else {
        alert("Ocurrió un error inesperado al actualizar la actividad.");
      }
    } finally {
      // Asegurarse de reactivar el botón incluso si no hubo cambios detectados antes
      if (Object.keys(updateData).length > 0) {
        setSubmitting(false);
      }
    }
  };

  // Cancelar edición (limpia el formulario y deselecciona)
  const handleCancelar = () => {
    setSelectedProyecto(null);
    setFormData({ tit: "", descr: "" });
    setImagenFile(null);
    setImagenPreview(null);
  };

  return (
    <div className="min-h-screen bg-[#f5f5dc] font-sans">
      <NavnoCAdm />

      {/* Dividir la pantalla: Lista a la izquierda, Formulario a la derecha */}
      <main className="flex flex-col md:flex-row max-w-7xl mx-auto py-10 px-6 gap-8 min-h-[calc(100vh-160px)]">
        {" "}
        {/* Ajuste de altura */}
        {/* Sección Lista de Actividades */}
        <div className="w-full md:w-1/3 lg:w-2/5 flex flex-col">
          <h2 className="text-2xl font-bold text-gray-800 mb-4 flex-shrink-0">
            Selecciona Actividad a Editar
          </h2>
          {loadingList && <p>Cargando actividades...</p>}
          {listError && <p className="text-red-600">{listError}</p>}
          {!loadingList && !listError && (
            <div className="space-y-3 overflow-y-auto pr-2 flex-grow">
              {" "}
              {/* Scrollable list */}
              {proyectos.length > 0 ? (
                proyectos.map((proj) => (
                  <div
                    key={proj.id}
                    onClick={() => handleSelectProyecto(proj)}
                    className={`p-3 rounded-lg cursor-pointer transition-all border-2 ${
                      selectedProyecto?.id === proj.id
                        ? "bg-orange-100 border-orange-500 shadow-md"
                        : "bg-white border-transparent hover:bg-gray-50 hover:shadow-sm"
                    }`}
                  >
                    <h3 className="font-semibold text-gray-700">{proj.tit}</h3>
                    <p className="text-xs text-gray-500 truncate">
                      {proj.descr}
                    </p>
                  </div>
                ))
              ) : (
                <p>
                  No hay actividades creadas. Puedes crear una{" "}
                  <a
                    href="/crear-actividad"
                    className="text-orange-600 hover:underline"
                  >
                    aquí
                  </a>
                  .
                </p> // Enlace opcional
              )}
            </div>
          )}
        </div>
        {/* Sección Formulario de Edición */}
        <div className="w-full md:w-2/3 lg:w-3/5">
          <h2 className="text-2xl font-bold text-gray-800 mb-4">
            {selectedProyecto
              ? `Editando: ${selectedProyecto.tit}`
              : "Selecciona una actividad de la lista"}
          </h2>
          {/* Mostramos el formulario solo si hay una actividad seleccionada */}
          {selectedProyecto && (
            <div className="bg-[#F5F0DC] p-6 rounded-3xl shadow-md border border-black">
              <form onSubmit={handleSubmit} className="space-y-4">
                {/* Área de imagen */}
                <div className="flex items-center justify-center border-2 border-dashed border-gray-400 rounded-xl h-40 bg-[#fdfdf5]">
                  <label
                    htmlFor="imagen-edit"
                    className="cursor-pointer w-full h-full flex flex-col items-center justify-center text-gray-400"
                  >
                    {imagenPreview ? (
                      <img
                        src={imagenPreview}
                        alt="Actividad"
                        className="object-cover h-full w-full rounded-xl"
                      />
                    ) : (
                      <>
                        <svg
                          className="w-10 h-10 mb-1"
                          fill="none"
                          stroke="currentColor"
                          strokeWidth="2"
                          viewBox="0 0 24 24"
                        >
                          <path d="M3 16l4-4a4 4 0 015.656 0L21 4M3 16v5h5M21 4v5h-5" />
                        </svg>
                        <p className="text-sm">
                          Sube una imagen nueva (Opcional)
                        </p>
                      </>
                    )}
                    <input
                      id="imagen-edit"
                      type="file"
                      accept="image/*"
                      onChange={handleImagenChange}
                      className="hidden"
                    />
                  </label>
                </div>

                {/* Campo Título */}
                <div>
                  <label
                    htmlFor="tit-edit"
                    className="block text-gray-700 font-medium mb-1"
                  >
                    Título
                  </label>
                  <input
                    type="text"
                    id="tit-edit"
                    name="tit"
                    value={formData.tit}
                    onChange={handleChange}
                    className="w-full bg-transparent border-b-2 border-gray-400 focus:border-orange-500 focus:outline-none py-1 text-sm"
                    required // Título sigue siendo requerido
                  />
                </div>

                {/* Campo Descripción */}
                <div>
                  <label
                    htmlFor="descr-edit"
                    className="block text-gray-700 font-medium mb-1"
                  >
                    Descripción
                  </label>
                  <textarea
                    id="descr-edit"
                    name="descr"
                    value={formData.descr}
                    onChange={handleChange}
                    rows="3"
                    className="w-full bg-transparent border-b-2 border-gray-400 focus:border-orange-500 focus:outline-none py-1 resize-none text-sm"
                    required // Descripción sigue siendo requerida
                  />
                </div>

                {/* Botones */}
                <div className="pt-2 flex flex-col sm:flex-row gap-3">
                  <button
                    type="button"
                    onClick={handleCancelar}
                    disabled={submitting}
                    className="w-full bg-[#007B8A] text-white py-2 rounded-md font-semibold hover:bg-[#006b75] transition text-sm disabled:opacity-50"
                  >
                    Cancelar / Deseleccionar
                  </button>
                  <button
                    type="submit"
                    disabled={submitting}
                    className="w-full bg-orange-500 text-white py-2 rounded-md font-semibold hover:bg-orange-600 transition text-sm disabled:opacity-50"
                  >
                    {submitting ? "Actualizando..." : "Actualizar Actividad"}
                  </button>
                </div>
              </form>
            </div>
          )}
          {/* Mensaje si no hay nada seleccionado */}
          {!selectedProyecto && !loadingList && !listError && (
            <p className="text-center text-gray-600 mt-10">
              Haz clic en una actividad de la lista para empezar a editarla.
            </p>
          )}
        </div>
      </main>

      <Footer />
    </div>
  );
};

export default ActualizarActividad;
