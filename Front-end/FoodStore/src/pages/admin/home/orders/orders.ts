import type { IPedido } from "../../../../types/IPedido";
import type { IUser } from "../../../../types/IUser";
import { cerrarSesion } from "../../../../utils/auth";
import pedidosData from "../../../../data/pedidos.json";

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

// Renderizar tarjetas de pedidos
const renderizarTarjetas = () => {
  const contenedor = document.getElementById("tabla-pedidos");
  if (!contenedor) return;

  let html = '<div class="cards-grid">';

  pedidosData.forEach((pedido) => {
    html += `
      <div class="card">
        <h3>Pedido #${pedido.id}</h3>
        <p><strong>Fecha:</strong> ${pedido.fecha}</p>
        <p><strong>Usuario:</strong> ${pedido.usuarioDto.nombre} ${pedido.usuarioDto.apellido}</p>
        <p><strong>Total:</strong> $${pedido.total.toLocaleString("es-AR")}</p>
        <p><strong>Estado:</strong> ${pedido.estado}</p>
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