import { BrowserRouter as Router, Route, Routes, Link } from "react-router-dom";
import { useState } from "react";

export default function NavnoCCli() {
  return (
    <header className="relative  text-center py-8 px-8">
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
            <Link to="/todas-rifas">
              <a href="#blog" className="hover:text-orange-500">
                Rifas
              </a>
            </Link>
          </li>
          <li>
            <Link to="/donaralcancia">
              <a href="#blog" className="hover:text-orange-500">
                Donar alcancia
              </a>
            </Link>
          </li>
        </ul>
        <div className="flex gap-4">
          <button className="  text-orange-400 text-2xl">Juanito</button>
        </div>
      </nav>
    </header>
  );
}
