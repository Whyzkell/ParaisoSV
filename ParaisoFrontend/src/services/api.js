const API_BASE_URL = 'http://localhost:3001/api';

export async function apiPost(ruta, datos) {
  try {
    const res = await fetch(`${API_BASE_URL}/${ruta}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(datos),
    });

    const resultado = await res.json();

    if (!res.ok) {
      throw new Error(resultado.error || 'Error en la petición');
    }

    return resultado;
  } catch (error) {
    console.error('Error en apiPost:', error.message);
    throw error;
  }
}

export async function apiGet(ruta) {
    try {
      const res = await fetch(`${API_BASE_URL}/${ruta}`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        },
      });
  
      const resultado = await res.json();
  
      if (!res.ok) {
        throw new Error(resultado.error || 'Error en la petición GET');
      }
  
      return resultado;
    } catch (error) {
      console.error('Error en apiGet:', error.message);
      throw error;
    }
  }