import type { IUser } from "../../../../types/IUser";
import { cerrarSesion } from "../../../../utils/auth";
import { obtenerProductos, obtenerCategorias, obtenerPedidos } from "../../../../utils/dataService";

const ProductosData = await obtenerProductos();
const CategoriasData = await obtenerCategorias();
const PedidosData = await obtenerPedidos();

const actualizarHeader = () => {
  const userData = localStorage.getItem("userData");
  if (userData) {
    const usuario: IUser = JSON.parse(userData);
    const nombreEl = document.getElementById("nombreUsuario");
    if (nombreEl) nombreEl.textContent = `${usuario.nombre} ${usuario.apellido}`;
  }
};

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

document.getElementById("logoutButton")?.addEventListener("click", cerrarSesion);
actualizarHeader();
cargarEstadisticas();
ocultarCarrito();
