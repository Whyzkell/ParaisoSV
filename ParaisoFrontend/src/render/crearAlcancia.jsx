import React, { useState } from "react";

const CrearAlcancia = () => {
  const [descripcion, setDescripcion] = useState("");
  const [meta, setMeta] = useState("");

  const handleCrear = () => {
    console.log("Descripción:", descripcion);
    console.log("Meta monetaria:", meta);
    // Aquí podrías agregar el fetch o lógica para guardar
    alert("¡Alcancía creada exitosamente!");
  };

  const handleCancelar = () => {
    setDescripcion("");
    setMeta("");
  };

  return (
    <div className="flex flex-col md:flex-row min-h-screen">
      {/* Lado Izquierdo */}
      <div className="w-full md:w-1/2 bg-[#e8e1cc] p-8">
        <h1 className="text-3xl font-bold text-gray-800 mb-8">
          Crear alcancía
        </h1>

        <div className="bg-gradient-to-br from-yellow-400 to-yellow-500 p-6 rounded-[60px] mb-6 transform rotate-[-2deg]">
          <label className="block text-lg font-bold text-gray-800 mb-2">
            Descripción
          </label>
          <textarea
            value={descripcion}
            onChange={(e) => setDescripcion(e.target.value)}
            className="w-full h-40 p-4 rounded-md bg-[#f8efdb] focus:outline-none resize-none text-gray-700"
            placeholder="Describe tu alcancía..."
          ></textarea>
        </div>

        <div className="mb-4">
          <label className="block text-lg font-bold text-gray-800 mb-2">
            Meta monetaria
          </label>
          <input
            type="number"
            value={meta}
            onChange={(e) => setMeta(e.target.value)}
            className="w-full p-3 rounded-md bg-[#f8efdb] focus:outline-none text-gray-700"
            placeholder="$0.00"
          />
        </div>

        <div className="flex flex-col gap-4 mt-6">
          <button
            onClick={handleCrear}
            className="bg-orange-500 text-white font-bold py-3 rounded-md hover:bg-orange-600 transition duration-300"
          >
            Crear alcancía
          </button>
          <button
            onClick={handleCancelar}
            className="bg-cyan-700 text-white font-bold py-3 rounded-md hover:bg-cyan-800 transition duration-300"
          >
            Cancelar
          </button>
        </div>
      </div>

      {/* Lado Derecho */}
      <div className="w-full md:w-1/2 bg-red-600 flex items-center justify-center">
        <img
          src="/path/to/your/piggybank-image.png"
          alt="Alcancía"
          className="w-2/3 object-contain"
        />
      </div>
    </div>
  );
};

export default CrearAlcancia;
