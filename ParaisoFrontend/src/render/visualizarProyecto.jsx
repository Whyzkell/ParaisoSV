import React from "react";

export default function VisuProyecto() {
  return (
    <div className="bg-gray-100 min-h-screen">
      <header className="flex justify-between items-center p-6">
        <h1 className="font-bold text-lg">Paraiso sv</h1>
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
          <button className="bg-white border border-blue-600 text-blue-600 rounded px-4 py-2 font-semibold">
            Iniciar Sesión
          </button>
          <button className="bg-transparent border border-orange-400 text-orange-400 rounded px-4 py-2 font-semibold">
            Registrarse
          </button>
        </nav>
      </header>

      <section>
        <img
          src="/main-dog.png"
          alt="Dog"
          className="w-full object-cover h-96"
        />
      </section>

      <section className="text-center py-10 px-4">
        <h2 className="text-xl font-bold">Lucha Contra El Cáncer</h2>
        <p className="text-gray-500 text-sm mt-2">22 May, 2024</p>
        <p className="text-gray-600 mt-4 max-w-2xl mx-auto">
          Los perritos de las calles también suelen y es común enfermarse de
          cáncer, como los tumores de stickers el cual es un tumor cancerígeno,
          su único tratamiento es con quimioterapias y se contrae mediante las
          relaciones sexuales entre perros contagiados. Al no tratarse a tiempo
          termina regándose por todo el cuerpo y se convierte en cáncer de piel.
        </p>
      </section>

      <section className="bg-yellow-100 p-10">
        <div className="max-w-6xl mx-auto space-y-10">
          <div>
            <img
              src="/dog-history.png"
              alt="Dog"
              className="w-full rounded-lg mb-6"
            />
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
              <img
                src="/dog-progress-1.png"
                alt="Dog"
                className="w-full rounded-lg"
              />
              <img
                src="/dog-progress-2.png"
                alt="Dog"
                className="w-full rounded-lg"
              />
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
    </div>
  );
}
