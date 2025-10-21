import { BrowserRouter as Router, Route, Routes, Link } from "react-router-dom";
import { useState } from "react";
import { ArrowLeft, ArrowRight } from "lucide-react";
import Login from "./render/login.jsx";
import VSMascota from "./render/visualizarMascota.jsx";
import AdoptarMascota from "./render/adoptarMascota.jsx";
import VisuProyecto from "./render/visualizarProyecto.jsx";
import Donaciones from "./render/verdonaciones.jsx";
import CrearAlcancia from "./render/crearAlcancia.jsx";
import Register from "./render/register.jsx"; // Asegúrate de que este archivo exista
import VentaProducto from "./render/ventaProducto.jsx";
import DetalleProducto from "./render/detalleProducto.jsx";
import ComprarRifa from "./render/compraRifa.jsx";
import CrearRifa from "./render/crearRifa.jsx";
import AgregarMascota from "./render/agregarMascosta.jsx";
import AgregarProducto from "./render/agregarProducto.jsx";
import ActualizarActividad from "./render/actualizarActividades.jsx";
import DonarAlcancia from "./render/donarAlcancia.jsx";
import TodasRifas from "./render/ToRifas.jsx";
import EnviarDonacion from "./render/enviarDonacion.jsx";

import perro from "./assets/perroSueter.png";
import vector from "./assets/Vector.png";
import union from "./assets/Union.png";
import sanivet from "./assets/sanivet.png";
import { Facebook, Instagram, Twitter } from "lucide-react";
import Navno from "./render/componentes/navNocesion.jsx";
import NavnoCCli from "./render/componentes/navCesionCli.jsx";
import NavnoCAdm from "./render/componentes/navCesionAdm.jsx";
import Footer from "./render/componentes/footer.jsx";

const projects = [
  {
    title: "Rutas de alimentación",
    description:
      "Es nuestro programa principal donde recorremos las carreteras llevando alimentos a los perritos y gatitos que encontramos por las calles y seguimos la rutina día a día, alimentamos a más de 160 perritos, invirtiendo más de 60lb al día.",
    icon: "🍲",
    color: "bg-[#FDC400]",
  },
  {
    title: "Lucha contra el cancer",
    description:
      "Gracias a este programa hemos logrado rescatar y curar perritos en estado crítico, con infecciones graves y seguimos ayudando a más perritos a que también tengan la oportunidad de salvarse.",
    icon: "🩺",
    color: "bg-[#F5F0E5]",
  },
  {
    title: "Adopciones responsables",
    description:
      "Buscamos familias amorosas que deseen adoptar con compromiso. Todos nuestros perritos están vacunados y esterilizados antes de entregarse.",
    icon: "🏡",
    color: "bg-[#FDC400]",
  },
  {
    title: "Jornadas de salud",
    description:
      "Organizamos jornadas médicas veterinarias gratuitas para comunidades vulnerables, ayudando a cuidar la salud de sus mascotas.",
    icon: "💉",
    color: "bg-[#F5F0E5]",
  },
  {
    title: "Educación y conciencia",
    description:
      "Promovemos el respeto animal mediante talleres y campañas educativas en escuelas y comunidades.",
    icon: "📚",
    color: "bg-[#FDC400]",
  },
];

const sponsors = [
  {
    name: "Sanivet",
    image: sanivet, // usa tu imagen local o URL
    description:
      "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua...",
  },
  {
    name: "Veterinaria PetCare",
    image: perro,
    description:
      "Gracias a su apoyo, hemos podido brindar atención médica a decenas de perritos en situación vulnerable.",
  },
  {
    name: "AgroVet",
    image: perro,
    description:
      "AgroVet dona medicina veterinaria para nuestras jornadas de salud en comunidades rurales.",
  },
];

