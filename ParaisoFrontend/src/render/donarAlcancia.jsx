import React, { useState, useEffect } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import alcanciaImg from "../assets/alcancia.png";
import NavnoCAdm from "./componentes/navCesionAdm.jsx"; // Admin
import NavnoCesion from "./componentes/navNocesion.jsx"; // Invitado
import NavCesionCli from "./componentes/navCesionCli.jsx"; // Cliente
import useNavKind from "../hooks/useNavKind.js";
import { useAuth } from "../context/AuthContext.jsx"; // 1. Importar Auth
import Footer from "./componentes/footer.jsx";

// URL del backend
const API_URL = "http://localhost:8081";

// --- Componente de Tarjeta para cada Alcancía ---
// (Lo pongo en el mismo archivo para que solo copies y pegues una vez)
const AlcanciaCard = ({ alcancia, isSelected, onSelect }) => {
  const { id, descr, precioMeta, precioActual } = alcancia;

  // Calcular el progreso
  const progreso = (precioActual / precioMeta) * 100;

  // Formatear a dólares
  const formatCurrency = (num) => `$${Number(num).toFixed(2)}`;

  return (
    <div
      onClick={() => onSelect(id)}
      className={`p-4 border-2 rounded-xl bg-white shadow-md cursor-pointer transition-all ${
        isSelected
          ? "border-orange-500 scale-105"
          : "border-transparent hover:shadow-lg"
      }`}
    >
      <h3 className="font-bold text-lg text-gray-800">{descr}</h3>
      <p className="text-sm text-gray-600 mt-2">
        Donado:{" "}
        <span className="font-semibold text-teal-600">
          {formatCurrency(precioActual)}
        </span>
      </p>
      <p className="text-sm text-gray-600">
        Meta:{" "}
        <span className="font-semibold">{formatCurrency(precioMeta)}</span>
      </p>

      {/* Barra de Progreso */}
      <div className="w-full bg-gray-200 rounded-full h-2.5 mt-3">
        <div
          className="bg-teal-500 h-2.5 rounded-full transition-all"
          style={{ width: `${progreso > 100 ? 100 : progreso}%` }}
        ></div>
      </div>
      <p className="text-xs text-right mt-1 font-medium">
        {progreso.toFixed(0)}%
      </p>
    </div>
  );
};

