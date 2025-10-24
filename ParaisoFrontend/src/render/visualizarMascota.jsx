import React, { useEffect, useState } from "react";
import { perrosApi } from "../services/api";


export default function VisualizarMascota() {
const [items, setItems] = useState([]);
const [nombre, setNombre] = useState("");
const [raza, setRaza] = useState("");
const [error, setError] = useState("");


const load = () => perrosApi.list({ nombre, raza }).then(setItems).catch((e)=>setError(e.message));
useEffect(load, []);


return (
<div className="min-h-screen p-6 bg-[#F3EFD2]">
<div className="max-w-5xl mx-auto">
<div className="grid grid-cols-1 md:grid-cols-3 gap-2 mb-4">
<input className="p-3 border rounded" placeholder="Nombre" value={nombre} onChange={(e)=>setNombre(e.target.value)} />
<input className="p-3 border rounded" placeholder="Raza" value={raza} onChange={(e)=>setRaza(e.target.value)} />
<button onClick={load} className="px-4 bg-black text-white rounded">Buscar</button>
</div>
{error && <p className="text-red-600 mb-2">{error}</p>}
<div className="grid md:grid-cols-3 gap-4">
{items.map(p => (
<div key={p.id} className="bg-white rounded-xl shadow p-4">
{p.img && <img src={p.img} alt="perro" className="rounded mb-2 max-h-40 w-full object-cover"/>}
<h3 className="font-bold">{p.nombre}</h3>
<p className="text-sm text-gray-600">{p.raza} · {p.edad} años</p>
<p className="text-sm text-gray-700">{p.descr}</p>
</div>
))}
</div>
</div>
</div>
);
}