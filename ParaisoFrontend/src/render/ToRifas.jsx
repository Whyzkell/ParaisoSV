import React from "react";
import { Facebook, Instagram, Twitter } from "lucide-react";
import { BrowserRouter as Router, Route, Routes, Link } from "react-router-dom";
import { useState } from "react";
import chuchitos from "../assets/chuchitos.png";
import hosh from "../assets/hosh.png";
import { useNavigate } from "react-router-dom";import NavnoCAdm from "./componentes/navCesionAdm.jsx";   // Admin
import NavnoCesion from "./componentes/navNocesion.jsx";  // Invitado
import NavCesionCli from "./componentes/navCesionCli.jsx"; // Cliente
import useNavKind from "../hooks/useNavKind.js";
import Footer from "./componentes/footer.jsx";

const rifas = [
  {
    name: "Rifa de Gorra",
    descri: "Estamos rifando gorras",
    costo: "$5",
    image: hosh,
  },
];

export default function TodasRifas() {
  const navigate = useNavigate();

  const handleClick = () => {
    navigate("/comprarrifa");
  };
  const navKind = useNavKind(); // 'admin' | 'client' | 'guest'
  return (
    <div className="bg-yellow-50 min-h-screen">
      {/* NAV dinámico por rol */}
      {navKind === "admin" ? <NavnoCAdm /> : navKind === "client" ? <NavCesionCli /> : <NavnoCesion />}

      <section className="flex text-center px-6 w-full">
        <div className="flex-row justify-items-start justify-start ml-8 w-2/5 mt-8">
          <h2 className="text-teal-600 font-bold text-6xl ">Apoya a la</h2>
          <h3 className="text-green-400 font-bold text-6xl  mt-4">causa</h3>
          <p className="text-gray-600 text-3xl mt-4  text-left w-10/12">
            Lorem ipsum dolor sit, amet consectetur adipisicing elit. Soluta
            fuga maiores incidunt, facilis possimus dicta, doloribus cupiditate
            eos commodi est amet, provident aut. Nihil, deserunt maiores. Ea,
            cum expedita. Ullam!
          </p>
        </div>
        <div className="flex justify-center mt-6  w-3/5">
          <img src={chuchitos} alt="Pets" className="w-8/12" />
        </div>
      </section>

      <section className="bg-gradient-to-b from-yellow-400 via-yellow-400 to-red-500 -mt-24 flex justify-center">
        <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-6 w-11/12 mt-24">
          {Array.from({ length: 7 }).map((_, index) =>
            rifas.map((rifas, i) => (
              <div
                key={index + "-" + i}
                className="bg-white rounded-lg shadow-md p-4 text-center mt-8 flex "
              >
                <div className="flex-row w-3/5 justify-items-start">
                  <h4 className="text-3xl font-bold ml-5 mt-4">{rifas.name}</h4>
                  <p className="text-gray-500 ml-5 text-2xl mt-3">
                    {rifas.descri}
                  </p>
                  <p className="text-gray-400 text-xl ml-5 mt-3">
                    {rifas.costo}
                  </p>
                  <button
                    onClick={handleClick}
                    className="mt-16 bg-orange-500 text-white px-4 py-2 rounded hover:bg-orange-600 ml-5"
                  >
                    Comprar Ticket
                  </button>
                </div>
                <div className="w-2/5">
                  <img
                    src={rifas.image}
                    alt={rifas.name}
                    className="w-10/12 object-cover "
                  />
                </div>
              </div>
            ))
          )}
        </div>
      </section>
      <Footer />
    </div>
  );
}
