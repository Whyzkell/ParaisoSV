import React, { useState } from "react";
import comida from "../assets/comida.png";
import NavnoCCli from "./componentes/navCesionCli";

export default function DetalleProducto() {
  const [cantidad, setCantidad] = useState(1);
  const stock = 45;

  return (
    <div className="bg-[#f7f3e7] min-h-screen">
      {/* Encabezado */}
      <div className="bg-[#00848E]">
        <NavnoCCli />
      </div>

      {/* Barra de búsqueda */}
      <div className="bg-[#00848E] px-6 py-4">
        <h2 className="text-white text-lg font-semibold mb-2">Productos</h2>
        <input
          type="text"
          placeholder="Buscar productos..."
          className="w-full p-3 rounded-full border border-gray-300 text-black bg-white focus:outline-none focus:ring-2 focus:ring-[#007C8C] placeholder-gray-500"
        />
      </div>

      {/* Contenido principal */}
      <main className="p-6 max-w-6xl mx-auto">
        <div className="bg-[#fdf7ea] rounded-2xl p-8 grid md:grid-cols-2 gap-8 shadow-md">
          <div className="flex justify-center">
            <img
              src={comida}
              alt="Producto"
              className="max-h-80 object-contain"
            />
          </div>

          <div>
            <h3 className="text-lg font-bold uppercase mb-2">
              FILLET ‘O’ LAKES - KIT CAT GREEN || 50G
            </h3>
            <p className="text-orange-600 text-2xl font-bold mb-4">$100.00</p>

            <div className="mb-4">
              <p className="font-semibold mb-1">Cantidad</p>
              <div className="flex gap-1 flex-wrap">
                {[...Array(9).keys()].map((i) => (
                  <button
                    key={i}
                    onClick={() => setCantidad(i + 1)}
                    className={`w-8 h-8 border rounded ${
                      cantidad === i + 1
                        ? "bg-[#00848E] text-white"
                        : "bg-white"
                    }`}
                  >
                    {i + 1}
                  </button>
                ))}
              </div>
              <p className="text-sm mt-2">{stock} en existencia</p>
            </div>

            <div className="flex gap-2">
              <button className="bg-[#00848E] text-white px-6 py-2 rounded hover:bg-[#006e76]">
                Buy now
              </button>
              <button className="border border-gray-300 px-3 py-2 rounded hover:bg-gray-100">
                ❤️
              </button>
              <button className="border border-gray-300 px-3 py-2 rounded hover:bg-gray-100">
                🛒
              </button>
            </div>
          </div>
        </div>

        {/* Panel inferior */}
        <div className="bg-[#fdf7ea] mt-8 p-6 rounded-2xl shadow-sm">
          <h4 className="font-semibold mb-4">Información</h4>
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4 text-sm">
            <p>
              <strong>Categoría:</strong> Comida de gato
            </p>
            <p>
              <strong>Existencias:</strong> 45
            </p>
            <p>
              <strong>Marca:</strong> Kit cat
            </p>
          </div>
          <div className="mt-4 text-sm leading-relaxed text-gray-700">
            <h5 className="font-semibold mb-1">Descripción del producto</h5>
            <p>
              Kit Cat Fillet ‘O’ Flakes es un alimento completo y de primera
              calidad especialmente elaborado para gatos adultos de más de 1
              año. Esta fórmula combina un sabroso sabor con copos de pescado
              deshidratado de primera calidad, garantizando una comida deliciosa
              y nutritiva para su amigo felino. Enriquecido con Omega-3,
              Omega-6, taurina, vitaminas prebióticas y sin cerdo ni manteca de
              cerdo, favorece la salud y el bienestar general de su gato. Cada
              envase está diseñado para mantener la frescura con cómodas bolsas
              de 1 kg.
            </p>
          </div>
        </div>
      </main>
    </div>
  );
}
