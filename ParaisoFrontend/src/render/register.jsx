import React, { useState } from "react";
import { register } from "../services/api";


export default function Register() {
const [nombre, setNombre] = useState("");
const [correo, setCorreo] = useState("");
const [password, setPassword] = useState("");
const [confirm, setConfirm] = useState("");
const [ok, setOk] = useState("");
const [error, setError] = useState("");


const onSubmit = async (e) => {
e.preventDefault();
setOk(""); setError("");
if (password !== confirm) { setError("Las contraseñas no coinciden"); return; }
try {
await register({ nombre, correo, password });
setOk("Registro exitoso. Ya puedes iniciar sesión.");
setNombre(""); setCorreo(""); setPassword(""); setConfirm("");
} catch (err) { setError(err.message || "Error registrando"); }
};


return (
<div className="min-h-screen grid place-items-center bg-[#F3EFD2] p-6">
<form onSubmit={onSubmit} className="w-full max-w-md bg-white p-6 rounded-xl shadow">
<h1 className="text-2xl font-bold mb-4">Crear cuenta</h1>
{ok && <p className="text-green-700 mb-2">{ok}</p>}
{error && <p className="text-red-600 mb-2">{error}</p>}
<label className="block mb-2">Nombre</label>
<input className="w-full mb-4 p-3 border rounded" value={nombre} onChange={(e)=>setNombre(e.target.value)} required/>
<label className="block mb-2">Correo</label>
<input className="w-full mb-4 p-3 border rounded" type="email" value={correo} onChange={(e)=>setCorreo(e.target.value)} required/>
<label className="block mb-2">Contraseña</label>
<input className="w-full mb-4 p-3 border rounded" type="password" value={password} onChange={(e)=>setPassword(e.target.value)} required/>
<label className="block mb-2">Confirmar contraseña</label>
<input className="w-full mb-6 p-3 border rounded" type="password" value={confirm} onChange={(e)=>setConfirm(e.target.value)} required/>
<button className="w-full bg-black text-white p-3 rounded hover:opacity-90">Registrarme</button>
</form>
</div>
);
}