import { BrowserRouter as Router, Route, Routes, Link } from "react-router-dom";
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

function App() {
  return (
    <Router>
      <Routes>
        <Route
          path="/"
          element={
            <div className="bg-beige text-gray-800 font-sans">
              {/* Hero Section */}
              <header className="relative bg-[#f5f1e7] text-center py-16 px-8">
                <nav className="flex justify-between items-center max-w-7xl mx-auto">
                  <h1 className="text-3xl font-bold text-orange-500">Paraiso SV</h1>
                  <ul className="flex gap-6 text-lg">
                    <li><a href="#proyectos" className="hover:text-orange-500">Proyectos</a></li>
                    <li><a href="#medicare" className="hover:text-orange-500">Medi-Care</a></li>
                    <li><a href="#blog" className="hover:text-orange-500">Blog</a></li>
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

              <section className="flex flex-col md:flex-row items-center bg-[#F5F0DC] p-10">
                <div className="flex-1">
                  <h1 className="text-5xl font-bold leading-tight">
                    EL BIENESTAR <br /> DE LAS <br />
                    <span className="text-orange-500">MASCOTAS</span> ES <br /> LO{" "}
                    <span className="text-orange-500">PRIMERO</span>
                  </h1>
                  <button className="mt-6 px-6 py-3 bg-orange-500 text-white rounded-full">
                    Leer más
                  </button>
                </div>
                <div className="flex-1">
                  <img src="/images/dog-hero.png" alt="Dog Hero" className="w-full" />
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
              <section id="proyectos" className="bg-[#f5f1e7] py-16">
                <div className="max-w-7xl mx-auto px-4">
                  <h3 className="text-2xl font-bold text-center">Nuestros Proyectos</h3>
                  <div className="mt-8 grid grid-cols-1 md:grid-cols-3 gap-8">
                    <div className="bg-yellow-100 p-6 rounded-lg shadow-md">
                      <h4 className="text-lg font-bold">Rutas de alimentación</h4>
                      <p className="mt-4">Programa para alimentar a perritos en situación de calle.</p>
                      <button className="mt-4 px-4 py-2 bg-yellow-300 text-gray-800 rounded-full hover:bg-yellow-400">
                        Leer más
                      </button>
                    </div>
                    <div className="bg-white p-6 rounded-lg shadow-md">
                      <h4 className="text-lg font-bold">Lucha contra el cáncer</h4>
                      <p className="mt-4">Tratamientos para perritos en estado crítico.</p>
                      <button className="mt-4 px-4 py-2 bg-gray-200 text-gray-800 rounded-full hover:bg-gray-300">
                        Leer más
                      </button>
                    </div>
                    <div className="bg-white p-6 rounded-lg shadow-md">
                      <h4 className="text-lg font-bold">Esterilización</h4>
                      <p className="mt-4">Programas para esterilizar a más de 100 mascotas.</p>
                      <button className="mt-4 px-4 py-2 bg-gray-200 text-gray-800 rounded-full hover:bg-gray-300">
                        Leer más
                      </button>
                    </div>
                  </div>
                </div>
              </section>

              <section className="flex flex-col md:flex-row items-center bg-[#007C8C] text-white p-10">
                <div className="flex-1">
                  <img src="/images/adoption-program.png" alt="Adoption" className="rounded-3xl" />
                </div>
                <div className="flex-1 p-6">
                  <h2 className="text-3xl font-semibold mb-4">Programa de adopción</h2>
                  <p className="text-sm mb-6">Todos merecen una segunda oportunidad. ¡Adopta!</p>
                </div>
              </section>

              <section className="p-10 bg-[#F5F0DC]">
                <h2 className="text-3xl font-semibold mb-8">Patrocinadores</h2>
                <div className="grid md:grid-cols-2 gap-6">
                  <div className="bg-white p-6 rounded-2xl">
                    <img src="/images/sanivet.png" alt="Sanivet" className="mx-auto mb-4" />
                  </div>
                </div>
              </section>
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
        <Route path="/detalleproducto" element={<DetalleProducto/>} />
        <Route path="/comprarrifa" element={<ComprarRifa/>} />
        <Route path="/crearrifa" element={<CrearRifa/>} />
      </Routes>
    </Router>
  );
}

export default App;
