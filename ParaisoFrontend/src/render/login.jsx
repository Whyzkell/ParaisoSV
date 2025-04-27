import React from "react";
import gatico from "../assets/gatico.png";

const Login = () => {
  return (
    <div className="flex flex-col lg:flex-row h-screen w-full">
      {/* Lado Izquierdo - Formulario de Login */}
      <div className="flex-1 flex items-center justify-center bg-[#F3EFD2] p-6">
        <div className="max-w-md w-full">
          <h1 className="text-3xl font-bold mb-6">Aporta a la causa</h1>
          <form>
            <div className="mb-4">
              <label className="block text-sm font-medium text-gray-700">
                Usuario o correo electrónico
              </label>
              <input
                type="text"
                className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm p-3 focus:outline-none focus:ring focus:ring-blue-500"
                placeholder="Introduce tu usuario o correo"
              />
            </div>
            <div className="mb-4">
              <label className="block text-sm font-medium text-gray-700">
                Contraseña
              </label>
              <input
                type="password"
                className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm p-3 focus:outline-none focus:ring focus:ring-blue-500"
                placeholder="Introduce tu contraseña"
              />
              <a
                href="#"
                className="text-sm text-blue-500 hover:underline mt-2 block"
              >
                Olvidaste tu contraseña?
              </a>
            </div>
            <div className="flex flex-col gap-4">
              <button
                type="submit"
                className="w-full bg-[#005F73] text-white py-2 rounded-md hover:bg-[#0A9396]"
              >
                Iniciar sesión
              </button>
              <button
                type="button"
                className="w-full bg-[#C75B0C] text-white py-2 rounded-md hover:bg-[#F3722C]"
              >
                Registrarse (si no posees cuenta)
              </button>
            </div>
          </form>
        </div>
      </div>

      {/* Lado Derecho - Imagen y Descripción */}
      <div
        className="hidden lg:flex flex-1 items-center justify-center bg-cover bg-center"
        style={{ backgroundImage: `url(${gatico})` }}
      ></div>
    </div>
  );
};

export default Login;
