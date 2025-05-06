import React, { useState } from 'react';

const AgregarMascota = () => {
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
      <header className="bg-white shadow p-4 flex justify-between items-center">
        <div className="text-2xl font-bold text-orange-500">Paraíso SV</div>
        <nav className="space-x-6 text-gray-700 font-medium">
          <a href="#" className="hover:text-orange-500">Productos</a>
          <a href="#" className="hover:text-orange-500">Proyectos</a>
          <a href="#" className="hover:text-orange-500">Madi-Care</a>
          <a href="#" className="hover:text-orange-500">Blog</a>
        </nav>
        <div className="text-gray-600 font-semibold"></div>
      </header>

      {/* Contenido principal */}
      <main className="max-w-5xl mx-auto mt-10 flex flex-col lg:flex-row items-center justify-center gap-10">
        {/* Área de imagen */}
        <div className="w-full lg:w-1/2 flex items-center justify-center border-2 border-dashed border-gray-400 rounded-3xl h-64 bg-[#fdfdf5]">
          <label htmlFor="imagen" className="cursor-pointer w-full h-full flex flex-col items-center justify-center text-gray-400">
            {imagen ? (
              <img src={imagen} alt="Mascota" className="object-cover h-full w-full rounded-3xl" />
            ) : (
              <>
                <svg className="w-12 h-12 mb-2" fill="none" stroke="currentColor" strokeWidth="2" viewBox="0 0 24 24">
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

        {/* Formulario con líneas inferiores */}
        <div className="relative transform -skew-y-2 border border-black rounded-3xl bg-[#F5F0DC] p-6 w-full lg:w-1/2">
          <form className="transform skew-y-2 space-y-6">
            {['Nombre', 'Edad', 'Caso', 'Vacunas'].map((label) => (
              <div key={label}>
                <label className="block text-gray-700 font-medium mb-1">{label}</label>
                <input
                  type="text"
                  placeholder={`Ingrese ${label.toLowerCase()}`}
                  className="w-full bg-transparent border-b-2 border-gray-400 focus:border-orange-500 focus:outline-none py-1"
                />
              </div>
            ))}
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

export default AgregarMascota;