function App() {
  const [current, setCurrent] = useState(0);

  const visibleCount = 3;
  const totalSlides = projects.length;

  const nextSlide = () => {
    setCurrent((prev) => (prev + visibleCount >= totalSlides ? 0 : prev + 1));
  };

  const prevSlide = () => {
    setCurrent((prev) =>
      prev - 1 < 0 ? totalSlides - visibleCount : prev - 1
    );
  };

  const [currentIndex, setCurrentIndex] = useState(0);

  const next = () => {
    setCurrentIndex((prev) => (prev === sponsors.length - 1 ? 0 : prev + 1));
  };

  const prev = () => {
    setCurrentIndex((prev) => (prev === 0 ? sponsors.length - 1 : prev - 1));
  };

  return (
    <Router>
      <Routes>
        <Route
          path="/"
          element={
            <div className="bg-beige text-gray-800 font-sans">
              {/* Hero Section */}
              <NavnoCAdm />
              <section className="flex flex-col md:flex-row items-center bg-[#F5F0DC] justify-between">
                <div className="flex-1 ml-40 w-2/5">
                  <h1 className="text-8xl font-bold leading-tight">
                    EL BIENESTAR <br /> DE LAS <br />
                    <span className="text-orange-500">MASCOTAS</span> ES <br />{" "}
                    LO <span className="text-orange-500">PRIMERO</span>
                  </h1>
                  <button className="mt-6 px-6 py-3 bg-orange-500 text-white rounded-full">
                    Leer más
                  </button>
                </div>
                <div className="flex-1 w-2/5 bg-[url(./assets/Vector.png)] ml-25 ">
                  <img src={perro} alt="Dog Hero" className="w-11/12 " />
                </div>
              </section>

              {/* Casos */}
              <section className="bg-orange-500 p-8 text-white text-center ">
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

              <section className="bg-[#F5F0E5] flex-row">
                <div className="flex ">
                  <h2 className="text-4xl font-bold mb-10 mt-8 ml-8 text-[#06222E] text-center">
                    Nuestros Proyectos
                  </h2>
                </div>

                <div className="flex w-11/12 mx-auto">
                  <div className="flex gap-6 overflow-hidden mb-4">
                    {projects
                      .slice(current, current + visibleCount)
                      .map((project, index) => (
                        <div
                          key={index}
                          className={`w-full md:w-1/3 mb-8 flex-shrink-0 rounded-3xl p-6 shadow-lg transition-all duration-300 ${project.color}`}
                        >
                          <div className="text-5xl mb-4">{project.icon}</div>
                          <h3 className="text-2xl font-semibold text-[#06222E]">
                            {project.title}
                          </h3>
                          <p className="text-[#06222E] mt-2 text-sm">
                            {project.description}
                          </p>
                          <button className="mt-4 px-5 py-2 bg-[#06222E] text-white rounded-full text-sm hover:bg-[#03141A] transition">
                            Leer más
                          </button>
                        </div>
                      ))}
                  </div>

                  {/* Controles */}
                </div>
                <div className=" flex gap-3 justify-end ">
                  <div className="flex gap-3 mr-10 mb-6">
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
                </div>
              </section>

              <section className="flex flex-col md:flex-row items-center bg-[#007C8C] text-white p-10">
                <div className="flex w-3/5 justify-center">
                  <img src={union} alt="Adoption" className="rounded-3xl" />
                </div>
                <div className="flex-1 w-2/5 mr-8">
                  <h2 className="text-4xl font-semibold mb-12">
                    Programa de adopción
                  </h2>
                  <p className="text-2xl mb-12">
                    Todos merecemos una segunda oportunidad, los perritos y
                    gatitos ya sean cachorros, jóvenes, adultos o ancianos,
                    también lo merecen. Por eso Creamos campañas masivas de
                    adopción para que ellos tengan la oportunidad de ir a un
                    buen hogar. Ya hemos dado en adopción muchos animalitos a
                    hogares que cumplan los requisitos y ahora viven muy felices
                    luego de pasar sufrimiento y seguimos con esa misión día a
                    día
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
          }
        />

        <Route path="/login" element={<Login />} />
        <Route path="/visualizar-mascota" element={<VSMascota />} />
        <Route path="/adoptar-mascota" element={<AdoptarMascota />} />
        <Route path="/visualizar-proyecto" element={<VisuProyecto />} />
        <Route path="/donaciones" element={<Donaciones />} />
        <Route path="/crear-alcancia" element={<CrearAlcancia />} />
        <Route path="/register" element={<Register />} />
        <Route path="/ventaproducto" element={<VentaProducto />} />
        <Route path="/detalleproducto" element={<DetalleProducto />} />
        <Route path="/comprarrifa" element={<ComprarRifa />} />
        <Route path="/crearrifa" element={<CrearRifa />} />
        <Route path="/agregarmascota" element={<AgregarMascota />} />
        <Route path="/agregarproducto" element={<AgregarProducto />} />
        <Route path="/actualizaractividad" element={<ActualizarActividad />} />
        <Route path="/donaralcancia" element={<DonarAlcancia />} />
        <Route path="/todas-rifas" element={<TodasRifas />} />
        <Route path="/enviadonacion" element={<EnviarDonacion />} />

      </Routes>
    </Router>
  );
}

export default App;
