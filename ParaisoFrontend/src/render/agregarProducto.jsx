import React, { useState } from "react";
import NavnoCAdm from "./componentes/navCesionAdm.jsx";

const AgregarProducto = () => {
  const [imagen, setImagen] = useState(null);

  const handleImagenChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      setImagen(URL.createObjectURL(file));
    }
  };

  return (
    <div className="min-h-screen bg-[#f5f5dc] font-sans">
      {/* Encabezado */}
      <NavnoCAdm />

      {/* Contenido principal */}
      <main className="max-w-5xl mx-auto mt-10 flex flex-col lg:flex-row items-center justify-center gap-10">
        {/* Área de imagen */}
        <div className="w-full lg:w-1/2 flex items-center justify-center border-2 border-dashed border-gray-400 rounded-3xl h-64 bg-[#fdfdf5]">
          <label
            htmlFor="imagen"
            className="cursor-pointer w-full h-full flex flex-col items-center justify-center text-gray-400"
          >
            {imagen ? (
              <img
                src={imagen}
                alt="Producto"
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
                <p>Sube una imagen del producto</p>
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

        {/* Formulario con líneas inferiores */}
        <div className="relative transform -skew-y-2 border border-black rounded-3xl bg-[#F5F0DC] p-6 w-full lg:w-1/2">
          <form className="transform skew-y-2 space-y-6">
            {["Nombre del producto", "Descripción", "Precio", "Categoría"].map(
              (label) => (
                <div key={label}>
                  <label className="block text-gray-700 font-medium mb-1">
                    {label}
                  </label>
                  <input
                    type="text"
                    placeholder={`Ingrese ${label.toLowerCase()}`}
                    className="w-full bg-transparent border-b-2 border-gray-400 focus:border-orange-500 focus:outline-none py-1"
                  />
                </div>
              )
            )}
            <div className="pt-4 flex gap-4">
              <button
                type="submit"
                className="w-full bg-orange-500 text-white py-2 rounded-md font-semibold hover:bg-orange-600 transition"
              >
                Publicar
              </button>
              <button
                type="button"
                className="w-full bg-[#007B8A] text-white py-2 rounded-md font-semibold hover:bg-[#006b75] transition"
              >
                Cancelar
              </button>
            </div>
          </form>
        </div>
      </main>
    </div>
  );
};

export default AgregarProducto;
