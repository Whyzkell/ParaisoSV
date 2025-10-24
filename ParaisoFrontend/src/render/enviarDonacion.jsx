import React, { useEffect, useState } from "react";
import { alcanciasApi, donacionesApi } from "../services/api";


export default function EnviarDonacion() {
const [alcancias, setAlcancias] = useState([]);
const [idAlcancia, setIdAlcancia] = useState("");
const [cantidad, setCantidad] = useState("");
const [ok, setOk] = useState("");
const [error, setError] = useState("");


useEffect(() => {
alcanciasApi.list().then(setAlcancias).catch((e)=>setError(e.message));
}, []);


const onSubmit = async (e) => {
e.preventDefault();
setOk(""); setError("");
try {
await donacionesApi.create({ idAlcancia: Number(idAlcancia), cantidad: Number(cantidad) });
setOk("¡Gracias por tu donación!");
setCantidad("");
} catch (err) { setError(err.message); }
};


return (
<div className="min-h-screen grid place-items-center bg-[#F3EFD2] p-6">
<form onSubmit={onSubmit} className="w-full max-w-lg bg-white p-6 rounded-xl shadow">
<h1 className="text-2xl font-bold mb-4">Enviar Donación</h1>
{ok && <p className="text-green-700 mb-2">{ok}</p>}
{error && <p className="text-red-600 mb-2">{error}</p>}
<label className="block mb-2">Alcancía</label>
<select className="w-full mb-4 p-3 border rounded" value={idAlcancia} onChange={(e)=>setIdAlcancia(e.target.value)} required>
<option value="" disabled>Seleccione</option>
{alcancias.map(a => (
<option key={a.id} value={a.id}>{a.descr}</option>
))}
</select>
<label className="block mb-2">Monto</label>
<input className="w-full mb-6 p-3 border rounded" type="number" step="0.01" value={cantidad} onChange={(e)=>setCantidad(e.target.value)} required/>
<button className="w-full bg-black text-white p-3 rounded hover:opacity-90">Donar</button>
</form>
</div>
);
}