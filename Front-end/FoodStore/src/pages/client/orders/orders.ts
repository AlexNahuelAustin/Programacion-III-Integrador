import type { IPedido } from "../../../types/IPedido";
import type { IUser } from "../../../types/IUser";
import { cerrarSesion, checkAuhtUser } from "../../../utils/auth";
import { obtenerPedidos } from "../../../utils/dataService";

checkAuhtUser("/src/pages/auth/login/login.html", "/src/pages/store/home/home.html", "USUARIO");

let pedidosData: IPedido[] = [];

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
  const pedidosJson = pedidosData.filter(p => Number(p.usuarioDto.id) === Number(usuario.id));
  const pedidosLocal: IPedido[] = JSON.parse(localStorage.getItem(`pedidos_${usuario.id}`) || "[]");
  const misPedidos = [...pedidosJson, ...pedidosLocal];

  let html = '<div class="pedidos-lista">';

  // Si no hay pedidos, mostrar mensaje
  if (misPedidos.length === 0) {
    html += '<p class="sin-pedidos">No tienes pedidos</p>';
  } else {
    // Renderizar cada pedido con sus productos
    misPedidos.forEach((pedido: IPedido) => {
      const productosTexto = pedido.detalles.map(detalle => 
        `• ${detalle.producto.nombre} (x${detalle.cantidad})`
      ).join("<br>");

      html += `
        <div class="pedido-item-horizontal">
          <div class="pedido-izq">
            <h3>Pedido #${pedido.id}</h3>
            <p class="pedido-meta">📅 ${pedido.fecha} | Estado: <span>${pedido.estado}</span> | Pago: ${pedido.formaPago}</p>
            <div class="pedido-detalles-lista">${productosTexto}</div>
          </div>
          <div class="pedido-der">
            <span class="pedido-monto-total">$${pedido.total.toLocaleString("es-AR")}</span>
          </div>
        </div>
      `;
    });
  }

  html += '</div>';
  contenedor.innerHTML = html;
};

// Inicializar async
const init = async () => {
  pedidosData = await obtenerPedidos();
  actualizarHeader();
  renderizarTabla();
};

// Event listeners
document.getElementById("logoutButton")?.addEventListener("click", cerrarSesion);

// Ejecutar
init();