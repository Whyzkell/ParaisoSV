import { BrowserRouter as Router, Route, Routes, Link } from "react-router-dom";
import { useState } from "react";

export default function NavnoCAdm() {
  const [isAdminOpen, setIsAdminOpen] = useState(false);

  return (
    <header className="relative  text-center py-8 px-8">
      <nav className="flex justify-between items-center w-10/12 mx-auto">
        <Link to="/">
          <h1 className="text-3xl font-bold text-orange-500">Paraiso SV</h1>
        </Link>
        <ul className="flex gap-6 text-lg items-center">
          <li>
            <Link to="/visualizar-proyecto" className="hover:text-orange-500">
              Proyectos
            </Link>
          </li>
          <li>
            <Link to="/adoptar-mascota" className="hover:text-orange-500">
              Adoptar
            </Link>
          </li>
          <li>
            <Link to="/ventaproducto" className="hover:text-orange-500">
              Comprar producto
            </Link>
          </li>
          <li>
            <Link to="/comprarrifa" className="hover:text-orange-500">
              Comprar rifa
            </Link>
          </li>
          <li>
            <Link to="/donaralcancia" className="hover:text-orange-500">
              Donar alcancia
            </Link>
          </li>
          <li className="relative">
            <button
              onClick={() => setIsAdminOpen(!isAdminOpen)}
              className="hover:text-orange-500 focus:outline-none"
            >
              Funciones admin ▾
            </button>
            {isAdminOpen && (
              <ul className="absolute right-0 mt-2 w-48 bg-white border border-gray-300 rounded shadow-lg z-10 text-left">
                <li className="px-4 py-2 hover:bg-orange-100">
                  <Link to="/crear-alcancia">Crear alcancía</Link>
                </li>
                <li className="px-4 py-2 hover:bg-orange-100">
                  <Link to="/crearrifa">Crear rifa</Link>
                </li>
                <li className="px-4 py-2 hover:bg-orange-100">
                  <Link to="/agregarmascota">Agregar mascota</Link>
                </li>
                <li className="px-4 py-2 hover:bg-orange-100">
                  <Link to="/agregarproducto">Agregar producto</Link>
                </li>
                <li className="px-4 py-2 hover:bg-orange-100">
                  <Link to="/actualizaractividad">Actualizar actividad</Link>
                </li>
              </ul>
            )}
          </li>
        </ul>
        <div className="flex gap-4">
          <button className="text-orange-400 text-2xl">Juanito</button>
        </div>
      </nav>
    </header>
  );
}
