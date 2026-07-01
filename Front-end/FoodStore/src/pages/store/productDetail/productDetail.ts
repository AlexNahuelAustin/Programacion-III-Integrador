import type { IProducto } from "../../../types/IProducto";
import type { IUser } from "../../../types/IUser";
import { obtenerProductos } from "../../../utils/dataService";
import { cerrarSesion, checkAuhtUser } from "../../../utils/auth";
checkAuhtUser("/src/pages/auth/login/login.html", "/src/pages/admin/home/adminHome/adminHome.html", "USUARIO");
const ProductoData = await obtenerProductos();

// Actualizar nombre usuario en header y mostrar admin si corresponde
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

// Obtener ID del producto de la URL
const params = new URLSearchParams(window.location.search);
const id = parseInt(params.get("id") || "0");

// Buscar producto por ID
const producto = ProductoData.find((p: IProducto) => p.id === id);

// Validar que el producto exista
if (!producto) {
  alert("Producto no encontrado");
  window.location.href = "/src/pages/store/home/home.html";
  throw new Error("Producto no existe");
}

// Renderizar detalles del producto
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

// Agregar event listeners a los botones
const agregarEventListeners = () => {
  const cantidadInput = document.getElementById("cantidad") as HTMLInputElement;
  const btnMas = document.getElementById("btnMas");
  const btnMenos = document.getElementById("btnMenos");
  const btnAgregar = document.getElementById("btnAgregar");

  // Botón aumentar cantidad
  btnMas?.addEventListener("click", () => {
    cantidadInput.value = String(parseInt(cantidadInput.value) + 1);
  });

  // Botón disminuir cantidad
  btnMenos?.addEventListener("click", () => {
    if (parseInt(cantidadInput.value) > 1) {
      cantidadInput.value = String(parseInt(cantidadInput.value) - 1);
    }
  });

  // Botón agregar al carrito
  btnAgregar?.addEventListener("click", () => {
    const cantidad = parseInt(cantidadInput.value);
    agregarAlCarrito(cantidad);
  });
};

// Agregar producto al carrito con cantidad especificada
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
};

// Actualizar contador de productos en header
const actualizarContador = () => {
  const carrito = JSON.parse(localStorage.getItem("carrito") || "[]");
  const total = carrito.reduce((acc: number, item: { cantidad: number }) => acc + item.cantidad, 0);
  const contador = document.getElementById("carritoContador");
  if (contador) contador.textContent = `${total}`;
};

// Inicializar
document.getElementById("logoutButton")?.addEventListener("click", cerrarSesion);
actualizarHeader();
renderizarProducto();