import React, { useState, useEffect } from "react";
import axios from "axios"; // 1. Importar axios
import Footer from "./componentes/footer";
import NavnoCAdm from "./componentes/navCesionAdm.jsx"; // Admin
import NavnoCesion from "./componentes/navNocesion.jsx"; // Invitado
import NavCesionCli from "./componentes/navCesionCli.jsx"; // Cliente
import useNavKind from "../hooks/useNavKind.js";
import { useAuth } from "../context/AuthContext.jsx"; // 2. Importar useAuth

const API_URL = "http://localhost:8081"; // La URL de tu backend

// Helper para formatear la fecha
const formatDate = (isoString) => {
  if (!isoString) return "Fecha desconocida";
  return new Date(isoString).toLocaleDateString("es-ES", {
    day: "2-digit",
    month: "2-digit",
    year: "numeric",
  });
};

const Donaciones = () => {
  const [donaciones, setDonaciones] = useState([]); // 3. Estado para datos reales
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const navKind = useNavKind();
  const { user } = useAuth(); // 4. Obtener el usuario (para el token)

  // 5. useEffect para cargar las donaciones
  useEffect(() => {
    const fetchDonaciones = async () => {
      // 6. Proteger la vista a nivel de frontend
      // (Aunque el backend ya la protege, esto es mejor UI)
      if (navKind !== "admin" || !user || !user.token) {
        setError(
          "Acceso denegado. Debes ser Administrador para ver esta página."
        );
        setLoading(false);
        return;
      }

      // 7. Configurar el token en los headers
      const config = {
        headers: {
          Authorization: `Bearer ${user.token}`,
        },
      };

      try {
        setLoading(true);
        // 8. Llamar al endpoint protegido
        const response = await axios.get(`${API_URL}/api/donaciones`, config);
        setDonaciones(response.data);
        setError(null);
      } catch (err) {
        console.error("Error cargando donaciones:", err);
        if (err.response?.status === 403 || err.response?.status === 401) {
          setError("Acceso denegado. No tienes permisos para ver esto.");
        } else {
          setError("Error al cargar las donaciones.");
        }
      } finally {
        setLoading(false);
      }
    };

    fetchDonaciones();
  }, [navKind, user]); // Depende del usuario para tener el token

  return (
    <div className="min-h-screen bg-[#e7e1d2]">
      {/* Header */}
      <div className="bg-red-600">
        {/* NAV dinámico por rol */}
        {navKind === "admin" ? (
          <NavnoCAdm />
        ) : navKind === "client" ? (
          <NavCesionCli />
        ) : (
          <NavnoCesion />
        )}
        <header className="bg-red-600 py-6 flex flex-col items-center">
          <h1 className="text-3xl font-bold text-gray-800">Donaciones</h1>
        </header>
      </div>

      {/* Grid de donaciones */}
      <div className="max-w-6xl mx-auto py-10 px-6">
        {/* 9. Renderizado condicional */}

        {loading && (
          <p className="text-center text-xl text-gray-700">
            Cargando donaciones...
          </p>
        )}

        {error && (
          <p className="text-center text-xl text-red-700 bg-red-100 p-4 rounded-lg shadow-md">
            {error}
          </p>
        )}

        {!loading && !error && donaciones.length === 0 && (
          <p className="text-center text-xl text-gray-700">
            No se encontraron donaciones.
          </p>
        )}

        {!loading && !error && donaciones.length > 0 && (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {/* 10. Mapear los datos reales */}
            {donaciones.map((dona) => (
              <div
                key={dona.id}
                className="bg-[#f5eedf] rounded-lg p-6 shadow-md"
              >
                <div className="flex justify-between items-center">
                  <h2 className="font-bold text-gray-700">
                    {/* Usamos '?' por si el usuario fue borrado (null safety) */}
                    {dona.usuario?.nombre || "Donador Anónimo"}
                  </h2>
                  <span className="text-sm text-gray-500">
                    {formatDate(dona.fecha)}
                  </span>
                </div>
                <p className="mt-4 text-gray-600 font-semibold">
                  Alcancía: {dona.alcancia?.descr || "N/A"}
                </p>
                <p className="mt-2 text-teal-600 font-bold text-xl">
                  {/* El backend envía 'cantidadDonada' */}$
                  {Number(dona.cantidadDonada).toFixed(2)}
                </p>
              </div>
            ))}
          </div>
        )}
      </div>
      <Footer />
    </div>
  );
};

export default Donaciones;
