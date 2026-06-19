import type { IUser } from "../../../types/IUser";
import type { Rol } from "../../../types/Rol";
import { navigate } from "../../../utils/navigate";

const formulario = document.getElementById("login") as HTMLFormElement;
const inputEmail = document.getElementById("email") as HTMLInputElement;
const inputContraseña = document.getElementById("password") as HTMLInputElement;
const selectRol = document.getElementById("rol") as HTMLSelectElement;

formulario?.addEventListener("submit", (e: SubmitEvent) => {
  e.preventDefault();
  e.stopPropagation();
  
  const email = inputEmail.value;
  const password = inputContraseña.value;
  const rol = selectRol.value as Rol;

  // 1. Obtener usuarios del localStorage
  const usersString = localStorage.getItem("Users");

  if (!usersString) {
    alert("No hay usuarios registrados. Por favor, regístrate primero.");
    return;
  }

  const Users: IUser[] = JSON.parse(usersString);

  // 2. Buscar usuario que coincida con email y password
  const usuarioEncontrado = Users.find(
    (u) => u.email === email && u.password === password
  );

  // 3. Validar si existe el usuario
  if (!usuarioEncontrado) {
    alert("Credenciales incorrectas. Inténtalo de nuevo.");
    return;
  }

  // 4. Iniciar sesión (Guardar en userData)
  const sesion: IUser = {
    email: usuarioEncontrado.email,
    password: usuarioEncontrado.password,
    loggedIn: false,
    role: rol
  };

  localStorage.setItem("userData", JSON.stringify(sesion));

  // 5. Redireccionar según el rol
  const ruta = rol === "admin" 
    ? "/src/pages/admin/home/home.html" 
    : "/src/pages/client/home/home.html";
  
  window.location.href = ruta;
});