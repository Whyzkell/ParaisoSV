import React, { useState, useEffect } from "react"; // 1. Importar hooks
import axios from "axios"; // 2. Importar axios
import { Facebook, Instagram, Twitter } from "lucide-react";
import { BrowserRouter as Router, Route, Routes, Link } from "react-router-dom";
// import chuchitos from "../assets/chuchitos.png"; // No necesitas 'hosh', las imgs vienen de la API
import chuchitos from "../assets/chuchitos.png";
import { useNavigate } from "react-router-dom";
import Footer from "./componentes/footer.jsx";
import NavnoCAdm from "./componentes/navCesionAdm.jsx"; // Admin
import NavnoCesion from "./componentes/navNocesion.jsx"; // Invitado
import NavCesionCli from "./componentes/navCesionCli.jsx"; // Cliente
import useNavKind from "../hooks/useNavKind.js";

const API_URL = "http://localhost:8081"; // La URL de tu backend

// Ya no usamos la lista harcodeada
// const pets = [ ... ];

export default function AdoptarMascota() {
  const navKind = useNavKind(); // 'admin' | 'client' | 'guest'
  const navigate = useNavigate();

  // 3. Estados para manejar los datos de la API
  const [pets, setPets] = useState([]); // Almacenará las mascotas
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // 4. useEffect para cargar las mascotas cuando el componente se monta
  useEffect(() => {
    const fetchPets = async () => {
      try {
        setLoading(true);
        // Endpoint público, no requiere token
        const response = await axios.get(`${API_URL}/api/perros`);
        setPets(response.data); // Guardamos la lista de mascotas en el estado
        setError(null);
      } catch (err) {
        console.error("Error cargando las mascotas:", err);
        setError("No se pudieron cargar las mascotas. Intenta más tarde.");
      } finally {
        setLoading(false);
      }
    };

    fetchPets();
  }, []); // El array vacío [] significa que esto se ejecuta solo una vez

  // 5. Modificamos el click para que sepa a QUÉ mascota ir
  const handleClick = (id) => {
    // Navegamos a una ruta dinámica. Ej: /visualizar-mascota/3
    navigate(`/visualizar-mascota/${id}`);
  };

  return (
    <div className="bg-yellow-50 min-h-screen">
      {/* NAV dinámico por rol */}
      {navKind === "admin" ? (
        <NavnoCAdm />
      ) : navKind === "client" ? (
        <NavCesionCli />
      ) : (
        <NavnoCesion />
      )}

      <section className="flex text-center px-6 w-full">
        <div className="flex-row justify-items-start justify-start ml-8 w-2/5 mt-8">
          <h2 className="text-teal-600 font-bold text-6xl ">LADOPTA A TU</h2>
          <h3 className="text-green-400 font-bold text-6xl  mt-4">
            NUEVO AMIGO
          </h3>
          <p className="text-gray-600 text-3xl mt-4  text-left w-10/12">
            Todos merecemos una segunda oportunidad, los perritos y gatitos ya
            sean cachorros, jóvenes, adultos o ancianos, también lo merecen. Por
            eso creamos campañas masivas de adopción para que ellos tengan la
            oportunidad de ir a un buen hogar.
          </p>
        </div>
        <div className="flex justify-center mt-6  w-3/5">
          <img src={chuchitos} alt="Pets" className="w-8/12" />
        </div>
      </section>

      <section className="bg-gradient-to-b from-yellow-400 via-yellow-400 to-red-500 -mt-24 flex justify-center py-12">
        <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-6 w-11/12 mt-24">
          {/* 6. Lógica de renderizado con los datos de la API */}

          {/* Caso 1: Cargando */}
          {loading && (
            <p className="text-white text-2xl col-span-3 text-center">
              Cargando mascotas...
            </p>
          )}

          {/* Caso 2: Error */}
          {error && (
            <p className="text-red-900 text-2xl col-span-3 text-center bg-red-200 p-4 rounded-md">
              {error}
            </p>
          )}

          {/* Caso 3: Éxito, pero no hay mascotas */}
          {!loading && !error && pets.length === 0 && (
            <p className="text-white text-2xl col-span-3 text-center">
              No hay mascotas disponibles para adoptar por el momento.
            </p>
          )}

          {/* Caso 4: Éxito, mostrar mascotas */}
          {!loading &&
            !error &&
            pets.map((pet) => (
              <div
                key={pet.id} // Usamos el ID de la base de datos
                className="bg-white rounded-lg shadow-md p-4 text-center mt-8 flex "
              >
                <div className="flex-row w-3/5 justify-items-start">
                  <h4 className="text-3xl font-bold ml-5 mt-4">{pet.nombre}</h4>
                  <p className="text-gray-500 ml-5 text-2xl mt-3">{pet.raza}</p>
                  <p className="text-gray-400 text-xl ml-5 mt-3">
                    {/* Formateamos la edad */}
                    {pet.edad} {pet.edad === 1 ? "año" : "años"}
                  </p>
                  <button
                    onClick={() => handleClick(pet.id)} // Pasamos el ID al handler
                    className="mt-16 bg-orange-500 text-white px-4 py-2 rounded hover:bg-orange-600 ml-5"
                  >
                    Más Información
                  </button>
                </div>
                <div className="w-2/5">
                  <img
                    src={pet.img} // Usamos la URL de la imagen del backend
                    alt={pet.nombre}
                    className="w-10/12 object-cover "
                  />
                </div>
              </div>
            ))}
        </div>
      </section>
      <Footer />
    </div>
  );
}
