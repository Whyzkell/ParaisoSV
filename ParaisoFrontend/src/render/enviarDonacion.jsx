// ParaisoFrontend/src/render/enviarDonacion.jsx
import React, { useState } from "react";
import alcanciaImg from "../assets/alcancia.png";
import NavnoCCli from "./componentes/navCesionCli.jsx";
import Footer from "./componentes/footer.jsx";
import { apiPost } from "../services/api";


const EnviarDonacion = () => {
  const [monto, setMonto] = useState("");
  const [descripcion, setDescripcion] = useState("");
  const [metodo, setMetodo] = useState("tarjeta");
  const [enviando, setEnviando] = useState(false);
  const [mensaje, setMensaje] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();
    setMensaje("");
    if (!monto || Number(monto) <= 0) {
      setMensaje("Ingresa un monto válido.");
      return;
    }
    setEnviando(true);
    try {
      // Ajusta la ruta si tu backend expone otra:
      const res = await apiPost("donaciones/enviar", {
        monto: Number(monto),
        descripcion,
        metodo, // tarjeta | transferencia | paypal (ejemplo)
      });
      setMensaje("¡Donación enviada correctamente!");
      setMonto("");
      setDescripcion("");
      setMetodo("tarjeta");
      console.log("Respuesta backend:", res);
    } catch (err) {
      setMensaje(`Ocurrió un error: ${err.message || "intenta de nuevo"}`);
    } finally {
      setEnviando(false);
    }
  };

  return (
    <div className="min-h-screen bg-red-600 font-sans">
      {/* Encabezado sobre fondo claro para hacer match con tu estilo */}
      <div className="bg-[#F5F0DC]">
        <NavnoCCli />
      </div>

      <main className="flex flex-col lg:flex-row w-full h-full">
        {/* Lado izquierdo: formulario con borde y leve inclinación como tu “Donar alcancía” */}
        <div className="w-full lg:w-2/3 bg-[#F5F0DC] flex items-center justify-center min-h-[calc(100vh-80px)]">
          <div className="relative transform -skew-y-2 border border-black rounded-3xl bg-[#F5F0DC] p-6 w-full max-w-2xl">
            <form onSubmit={handleSubmit} className="transform skew-y-2 space-y-6">
              <h2 className="text-3xl font-bold text-center text-gray-800">
                Enviar donación
              </h2>

              {/* Monto */}
              <div>
                <label className="block text-gray-700 font-medium mb-1">
                  Monto a donar
                </label>
                <div className="flex items-center gap-2">
                  <span className="text-xl text-gray-600 select-none">$</span>
                  <input
                    type="number"
                    step="0.01"
                    min="0"
                    value={monto}
                    onChange={(e) => setMonto(e.target.value)}
                    placeholder="0.00"
                    className="w-full bg-transparent border-b-2 border-gray-300 focus:border-orange-500 focus:outline-none py-2 text-lg"
                  />
                </div>
              </div>

              {/* Descripción / mensaje */}
              <div>
                <label className="block text-gray-700 font-medium mb-1">
                  Descripción (opcional)
                </label>
                <input
                  type="text"
                  value={descripcion}
                  onChange={(e) => setDescripcion(e.target.value)}
                  placeholder="¿Quieres dejar un mensaje en tu donación?"
                  className="w-full bg-transparent border-b-2 border-gray-300 focus:border-orange-500 focus:outline-none py-2 text-lg"
                />
              </div>

              {/* Método de pago */}
              <div>
                <label className="block text-gray-700 font-medium mb-1">
                  Método de pago
                </label>
                <select
                  value={metodo}
                  onChange={(e) => setMetodo(e.target.value)}
                  className="w-full bg-transparent border-b-2 border-gray-300 focus:border-orange-500 focus:outline-none py-2 text-lg"
                >
                  <option value="tarjeta">Tarjeta</option>
                  <option value="transferencia">Transferencia</option>
                  <option value="paypal">PayPal</option>
                </select>
              </div>

              {/* Mensaje de estado */}
              {mensaje && (
                <p className="text-sm text-gray-700 bg-white/60 px-3 py-2 rounded">
                  {mensaje}
                </p>
              )}

              <div className="pt-4 flex flex-col sm:flex-row gap-4">
                <button
                  type="submit"
                  disabled={enviando}
                  className="w-full bg-orange-500 text-white py-3 rounded-md font-semibold hover:bg-orange-600 transition disabled:opacity-60"
                >
                  {enviando ? "Enviando..." : "Enviar donación"}
                </button>
                <button
                  type="button"
                  onClick={() => {
                    setMonto("");
                    setDescripcion("");
                    setMetodo("tarjeta");
                    setMensaje("");
                  }}
                  className="w-full bg-[#007B8A] text-white py-3 rounded-md font-semibold hover:bg-[#006b75] transition"
                >
                  Limpiar
                </button>
              </div>
            </form>
          </div>
        </div>

        {/* Lado derecho: imagen */}
        <div className="w-full lg:w-1/3 bg-red-600 flex items-center justify-center p-10 min-h-[calc(100vh-80px)]">
          <img
            src={alcanciaImg}
            alt="Alcancía"
            className="max-h-80 object-contain rounded-xl"
          />
        </div>
      </main>
      <Footer />
    </div>
  );
};

export default EnviarDonacion;