// --- Componente Principal de la Página ---
const DonarAlcancia = () => {
  const navKind = useNavKind();
  const { user } = useAuth(); // 2. Traer el usuario (para token)
  const navigate = useNavigate();

  // 3. Estados para la lógica de la página
  const [alcancias, setAlcancias] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const [selectedId, setSelectedId] = useState(null); // ID de la alcancía clickeada
  const [monto, setMonto] = useState(""); // Monto a donar
  const [submitting, setSubmitting] = useState(false);

  // 4. Función para cargar las alcancías
  const fetchAlcancias = async () => {
    try {
      setLoading(true);
      const response = await axios.get(`${API_URL}/api/alcancias`);
      setAlcancias(response.data);
      setError(null);
    } catch (err) {
      console.error("Error cargando alcancías:", err);
      setError("No se pudieron cargar las alcancías.");
    } finally {
      setLoading(false);
    }
  };

  // 5. Cargar las alcancías al iniciar
  useEffect(() => {
    fetchAlcancias();
  }, []); // El [] asegura que solo se ejecute una vez al montar

  // 6. Lógica para enviar la donación
  const handleSubmitDonacion = async (e) => {
    e.preventDefault();

    // Validaciones
    if (!user) {
      alert("Necesitas iniciar sesión para poder donar.");
      navigate("/login");
      return;
    }
    const cantidad = parseFloat(monto);
    if (!selectedId || !cantidad || cantidad <= 0) {
      alert("Selecciona una alcancía e ingresa un monto válido.");
      return;
    }

    setSubmitting(true);

    // El DTO que espera el backend
    const body = {
      idAlcancia: selectedId,
      cantidad: cantidad,
    };

    // La configuración con el token
    const config = {
      headers: {
        Authorization: `Bearer ${user.token}`,
      },
    };

    try {
      // Petición POST a la API de donaciones
      await axios.post(`${API_URL}/api/donaciones`, body, config);

      alert("¡Donación realizada con éxito! Muchas gracias.");

      // Limpiar y refrescar
      setMonto("");
      setSelectedId(null);
      fetchAlcancias(); // ¡Refrescamos la lista para ver el progreso!
    } catch (error) {
      console.error("Error al donar:", error);
      if (error.response?.status === 401) {
        alert("Tu sesión ha expirado. Por favor, inicia sesión de nuevo.");
      } else {
        alert("Ocurrió un error al procesar tu donación.");
      }
    } finally {
      setSubmitting(false);
    }
  };

  // 7. Encontrar la alcancía seleccionada (para el formulario)
  const selectedAlcancia = alcancias.find((a) => a.id === selectedId);

  return (
    <div className="min-h-screen bg-red-600 font-sans">
      <div className="bg-[#F5F0DC]">
        {navKind === "admin" ? (
          <NavnoCAdm />
        ) : navKind === "client" ? (
          <NavCesionCli />
        ) : (
          <NavnoCesion />
        )}
      </div>

      <main className="flex flex-col lg:flex-row w-full h-full">
        {/* Lado izquierdo: LISTA de alcancías */}
        <div className="w-full lg:w-2/3 bg-[#F5F0DC] p-8 min-h-[calc(100vh-80px)] overflow-y-auto">
          <h2 className="text-3xl font-bold text-gray-800 mb-6">
            Elige una causa para donar
          </h2>

          {loading && <p>Cargando alcancías...</p>}
          {error && <p className="text-red-600">{error}</p>}

          {!loading && !error && (
            <div className="grid grid-cols-1 md:grid-cols-2 gap-5">
              {alcancias.length > 0 ? (
                alcancias.map((alcancia) => (
                  <AlcanciaCard
                    key={alcancia.id}
                    alcancia={alcancia}
                    isSelected={alcancia.id === selectedId}
                    onSelect={setSelectedId}
                  />
                ))
              ) : (
                <p>No hay alcancías disponibles por el momento.</p>
              )}
            </div>
          )}
        </div>

        {/* Lado derecho: FORMULARIO de donación */}
        <div className="w-full lg:w-1/3 bg-red-600 flex items-center justify-center p-10 min-h-[calc(100vh-80px)]">
          {/* Si no hay nada seleccionado, mostramos la imagen */}
          {!selectedId && (
            <div className="text-center">
              <img
                src={alcanciaImg}
                alt="Alcancía"
                className="max-h-80 object-contain rounded-xl"
              />
              <p className="text-white text-xl font-semibold mt-4">
                Haz clic en una alcancía de la izquierda para donar.
              </p>
            </div>
          )}

          {/* Si SÍ hay algo seleccionado, mostramos el formulario */}
          {selectedId && selectedAlcancia && (
            <div className="relative transform -skew-y-2 border border-black rounded-3xl bg-[#F5F0DC] p-6 w-full max-w-2xl">
              <form
                className="transform skew-y-2 space-y-6"
                onSubmit={handleSubmitDonacion}
              >
                <h2 className="text-2xl font-bold text-center text-gray-800">
                  Donar a: <br />
                  <span className="text-orange-600">
                    {selectedAlcancia.descr}
                  </span>
                </h2>

                <div>
                  <label className="block text-gray-700 font-medium mb-1">
                    Cantidad a donar
                  </label>
                  <input
                    type="number"
                    min="0.01"
                    step="0.01"
                    value={monto}
                    onChange={(e) => setMonto(e.target.value)}
                    placeholder="$ 0.00"
                    className="w-full bg-transparent border-b-2 border-gray-400 focus:border-orange-500 focus:outline-none py-2 text-lg"
                    required
                  />
                </div>

                <div className="pt-4 flex flex-col gap-4">
                  <button
                    type="submit"
                    disabled={submitting}
                    className="w-full bg-orange-500 text-white py-3 rounded-md font-semibold hover:bg-orange-600 transition disabled:opacity-50"
                  >
                    {submitting ? "Procesando..." : "Confirmar Donación"}
                  </button>
                  <button
                    type="button"
                    onClick={() => setSelectedId(null)} // Botón para "des-seleccionar"
                    className="w-full bg-[#007B8A] text-white py-3 rounded-md font-semibold hover:bg-[#006b75] transition"
                  >
                    Cancelar
                  </button>
                </div>
              </form>
            </div>
          )}
        </div>
      </main>
      <Footer />
    </div>
  );
};

export default DonarAlcancia;
