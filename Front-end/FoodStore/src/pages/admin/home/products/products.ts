import type { IProducto } from "../../../../types/IProducto";
import type { IUser } from "../../../../types/IUser";
import { cerrarSesion, checkAuhtUser } from "../../../../utils/auth";
import { obtenerProductos } from "../../../../utils/dataService";

const productosData = await obtenerProductos();

// Proteger ruta: redirige si no está autenticado o no es ADMIN
checkAuhtUser("/src/pages/auth/login/login.html", "/src/pages/store/home/home.html", "ADMIN");

// Actualiza nombre usuario en header desde localStorage
const actualizarHeader = () => {
  const userData = localStorage.getItem("userData");
  if (userData) {
    const usuario: IUser = JSON.parse(userData);
    const nombreEl = document.getElementById("nombreUsuario");
    if (nombreEl)
      nombreEl.textContent = `${usuario.nombre} ${usuario.apellido}`;
  }
};

// Llena tabla con productos (ID, imagen, nombre, descripción, precio, stock, disponibilidad)
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

// Event listeners e inicialización
document.getElementById("logoutButton")?.addEventListener("click", cerrarSesion);
actualizarHeader();
renderizarTabla();