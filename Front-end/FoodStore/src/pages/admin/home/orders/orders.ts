import type { IPedido } from "../../../../types/IPedido";
import type { IUser } from "../../../../types/IUser";
import { cerrarSesion, checkAuhtUser } from "../../../../utils/auth";
import { obtenerPedidos } from "../../../../utils/dataService";
const pedidosData = await obtenerPedidos();

checkAuhtUser("/src/pages/auth/login/login.html", "/src/pages/store/home/home.html", "ADMIN");

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

// Renderizar filas de pedidos
const renderizarTarjetas = () => {
  const tbody = document.getElementById("tbody-pedidos");
  if (!tbody) return;

  tbody.innerHTML = "";

  pedidosData.forEach((pedido: IPedido) => {
    const tr = document.createElement("tr");
    tr.innerHTML = `
      <td>${pedido.id}</td>
      <td>${pedido.usuarioDto.nombre} ${pedido.usuarioDto.apellido}</td>
      <td>$${pedido.total.toLocaleString("es-AR")}</td>
      <td>${pedido.estado}</td>
      <td>${pedido.fecha}</td>
    `;
    tbody.appendChild(tr);
  });
};

// Event listeners
document.getElementById("logoutButton")?.addEventListener("click", cerrarSesion);

// Inicializar
actualizarHeader();
renderizarTarjetas();