import type { IUser } from "./types/IUser";
import { obtenerUsuarios } from "./utils/dataService";
const usuariosData = await obtenerUsuarios();

// Cargar usuarios del JSON al localStorage si no existen
const cargarUsuarios = () => {
  if (!localStorage.getItem("Users")) {
    localStorage.setItem("Users", JSON.stringify(usuariosData));
  }
};

// Proteger rutas según rol y sesión
const protegerRuta = (): void => {
  const rutaActual = window.location.pathname;
  const userDataString = localStorage.getItem("userData");

  // Si está en login o registro no proteger
  if (rutaActual.includes("/auth/")) return;

  const usuario = userDataString 
    ? JSON.parse(userDataString) as IUser 
    : null;

  // Si no hay sesión, redirigir al login
  if (!usuario) {
    window.location.href = "/src/pages/auth/login/login.html";
    return;
  }

  // Bloquear USUARIO de rutas admin
  if (rutaActual.includes("/admin/") && usuario.rol === "USUARIO") {
    alert("Acceso denegado.");
    window.location.href = "/src/pages/client/orders/orders.html";
    return;
  }

  // Bloquear ADMIN de rutas cliente
  if (rutaActual.includes("/client/") && usuario.rol === "ADMIN") {
    window.location.href = "/src/pages/admin/home/adminHome/adminHome.html";
    return;
  }

  // Redirigir desde la raíz según rol
  if (rutaActual === "/" || rutaActual === "/index.html") {
    const ruta = usuario.rol === "ADMIN"
      ? "/src/pages/admin/home/adminHome/adminHome.html"
      : "/src/pages/store/home/home.html";
    window.location.href = ruta;
    return;
  }
};

// Inicializar
cargarUsuarios();
protegerRuta();