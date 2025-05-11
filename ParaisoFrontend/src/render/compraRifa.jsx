import React from "react";
import rifa from "../assets/rifa.jpg";
import { Link } from "react-router-dom";
import NavnoCCli from "./componentes/navCesionCli.jsx";
import Footer from "./componentes/footer.jsx";

const comprarRifa = () => {
  return (
    <>
      {/* HEADER con menú */}
      <div className="bg-[#F5F0DC]">
        <NavnoCCli />
      </div>

      {/* Sección principal de la rifa */}
      <section className="bg-[#F5F0DC] py-16 px-4 flex flex-col lg:flex-row items-center justify-center gap-10">
        {/* Cuadro izquierdo - paralelogramo con imagen */}
        <div className="relative transform -skew-y-2 border border-black rounded-3xl bg-[#F5F0DC] p-6 max-w-sm">
          <div className="transform skew-y-2">
            <h2 className="text-2xl font-bold mb-4 text-gray-800 text-center">
              ¡Gran Rifa para Apoyar a <br /> Nuestros Peluditos!🐾
            </h2>
            <img
              src={rifa}
              alt="Rifa peluditos"
              className="rounded-xl w-full mb-4"
            />
            <p className="text-lg text-gray-800 text-center mb-2 font-semibold">
              Costo por ticket
            </p>
            <p className="text-2xl font-bold text-center text-gray-900 mb-4">
              $5
            </p>
            <button className="w-full py-3 bg-orange-500 text-white rounded-xl font-semibold hover:bg-orange-600">
              Comprar ticket
            </button>
          </div>
        </div>

        {/* Cuadro derecho - paralelogramo amarillo */}
        <div className="relative transform skew-y-2 bg-yellow-400 p-8 rounded-3xl shadow-md max-w-xl text-gray-900">
          <div className="transform -skew-y-2">
            <h3 className="text-lg font-bold mb-4">
              ¡Hola, amigos de los animales! 🐶🐱
            </h3>
            <p className="mb-4">
              Nuestro refugio está organizando una rifa súper especial para
              recaudar fondos y seguir brindando amor y cuidado a nuestros
              peluditos rescatados. ¡Y necesitamos de su apoyo!
            </p>
            <h4 className="font-semibold mb-2">🎁 Premios increíbles:</h4>
            <ul className="list-disc list-inside mb-4 space-y-1">
              <li>
                🥇 Primer lugar: Una canasta sorpresa llena de productos para
                consentir a tu mascota 🐾
              </li>
              <li>
                🥈 Segundo lugar: Un día de spa para tu peludo favorito (baño,
                corte y peinado) ✂️🛁
              </li>
              <li>
                🥉 Tercer lugar: Un set de accesorios personalizados para tu
                mascota 🦴🦮
              </li>
            </ul>
            <p className="mb-4">
              Todo lo recaudado será destinado a mejorar las condiciones del
              refugio y a darles a nuestros peluditos una segunda oportunidad
              para ser felices. 🏡❤️
            </p>
            <p className="font-semibold">
              ¡Corre la voz! Ayúdanos a hacer de esta rifa un éxito y juntos
              podremos marcar la diferencia en la vida de estos pequeños
              guerreros de cuatro patas. 🐾✨
            </p>
          </div>
        </div>
      </section>
      <Footer />
    </>
  );
};

export default comprarRifa;
