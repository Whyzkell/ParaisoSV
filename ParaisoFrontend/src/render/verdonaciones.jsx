import React, { useEffect, useState } from "react";
import { donacionesApi } from "../services/api";


export default function VerDonaciones() {
const [items, setItems] = useState([]);
const [alcanciaId, setAlcanciaId] = useState("");
const [montoMin, setMontoMin] = useState("");
const [montoMax, setMontoMax] = useState("");
const [error, setError] = useState("");


const load = () => donacionesApi.list({ alcanciaId, montoMin, montoMax }).then(setItems).catch((e)=>setError(e.message));
useEffect(load, []);


return (
<div className="min-h-screen p-6 bg-[#F3EFD2]">
<div className="max-w-5xl mx-auto">
<div className="grid grid-cols-1 md:grid-cols-5 gap-2 mb-4">
<input className="p-3 border rounded" placeholder="Alcancía ID" value={alcanciaId} onChange={(e)=>setAlcanciaId(e.target.value)} />
<input className="p-3 border rounded" type="number" step="0.01" placeholder="Monto min" value={montoMin} onChange={(e)=>setMontoMin(e.target.value)} />
<input className="p-3 border rounded" type="number" step="0.01" placeholder="Monto max" value={montoMax} onChange={(e)=>setMontoMax(e.target.value)} />
<button onClick={load} className="px-4 bg-black text-white rounded">Filtrar</button>
<button onClick={()=>{setAlcanciaId("");setMontoMin("");setMontoMax("");load();}} className="px-4 bg-gray-700 text-white rounded">Limpiar</button>
</div>
{error && <p className="text-red-600 mb-2">{error}</p>}
<table className="w-full bg-white rounded-xl shadow overflow-hidden">
<thead>
<tr className="bg-gray-100 text-left">
<th className="p-3">ID</th>
<th className="p-3">Alcancía</th>
<th className="p-3">Usuario</th>
<th className="p-3">Monto</th>
<th className="p-3">Fecha</th>
</tr>
</thead>
<tbody>
{items.map(d => (
<tr key={d.id} className="border-t">
<td className="p-3">{d.id}</td>
<td className="p-3">{d.alcancia?.id}</td>
<td className="p-3">{d.usuario?.correo || d.usuario?.nombre || d.usuario?.id}</td>
<td className="p-3">{d.cantidadDonada}</td>
<td className="p-3">{d.fecha}</td>
</tr>
))}
</tbody>
</table>
</div>
</div>
);
}