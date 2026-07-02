import type { IProducto } from "../../../types/IProducto";
import type { IUser } from "../../../types/IUser";
import { cerrarSesion } from "../../../utils/auth";

// Actualiza nombre usuario en header y muestra link admin si es ADMIN
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

// Redirige a ADMIN al panel de administración si intenta acceder al carrito
const mostrarCarrito = () => {
  const userData = localStorage.getItem("userData");
  if (userData) {
    const usuario: IUser = JSON.parse(userData);
    if (usuario.rol === "ADMIN") {
      window.location.href = "/src/pages/admin/home/adminHome/adminHome.html";
      return;
    }
  }
};

// Obtiene carrito desde localStorage
const obtenerCarrito = (): (IProducto & { cantidad: number })[] => {
  const productosAgregados = localStorage.getItem("carrito");
  return productosAgregados ? JSON.parse(productosAgregados) : [];
};

// Renderiza items del carrito: imagen, nombre, precio, cantidad, subtotal, botones +/-/eliminar
const productosDeCarrito = () => {
  const listCarrito = document.getElementById("items-carrito");
  if (!listCarrito) return;
  listCarrito.innerHTML = "";

  const carrito = obtenerCarrito();
  
  if (carrito.length === 0) {
    listCarrito.innerHTML = "<p>No hay productos en el carrito</p>";
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
          <p>Subtotal: $${(producto.precio * producto.cantidad).toLocaleString("es-AR")}</p>
          <button class="btn-eliminar">Eliminar</button>
        </div>
      `;

      const btmMas = itemsCarrito.querySelector(".btn-mas");
      const btmMenos = itemsCarrito.querySelector(".btn-menos");
      const btnEliminar = itemsCarrito.querySelector(".btn-eliminar");

      btmMas?.addEventListener("click", () => aumentarCantidad(producto.id));
      btmMenos?.addEventListener("click", () => disminuirCantidad(producto.id));
      btnEliminar?.addEventListener("click", () => eliminarProducto(producto.id));

      listCarrito.appendChild(itemsCarrito);
    });
  }
};

// Calcula subtotal (suma de todos los items) + envío fijo $1.000, actualiza DOM
const calcularTotal = () => {
  const carrito = obtenerCarrito();
  const carritoContenidoEl = document.getElementById("carrito-contenido");
  const carritoVacioEl = document.getElementById("carrito-vacio");
  const totalEl = document.getElementById("total");
  const subtotalEl = document.getElementById("subtotal");
  const envioEl = document.getElementById("envio");

  // Si carrito vacío: oculta resumen, muestra mensaje
  if (carrito.length === 0) {
    if (carritoContenidoEl) carritoContenidoEl.style.display = "none";
    if (carritoVacioEl) carritoVacioEl.style.display = "block";
    return;
  }

  // Si hay items: muestra resumen, oculta mensaje
  if (carritoContenidoEl) carritoContenidoEl.style.display = "";
  if (carritoVacioEl) carritoVacioEl.style.display = "none";

  if (!totalEl) return;

  const ENVIO_FIJO = 1000;

  // Suma precio * cantidad de todos los items
  const subtotal = carrito.reduce((acumulador: number, producto: IProducto & { cantidad: number }) => {
    return acumulador + producto.precio * producto.cantidad;
  }, 0);

  const totalConEnvio = subtotal + ENVIO_FIJO;

  // Actualiza etiquetas en el DOM
  if (subtotalEl) subtotalEl.textContent = `$${subtotal.toLocaleString("es-AR")}`;
  if (envioEl) envioEl.textContent = `$${ENVIO_FIJO.toLocaleString("es-AR")}`;
  if (totalEl) totalEl.textContent = `$${totalConEnvio.toLocaleString("es-AR")}`;
};

// Aumenta cantidad de producto en carrito y actualiza UI
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

// Disminuye cantidad de producto (si llega a 0 lo elimina) y actualiza UI
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

// Elimina producto del carrito y actualiza UI
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

// Actualiza contador de items totales en header
const actualizarContadorCarrito = () => {
  const carrito = obtenerCarrito();
  const total = carrito.reduce(
    (acc: number, item: IProducto & { cantidad: number }) => acc + item.cantidad,
    0,
  );

  const contador = document.getElementById("carritoContador");
  if (contador) contador.textContent = `${total}`;
};

// Abre modal checkout: valida sesión, carrito no vacío, calcula total
const abrirCheckout = () => {
  const userData = localStorage.getItem("userData");
  if (!userData) { alert("Debes estar logueado"); return; }
  
  const usuario: IUser = JSON.parse(userData);
  const carrito = obtenerCarrito();
  
  if (carrito.length === 0) { alert("El carrito está vacío"); return; }
  
  const modal = document.getElementById("modal-checkout") as HTMLDivElement;
  const telefonoEl = document.getElementById("checkout-telefono") as HTMLInputElement;
  const direccionEl = document.getElementById("checkout-direccion") as HTMLTextAreaElement;
  const totalEl = document.getElementById("checkout-total") as HTMLSpanElement;
  

  if (telefonoEl) telefonoEl.value = "";
  if (direccionEl) direccionEl.value = "";
  
  const totalConEnvio = carrito.reduce((sum:number, p: IProducto & { cantidad: number }) => sum + p.precio * p.cantidad, 0) + 1000;
  totalEl.textContent = totalConEnvio.toLocaleString("es-AR");
  
  modal.style.display = "flex";
};

// Cierra modal de checkout
const cerrarCheckout = () => {
  const modal = document.getElementById("modal-checkout") as HTMLDivElement;
  modal.style.display = "none";
};

// Valida formulario y guarda pedido en localStorage por usuario, redirige a mis pedidos
const guardarPedido = () => {
  const userData = localStorage.getItem("userData");
  if (!userData) return;
  
  const usuario: IUser = JSON.parse(userData);
  const carrito = obtenerCarrito();
  
  const telefonoEl = document.getElementById("checkout-telefono") as HTMLInputElement;
  const direccionEl = document.getElementById("checkout-direccion") as HTMLTextAreaElement;
  const metodoEl = document.getElementById("checkout-metodo") as HTMLSelectElement;

  const totalConEnvio = carrito.reduce((sum: number, p: IProducto & { cantidad: number }) => sum + p.precio * p.cantidad, 0) + 1000;
  
  // Obtiene pedidos previos del usuario o crea array vacío
  const nuevosPedidos = JSON.parse(localStorage.getItem(`pedidos_${usuario.id}`) || "[]");
  
  // Construye objeto pedido con detalles, total, método pago
  const pedido = {
    id: Date.now(),
    fecha: new Date().toLocaleDateString("es-AR"),
    estado: "PENDIENTE",
    total: totalConEnvio,
    formaPago: metodoEl.value,
    detalles: carrito.map((p: IProducto & { cantidad: number }) => ({
      cantidad: p.cantidad,
      subtotal: p.precio * p.cantidad,
      producto: p
    })),
    usuarioDto: usuario
  };
  
  // Guarda pedido en localStorage y limpia carrito
  nuevosPedidos.push(pedido);
  localStorage.setItem(`pedidos_${usuario.id}`, JSON.stringify(nuevosPedidos));
  localStorage.removeItem("carrito");
  localStorage.removeItem(`carrito_${usuario.id}`);

  alert("¡Pedido realizado con éxito!");
  window.location.href = "/src/pages/client/orders/orders.html";
};

// Event listeners: logout, botones carrito, modal, form checkout
document.getElementById("logoutButton")?.addEventListener("click", cerrarSesion);
document.getElementById("btn-proceder")?.addEventListener("click", abrirCheckout);
document.querySelector(".close")?.addEventListener("click", cerrarCheckout);
document.getElementById("form-checkout")?.addEventListener("submit", (e) => {
  e.preventDefault();
  guardarPedido();
});
document.getElementById("btn-vaciar")?.addEventListener("click", () => {
  if (confirm("Seguro que queres vaciar el carrito?")) {
    localStorage.removeItem("carrito");
    productosDeCarrito();
    calcularTotal();
    actualizarContadorCarrito();
  }
});

// Inicializar página: redirige ADMIN, carga header, renderiza carrito, calcula total
mostrarCarrito()
actualizarHeader();
productosDeCarrito();
calcularTotal();
actualizarContadorCarrito();