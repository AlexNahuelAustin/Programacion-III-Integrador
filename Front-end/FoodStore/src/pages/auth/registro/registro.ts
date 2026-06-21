import type { IUser } from "../../../types/IUser";
import { navigate } from "../../../utils/navigate";

const formulario = document.getElementById("registro") as HTMLFormElement;
const mensaje = document.getElementById("mensaje") as HTMLDivElement;

// Manejar envío del formulario
formulario?.addEventListener("submit", (event: Event) => {
  event.preventDefault();

  // Obtener valores del formulario
  const nombre = (document.getElementById("nombre") as HTMLInputElement).value.trim();
  const apellido = (document.getElementById("apellido") as HTMLInputElement).value.trim();
  const mail = (document.getElementById("email") as HTMLInputElement).value.trim();
  const celular = (document.getElementById("celular") as HTMLInputElement).value.trim();
  const password = (document.getElementById("password") as HTMLInputElement).value.trim();

  // Validar que todos los campos estén completos
  if (!nombre || !apellido || !mail || !celular || !password) {
    mensaje.textContent = "Por favor completa todos los campos";
    mensaje.style.color = "red";
    return;
  }

  // Crear objeto usuario nuevo
  const nuevoUsuario: IUser = {
    id: Date.now(),
    nombre,
    apellido,
    mail,
    celular,
    password,
    rol: "USUARIO",
  };

  // Obtener usuarios existentes
  const users = JSON.parse(localStorage.getItem("Users") || "[]") as IUser[];

  // Verificar si el email ya está registrado
  const usuarioExistente = users.find((user) => user.mail === mail);

  if (usuarioExistente) {
    mensaje.textContent = "El email ya esta registrado.";
    mensaje.style.color = "red";
    return;
  }

  // Guardar nuevo usuario
  users.push(nuevoUsuario);
  localStorage.setItem("Users", JSON.stringify(users));
  mensaje.textContent = "Registro exitoso. Redirigiendo a login...";
  mensaje.style.color = "green";

  // Redirigir a login después de 2 segundos
  setTimeout(() => {
    navigate("/src/pages/auth/login/login.html");
  }, 2000);
});