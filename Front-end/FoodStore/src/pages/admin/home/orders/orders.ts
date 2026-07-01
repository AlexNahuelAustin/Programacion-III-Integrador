import type { IPedido } from "../../../../types/IPedido";
import type { IUser } from "../../../../types/IUser";
import { cerrarSesion, checkAuhtUser } from "../../../../utils/auth";
import { obtenerPedidos } from "../../../../utils/dataService";

const pedidosJson = await obtenerPedidos();

// Recolectar pedidos guardados en localStorage de todos los usuarios
const pedidosLocal: IPedido[] = [];
for (let i = 0; i < localStorage.length; i++) {
  const key = localStorage.key(i);
  if (key && key.startsWith("pedidos_")) {
    const ordenes: IPedido[] = JSON.parse(localStorage.getItem(key) || "[]");
    pedidosLocal.push(...ordenes);
  }
}

const pedidosData = [...pedidosJson, ...pedidosLocal];

// Proteger ruta: redirige si no está autenticado o no es ADMIN
checkAuhtUser("/src/pages/auth/login/login.html", "/src/pages/store/home/home.html", "ADMIN");

// Actualiza nombre usuario en header desde localStorage
const actualizarHeader = () => {
  const userData = localStorage.getItem("userData");
  if (userData) {
    const usuario: IUser = JSON.parse(userData);
    const nombreEl = document.getElementById("nombreUsuario");
    if (nombreEl)
      nombreEl.textContent = `${usuario.nombre} ${usuario.apellido}`;
  }
};

// Abre modal con detalles: cliente, productos, subtotales, total, forma de pago
const abrirModalPedido = (pedido: IPedido) => {
  const modal = document.getElementById("modal-pedido") as HTMLElement;
  const titulo = document.getElementById("modal-pedido-titulo") as HTMLElement;
  const info = document.getElementById("modal-pedido-info") as HTMLElement;

  titulo.textContent = `Detalle del Pedido #ORD-${pedido.id}`;

  // Mapea detalles del pedido en HTML (producto, cantidad, precio unitario, subtotal)
  const itemsHTML = pedido.detalles
    .map(
      (d) => `
      <div class="modal-pedido-item">
        <div class="modal-pedido-item-info">
          <span class="modal-pedido-item-nombre">${d.producto.nombre}</span>
          <span class="modal-pedido-item-cantidad">Cantidad: ${d.cantidad} × $${d.producto.precio.toLocaleString("es-AR")}</span>
        </div>
        <span class="modal-pedido-item-precio">$${d.subtotal.toLocaleString("es-AR")}</span>
      </div>`
    )
    .join("");

  // Construye el contenido del modal: datos cliente, productos, totales
  info.innerHTML = `
    <div class="modal-pedido-datos">
      <p><strong>Cliente:</strong> ${pedido.usuarioDto.nombre} ${pedido.usuarioDto.apellido}</p>
      <p><strong>Fecha:</strong> ${pedido.fecha}</p>
      <p><strong>Teléfono:</strong> ${pedido.usuarioDto.celular}</p>
      <p><strong>Método de pago:</strong> ${pedido.formaPago}</p>
      <p><strong>Estado:</strong> <span class="modal-pedido-estado">${pedido.estado}</span></p>
    </div>

    <div class="modal-pedido-productos">
      <h3>Productos:</h3>
      ${itemsHTML}
    </div>

    <div class="modal-pedido-totales">
      <div class="modal-pedido-fila-total">
        <span>Subtotal:</span>
        <span>$${pedido.detalles.reduce((acc, d) => acc + d.subtotal, 0).toLocaleString("es-AR")}</span>
      </div>
      <div class="modal-pedido-fila-total modal-pedido-fila-total--grande">
        <span>Total:</span>
        <span>$${pedido.total.toLocaleString("es-AR")}</span>
      </div>
    </div>
  `;

  modal.style.display = "flex";
};

// Llena tabla con pedidos (ID, usuario, total, estado, fecha) - click abre modal
const renderizarTarjetas = () => {
  const tbody = document.getElementById("tbody-pedidos");
  if (!tbody) return;

  tbody.innerHTML = "";

  pedidosData.forEach((pedido: IPedido) => {
    const tr = document.createElement("tr");
    tr.style.cursor = "pointer";
    tr.innerHTML = `
      <td>${pedido.id}</td>
      <td>${pedido.usuarioDto.nombre} ${pedido.usuarioDto.apellido}</td>
      <td>$${pedido.total.toLocaleString("es-AR")}</td>
      <td>${pedido.estado}</td>
      <td>${pedido.fecha}</td>
    `;
    tr.addEventListener("click", () => abrirModalPedido(pedido));
    tbody.appendChild(tr);
  });
};

// Cierra el modal
const cerrarModal = () => {
  const modal = document.getElementById("modal-pedido") as HTMLElement;
  modal.style.display = "none";
};

// Event listeners
document.getElementById("logoutButton")?.addEventListener("click", cerrarSesion);
document.getElementById("cerrar-modal-pedido")?.addEventListener("click", cerrarModal);
// Cierra modal si hace click afuera del contenido
document.getElementById("modal-pedido")?.addEventListener("click", (e) => {
  if (e.target === e.currentTarget) cerrarModal();
});

// Inicializar
actualizarHeader();
renderizarTarjetas();