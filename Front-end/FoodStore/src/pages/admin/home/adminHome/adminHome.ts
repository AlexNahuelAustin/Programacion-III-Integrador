import type { IUser } from "../../../../types/IUser";
import { cerrarSesion, checkAuhtUser } from "../../../../utils/auth";
import { obtenerProductos, obtenerCategorias, obtenerPedidos } from "../../../../utils/dataService";

checkAuhtUser("/src/pages/auth/login/login.html", "/src/pages/store/home/home.html", "ADMIN");

// Fetch inicial de datos desde JSON locales
const ProductosData = await obtenerProductos();
const CategoriasData = await obtenerCategorias();
const PedidosData = await obtenerPedidos();

// Actualiza el nombre del usuario en el header desde localStorage
const actualizarHeader = () => {
  const userData = localStorage.getItem("userData");
  if (userData) {
    const usuario: IUser = JSON.parse(userData);
    const nombreEl = document.getElementById("nombreUsuario");
    if (nombreEl) nombreEl.textContent = `${usuario.nombre} ${usuario.apellido}`;
  }
};

// Carga estadísticas: totales de categorías, productos, pedidos y productos disponibles
const cargarEstadisticas = () => {
  const totalCategorias = CategoriasData.length;
  const totalProductos = ProductosData.length;
  const totalPedidos = PedidosData.length;
  const productosDisponibles = ProductosData.filter((p) => p.disponible).length;

  const countCategorias = document.getElementById("count-categorias");
  const countProductos = document.getElementById("count-productos");
  const countPedidos = document.getElementById("count-pedidos");
  const countDisponibles = document.getElementById("count-disponibles");

  if (countCategorias) countCategorias.textContent = `${totalCategorias}`;
  if (countProductos) countProductos.textContent = `${totalProductos}`;
  if (countPedidos) countPedidos.textContent = `${totalPedidos}`;
  if (countDisponibles) countDisponibles.textContent = `${productosDisponibles}`;

  const resumenTexto = document.getElementById("resumen-texto");
  if (resumenTexto) {
    resumenTexto.innerHTML = `
      <p>Total de Categorías: <strong>${totalCategorias}</strong></p>
      <p>Total de Productos: <strong>${totalProductos}</strong></p>
      <p>Productos Disponibles: <strong>${productosDisponibles}</strong></p>
      <p>Total de Pedidos: <strong>${totalPedidos}</strong></p>
    `;
  }
};

// Oculta el carrito si el usuario es ADMIN (los admins no compran)
const ocultarCarrito = () => {
  const userData = localStorage.getItem("userData");
  if (userData) {
    const usuario: IUser = JSON.parse(userData);
    if (usuario.rol === "ADMIN") {
      const carritoLink = document.querySelector<HTMLElement>(".carrito-link");
      if (carritoLink) carritoLink.style.display = "none";
    }
  }
};

// Event listeners e inicialización
document.getElementById("logoutButton")?.addEventListener("click", cerrarSesion);
actualizarHeader();
cargarEstadisticas();
ocultarCarrito();