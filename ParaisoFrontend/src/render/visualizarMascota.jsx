import React, { useState, useEffect } from "react"; // 1. Importar hooks
import { useParams, useNavigate } from "react-router-dom"; // 2. Importar useParams
import axios from "axios"; // 3. Importar axios
import Footer from "./componentes/footer";
import NavnoCAdm from "./componentes/navCesionAdm.jsx"; // Admin
import NavnoCesion from "./componentes/navNocesion.jsx"; // Invitado
import NavCesionCli from "./componentes/navCesionCli.jsx"; // Cliente
import useNavKind from "../hooks/useNavKind.js";

const API_URL = "http://localhost:8081"; // La URL de tu backend

function VSMascota() {
  const navKind = useNavKind(); // 'admin' | 'client' | 'guest'
  const { id } = useParams(); // 4. Obtener el ID de la URL
  const navigate = useNavigate();

  // 5. Estados para manejar la mascota, carga y errores
  const [pet, setPet] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // 6. useEffect para cargar la mascota específica
  useEffect(() => {
    // No hacer nada si no hay ID
    if (!id) return;

    const fetchPet = async () => {
      try {
        setLoading(true);
        // Endpoint público para obtener un perro por su ID
        const response = await axios.get(`${API_URL}/api/perros/${id}`);
        setPet(response.data); // Guardamos la mascota en el estado
        setError(null);
      } catch (err) {
        console.error(`Error cargando mascota con id ${id}:`, err);
        if (err.response?.status === 404) {
          setError("Mascota no encontrada.");
        } else {
          setError("No se pudo cargar la información. Intenta más tarde.");
        }
      } finally {
        setLoading(false);
      }
    };

    fetchPet();
  }, [id]); // Se vuelve a ejecutar si el ID en la URL cambia

  // 7. Lógica del botón Adoptar (por ahora solo navega)
  // (En un futuro, esto podría llevar a un formulario de adopción)
  const handleAdoptar = () => {
    alert(`Iniciando proceso de adopción para ${pet.nombre}!`);
    // navigate('/formulario-adopcion/' + pet.id);
  };

  return (
    <div className="bg-[#F9EFE0] min-h-screen flex flex-col w-full">
      {/* NAV dinámico por rol */}
      {navKind === "admin" ? (
        <NavnoCAdm />
      ) : navKind === "client" ? (
        <NavCesionCli />
      ) : (
        <NavnoCesion />
      )}

      <main className="flex-grow flex items-center justify-center w-full px-8 py-12">
        {/* 8. Renderizado condicional */}

        {/* Caso 1: Cargando */}
        {loading && (
          <p className="text-2xl text-center text-gray-700">
            Cargando información de la mascota...
          </p>
        )}

        {/* Caso 2: Error */}
        {error && (
          <p className="text-2xl text-center text-red-600 bg-red-100 p-6 rounded-lg">
            {error}
          </p>
        )}

        {/* Caso 3: Éxito (mostrar la mascota) */}
        {!loading && !error && pet && (
          <>
            {/* Contenedor de la Imagen */}
            <div className="w-1/3 flex-shrink-0 mr-16 mt-8 rounded-full overflow-hidden border-4 border-yellow-400 flex items-center justify-center bg-[#FFD700]">
              <img
                src={pet.img} // <-- Dinámico
                alt={pet.nombre} // <-- Dinámico
                className="w-full h-full object-cover"
              />
            </div>

            {/* Contenedor de la Información */}
            <div className="flex rounded-lg shadow-lg max-w-4xl w-2/5 p-8 bg-white">
              <div className="ml-6 flex flex-col justify-between">
                <h2 className="text-3xl font-bold mb-6">{pet.nombre}</h2>{" "}
                {/* <-- Dinámico */}
                <p className="text-lg font-semibold">
                  Raza: <span>{pet.raza}</span> {/* <-- Dinámico */}
                </p>
                <p className="text-lg font-semibold">
                  Edad:{" "}
                  <span>
                    {pet.edad} {pet.edad === 1 ? "año" : "años"}
                  </span>{" "}
                  {/* <-- Dinámico */}
                </p>
                {/* La descripción viene del backend */}
                <p className="mt-4 text-gray-700">{pet.descr}</p>{" "}
                {/* <-- Dinámico */}
                <button
                  onClick={handleAdoptar}
                  className="mt-6 bg-orange-500 text-white font-bold py-2 px-4 rounded hover:bg-orange-600"
                >
                  Adoptar a {pet.nombre}
                </button>
              </div>
            </div>
          </>
        )}
      </main>

      <Footer />
    </div>
  );
}

export default VSMascota;
