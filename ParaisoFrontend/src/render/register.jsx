import React from "react";
import gatico from "../assets/gatico.png";
import { ArrowLeft, ArrowRight } from "lucide-react"; // Asegúrate de tener instalado lucide-react

const Register = () => {
  return (
    <div className="flex flex-col lg:flex-row h-screen w-full font-sans">
      {/* Lado Izquierdo - Formulario */}
      <div className="flex-1 flex flex-col justify-center bg-[#F3EFD2] px-10 py-12 rounded-l-3xl">
        <h1 className="text-4xl font-bold mb-10 text-gray-900">Se uno más</h1>
        <form className="space-y-6">
          <div>
            <label className="block text-sm font-medium mb-1">Correo electrónico</label>
            <input
              type="email"
              className="w-full px-4 py-3 bg-[#E7E0C9] rounded-md outline-none"
            />
          </div>
          <div>
            <label className="block text-sm font-medium mb-1">Nombre</label>
            <input
              type="text"
              className="w-full px-4 py-3 bg-[#E7E0C9] rounded-md outline-none"
            />
          </div>
          <div>
            <label className="block text-sm font-medium mb-1">Contraseña</label>
            <input
              type="password"
              className="w-full px-4 py-3 bg-[#E7E0C9] rounded-md outline-none"
            />
          </div>
          <div>
            <label className="block text-sm font-medium mb-1">Ingrese otra vez la contraseña</label>
            <input
              type="password"
              className="w-full px-4 py-3 bg-[#E7E0C9] rounded-md outline-none"
            />
          </div>
          <button
            type="submit"
            className="w-full py-3 bg-[#EF5B00] text-white font-semibold rounded-md"
          >
            Registrarse
          </button>
        </form>
      </div>

      {/* Lado Derecho - Imagen y descripción del gato */}
      <div className="flex-1 relative hidden lg:block">
        <img src={gatico} alt="Gato" className="object-cover w-full h-full" />
        <div className="absolute bottom-8 left-20 bg-black/40 text-white p-6 rounded-xl max-w-xl w-full">

          <h2 className="text-2xl font-semibold mb-2">Misifu</h2>
          <p className="text-sm">
            “Misifu es un gatito muy juguetón y energético. Cada mañana, al despertar, corretea por toda la casa, jugando con cualquier cosa que encuentre a su paso”
          </p>
          <div className="flex gap-4 mt-4">
            <button className="p-2 bg-white/20 rounded-full hover:bg-white/30">
              <ArrowLeft size={20} />
            </button>
            <button className="p-2 bg-white/20 rounded-full hover:bg-white/30">
              <ArrowRight size={20} />
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Register;
