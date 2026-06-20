import type { IProducto } from "../../../types/IProducto";
import type { ICategoria } from "../../../types/ICategoria";
import type { IUser } from "../../../types/IUser";
import { cerrarSesion } from "../../../utils/auth";
import productosData from "../../../data/productos.json";
import categoriasData from "../../../data/categorias.json"


// Header dinamico
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

const productos: IProducto[] = productosData.filter(p => p.disponible);
const categorias: ICategoria[] = categoriasData;

document.getElementById("logoutButton")?.addEventListener("click", cerrarSesion);

// Renderizar categorias
const cargarCategorias = () => {
  const ul = document.querySelector<HTMLUListElement>("ul");

  const li = document.createElement("li");
  li.innerHTML = `<a href="#" class="list-categorias">todos los productos</a>`;

  li.addEventListener("click", (e) => {
    e.preventDefault();
    cargarProductos(productosData.filter(p => p.disponible));
  });
  ul?.appendChild(li);

  categorias.forEach((categoria) => {
    const li = document.createElement("li");
    li.innerHTML = `<a href="#" class="list-categoria">${categoria.nombre}</a>`;
    li.addEventListener("click", (e) => {
      e.preventDefault();
      const filtrados = productosData.filter(
        (p) => p.categoria.id === categoria.id && p.disponible
      );
      cargarProductos(filtrados);
    });
    ul?.appendChild(li);
  });
};

// Renderizar productos
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
    
 // Click en tarjeta → va a detalle
    card.addEventListener("click", (e) => {
      if ((e.target as HTMLElement).className !== "btn-agregar") {
        window.location.href = `/src/pages/store/productDetail/productDetail.html?id=${producto.id}`;
      }
    });

    // Click en botón → agrega al carrito
    card
      .querySelector<HTMLButtonElement>(".btn-agregar")
      ?.addEventListener("click", (e) => {
        e.stopPropagation();
        agregarAlCarrito(producto);
      });
  });
};

// Buscador
document
  .querySelector<HTMLInputElement>("#buscador")
  ?.addEventListener("input", (e) => {
    const texto = (e.target as HTMLInputElement).value.toLowerCase();
    const filtrado = productosData.filter(
      (producto) =>
        producto.nombre.toLowerCase().includes(texto) && producto.disponible
    );
    cargarProductos(filtrado);
  });

// Agregar al carrito
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
  alert(`${producto.nombre} agregado al carrito`);
};

// Actualizar contador
const actualizarContador = () => {
  const carrito = JSON.parse(localStorage.getItem("carrito") || "[]");
  const total = carrito.reduce(
    (acc: number, item: { cantidad: number }) => acc + item.cantidad,
    0
  );
  const contador = document.getElementById("carritoContador");
  if (contador) contador.textContent = `${total}`;
};

// Inicializar
actualizarHeader();
cargarCategorias();
cargarProductos(productos);
actualizarContador();