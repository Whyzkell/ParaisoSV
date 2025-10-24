// src/render/RequireAuth.jsx
import React from 'react';
import { Navigate, Outlet, useLocation } from 'react-router-dom';
import { useAuth } from '../auth/authContext';

export default function RequireAuth(){
  const { token } = useAuth();
  const loc = useLocation();
  if (!token) return <Navigate to="/login" replace state={{ from: loc }} />;
  return <Outlet/>;
}
