import type { ICategoria } from "../../../../types/ICategoria";
import type { IUser } from "../../../../types/IUser";
import { cerrarSesion, checkAuhtUser } from "../../../../utils/auth";
import { obtenerCategorias } from "../../../../utils/dataService";
const categoriasData = await obtenerCategorias();

checkAuhtUser("/src/pages/auth/login/login.html", "/src/pages/client/orders/orders.html", "ADMIN");

// Actualizar nombre usuario en header
const actualizarHeader = () => {
  const userData = localStorage.getItem("userData");
  if (userData) {
    const usuario: IUser = JSON.parse(userData);
    const nombreEl = document.getElementById("nombreUsuario");
    if (nombreEl)
      nombreEl.textContent = `${usuario.nombre} ${usuario.apellido}`;
  }
};

// Renderizar tabla
const renderizarTabla = () => {
  const tbody = document.getElementById("tbody-categorias");
  if (!tbody) return;

  tbody.innerHTML = "";

  categoriasData.forEach((categoria: ICategoria) => {
    const tr = document.createElement("tr");
    tr.innerHTML = `
      <td>${categoria.id}</td>
      <td>${categoria.nombre}</td>
      <td>${categoria.descripcion}</td>
    `;
    tbody.appendChild(tr);
  });
};

// Event listeners
document.getElementById("logoutButton")?.addEventListener("click", cerrarSesion);

// Inicializar
actualizarHeader();
renderizarTabla();