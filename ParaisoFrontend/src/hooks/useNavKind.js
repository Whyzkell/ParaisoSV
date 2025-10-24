import { useEffect, useState } from "react";
import { useAuth } from "../context/AuthContext.jsx";

/* ---- helpers ---- */
function decodeJwtPayload(token) {
  try {
    const [, payloadB64Url] = token.split(".");
    if (!payloadB64Url) return null;
    let base64 = payloadB64Url.replace(/-/g, "+").replace(/_/g, "/");
    while (base64.length % 4 !== 0) base64 += "="; // padding
    const json = decodeURIComponent(
      atob(base64)
        .split("")
        .map((c) => "%" + ("00" + c.charCodeAt(0).toString(16)).slice(-2))
        .join("")
    );
    return JSON.parse(json);
  } catch {
    return null;
  }
}

function deriveNavKind(userLike) {
  if (!userLike) return "guest";

  // 1) localizar token
  let token;
  if (typeof userLike === "string") token = userLike;
  else token = userLike.token ?? userLike.access_token ?? userLike.jwt;

  // 2) si hay token, decodificar payload y chequear exp / role
  if (typeof token === "string") {
    const clean = token.startsWith("Bearer ") ? token.slice(7) : token;
    const payload = decodeJwtPayload(clean);
    if (payload) {
      // exp en segundos UNIX
      const now = Math.floor(Date.now() / 1000);
      if (typeof payload.exp === "number" && payload.exp < now) {
        return "guest";
      }
      const role =
        typeof payload.role === "string"
          ? payload.role
          : Array.isArray(payload.roles)
          ? payload.roles[0]
          : Array.isArray(payload.authorities)
          ? payload.authorities[0]
          : typeof payload.scope === "string"
          ? payload.scope.split(" ")[0]
          : undefined;

      return role === "ROLE_ADMIN" ? "admin" : "client";
    }
  }

  // 3) sin token decodificable: fallback por propiedad role del objeto
  const roleProp =
    typeof userLike?.role === "string" ? userLike.role : undefined;
  if (roleProp === "ROLE_ADMIN") return "admin";
  if (roleProp) return "client";

  return "guest";
}

function readUserFromLocalStorage() {
  const raw = localStorage.getItem("user");
  if (!raw) return null;
  try {
    return JSON.parse(raw);
  } catch {
    return raw; // era string (token)
  }
}

/* ---- hook ---- */
export default function useNavKind() {
  const { user } = useAuth?.() ?? {};
  const compute = () => deriveNavKind(user ?? readUserFromLocalStorage());

  const [navKind, setNavKind] = useState(compute);

  useEffect(() => {
    setNavKind(compute());
    // actualiza si cambia localStorage desde otra pestaña
    const onStorage = (e) => {
      if (e.key === "user") setNavKind(compute());
    };
    window.addEventListener("storage", onStorage);
    return () => window.removeEventListener("storage", onStorage);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [user]);

  return navKind; // 'admin' | 'client' | 'guest'
}
