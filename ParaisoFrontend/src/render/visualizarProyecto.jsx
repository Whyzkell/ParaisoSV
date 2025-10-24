import React, { useEffect, useState } from "react";
import { proyectosApi } from "../services/api";


export default function VisualizarProyecto() {
const [items, setItems] = useState([]);
const [q, setQ] = useState("");
const [error, setError] = useState("");


const load = () => proyectosApi.list(q).then(setItems).catch((e)=>setError(e.message));
useEffect(load, []);


return (
<div className="min-h-screen p-6 bg-[#F3EFD2]">
<div className="max-w-5xl mx-auto">
<div className="flex gap-2 mb-4">
<input className="flex-1 p-3 border rounded" placeholder="Buscar" value={q} onChange={(e)=>setQ(e.target.value)} />
<button onClick={load} className="px-4 bg-black text-white rounded">Buscar</button>
</div>
{error && <p className="text-red-600 mb-2">{error}</p>}
<div className="grid md:grid-cols-3 gap-4">
{items.map(p => (
<div key={p.id} className="bg-white rounded-xl shadow p-4">
{p.img && <img src={p.img} alt="proyecto" className="rounded mb-2 max-h-40 w-full object-cover"/>}
<h3 className="font-bold">{p.tit}</h3>
<p className="text-sm text-gray-600">{p.descr}</p>
</div>
))}
</div>
</div>
</div>
);
}