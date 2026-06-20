import type { IProducto } from "../../../types/IProducto";
import type { IUser } from "../../../types/IUser";
import { cerrarSesion } from "../../../utils/auth";
import ProductoData from "../../../data/productos.json";

// Header dinamico
const actualizarHeader = () => {
  const userData = localStorage.getItem("userData");
  if (userData) {
    const usuario: IUser = JSON.parse(userData);
    const nombreEl = document.getElementById("nombreUsuario");
    if (nombreEl)
      nombreEl.textContent = `${usuario.nombre} ${usuario.apellido}`;

    if (usuario.rol === "ADMIN") {
      document.getElementById("adminLink")?.style.removeProperty("display");
    }
  }
};

const params = new URLSearchParams(window.location.search);
const id = parseInt(params.get("id") || "0");

const producto = ProductoData.find((p) => p.id === id);

if (!producto) {
  alert("Producto no encontrado");
  window.location.href = "/src/pages/store/home/home.html";
  throw new Error("Producto no existe");
}

const renderizarProducto = () => {
  const contenedor = document.getElementById("contenedor-detail");
  if (!contenedor) return;

  contenedor.innerHTML = `
    <img src="${producto.imagen}" alt="${producto.nombre}">
    <div class="detail-info">
      <h1>${producto.nombre}</h1>
      <p>${producto.descripcion}</p>
      <p class="precio">$${producto.precio.toLocaleString("es-AR")}</p>
      <span class="${producto.disponible ? "disponible" : "no-disponible"}">
        ${producto.disponible ? "Disponible" : "No disponible"}
      </span>
      <div class="cantidad-selector">
        <button id="btnMenos">-</button>
        <input type="number" id="cantidad" value="1" min="1">
        <button id="btnMas">+</button>
      </div>
      <button id="btnAgregar">Agregar al Carrito</button>
    </div>
  `;

  agregarEventListeners();
};

const agregarEventListeners = () => {
  const cantidadInput = document.getElementById("cantidad") as HTMLInputElement;
  const btnMas = document.getElementById("btnMas");
  const btnMenos = document.getElementById("btnMenos");
  const btnAgregar = document.getElementById("btnAgregar");

  // Botón +
  btnMas?.addEventListener("click", () => {
    cantidadInput.value = String(parseInt(cantidadInput.value) + 1);
  });

  // Botón -
  btnMenos?.addEventListener("click", () => {
    if (parseInt(cantidadInput.value) > 1) {
      cantidadInput.value = String(parseInt(cantidadInput.value) - 1);
    }
  });

  // Botón Agregar
  btnAgregar?.addEventListener("click", () => {
    const cantidad = parseInt(cantidadInput.value);
    agregarAlCarrito(cantidad);
  });
};

// Agregar al carrito
const agregarAlCarrito = (cantidad: number) => {
  const carrito = JSON.parse(localStorage.getItem("carrito") || "[]");
  const existe = carrito.find((item: IProducto & { cantidad: number }) => item.id === producto.id);

  if (existe) {
    existe.cantidad += cantidad;
  } else {
    carrito.push({ ...producto, cantidad });
  }

  localStorage.setItem("carrito", JSON.stringify(carrito));
  actualizarContador();
  alert(`${producto.nombre} agregado al carrito`);
};

// Actualizar contador
const actualizarContador = () => {
  const carrito = JSON.parse(localStorage.getItem("carrito") || "[]");
  const total = carrito.reduce((acc: number, item: { cantidad: number }) => acc + item.cantidad, 0);
  const contador = document.getElementById("carritoContador");
  if (contador) contador.textContent = `${total}`;
};

// Inicializar
actualizarHeader();
renderizarProducto();
