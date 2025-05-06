import React, { useState } from 'react';
import alcanciaImg from '../assets/alcancia.png';

const DonarAlcancia = () => {
  const [descripcion, setDescripcion] = useState('');
  const [meta, setMeta] = useState('');

  return (
    <div className="min-h-screen bg-red-600 font-sans">
      {/* Encabezado dividido en dos mitades */}
      <header className="flex w-full h-20">
        {/* Mitad izquierda: beige */}
        <div className="w-2/3 bg-[#F5F0DC] flex items-center justify-between px-6">
          <div className="text-2xl font-bold text-orange-500">Paraíso SV</div>
          <nav className="space-x-6 text-gray-700 font-medium">
            <a href="#" className="hover:text-orange-500">Productos</a>
            <a href="#" className="hover:text-orange-500">Proyectos</a>
            <a href="#" className="hover:text-orange-500">Madi-Care</a>
            <a href="#" className="hover:text-orange-500">Blog</a>
          </nav>
        </div>

        {/* Mitad derecha: roja */}
        <div className="w-1/3 bg-red-600 flex items-center justify-end px-6">
          <div className="text-white font-semibold">JUANCHO</div>
        </div>
      </header>

      {/* Contenido dividido */}
      <main className="flex flex-col lg:flex-row w-full h-full">
        {/* Lado izquierdo: formulario con diseño inclinado */}
        <div className="w-full lg:w-2/3 bg-[#F5F0DC] flex items-center justify-center min-h-[calc(100vh-80px)]">
          <div className="relative transform -skew-y-2 border border-black rounded-3xl bg-[#F5F0DC] p-6 w-full max-w-2xl">
            <form className="transform skew-y-2 space-y-6">
              <h2 className="text-3xl font-bold text-center text-gray-800"></h2>

              <div>
                <label className="block text-gray-700 font-medium mb-1">Descripción</label>
                <input
                  type="text"
                  value={descripcion}
                  onChange={(e) => setDescripcion(e.target.value)}
                  placeholder="¿Para qué es esta alcancía?"
                  className="w-full bg-transparent border-b-2 border-gray-400 focus:border-orange-500 focus:outline-none py-2 text-lg"
                />
              </div>

              <div>
                <label className="block text-gray-700 font-medium mb-1">Meta monetaria</label>
                <input
                  type="number"
                  value={meta}
                  onChange={(e) => setMeta(e.target.value)}
                  placeholder="$ 1000"
                  className="w-full bg-transparent border-b-2 border-gray-400 focus:border-orange-500 focus:outline-none py-2 text-lg"
                />
              </div>

              <div className="pt-4 flex flex-col sm:flex-row gap-4">
                <button
                  type="submit"
                  className="w-full bg-orange-500 text-white py-3 rounded-md font-semibold hover:bg-orange-600 transition"
                >
                  Donar alcancía
                </button>
                <button
                  type="button"
                  className="w-full bg-[#007B8A] text-white py-3 rounded-md font-semibold hover:bg-[#006b75] transition"
                >
                  Cancelar
                </button>
              </div>
            </form>
          </div>
        </div>

        {/* Lado derecho: imagen */}
        <div className="w-full lg:w-1/3 bg-red-600 flex items-center justify-center p-10 min-h-[calc(100vh-80px)]">
          <img
            src={alcanciaImg}
            alt="Alcancía"
            className="max-h-80 object-contain rounded-xl"
          />
        </div>
      </main>
    </div>
  );
};

export default DonarAlcancia;
