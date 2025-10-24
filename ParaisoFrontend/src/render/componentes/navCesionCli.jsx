import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../../context/AuthContext.jsx";

export default function NavnoCCli() {
  const { logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate("/login", { replace: true }); // o navigate("/", { replace: true })
  };

  return (
    <header className="relative text-center py-8 px-8">
      <nav className="flex justify-between items-center w-10/12 mx-auto">
        <Link to="/">
          <h1 className="text-3xl font-bold text-orange-500">Paraiso SV</h1>
        </Link>

        <ul className="flex gap-6 text-lg">
          <li>
            <Link to="/visualizar-proyecto" className="hover:text-orange-500">
              Proyectos
            </Link>
          </li>
          <li>
            <Link to="/adoptar-mascota" className="hover:text-orange-500">
              Adoptar
            </Link>
          </li>
          <li>
            <Link to="/donaralcancia" className="hover:text-orange-500">
              Donar alcancia
            </Link>
          </li>
        </ul>

        <div className="flex gap-4">
          <button className="text-orange-400 text-2xl" onClick={handleLogout}>
            Log out
          </button>
        </div>
      </nav>
    </header>
  );
}
