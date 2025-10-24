import React from "react";
import Footer from "./componentes/footer"
import NavnoCAdm from "./componentes/navCesionAdm.jsx";   // Admin
import NavnoCesion from "./componentes/navNocesion.jsx";  // Invitado
import NavCesionCli from "./componentes/navCesionCli.jsx"; // Cliente
import useNavKind from "../hooks/useNavKind.js";

const Donaciones = () => {
  const donaciones = Array.from({ length: 12 }, (_, i) => ({
    nombre: "Juan",
    fecha: "25/08/2022",
    alcancia: 89,
    monto: 400,
  }));
  const navKind = useNavKind(); // 'admin' | 'client' | 'guest'
  return (
    <div className="min-h-screen bg-[#e7e1d2]">
      {/* Header */}
      <div className="bg-red-600">
        {/* NAV dinámico por rol */}
        {navKind === "admin" ? <NavnoCAdm /> : navKind === "client" ? <NavCesionCli /> : <NavnoCesion />}
        <header className="bg-red-600 py-6 flex flex-col items-center">
          <h1 className="text-3xl font-bold text-gray-800">Donaciones</h1>
        </header>
      </div>

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
      <Footer />
    </div>
  );
};

export default Donaciones;
