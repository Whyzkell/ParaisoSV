import React, { useState } from "react";
import { Link } from "react-router-dom";
import NavnoCAdm from "./componentes/navCesionAdm.jsx";
import Footer from "./componentes/footer.jsx";

const RifaPeluditos = () => {
  const [titulo, setTitulo] = useState("");
  const [costo, setCosto] = useState("");
  const [descripcion, setDescripcion] = useState("");
  const [imagen, setImagen] = useState(null);

  const handleImageChange = (e) => {
    const file = e.target.files[0];
    if (file) setImagen(URL.createObjectURL(file));
  };

  return (
    <>
      {/* HEADER */}
      <div className="bg-[#F5F0DC] ">
        <NavnoCAdm />
      </div>

      {/* CONTENIDO PRINCIPAL */}
      <section className="bg-[#F5F0DC]  py-20 px-4 flex flex-col lg:flex-row items-start justify-center gap-14">
        {/* FORMULARIO IZQUIERDO - MÁS GRANDE */}
        <div className="relative transform -skew-y-3 bg-[#f5f0dc] border border-gray-300 rounded-3xl shadow-md p-6 max-w-md w-full">
          <div className="transform skew-y-3">
            <label className="block mb-2 text-sm font-bold text-gray-700">
              Título
            </label>
            <input
              type="text"
              value={titulo}
              onChange={(e) => setTitulo(e.target.value)}
              className="w-full mb-4 px-4 py-2 rounded bg-[#fff6d8] border border-gray-300"
              placeholder="Nombre de la rifa"
            />

            <div className="w-full h-56 bg-gray-100 rounded-lg flex items-center justify-center mb-4 overflow-hidden">
              {imagen ? (
                <img
                  src={imagen}
                  alt="Preview"
                  className="object-cover w-full h-full"
                />
              ) : (
                <label className="cursor-pointer text-gray-400">
                  <span className="text-2xl">📷</span>
                  <input
                    type="file"
                    onChange={handleImageChange}
                    className="hidden"
                  />
                </label>
              )}
            </div>

            <label className="block mb-2 text-sm font-bold text-gray-700">
              Costo
            </label>
            <input
              type="number"
              value={costo}
              onChange={(e) => setCosto(e.target.value)}
              className="w-full mb-6 px-4 py-2 rounded bg-[#fff6d8] border border-gray-300"
              placeholder="$"
            />

            <div className="flex justify-between gap-4">
              <button className="flex-1 py-2 bg-teal-500 text-white font-semibold rounded hover:bg-teal-600">
                Cancelar
              </button>
              <button className="flex-1 py-2 bg-orange-500 text-white font-semibold rounded hover:bg-orange-600">
                Comprar ticket
              </button>
            </div>
          </div>
        </div>

        {/* DERECHA: TÍTULO + DESCRIPCIÓN */}
        <div className="flex flex-col w-full max-w-2xl">
          {/* Título "Crear rifa:" */}
          <h2 className="text-3xl font-bold mb-4 text-gray-800">Crear rifa:</h2>

          {/* Paralelepípedo amarillo */}
          <div className="relative transform skew-y-3 bg-yellow-400 p-6 rounded-3xl shadow-md w-full">
            <div className="transform -skew-y-3">
              <label className="block mb-2 text-sm font-bold text-gray-900">
                Descripción
              </label>
              <textarea
                value={descripcion}
                onChange={(e) => setDescripcion(e.target.value)}
                rows="15"
                className="w-full p-4 rounded-lg bg-[#fff3d0] text-gray-800 border border-gray-300"
                placeholder="Describe tu rifa aquí..."
              />
            </div>
          </div>
        </div>
      </section>
      <Footer />
    </>
  );
};

export default RifaPeluditos;
