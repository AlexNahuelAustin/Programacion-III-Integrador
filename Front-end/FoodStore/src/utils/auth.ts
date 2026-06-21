import type { IUser } from "../types/IUser";
import type { Rol } from "../types/Rol";
import { getUSer, removeUser } from "./localStorage";
import { navigate } from "./navigate";

// Verificar que el usuario tenga el rol requerido para acceder
export const checkAuhtUser = (
  redireccion1: string,
  redireccion2: string,
  rol: Rol
) => {
  const user = getUSer();
  if (!user) {
    navigate(redireccion1);
    return;
  }
  const parserUser = JSON.parse(user) as IUser;

  if (parserUser.rol !== rol) {
    if (parserUser.rol === "USUARIO" && rol === "ADMIN") {
      alert("Acceso denegado. Solo administradores pueden acceder.");
    }
    if (parserUser.rol !== "ADMIN") {
      navigate(redireccion2);
    }
    return;
  }
};

// Cerrar sesión: guardar carrito por usuario y limpiar datos
export const cerrarSesion = () => {
  const userString = getUSer();
  if (userString) {
    const usuario = JSON.parse(userString) as IUser;
    const carrito = localStorage.getItem("carrito");
    if (carrito) {
      localStorage.setItem(`carrito_${usuario.id}`, carrito);
    }
  }
  localStorage.removeItem("carrito");
  removeUser();
  navigate("/src/pages/auth/login/login.html");
};