import React, { useState } from "react";
import gatico from "../assets/gatico.png";
import { ArrowLeft, ArrowRight } from "lucide-react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext.jsx";

const Register = () => {
  const navigate = useNavigate();
  const { register } = useAuth();

  const [formData, setFormData] = useState({
    nombre: "", // <-- CAMBIO: Añadido
    email: "",
    password: "",
    confirmarPassword: "",
  });
  const [submitting, setSubmitting] = useState(false);

  const handleChange = (e) => {
    setFormData((prev) => ({
      ...prev,
      [e.target.name]: e.target.value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    // <-- CAMBIO: Validación
    if (!formData.nombre || !formData.email || !formData.password) {
      alert("Completa nombre, email y contraseña.");
      return;
    }
    if (formData.password !== formData.confirmarPassword) {
      alert("Las contraseñas no coinciden");
      return;
    }

    setSubmitting(true);
    try {
      // <-- CAMBIO: Envío de datos
      await register({
        nombre: formData.nombre,
        correo: formData.email, // Mapeamos 'email' del form a 'correo'
        password: formData.password,
      });

      alert("Registro exitoso. Ahora inicia sesión.");
      navigate("/login", { replace: true }); // No crea sesión automáticamente
    } catch (error) {
      console.error("Error al registrar:", error);
      const msg =
        error?.response?.data?.message ||
        error?.response?.data?.error ||
        "Error al registrar usuario";
      alert(msg);
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div className="flex flex-col lg:flex-row h-screen w-full font-sans">
      {/* Lado Izquierdo - Formulario */}
      <div className="flex-1 flex flex-col justify-center bg-[#F3EFD2] px-10 py-12 rounded-l-3xl">
        <h1 className="text-4xl font-bold mb-10 text-gray-900">Sé uno más</h1>
        <form className="space-y-6" onSubmit={handleSubmit}>
          {/* <-- CAMBIO: Añadido bloque de Nombre --> */}
          <div>
            <label className="block text-sm font-medium mb-1">Nombre</label>
            <input
              type="text"
              name="nombre"
              value={formData.nombre}
              onChange={handleChange}
              className="w-full px-4 py-3 bg-[#E7E0C9] rounded-md outline-none"
              required
              autoComplete="name"
              placeholder="Tu nombre completo"
            />
          </div>

          <div>
            <label className="block text-sm font-medium mb-1">
              Correo electrónico
            </label>
            <input
              type="email"
              name="email"
              value={formData.email}
              onChange={handleChange}
              className="w-full px-4 py-3 bg-[#E7E0C9] rounded-md outline-none"
              required
              autoComplete="email"
              placeholder="tucorreo@dominio.com"
            />
          </div>

          <div>
            <label className="block text-sm font-medium mb-1">Contraseña</label>
            <input
              type="password"
              name="password"
              value={formData.password}
              onChange={handleChange}
              className="w-full px-4 py-3 bg-[#E7E0C9] rounded-md outline-none"
              required
              autoComplete="new-password"
              placeholder="••••••••"
            />
          </div>

          <div>
            <label className="block text-sm font-medium mb-1">
              Ingrese otra vez la contraseña
            </label>
            <input
              type="password"
              name="confirmarPassword"
              value={formData.confirmarPassword}
              onChange={handleChange}
              className="w-full px-4 py-3 bg-[#E7E0C9] rounded-md outline-none"
              required
              autoComplete="new-password"
              placeholder="••••••••"
            />
          </div>

          <button
            type="submit"
            className="w-full py-3 bg-[#EF5B00] text-white font-semibold rounded-md disabled:opacity-60"
            disabled={submitting}
          >
            {submitting ? "Registrando..." : "Registrarse"}
          </button>
        </form>
      </div>

      {/* Lado Derecho - Imagen y descripción del gato */}
      <div className="flex-1 relative hidden lg:block">
        <img src={gatico} alt="Gato" className="object-cover w-full h-full" />
        <div className="absolute bottom-8 left-20 bg-black/40 text-white p-6 rounded-xl max-w-xl w-full">
          <h2 className="text-2xl font-semibold mb-2">Misifu</h2>
          <p className="text-sm">
            “Misifu es un gatito muy juguetón y energético. Cada mañana, al
            despertar, corretea por toda la casa, jugando con cualquier cosa que
            encuentre a su paso”
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
