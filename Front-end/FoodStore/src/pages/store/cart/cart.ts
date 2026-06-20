
import type { IProducto } from "../../../types/IProducto";
import type { IUser } from "../../../types/IUser";
import { cerrarSesion } from "../../../utils/auth";

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

// Obtener carrito
const obtenerCarrito = () => {
  const productosAgregados = localStorage.getItem("carrito");
  return productosAgregados ? JSON.parse(productosAgregados) : [];
};

// Renderizar carrito
const productosDeCarrito = () => {
  const listCarrito = document.querySelector<HTMLDivElement>("#lista-carrito");
  if (!listCarrito) return;
  listCarrito.innerHTML = "";

  const carrito = obtenerCarrito();
  
  if (carrito.length === 0) {
    const carritoVacio = document.createElement("h3");
    const btnVolverCatalogo = document.createElement("button");
    
    carritoVacio.textContent = "No hay productos en el carrito";
    btnVolverCatalogo.textContent = "Volver al catalogo";
    btnVolverCatalogo.className = "btn";
    
    listCarrito?.append(carritoVacio, btnVolverCatalogo);
    
    btnVolverCatalogo.addEventListener("click", () => {
      window.location.href = "/src/pages/store/home/home.html";
    });
  } else {
    carrito.forEach((producto: IProducto & { cantidad: number }) => {
      const itemsCarrito = document.createElement("div");
      itemsCarrito.className = "card-producto--carrito";
      itemsCarrito.innerHTML = `
        <img src="${producto.imagen}" alt="${producto.nombre}">
        <div class="carrito-info">
          <h3>${producto.nombre}</h3>
          <p>Precio: $${producto.precio.toLocaleString("es-AR")}</p>
          <div class="carrito-cantidad">
            <button class="btn-menos">-</button>
            <span>Cantidad: ${producto.cantidad}</span>
            <button class="btn-mas">+</button>
          </div>
          <p class="carrito-subtotal">Subtotal: $${(producto.precio * producto.cantidad).toLocaleString("es-AR")}</p>
          <button class="btn-eliminar">Eliminar</button>
        </div>
      `;

      const btmMas = itemsCarrito.querySelector(".btn-mas");
      const btmMenos = itemsCarrito.querySelector(".btn-menos");
      const btnEliminar = itemsCarrito.querySelector(".btn-eliminar");

      btmMas?.addEventListener("click", () => aumentarCantidad(producto.id));
      btmMenos?.addEventListener("click", () => disminuirCantidad(producto.id));
      btnEliminar?.addEventListener("click", () => eliminarProducto(producto.id));

      listCarrito?.appendChild(itemsCarrito);
    });
  }
};

// Calcular total
const calcularTotal = () => {
  const carrito = obtenerCarrito();
  const totalCarrito = document.querySelector("#total-carrito");
  if (!totalCarrito) return;

  if (carrito.length === 0) {
    totalCarrito.innerHTML = "";
    return;
  }

  const total = carrito.reduce((acumulador: number, producto: IProducto & { cantidad: number }) => {
    return acumulador + producto.precio * producto.cantidad;
  }, 0);

  totalCarrito.innerHTML = `
    <h3>Resumen</h3>
    <h2>Total: $${total.toLocaleString("es-AR")}</h2>
    <button class="btn-finalizar" disabled>Finalizar compra</button>
    <button class="btn-vaciar">Vaciar carrito</button>
    <p class="aviso">El checkout no esta disponible en esta version.</p>
  `;

  const btnVaciar = totalCarrito.querySelector(".btn-vaciar");
  btnVaciar?.addEventListener("click", () => {
    if (confirm("Seguro que queres vaciar el carrito?")) {
      localStorage.removeItem("carrito");
      productosDeCarrito();
      calcularTotal();
      actualizarContadorCarrito();
    }
  });
};

// Aumentar cantidad
const aumentarCantidad = (id: number) => {
  const carrito = obtenerCarrito();
  const producto = carrito.find((item: IProducto & { cantidad: number }) => item.id === id);

  if (producto) {
    producto.cantidad++;
    localStorage.setItem("carrito", JSON.stringify(carrito));
    productosDeCarrito();
    calcularTotal();
    actualizarContadorCarrito();
  }
};

// Disminuir cantidad
const disminuirCantidad = (id: number) => {
  const carrito = obtenerCarrito();
  const producto = carrito.find((item: IProducto & { cantidad: number }) => item.id === id);

  if (producto) {
    producto.cantidad--;

    if (producto.cantidad === 0) {
      eliminarProducto(id);
      return;
    }

    localStorage.setItem("carrito", JSON.stringify(carrito));
    productosDeCarrito();
    calcularTotal();
    actualizarContadorCarrito();
  }
};

// Eliminar producto
const eliminarProducto = (id: number) => {
  const carrito = obtenerCarrito();
  const carritoActualizado = carrito.filter(
    (item: IProducto & { cantidad: number }) => item.id !== id,
  );

  localStorage.setItem("carrito", JSON.stringify(carritoActualizado));
  productosDeCarrito();
  calcularTotal();
  actualizarContadorCarrito();
};

// Actualizar contador
const actualizarContadorCarrito = () => {
  const carrito = obtenerCarrito();
  const total = carrito.reduce(
    (acc: number, item: IProducto & { cantidad: number }) => acc + item.cantidad,
    0,
  );

  const contador = document.getElementById("carritoContador");
  if (contador) contador.textContent = `${total}`;
};

document.getElementById("logoutButton")?.addEventListener("click", cerrarSesion);

// Inicializar
actualizarHeader();
productosDeCarrito();
calcularTotal();
actualizarContadorCarrito();