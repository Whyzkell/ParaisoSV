import React, { useState } from "react";
import { uploadFile, perrosApi } from "../services/api";


export default function AgregarMascota() {
const [nombre, setNombre] = useState("");
const [raza, setRaza] = useState("");
const [edad, setEdad] = useState(0);
const [descr, setDescr] = useState("");
const [file, setFile] = useState(null);
const [preview, setPreview] = useState(null);
const [ok, setOk] = useState("");
const [error, setError] = useState("");


const onFile = (e) => {
const f = e.target.files?.[0];
setFile(f || null);
setPreview(f ? URL.createObjectURL(f) : null);
};


const onSubmit = async (e) => {
e.preventDefault(); setOk(""); setError("");
try {
let url = "";
if (file) {
const up = await uploadFile(file); // { url: "http://localhost:8081/files/xxx.jpg" }
url = up.url;
}
await perrosApi.create({ nombre, raza, edad: Number(edad), descr, img: url });
setOk("Mascota guardada");
setNombre(""); setRaza(""); setEdad(0); setDescr(""); setFile(null); setPreview(null);
} catch (err) { setError(err.message); }
};


return (
<div className="min-h-screen grid place-items-center bg-[#F3EFD2] p-6">
<form onSubmit={onSubmit} className="w-full max-w-xl bg-white p-6 rounded-xl shadow space-y-4">
<h1 className="text-2xl font-bold">Agregar Mascota</h1>
{ok && <p className="text-green-700">{ok}</p>}
{error && <p className="text-red-600">{error}</p>}
<div>
<label className="block mb-2">Nombre</label>
<input className="w-full p-3 border rounded" value={nombre} onChange={(e)=>setNombre(e.target.value)} required/>
</div>
<div>
<label className="block mb-2">Raza</label>
<input className="w-full p-3 border rounded" value={raza} onChange={(e)=>setRaza(e.target.value)} required/>
</div>
<div>
<label className="block mb-2">Edad</label>
<input className="w-full p-3 border rounded" type="number" min="0" max="30" value={edad} onChange={(e)=>setEdad(e.target.value)} required/>
</div>
<div>
<label className="block mb-2">Descripción</label>
<textarea className="w-full p-3 border rounded" value={descr} onChange={(e)=>setDescr(e.target.value)} required/>
</div>
<div>
<label className="block mb-2">Imagen</label>
<input type="file" accept="image/*" onChange={onFile} />
{preview && <img alt="preview" src={preview} className="mt-2 max-h-48 rounded" />}
</div>
<button className="w-full bg-black text-white p-3 rounded hover:opacity-90">Guardar</button>
</form>
</div>
);
}