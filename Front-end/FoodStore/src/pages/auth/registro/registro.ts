import type { IUser } from "../../../types/IUser";

// Seleccionar elementos
const formulario = document.getElementById(
  "registro",
) as HTMLFormElement;
const mensaje = document.getElementById("mensaje") as HTMLDivElement;

// Evento de envío del formulario
formulario?.addEventListener("submit", (event: Event) => {
  event.preventDefault();

  // Obtener valores del formulario
  const formData = new FormData(formulario);
  const email = formData.get("email") as string;
  const password = formData.get("password") as string;

  // Crear usuario
  const nuevoUsuario: IUser = {
    email,
    password,
    loggedIn: false,
    role: "client",
  };

  // obtener usuarios
  const users = JSON.parse(localStorage.getItem("Users") || "[]") as IUser[];

  //Verificar si el email ya existe
  const usuarioExistente = users.find((user) => user.email === email);

  if (usuarioExistente) {
    mensaje.textContent = "El email ya está registrado.";
     mensaje.style.color = "red";
     return;
    } 
    // Agregar nuevo usuario a la lista
    users.push(nuevoUsuario);
    localStorage.setItem("Users",JSON.stringify(users));
    mensaje.textContent ="Regristro exitoso. Ahora puedes iniciar sesión.";
    mensaje.style.color = "green";

    setTimeout(() => {
        window.location.href = "../login/login.html";
    }, 2500);
  
});
