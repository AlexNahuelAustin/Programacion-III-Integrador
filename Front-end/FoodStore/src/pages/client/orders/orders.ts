import type { IPedido } from "../../../types/IPedido";
import type { IUser } from "../../../types/IUser";
import { cerrarSesion, checkAuhtUser } from "../../../utils/auth";
import { obtenerPedidos } from "../../../utils/dataService";

// Proteger ruta: redirige si no está autenticado o no es USUARIO
checkAuhtUser("/src/pages/auth/login/login.html", "/src/pages/admin/home/adminHome/adminHome.html", "USUARIO");

let pedidosData: IPedido[] = [];

// Actualiza nombre usuario en header y recuenta items del carrito
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

// Actualiza contador de items en carrito desde localStorage
const actualizarCarrito = () => {
  const carrito = JSON.parse(localStorage.getItem("carrito") || "[]");
  const total = carrito.reduce((acc: number, item: { cantidad: number }) => acc + item.cantidad, 0);
  const contador = document.getElementById("carritoContador");
  if (contador) contador.textContent = `${total}`;
};

// Renderiza tarjetas de pedidos: combina JSON + localStorage por usuario
const renderizarTabla = () => {
  const contenedor = document.getElementById("tabla-pedidos");
  if (!contenedor) return;

  const userData = localStorage.getItem("userData");
  if (!userData) return;

  const usuario: IUser = JSON.parse(userData);

  // Filtrar pedidos del JSON por usuario actual
  const pedidosJson = pedidosData.filter(p => Number(p.usuarioDto.id) === Number(usuario.id));
  // Obtener pedidos guardados en localStorage del usuario
  const pedidosLocal: IPedido[] = JSON.parse(localStorage.getItem(`pedidos_${usuario.id}`) || "[]");
  // Combinar ambas fuentes
  const misPedidos = [...pedidosJson, ...pedidosLocal];

  let html = '<div class="pedidos-lista">';

  // Mostrar mensaje si no hay pedidos
  if (misPedidos.length === 0) {
    html += '<p class="sin-pedidos">No tienes pedidos</p>';
  } else {
    // Renderizar cada pedido con detalles (productos, fecha, estado, total)
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

// Inicializa: carga pedidos desde JSON e inicializa UI
const init = async () => {
  pedidosData = await obtenerPedidos();
  actualizarHeader();
  renderizarTabla();
};

// Event listeners
document.getElementById("logoutButton")?.addEventListener("click", cerrarSesion);

// Ejecutar al cargar página
init();