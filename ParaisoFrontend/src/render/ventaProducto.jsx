import React, { useState } from "react";
import comida from "../assets/comida.png";
import comidaJpeg from "../assets/comida.jpeg";
import NavnoCCli from "./componentes/navCesionCli.jsx";
import Footer from "./componentes/footer.jsx";
import { useNavigate } from "react-router-dom";

const productos = [
  {
    nombre: "FRISKIES WITH CHICKEN",
    precio: "$400.00",
    imagen: comida,
  },
  {
    nombre: "THÉRIE - THE FINESIT SE...",
    precio: "$550.00",
    imagen: comida,
  },
  {
    nombre: "NORTH PAW - GAIN FREE",
    precio: "$140.00",
    imagen: comida,
  },
  {
    nombre: "PEDIGREE - DOG FOOD",
    precio: "$200.00",
    imagen: comida,
  },
  {
    nombre: "FILLET ‘O’ LAKES - KIT CAT",
    precio: "$100.00",
    imagen: comida,
  },
  {
    nombre: "ENCORE - CAT FOOD",
    precio: "$400.00",
    imagen: comida,
  },
  {
    nombre: "ROYAL CANIN - CARE DIGESTIVE",
    precio: "$600.00",
    imagen: comidaJpeg,
  },
  {
    nombre: "WELLNESS - SIGNATURE",
    precio: "$200.00",
    imagen: comida,
  },
];

export default function VentaProducto() {
  const [busqueda, setBusqueda] = useState("");

  const productosFiltrados = productos.filter((producto) =>
    producto.nombre.toLowerCase().includes(busqueda.toLowerCase())
  );

  const navigate = useNavigate();

  const handleClick = () => {
    navigate("/detalleproducto");
  };

  return (
    <div className="bg-[#f5f0dc] min-h-screen">
      <NavnoCCli />

      <section className="p-6">
        <h2 className="text-xl font-semibold mb-4">Productos</h2>
        <input
          type="text"
          value={busqueda}
          onChange={(e) => setBusqueda(e.target.value)}
          placeholder="Buscar productos..."
          className="w-full p-3 rounded-full border border-gray-300 text-black bg-white focus:outline-none focus:ring-2 focus:ring-[#007C8C] placeholder-gray-500"
        />

        <div className="mt-8 grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">
          {productosFiltrados.map((producto, index) => (
            <div
              key={index}
              className="bg-white rounded-xl shadow-md p-4 flex flex-col items-center"
            >
              <img
                src={producto.imagen}
                alt={producto.nombre}
                className="w-32 h-32 object-contain mb-4"
              />
              <h3 className="text-sm font-semibold text-center mb-2">
                {producto.nombre}
              </h3>
              <p className="text-orange-600 font-bold mb-3">
                {producto.precio}
              </p>
              <div className="flex w-full gap-2">
                <button
                  onClick={handleClick}
                  className="flex-1 bg-[#007C8C] text-white text-sm px-3 py-2 rounded-full hover:bg-[#005f6a]"
                >
                  Buy Now
                </button>
                <button className="border border-gray-300 rounded-full p-2 hover:bg-gray-100">
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    className="w-5 h-5"
                    fill="none"
                    viewBox="0 0 24 24"
                    stroke="currentColor"
                  >
                    <path
                      strokeLinecap="round"
                      strokeLinejoin="round"
                      strokeWidth={2}
                      d="M3 3h2l.4 2M7 13h10l4-8H5.4M7 13l-1.6 8h13.2L17 13M7 13L5.4 5M17 13l1.6 8"
                    />
                  </svg>
                </button>
              </div>
            </div>
          ))}
        </div>
      </section>
      <Footer />
    </div>
  );
}
