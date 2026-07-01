import type { IUser } from "../../../types/IUser";
import { navigate } from "../../../utils/navigate";

const formulario = document.getElementById("login") as HTMLFormElement;
const inputEmail = document.getElementById("email") as HTMLInputElement;
const inputContraseña = document.getElementById("password") as HTMLInputElement;

// Formulario: valida credenciales, guarda sesión y redirige
formulario?.addEventListener("submit", (e: SubmitEvent) => {
  e.preventDefault();
  e.stopPropagation();

  const email = inputEmail.value.trim();
  const password = inputContraseña.value.trim();

  // Validar campos requeridos
  if (!email || !password) {
    alert("Por favor completa todos los campos");
    return;
  }

  // Obtener lista de usuarios desde localStorage
  const usersString = localStorage.getItem("Users");

  if (!usersString) {
    alert("No hay usuarios registrados.");
    return;
  }

  const users: IUser[] = JSON.parse(usersString);

  // Buscar usuario por email y password
  const usuarioEncontrado = users.find(
    (u) => u.mail === email && u.password === password,
  );

  // Validar credenciales
  if (!usuarioEncontrado) {
    alert("Mail o contraseña incorrectos.");
    return;
  }

  // Crear sesión sin guardar password
  const sesion: IUser = {
    id: usuarioEncontrado.id,
    nombre: usuarioEncontrado.nombre,
    apellido: usuarioEncontrado.apellido,
    mail: usuarioEncontrado.mail,
    celular: usuarioEncontrado.celular,
    rol: usuarioEncontrado.rol,
  };

  localStorage.setItem("userData", JSON.stringify(sesion));

  // Cargar carrito previo del usuario o limpiar
  const carritoGuardado = localStorage.getItem(`carrito_${usuarioEncontrado.id}`);
  if (carritoGuardado) {
    localStorage.setItem("carrito", carritoGuardado);
  } else {
    localStorage.removeItem("carrito");
  }

  // Redirigir según rol: ADMIN → panel, USUARIO → tienda
  const ruta =
    usuarioEncontrado.rol === "ADMIN"
      ? "/src/pages/admin/home/adminHome/adminHome.html"
      : "/src/pages/store/home/home.html";
  navigate(ruta);
});