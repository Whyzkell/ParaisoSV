import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { login } from "../services/api";


export default function Login() {
const nav = useNavigate();
const [correo, setCorreo] = useState("");
const [password, setPassword] = useState("");
const [loading, setLoading] = useState(false);
const [error, setError] = useState("");


const onSubmit = async (e) => {
e.preventDefault();
setError("");
setLoading(true);
try {
await login({ correo, password });
nav("/");
} catch (err) {
setError(err.message || "Error de autenticación");
} finally {
setLoading(false);
}
};


return (
<div className="min-h-screen grid place-items-center bg-[#F3EFD2] p-6">
<form onSubmit={onSubmit} className="w-full max-w-md bg-white p-6 rounded-xl shadow">
<h1 className="text-2xl font-bold mb-4">Iniciar sesión</h1>
{error && <p className="text-red-600 mb-2">{error}</p>}
<label className="block mb-2">Correo</label>
<input className="w-full mb-4 p-3 border rounded" type="email" value={correo} onChange={(e)=>setCorreo(e.target.value)} required/>
<label className="block mb-2">Contraseña</label>
<input className="w-full mb-4 p-3 border rounded" type="password" value={password} onChange={(e)=>setPassword(e.target.value)} required/>
<button disabled={loading} className="w-full bg-black text-white p-3 rounded hover:opacity-90">
{loading? "Ingresando..." : "Ingresar"}
</button>
</form>
</div>
);
}