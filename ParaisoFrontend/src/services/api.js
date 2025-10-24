// src/services/api.js

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL ?? "http://localhost:8081";

// ===== Token helpers =====
export function getToken() {
  return localStorage.getItem("token");
}
export function setToken(token) {
  if (token) localStorage.setItem("token", token);
}
export function clearToken() {
  localStorage.removeItem("token");
}
export function isLoggedIn() {
  return !!getToken();
}

// ===== JWT helpers =====
function decodeJwt(token) {
  try {
    const p = token.split(".")[1];
    const json = atob(p.replace(/-/g, "+").replace(/_/g, "/"));
    return JSON.parse(json);
  } catch {
    return null;
  }
}

export function getAuthInfo() {
  const token = getToken();
  if (!token) return { token: null, role: null, email: null, isAdmin: false };
  const payload = decodeJwt(token);
  // el backend puede enviar "role":"ADMIN" o "ROLE_ADMIN" o authorities
  let role = (payload?.role || payload?.authorities?.[0] || "").toString();
  role = role.replace(/^ROLE_/i, "").toUpperCase();
  const email = payload?.sub || payload?.email || payload?.username || null;
  return { token, role, email, isAdmin: role === "ADMIN" };
}

// ===== Low-level request =====
async function request(path, { method = "GET", body, isForm = false } = {}) {
  const headers = {};
  const token = getToken();
  if (token) headers["Authorization"] = `Bearer ${token}`;
  if (!isForm) headers["Content-Type"] = "application/json";

  const res = await fetch(`${API_BASE_URL}${path}`, {
    method,
    headers,
    body: isForm ? body : body ? JSON.stringify(body) : undefined,
  });

  let data = null;
  try {
    data = await res.json();
  } catch (e) {}

  if (!res.ok) {
    const msg = data?.message || data?.error || `${res.status} ${res.statusText}`;
    throw new Error(msg);
  }
  return data;
}

// ===== Auth =====
export async function login({ correo, password }) {
  const data = await request("/auth/login", { method: "POST", body: { correo, password } });
  if (data?.token) setToken(data.token);
  return data;
}

export async function register({ nombre, correo, password, rol }) {
  // si tu backend expone /auth/register público
  return request("/auth/register", { method: "POST", body: { nombre, correo, password, rol } });
}

export function logout() {
  clearToken();
}

// ===== Usuarios (solo ADMIN) =====
export const usuariosApi = {
  create: (dto) => request("/api/usuarios", { method: "POST", body: dto }),
  list: () => request("/api/usuarios"),
  get: (id) => request(`/api/usuarios/${id}`),
  update: (id, dto) => request(`/api/usuarios/${id}`, { method: "PUT", body: dto }),
  remove: (id) => request(`/api/usuarios/${id}`, { method: "DELETE" }),
};

// ===== Alcancías =====
export const alcanciasApi = {
  create: ({ descr, precioMeta }) => request("/api/alcancias", { method: "POST", body: { descr, precioMeta } }),
  update: (id, dto) => request(`/api/alcancias/${id}`, { method: "PUT", body: dto }),
  list: (q) => request(q ? `/api/alcancias?q=${encodeURIComponent(q)}` : "/api/alcancias"),
  get: (id) => request(`/api/alcancias/${id}`),
  resumen: (id) => request(`/api/alcancias/${id}/resumen`),
};

// ===== Donaciones =====
export const donacionesApi = {
  create: ({ idAlcancia, cantidad }) =>
    request("/api/donaciones", { method: "POST", body: { idAlcancia, cantidad } }),
  list: (params = {}) => {
    const qs = new URLSearchParams();
    for (const [k, v] of Object.entries(params)) if (v != null && v !== "") qs.append(k, v);
    const suf = qs.toString() ? `?${qs.toString()}` : "";
    return request(`/api/donaciones${suf}`);
  },
};

// ===== Perros =====
export const perrosApi = {
  create: ({ nombre, raza, edad, descr, img }) =>
    request("/api/perros", { method: "POST", body: { nombre, raza, edad, descr, img } }),
  update: (id, dto) => request(`/api/perros/${id}`, { method: "PUT", body: dto }),
  list: (params = {}) => {
    const qs = new URLSearchParams();
    for (const [k, v] of Object.entries(params)) if (v != null && v !== "") qs.append(k, v);
    const suf = qs.toString() ? `?${qs.toString()}` : "";
    return request(`/api/perros${suf}`);
  },
  get: (id) => request(`/api/perros/${id}`),
};

// ===== Proyectos =====
export const proyectosApi = {
  create: ({ tit, descr, img }) => request("/api/proyectos", { method: "POST", body: { tit, descr, img } }),
  update: (id, dto) => request(`/api/proyectos/${id}`, { method: "PUT", body: dto }),
  list: (q) => request(q ? `/api/proyectos?q=${encodeURIComponent(q)}` : "/api/proyectos"),
  get: (id) => request(`/api/proyectos/${id}`),
};

// ===== Files (upload) =====
export async function uploadFile(file) {
  const fd = new FormData();
  fd.append("file", file);
  return request("/api/files", { method: "POST", body: fd, isForm: true });
}
