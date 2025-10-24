import React, { useState } from "react";
import { alcanciasApi } from "../services/api";


export default function CrearAlcancia() {
const [descr, setDescr] = useState("");
const [precioMeta, setPrecioMeta] = useState("");
const [ok, setOk] = useState("");
const [error, setError] = useState("");


const onSubmit = async (e) => {
e.preventDefault();
setOk(""); setError("");
try {
await alcanciasApi.create({ descr, precioMeta: Number(precioMeta) });
setOk("Alcancía creada");
setDescr(""); setPrecioMeta("");
} catch (err) { setError(err.message); }
};


return (
<div className="min-h-screen grid place-items-center bg-[#F3EFD2] p-6">
<form onSubmit={onSubmit} className="w-full max-w-lg bg-white p-6 rounded-xl shadow">
<h1 className="text-2xl font-bold mb-4">Crear Alcancía</h1>
{ok && <p className="text-green-700 mb-2">{ok}</p>}
{error && <p className="text-red-600 mb-2">{error}</p>}
<label className="block mb-2">Descripción</label>
<textarea className="w-full mb-4 p-3 border rounded" value={descr} onChange={(e)=>setDescr(e.target.value)} required/>
<label className="block mb-2">Meta</label>
<input className="w-full mb-6 p-3 border rounded" type="number" step="0.01" value={precioMeta} onChange={(e)=>setPrecioMeta(e.target.value)} required/>
<button className="w-full bg-black text-white p-3 rounded hover:opacity-90">Guardar</button>
</form>
</div>
);
}