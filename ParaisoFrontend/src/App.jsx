import { useEffect, useState } from "react";
import { ArrowLeft, ArrowRight } from "lucide-react";
import { Link } from "react-router-dom";
import "./index.css";
import "./App.css";

import perro from "./assets/perroSueter.png";
import vector from "./assets/Vector.png";
import union from "./assets/Union.png";

import NavnoCAdm from "./render/componentes/navCesionAdm.jsx";   // Admin
import NavnoCesion from "./render/componentes/navNocesion.jsx";  // Invitado
import NavCesionCli from "./render/componentes/navCesionCli.jsx"; // Cliente
import Footer from "./render/componentes/footer.jsx";

import useNavKind from "./hooks/useNavKind.js";

const projects = [
  {
    title: "Rutas de alimentación",
    description: "Es nuestro programa principal donde recorremos las carreteras llevando alimentos a los perritos y gatitos que encontramos por las calles...",
    icon: "🍲",
    color: "bg-[#FDC400]",
  },
  {
    title: "Lucha contra el cancer",
    description: "Gracias a este programa hemos logrado rescatar y curar perritos en estado crítico...",
    icon: "🩺",
    color: "bg-[#F5F0E5]",
  },
  {
    title: "Adopciones responsables",
    description: "Buscamos familias amorosas que deseen adoptar con compromiso...",
    icon: "🏡",
    color: "bg-[#FDC400]",
  },
  {
    title: "Jornadas de salud",
    description: "Organizamos jornadas médicas veterinarias gratuitas para comunidades vulnerables...",
    icon: "💉",
    color: "bg-[#F5F0E5]",
  },
  {
    title: "Educación y conciencia",
    description: "Promovemos el respeto animal mediante talleres y campañas educativas...",
    icon: "📚",
    color: "bg-[#FDC400]",
  },
];

export default function App() {
  const navKind = useNavKind(); // 'admin' | 'client' | 'guest'

  const [current, setCurrent] = useState(0);
  const visibleCount = 3;
  const totalSlides = projects.length;

  const nextSlide = () =>
    setCurrent((prev) => (prev + visibleCount >= totalSlides ? 0 : prev + 1));
  const prevSlide = () =>
    setCurrent((prev) => (prev - 1 < 0 ? totalSlides - visibleCount : prev - 1));

  return (
    <div className="text-gray-800 font-sans bg-[#F5F0DC]">
      {/* NAV dinámico por rol */}
      {navKind === "admin" ? <NavnoCAdm /> : navKind === "client" ? <NavCesionCli /> : <NavnoCesion />}

      {/* Hero */}
      <section className="flex flex-col md:flex-row items-center bg-[#F5F0DC] justify-between">
        <div className="flex-1 ml-6 md:ml-40 w-full md:w-2/5">
          <h1 className="text-5xl md:text-8xl font-bold leading-tight">
            EL BIENESTAR <br /> DE LAS <br />
            <span className="text-orange-500">MASCOTAS</span> ES <br />
            LO <span className="text-orange-500">PRIMERO</span>
          </h1>
          <button className="mt-6 px-6 py-3 bg-orange-500 text-white rounded-full">
            Leer más
          </button>
        </div>

        <div
          className="flex-1 w-full md:w-2/5 ml-6"
          style={{
            backgroundImage: `url(${vector})`,
            backgroundRepeat: "no-repeat",
            backgroundSize: "contain",
          }}
        >
          <img src={perro} alt="Dog Hero" className="w-11/12" />
        </div>
      </section>

      {/* Casos */}
      <section className="bg-orange-500 p-8 text-white text-center">
        <h2 className="text-xl">Diferentes casos</h2>
        <p className="text-4xl font-bold my-4">150+</p>
        <div className="flex justify-center gap-6">
          {[...Array(6)].map((_, i) => (
            <img
              key={i}
              src={`/images/stats-dog-${i + 1}.png`}
              alt={`Dog ${i + 1}`}
              className="w-16 h-16 rounded-full object-cover"
            />
          ))}
        </div>
      </section>

      {/* Proyectos */}
      <section className="bg-[#F5F0E5]">
        <h2 className="text-4xl font-bold mb-10 mt-8 ml-8 text-[#06222E]">
          Nuestros Proyectos
        </h2>

        <div className="flex w-11/12 mx-auto">
          <div className="flex gap-6 overflow-hidden mb-4">
            {projects.slice(current, current + visibleCount).map((p, idx) => (
              <div
                key={idx}
                className={`w-full md:w-1/3 mb-8 flex-shrink-0 rounded-3xl p-6 shadow-lg transition-all duration-300 ${p.color}`}
              >
                <div className="text-5xl mb-4">{p.icon}</div>
                <h3 className="text-2xl font-semibold text-[#06222E]">
                  {p.title}
                </h3>
                <p className="text-[#06222E] mt-2 text-sm">{p.description}</p>
                <button className="mt-4 px-5 py-2 bg-[#06222E] text-white rounded-full text-sm hover:bg-[#03141A] transition">
                  Leer más
                </button>
              </div>
            ))}
          </div>
        </div>

        <div className="flex gap-3 justify-end mr-10 mb-6">
          <button
            onClick={prevSlide}
            className="bg-[#06222E] text-white p-3 rounded-full hover:scale-105 transition"
          >
            <ArrowLeft size={20} />
          </button>
          <button
            onClick={nextSlide}
            className="bg-[#06222E] text-white p-3 rounded-full hover:scale-105 transition"
          >
            <ArrowRight size={20} />
          </button>
        </div>
      </section>

      {/* Adopciones */}
      <section className="flex flex-col md:flex-row items-center bg-[#007C8C] text-white p-10">
        <div className="flex w-full md:w-3/5 justify-center">
          <img src={union} alt="Adoption" className="rounded-3xl" />
        </div>
        <div className="flex-1 w-full md:w-2/5 md:mr-8">
          <h2 className="text-4xl font-semibold mb-12">Programa de adopción</h2>
          <p className="text-2xl mb-12">
            Todos merecemos una segunda oportunidad...
          </p>
          <Link to="/adoptar-mascota">
            <button className="mt-4 px-5 py-2 bg-[#06222E] text-white rounded-full text-xl hover:bg-[#03141A] transition">
              Leer más
            </button>
          </Link>
        </div>
      </section>

      <Footer />
    </div>
  );
}
