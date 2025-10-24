// src/auth/AuthContext.jsx
import React, { createContext, useContext, useEffect, useState } from 'react';
import { getAuthInfo, setToken as saveToken, clearToken } from '../services/api';

const AuthCtx = createContext(null);

export function AuthProvider({ children }){
  const [auth, setAuth] = useState(getAuthInfo());

  useEffect(()=>{
    const onStorage = (e)=>{ if(e.key==='token') setAuth(getAuthInfo()); };
    window.addEventListener('storage', onStorage);
    return ()=>window.removeEventListener('storage', onStorage);
  },[]);

  const value = {
    ...auth,
    refresh(){ setAuth(getAuthInfo()); },
    setToken(t){ saveToken(t); setAuth(getAuthInfo()); },
    logout(){ clearToken(); setAuth(getAuthInfo()); },
  };

  return <AuthCtx.Provider value={value}>{children}</AuthCtx.Provider>;
}

export function useAuth(){ return useContext(AuthCtx); }
