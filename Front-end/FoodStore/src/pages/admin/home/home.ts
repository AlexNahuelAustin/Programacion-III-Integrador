import type { IUser } from "../../../types/IUser";
import { cerrarSesion } from "../../../utils/auth";

const actualizarHeader = () => {
  const userData = localStorage.getItem("userData");
  if (userData) {
    const usuario: IUser = JSON.parse(userData);
    const nombreEl = document.getElementById("nombreUsuario");
    if (nombreEl) nombreEl.textContent = `${usuario.nombre} ${usuario.apellido}`;
  }
};

document.getElementById("logoutButton")?.addEventListener("click", cerrarSesion);

actualizarHeader();