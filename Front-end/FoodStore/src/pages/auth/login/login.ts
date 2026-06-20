import type { IUser } from "../../../types/IUser";
import type { Rol } from "../../../types/Rol";
import { navigate } from "../../../utils/navigate";

const formulario = document.getElementById("login") as HTMLFormElement;
const inputEmail = document.getElementById("email") as HTMLInputElement;
const inputContraseña = document.getElementById("password") as HTMLInputElement;

formulario?.addEventListener("submit", (e: SubmitEvent) => {
  e.preventDefault();
  e.stopPropagation();

  const email = inputEmail.value.trim();
  const password = inputContraseña.value.trim();

  /// Validar que no este vacio
  if (!email || !password) {
    alert("Por favor completa todos los campos");
    return;
  }

  // Obtenemos usuario del LocalStore
  const usersString = localStorage.getItem("Users");

  if (!usersString) {
    alert("No hay usuarios registrados.");
    return;
  }

  const users: IUser[] = JSON.parse(usersString);

  // 2. Buscar usuario por mail y contraseña
  const usuarioEncontrado = users.find(
    (u) => u.mail === email && u.password === password,
  );

  // 3. Validar que exista
  if (!usuarioEncontrado) {
    alert("Mail o contraseña incorrectos.");
    return;
  }

  // 4. Guardamos la sesion
  const sesion: IUser = {
    id: usuarioEncontrado.id,
    nombre: usuarioEncontrado.nombre,
    apellido: usuarioEncontrado.apellido,
    mail: usuarioEncontrado.mail,
    celular: usuarioEncontrado.celular,
    rol: usuarioEncontrado.rol,
  };

  localStorage.setItem("userData", JSON.stringify(sesion));

  /// 5. Redirreccion segun rol
  const ruta =
    usuarioEncontrado.rol === "ADMIN"
      ? "/src/pages/admin/home/home.html"
      : "/src/pages/client/home/home.html";

      navigate(ruta);
});
