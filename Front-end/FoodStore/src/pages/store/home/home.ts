import type { IProducto } from "../../../types/IProducto";
import type { ICategoria } from "../../../types/ICategoria";
import type { IUser } from "../../../types/IUser";
import { cerrarSesion } from "../../../utils/auth";
import { obtenerProductos, obtenerCategorias } from "../../../utils/dataService";
const productosData = await obtenerProductos();
const categoriasData = await obtenerCategorias();

// Actualizar nombre usuario en header y mostrar admin si corresponde
const actualizarHeader = () => {
  const userData = localStorage.getItem("userData");
  if (userData) {
    const usuario: IUser = JSON.parse(userData);
    const nombreEl = document.getElementById("nombreUsuario");
    if (nombreEl) nombreEl.textContent = `${usuario.nombre} ${usuario.apellido}`;
    
    if (usuario.rol === "ADMIN") {
      document.getElementById("adminLink")?.style.removeProperty("display");
    }
  }
};

// Filtrar solo productos disponibles
const productos: IProducto[] = productosData.filter((p: IProducto) => p.disponible);
const categorias: ICategoria[] = categoriasData;

// Event listener para cerrar sesión
document.getElementById("logoutButton")?.addEventListener("click", cerrarSesion);

// Cargar categorías en sidebar
const cargarCategorias = () => {
  const ul = document.querySelector<HTMLUListElement>("ul");

  // Botón "todos los productos"
  const li = document.createElement("li");
  li.innerHTML = `<a href="#" class="list-categorias">todos los productos</a>`;

  li.addEventListener("click", (e) => {
    e.preventDefault();
    cargarProductos(productosData.filter((p: IProducto) => p.disponible));
  });
  ul?.appendChild(li);

  // Categorías dinámicas
  categorias.forEach((categoria) => {
    const li = document.createElement("li");
    li.innerHTML = `<a href="#" class="list-categoria">${categoria.nombre}</a>`;
    li.addEventListener("click", (e) => {
      e.preventDefault();
      const filtrados = productosData.filter(
        (p: IProducto) => p.categoria.id === categoria.id && p.disponible
      );
      cargarProductos(filtrados);
    });
    ul?.appendChild(li);
  });
};

// Cargar y renderizar productos
const cargarProductos = (lista: IProducto[]) => {
  const contenedor = document.querySelector<HTMLDivElement>(
    "#contenedor-productos"
  );
  if (!contenedor) return;
  contenedor.innerHTML = "";

  lista.forEach((producto) => {
    const card = document.createElement("article");
    card.className = "producto-card";
    card.innerHTML = `
      <img class="comidas" src="${producto.imagen}" alt="${producto.nombre}">
      <h3>${producto.nombre}</h3>
      <p>${producto.descripcion}</p>
      <p>Precio: <strong>$${producto.precio.toLocaleString("es-AR")}</strong></p>
      <span class="${producto.disponible ? "disponible" : "no-disponible"}">
        ${producto.disponible ? "Disponible" : "No disponible"}
      </span>
      <button class="btn">+ Agregar</button>
    `;
    contenedor.appendChild(card);
    
    // Click en tarjeta → ir a detalle del producto
    card.addEventListener("click", (e) => {
      if ((e.target as HTMLElement).className !== "btn") {
        window.location.href = `/src/pages/store/productDetail/productDetail.html?id=${producto.id}`;
      }
    });

    // Click en botón → agregar al carrito
    card
      .querySelector<HTMLButtonElement>(".btn")
      ?.addEventListener("click", (e) => {
        e.stopPropagation();
        agregarAlCarrito(producto);
      });
  });
};

// Buscador de productos
document
  .querySelector<HTMLInputElement>("#buscador")
  ?.addEventListener("input", (e) => {
    const texto = (e.target as HTMLInputElement).value.toLowerCase();
    const filtrado = productosData.filter(
      (producto: IProducto) =>
        producto.nombre.toLowerCase().includes(texto) && producto.disponible
    );
    cargarProductos(filtrado);
  });

// Agregar producto al carrito
const agregarAlCarrito = (producto: IProducto) => {
  const carrito = JSON.parse(localStorage.getItem("carrito") || "[]");
  const existe = carrito.find(
    (item: IProducto & { cantidad: number }) => item.id === producto.id
  );

  if (existe) {
    existe.cantidad += 1;
  } else {
    carrito.push({ ...producto, cantidad: 1 });
  }

  localStorage.setItem("carrito", JSON.stringify(carrito));
  actualizarContador();
};

// Actualizar contador del carrito en header
const actualizarContador = () => {
  const carrito = JSON.parse(localStorage.getItem("carrito") || "[]");
  const total = carrito.reduce(
    (acc: number, item: { cantidad: number }) => acc + item.cantidad,
    0
  );
  const contador = document.getElementById("carritoContador");
  if (contador) contador.textContent = `${total}`;
};

// Mostrar carrito si NO es admin
const mostrarCarrito = () => {
  const userData = localStorage.getItem("userData");
  if (userData) {
    const usuario: IUser = JSON.parse(userData);
    const carritoLink = document.querySelector<HTMLElement>(".carrito-link");
    if (usuario.rol === "ADMIN" && carritoLink) {
      carritoLink.style.display = "none";
    }
  }
};
// Ocultar Mis Pedidos si es admin
const ocultarMisPedidos = () => {
  const userData = localStorage.getItem("userData");
  if (userData) {
    const usuario: IUser = JSON.parse(userData);
    if (usuario.rol === "ADMIN") {
      const misPedidosLink = document.querySelector<HTMLAnchorElement>('a[href="/src/pages/client/orders/orders.html"]');
      if (misPedidosLink) misPedidosLink.style.display = "none";
    }
  }
};

// Inicializar
actualizarHeader();
cargarCategorias();
cargarProductos(productos);
actualizarContador();
mostrarCarrito();
ocultarMisPedidos();