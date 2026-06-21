import type { IProducto } from "../../../../types/IProducto";
import type { IUser } from "../../../../types/IUser";
import { cerrarSesion, checkAuhtUser } from "../../../../utils/auth";
import { obtenerProductos } from "../../../../utils/dataService";
const productosData = await obtenerProductos();

checkAuhtUser("/src/pages/auth/login/login.html", "/src/pages/client/orders/orders.html", "ADMIN");

// Header dinamico
const actualizarHeader = () => {
  const userData = localStorage.getItem("userData");
  if (userData) {
    const usuario: IUser = JSON.parse(userData);
    const nombreEl = document.getElementById("nombreUsuario");
    if (nombreEl)
      nombreEl.textContent = `${usuario.nombre} ${usuario.apellido}`;
  }
};

// Renderizar tabla
const renderizarTabla = () => {
  const tbody = document.getElementById("tbody-productos");
  if (!tbody) return;

  tbody.innerHTML = "";

  productosData.forEach((producto: IProducto) => {
    const tr = document.createElement("tr");
    const estado = producto.disponible ? "Disponible" : "No disponible";
    
    tr.innerHTML = `
      <td>${producto.id}</td>
      <td><img src="${producto.imagen}" alt="${producto.nombre}" class="tabla-img"></td>
      <td>${producto.nombre}</td>
      <td>${producto.descripcion}</td>
      <td>$${producto.precio.toLocaleString("es-AR")}</td>
      <td>${producto.stock}</td>
      <td><span class="estado-${producto.disponible ? "disponible" : "no-disponible"}">${estado}</span></td>
    `;
    tbody.appendChild(tr);
  });
};

// Inicializar
document.getElementById("logoutButton")?.addEventListener("click", cerrarSesion);
actualizarHeader();
renderizarTabla();