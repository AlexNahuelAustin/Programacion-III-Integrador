import type { IPedido } from "../../../types/IPedido";
import type { IUser } from "../../../types/IUser";
import { cerrarSesion, checkAuhtUser } from "../../../utils/auth";
import pedidosData from "../../../data/pedidos.json";

// Verificar que el usuario sea USUARIO (no ADMIN)
checkAuhtUser("/src/pages/auth/login/login.html", "/src/pages/store/home/home.html", "USUARIO");

// Actualizar nombre usuario y carrito en header
const actualizarHeader = () => {
  const userData = localStorage.getItem("userData");
  if (userData) {
    const usuario: IUser = JSON.parse(userData);
    const nombreEl = document.getElementById("nombreUsuario");
    if (nombreEl)
      nombreEl.textContent = `${usuario.nombre} ${usuario.apellido}`;
  }
  actualizarCarrito();
};

// Actualizar contador de productos en carrito
const actualizarCarrito = () => {
  const carrito = JSON.parse(localStorage.getItem("carrito") || "[]");
  const total = carrito.reduce((acc: number, item: { cantidad: number }) => acc + item.cantidad, 0);
  const contador = document.getElementById("carritoContador");
  if (contador) contador.textContent = `${total}`;
};

// Renderizar tarjetas de pedidos
const renderizarTabla = () => {
  const contenedor = document.getElementById("tabla-pedidos");
  if (!contenedor) return;

  const userData = localStorage.getItem("userData");
  if (!userData) return;

  const usuario: IUser = JSON.parse(userData);

  // Obtener pedidos del JSON + localStorage
  const pedidosJson = (pedidosData as IPedido[]).filter(p => Number(p.usuarioDto.id) === Number(usuario.id));
  const pedidosLocal: IPedido[] = JSON.parse(localStorage.getItem(`pedidos_${usuario.id}`) || "[]");
  const misPedidos = [...pedidosJson, ...pedidosLocal];

  let html = '<div class="cards-grid">';

  // Si no hay pedidos, mostrar mensaje
  if (misPedidos.length === 0) {
    html += '<p>No tienes pedidos</p>';
  } else {
    // Renderizar cada pedido con sus productos
    misPedidos.forEach((pedido: IPedido) => {
      const productosHtml = pedido.detalles.map(detalle => `
        <div class="pedido-detalle">
          <img src="${detalle.producto.imagen}" alt="${detalle.producto.nombre}">
          <div>
            <p><strong>${detalle.producto.nombre}</strong></p>
            <p>Cantidad: ${detalle.cantidad}</p>
            <p>Subtotal: $${detalle.subtotal.toLocaleString("es-AR")}</p>
          </div>
        </div>
      `).join("");

      html += `
        <div class="card">
          <h3>Pedido #${pedido.id}</h3>
          <p><strong>Fecha:</strong> ${pedido.fecha}</p>
          <p><strong>Estado:</strong> ${pedido.estado}</p>
          <p><strong>Forma de Pago:</strong> ${pedido.formaPago}</p>
          <div class="pedido-productos">${productosHtml}</div>
          <p class="pedido-total"><strong>Total: $${pedido.total.toLocaleString("es-AR")}</strong></p>
        </div>
      `;
    });
  }

  html += '</div>';
  contenedor.innerHTML = html;
};

// Event listeners
document.getElementById("logoutButton")?.addEventListener("click", cerrarSesion);

// Inicializar
actualizarHeader();
renderizarTabla();