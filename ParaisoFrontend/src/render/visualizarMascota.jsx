import React from "react";
import elnego from "../assets/elNegro.png";

function VSMascota() {
  return (
    <div className="bg-[#F9EFE0] min-h-screen flex flex-col items-center ">
      <header className="flex justify-between items-center w-full px-8 py-4">
        <h1 className="text-3xl font-bold">Paraiso SV</h1>
        <nav className="space-x-6">
          <a href="#" className="text-lg">
            Productos
          </a>
          <a href="#" className="text-lg">
            Proyectos
          </a>
          <a href="#" className="text-lg">
            Madi-Care
          </a>
          <a href="#" className="text-lg">
            Blog
          </a>
        </nav>
        <span className="text-lg font-bold">JUANCHO</span>
      </header>

      <main className="flex items-center justify-center w-full px-8 py-12">
        <div className="flex bg-white rounded-lg shadow-lg max-w-4xl w-full p-8">
          <div className="flex-shrink-0 w-40 h-40 rounded-full overflow-hidden border-4 border-yellow-400 flex items-center justify-center bg-[#FFD700]">
            <img
              src={elnego}
              alt="El Negro"
              className="w-full h-full object-cover"
            />
          </div>
          <div className="ml-6 flex flex-col justify-between">
            <h2 className="text-3xl font-bold">El Negro</h2>
            <p className="text-lg font-semibold">
              Edad: <span>1 año</span>
            </p>
            <p className="mt-4 text-gray-700">
              Max es un peludito con una personalidad única:
            </p>
            <ul className="list-disc pl-5 text-gray-600">
              <li>
                Carinoso: Le encanta acurrucarse contigo mientras ves tu serie
                favorita.
              </li>
              <li>
                Jugueón: Aunque es pequeño, tiene energía para jugar con su
                pelota y seguirte a donde vayas.
              </li>
              <li>
                Tranquilo: Después de un rato de diversión, disfruta de largas
                siestas en su camita.
              </li>
            </ul>
            <p className="mt-4 text-gray-700">
              Max ya está vacunado, desparacitado y esterilizado, listo para ser
              parte de tu familia. Solo necesita una familia responsable que le
              dé todo el amor y atención que merece.
            </p>
            <button className="mt-6 bg-orange-500 text-white font-bold py-2 px-4 rounded hover:bg-orange-600">
              Adoptar
            </button>
          </div>
        </div>
      </main>
    </div>
  );
}

export default VSMascota;
