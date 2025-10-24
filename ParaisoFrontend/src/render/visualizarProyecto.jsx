import React from "react";
import { BrowserRouter as Router, Route, Routes, Link } from "react-router-dom";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import pcancer from "../assets/perroCancer.jpg";
import pc2 from "../assets/pCancer2.png";
import pc3 from "../assets/pc3.png";
import pc4 from "../assets/pc4.png";
import Navno from "./componentes/navNocesion";
import Footer from "./componentes/footer";

export default function VisuProyecto() {
  return (
    <div className="bg-gray-100 min-h-screen">
      <Navno />

      <section>
        <img src={pcancer} alt="Dog" className="w-full h96 object-cover " />
      </section>

      <section className="text-center py-10 px-4">
        <h2 className="text-3xl font-bold mb-8">Lucha Contra El Cáncer</h2>
        <p className="text-gray-500 text-xl mt-2 mb-8">22 May, 2024</p>
        <p className="text-gray-600 text-xl mt-4 max-w-2xl mx-auto">
          Los perritos de las calles también suelen y es común enfermarse de
          cáncer, como los tumores de stickers el cual es un tumor cancerígeno,
          su único tratamiento es con quimioterapias y se contrae mediante las
          relaciones sexuales entre perros contagiados. Al no tratarse a tiempo
          termina regándose por todo el cuerpo y se convierte en cáncer de piel.
        </p>
      </section>

      <section className="bg-[#EBE3CC] p-10">
        <div className="max-w-6xl mx-auto space-y-10">
          <div>
            <img src={pc2} alt="Dog" className="w-full rounded-lg mb-6" />
            <h3 className="font-bold text-lg mb-2">Orígenes</h3>
            <p className="text-gray-600">
              Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do
              eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut
              enim ad minim veniam, quis nostrud exercitation ullamco laboris
              nisi ut aliquip ex ea commodo consequat.
            </p>
          </div>

          <div>
            <h3 className="font-bold text-lg mb-2">Progreso</h3>
            <p className="text-gray-600 mb-6">
              Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do
              eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut
              enim ad minim veniam, quis nostrud exercitation ullamco laboris
              nisi ut aliquip ex ea commodo consequat.
            </p>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              <img src={pc3} alt="Dog" className="w-full rounded-lg" />
              <img src={pc4} alt="Dog" className="w-full rounded-lg" />
            </div>
          </div>

          <div>
            <h3 className="font-bold text-lg mb-2">
              Lugares Donde Se Ha Ayudado
            </h3>
            <p className="text-gray-600">
              Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do
              eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut
              enim ad minim veniam, quis nostrud exercitation ullamco laboris
              nisi ut aliquip ex ea commodo consequat.
            </p>
          </div>
        </div>
      </section>
      <Footer />
    </div>
  );
}
