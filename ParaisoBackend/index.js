const express = require('express');
const cors = require('cors');
const { poolPromise, sql } = require('./db');

const app = express();
const PORT = 3001;

app.use(cors());
app.use(express.json());

app.get('/api/saludo', (req, res) => {
  res.json({ mensaje: 'Hola desde el backend!' });
});

app.listen(PORT, () => {
  console.log(`Servidor backend corriendo en http://localhost:${PORT}`);
});

app.post('/api/registro', async (req, res) => {
    const { nombre, email, password } = req.body;
  
    if (!nombre || !email || !password) {
      return res.status(400).json({ error: "Todos los campos son obligatorios." });
    }
  
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(email)) {
      return res.status(400).json({ error: "Correo no válido." });
    }
  
    if (password.length < 8) {
      return res.status(400).json({ error: "La contraseña debe tener al menos 8 caracteres." });
    }
  
    try {
      const pool = await poolPromise;
  
      await pool.request()
        .input('Nombre', sql.NVarChar(100), nombre)
        .input('Correo', sql.NVarChar(150), email)
        .input('Password', sql.NVarChar(100), password)
        .execute('sp_RegistrarUsuario');
  
      res.status(200).json({ mensaje: "Registro exitoso" });
    } catch (err) {
      console.error("Error en registro:", err);
      res.status(500).json({ error: err.message });
    }
  });
  
  
  