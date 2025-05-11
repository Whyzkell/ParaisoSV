import React, { useState } from "react";
import NavnoCAdm from "./componentes/navCesionAdm.jsx";

const ActualizarActividad = () => {
  const [imagen, setImagen] = useState(null);
  const [titulo, setTitulo] = useState("");
  const [descripcion, setDescripcion] = useState("");

  const handleImagenChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      setImagen(URL.createObjectURL(file));
    }
  };

  return (
    <div className="min-h-screen bg-[#f5f5dc] font-sans">
      <NavnoCAdm />

      {/* Formulario principal */}
      <main className="max-w-md mx-auto mt-10 bg-[#F5F0DC] p-6 rounded-3xl shadow-md border border-black">
        <form className="space-y-4">
          {/* Área de imagen */}
          <div className="flex items-center justify-center border-2 border-dashed border-gray-400 rounded-xl h-40 bg-[#fdfdf5]">
            <label
              htmlFor="imagen"
              className="cursor-pointer w-full h-full flex flex-col items-center justify-center text-gray-400"
            >
              {imagen ? (
                <img
                  src={imagen}
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
                  <p className="text-sm">Sube una imagen</p>
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

          {/* Campo Título */}
          <div>
            <label className="block text-gray-700 font-medium mb-1">
              Título
            </label>
            <input
              type="text"
              value={titulo}
              onChange={(e) => setTitulo(e.target.value)}
              placeholder="Ingrese el título"
              className="w-full bg-transparent border-b-2 border-gray-400 focus:border-orange-500 focus:outline-none py-1 text-sm"
            />
          </div>

          {/* Campo Descripción */}
          <div>
            <label className="block text-gray-700 font-medium mb-1">
              Descripción
            </label>
            <textarea
              value={descripcion}
              onChange={(e) => setDescripcion(e.target.value)}
              placeholder="Ingrese la descripción"
              rows="3"
              className="w-full bg-transparent border-b-2 border-gray-400 focus:border-orange-500 focus:outline-none py-1 resize-none text-sm"
            />
          </div>

          {/* Botones */}
          <div className="pt-2 flex flex-col sm:flex-row gap-3">
            <button
              type="button"
              className="w-full bg-[#007B8A] text-white py-2 rounded-md font-semibold hover:bg-[#006b75] transition text-sm"
            >
              Cancelar
            </button>
            <button
              type="submit"
              className="w-full bg-orange-500 text-white py-2 rounded-md font-semibold hover:bg-orange-600 transition text-sm"
            >
              Publicar
            </button>
          </div>
        </form>
      </main>
    </div>
  );
};

export default ActualizarActividad;
