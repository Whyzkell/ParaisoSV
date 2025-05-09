import React from "react";

const Donaciones = () => {
  const donaciones = Array.from({ length: 12 }, (_, i) => ({
    nombre: "Juan",
    fecha: "25/08/2022",
    alcancia: 89,
    monto: 400,
  }));

  return (
    <div className="min-h-screen bg-[#e7e1d2]">
      {/* Header */}
      <header className="bg-red-600 py-6 flex flex-col items-center">
        <nav className="flex justify-between items-center w-full max-w-6xl px-6">
          <div className="flex items-center gap-4">
            <span className="text-white font-semibold">Paraiso SV</span>
          </div>
          <div className="flex gap-8 text-white font-medium">
            <a href="#">Productos</a>
            <a href="#">Proyectos</a>
            <a href="#">Madi-Care</a>
            <a href="#">Blog</a>
          </div>
          <div className="flex gap-4">
            <button className="border border-white text-white rounded-full px-4 py-1">
              Iniciar Sesión
            </button>
            <button className="border border-white text-white rounded-full px-4 py-1">
              Registrarse
            </button>
          </div>
        </nav>
        <h1 className="text-3xl font-bold text-gray-800 mt-6">Donaciones</h1>
      </header>

      {/* Grid de donaciones */}
      <div className="max-w-6xl mx-auto py-10 px-6 grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {donaciones.map((dona, idx) => (
          <div key={idx} className="bg-[#f5eedf] rounded-lg p-6 shadow-md">
            <div className="flex justify-between items-center">
              <h2 className="font-bold text-gray-700">{dona.nombre}</h2>
              <span className="text-sm text-gray-500">{dona.fecha}</span>
            </div>
            <p className="mt-4 text-gray-600 font-semibold">
              Alcancia: {dona.alcancia}
            </p>
            <p className="mt-2 text-teal-600 font-bold text-xl">
              ${dona.monto}
            </p>
          </div>
        ))}
      </div>
    </div>
  );
};

export default Donaciones;
