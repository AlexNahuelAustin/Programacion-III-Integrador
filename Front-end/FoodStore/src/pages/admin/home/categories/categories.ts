import type { ICategoria } from "../../../../types/ICategoria";
import type { IUser } from "../../../../types/IUser";
import { cerrarSesion } from "../../../../utils/auth";
import categoriasData from "../../../../data/categorias.json";

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

// Renderizar tarjetas de categorias
const renderizarTarjetas = () => {
  const contenedor = document.getElementById("tabla-categorias");
  if (!contenedor) return;

  let html = '<div class="cards-grid">';

  categoriasData.forEach((categoria: ICategoria) => {
    html += `
      <div class="card">
        <h3>${categoria.nombre}</h3>
        <p>${categoria.descripcion}</p>
      </div>
    `;
  });

  html += '</div>';
  contenedor.innerHTML = html;
};

// Event listeners
document.getElementById("logoutButton")?.addEventListener("click", cerrarSesion);

// Inicializar
actualizarHeader();
renderizarTarjetas();