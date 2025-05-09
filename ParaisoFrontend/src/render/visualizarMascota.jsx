import React from "react";
import { BrowserRouter as Router, Route, Routes, Link } from "react-router-dom";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import elnego from "../assets/elNegro.png";

function VSMascota() {
  return (
    <div className="bg-[#F9EFE0] min-h-screen flex flex-col items-center w-full">
      <header className="relative bg-[#f5f1e7] text-center w-full mt-8">
        <nav className="flex justify-between items-center max-w-7xl mx-auto">
          <h1 className="text-3xl font-bold text-orange-500">Paraiso SV</h1>
          <ul className="flex gap-6 text-lg">
            <li>
              <a href="#proyectos" className="hover:text-orange-500">
                Proyectos
              </a>
            </li>
            <li>
              <Link to="/crear-alcancia">
                <a href="#m" className="hover:text-orange-500">
                  Medi-Care
                </a>
              </Link>
            </li>
            <li>
              <a href="#blog" className="hover:text-orange-500">
                Blog
              </a>
            </li>
          </ul>
          <div className="flex gap-4">
            <Link to="/login">
              <button className="px-4 py-2 bg-orange-400 text-white rounded-full hover:bg-orange-500">
                Iniciar Sesión
              </button>
            </Link>
            <Link to="/register">
              <button className="px-4 py-2 border border-orange-400 text-orange-400 rounded-full hover:bg-orange-400 hover:text-white">
                Registrarse
              </button>
            </Link>
          </div>
        </nav>
      </header>

      <main className="flex items-center justify-center  w-full px-8 py-12">
        <div className="w-1/3 flex-shrink-0 mr-16 mt-8 rounded-full overflow-hidden border-4 border-yellow-400 flex items-center justify-center bg-[#FFD700]">
          <img
            src={elnego}
            alt="El Negro"
            className="w-full h-full object-cover"
          />
        </div>
        <div className="flex  rounded-lg shadow-lg max-w-4xl w-2/5 p-8">
          <div className="ml-6 flex flex-col justify-between">
            <h2 className="text-3xl font-bold mb-6">El Negro</h2>
            <p className="text-lg font-semibold">
              Edad: <span>1 año</span>
            </p>
            <p className="mt-4 text-gray-700">
              Max es un peludito con una personalidad única:
            </p>
            <ul className="list-disc pl-5 text-gray-600">
              <li>
                Carinoso: Le encanta acurrucarse contigo mientras ves tu serie
                favorita.
              </li>
              <li>
                Jugueón: Aunque es pequeño, tiene energía para jugar con su
                pelota y seguirte a donde vayas.
              </li>
              <li>
                Tranquilo: Después de un rato de diversión, disfruta de largas
                siestas en su camita.
              </li>
            </ul>
            <p className="mt-8 text-gray-700">
              Max ya está vacunado, desparacitado y esterilizado, listo para ser
              parte de tu familia. Solo necesita una familia responsable que le
              dé todo el amor y atención que merece.
            </p>
            <button className="mt-6 bg-orange-500 text-white font-bold py-2 px-4 rounded hover:bg-orange-600">
              Adoptar
            </button>
          </div>
        </div>
      </main>
    </div>
  );
}

export default VSMascota;
