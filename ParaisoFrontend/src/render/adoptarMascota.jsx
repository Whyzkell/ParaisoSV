import React from "react";

const pets = [
  {
    name: "Kaiser",
    breed: "Pastor aleman",
    age: "2 años",
    image: "/kaiser.png",
  },
  { name: "Firulais", breed: "Beagle", age: "1 año", image: "/firulais.png" },
  { name: "Pepe", breed: "Beagle", age: "1 año", image: "/pepe.png" },
];

export default function AdoptarMascota() {
  return (
    <div className="bg-yellow-50 min-h-screen">
      <header className="flex justify-between items-center p-6">
        <h1 className="font-bold text-lg">Paraiso SV</h1>
        <nav className="flex space-x-6">
          <a href="#" className="text-blue-600 font-semibold">
            Productos
          </a>
          <a href="#" className="font-semibold">
            Proyectos
          </a>
          <a href="#" className="font-semibold">
            Madi-Care
          </a>
          <a href="#" className="font-semibold">
            Blog
          </a>
        </nav>
      </header>

      <section className="text-center px-6">
        <h2 className="text-teal-600 font-bold text-2xl">LADOPTA A TU</h2>
        <h3 className="text-green-400 font-bold text-xl">NUEVO AMIGO</h3>
        <p className="text-gray-700 mt-4 max-w-2xl mx-auto">
          Todos merecemos una segunda oportunidad, los perritos y gatitos ya
          sean cachorros, jóvenes, adultos o ancianos, también lo merecen. Por
          eso creamos campañas masivas de adopción para que ellos tengan la
          oportunidad de ir a un buen hogar.
        </p>
        <div className="flex justify-center mt-6">
          <img src="/pets-banner.png" alt="Pets" className="w-96" />
        </div>
      </section>

      <section className="mt-10 bg-gradient-to-b from-yellow-400 via-yellow-400 to-red-500 p-10">
        <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-6">
          {Array.from({ length: 7 }).map((_, index) =>
            pets.map((pet, i) => (
              <div
                key={index + "-" + i}
                className="bg-white rounded-lg shadow-md p-4 text-center"
              >
                <img
                  src={pet.image}
                  alt={pet.name}
                  className="w-32 h-32 object-cover mx-auto mb-4"
                />
                <h4 className="text-lg font-bold">{pet.name}</h4>
                <p className="text-gray-500">{pet.breed}</p>
                <p className="text-gray-400 text-sm">{pet.age}</p>
                <button className="mt-4 bg-orange-500 text-white px-4 py-2 rounded hover:bg-orange-600">
                  Más Información
                </button>
              </div>
            ))
          )}
        </div>
      </section>
    </div>
  );
}
