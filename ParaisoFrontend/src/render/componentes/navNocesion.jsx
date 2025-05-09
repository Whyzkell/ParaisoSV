import { BrowserRouter as Router, Route, Routes, Link } from "react-router-dom";
import { useState } from "react";

export default function Navno() {
  return (
    <header className="relative bg-[#f5f1e7] text-center py-8 px-8">
      <nav className="flex justify-between items-center w-10/12 mx-auto">
        <Link to="/">
          <h1 className="text-3xl font-bold text-orange-500">Paraiso SV</h1>
        </Link>
        <ul className="flex gap-6 text-lg">
          <li>
            <Link to="/visualizar-proyecto">
              <a href="/visualizar-proyecto" className="hover:text-orange-500">
                Proyectos
              </a>
            </Link>
          </li>
          <li>
            <Link to="/adoptar-mascota">
              <a href="#m" className="hover:text-orange-500">
                Adoptar
              </a>
            </Link>
          </li>
          <li>
            <Link to="/ventaproducto">
              <a href="#blog" className="hover:text-orange-500">
                Comprar producto
              </a>
            </Link>
          </li>
          <li>
            <Link to="/donaciones">
              <a href="#blog" className="hover:text-orange-500">
                Donaciones
              </a>
            </Link>
          </li>
          <li>
            <Link to="/crear-alcancia">
              <a href="#blog" className="hover:text-orange-500">
                Crear Alcancia
              </a>
            </Link>
          </li>
          <li>
            <Link to="/donaciones">
              <a href="#blog" className="hover:text-orange-500">
                Donaciones
              </a>
            </Link>
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
  );
}
