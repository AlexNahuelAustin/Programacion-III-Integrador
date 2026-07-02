import type { IProducto } from "../../../../types/IProducto";
import type { IUser } from "../../../../types/IUser";
import { cerrarSesion, checkAuhtUser } from "../../../../utils/auth";
import {
  obtenerProductos,
  obtenerProductosOverrides,
  guardarProductosOverrides,
} from "../../../../utils/dataService";

// obtenerProductos() ya viene con los cambios de localStorage aplicados (no se pisa el JSON base)
const productosData = await obtenerProductos();

// Proteger ruta: redirige si no está autenticado o no es ADMIN
checkAuhtUser("/src/pages/auth/login/login.html", "/src/pages/store/home/home.html", "ADMIN");

let productoEditandoId: number | null = null;

// Vuelve a aplicar los overrides más recientes (por si cambiaron después de la carga inicial)
const obtenerProductosMerged = (): IProducto[] => {
  const overrides = obtenerProductosOverrides();
  return productosData.map((producto: IProducto) => ({
    ...producto,
    ...overrides[producto.id],
  }));
};

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

  obtenerProductosMerged().forEach((producto: IProducto) => {
    const tr = document.createElement("tr");
    const estado = producto.disponible ? "Disponible" : "No disponible";

    tr.innerHTML = `
      <td>${producto.id}</td>
      <td><img src="${producto.imagen}" alt="${producto.nombre}" class="tabla-img"></td>
      <td>${producto.nombre}</td>
      <td>${producto.descripcion}</td>
      <td>$${producto.precio.toLocaleString("es-AR")}</td>
      <td>${producto.stock}</td>
      <td><span class="${producto.disponible ? "disponible" : "no-disponible"}">${estado}</span></td>
      <td>
        <div class="acciones-tabla">
          <button class="btn-tabla-editar" data-id="${producto.id}">Editar</button>
          <button class="${producto.disponible ? "btn-tabla-eliminar" : "btn-tabla-activar"}" data-id="${producto.id}">
            ${producto.disponible ? "Eliminar" : "Activar"}
          </button>
        </div>
      </td>
    `;
    tbody.appendChild(tr);
  });

  tbody.querySelectorAll<HTMLButtonElement>(".btn-tabla-editar").forEach((btn) => {
    btn.addEventListener("click", () => abrirModalEditar(Number(btn.dataset.id)));
  });
  tbody.querySelectorAll<HTMLButtonElement>(".btn-tabla-eliminar, .btn-tabla-activar").forEach((btn) => {
    btn.addEventListener("click", () => toggleDisponibilidad(Number(btn.dataset.id)));
  });
};

// Abre el modal de edición con los datos actuales del producto
const abrirModalEditar = (id: number) => {
  const producto = obtenerProductosMerged().find((p) => p.id === id);
  if (!producto) return;

  productoEditandoId = id;

  const modal = document.getElementById("modal-producto") as HTMLDivElement;
  const nombreEl = document.getElementById("producto-nombre") as HTMLInputElement;
  const descripcionEl = document.getElementById("producto-descripcion") as HTMLTextAreaElement;
  const precioEl = document.getElementById("producto-precio") as HTMLInputElement;
  const stockEl = document.getElementById("producto-stock") as HTMLInputElement;
  const disponibleEl = document.getElementById("producto-disponible") as HTMLSelectElement;

  nombreEl.value = producto.nombre;
  descripcionEl.value = producto.descripcion;
  precioEl.value = `${producto.precio}`;
  stockEl.value = `${producto.stock}`;
  disponibleEl.value = `${producto.disponible}`;

  modal.style.display = "flex";
};

const cerrarModal = () => {
  const modal = document.getElementById("modal-producto") as HTMLDivElement;
  modal.style.display = "none";
  productoEditandoId = null;
};

// Marca/desmarca el producto como no disponible (baja lógica)
const toggleDisponibilidad = (id: number) => {
  const producto = obtenerProductosMerged().find((p) => p.id === id);
  if (!producto) return;

  const overrides = obtenerProductosOverrides();
  overrides[id] = { ...overrides[id], disponible: !producto.disponible };
  guardarProductosOverrides(overrides);
  renderizarTabla();
};

// Valida y guarda los cambios del formulario de edición
const guardarProducto = () => {
  if (productoEditandoId === null) return;

  const nombreEl = document.getElementById("producto-nombre") as HTMLInputElement;
  const descripcionEl = document.getElementById("producto-descripcion") as HTMLTextAreaElement;
  const precioEl = document.getElementById("producto-precio") as HTMLInputElement;
  const stockEl = document.getElementById("producto-stock") as HTMLInputElement;
  const disponibleEl = document.getElementById("producto-disponible") as HTMLSelectElement;

  const nombre = nombreEl.value.trim();
  const precio = Number(precioEl.value);
  const stock = Number(stockEl.value);

  if (!nombre) {
    alert("El nombre es obligatorio");
    return;
  }
  if (!(precio > 0)) {
    alert("El precio debe ser mayor a 0");
    return;
  }
  if (!(stock >= 0)) {
    alert("El stock no puede ser negativo");
    return;
  }

  const overrides = obtenerProductosOverrides();
  overrides[productoEditandoId] = {
    ...overrides[productoEditandoId],
    nombre,
    descripcion: descripcionEl.value.trim(),
    precio,
    stock,
    disponible: disponibleEl.value === "true",
  };
  guardarProductosOverrides(overrides);

  cerrarModal();
  renderizarTabla();
};

// Event listeners e inicialización
document.getElementById("logoutButton")?.addEventListener("click", cerrarSesion);
document.getElementById("cerrar-modal-producto")?.addEventListener("click", cerrarModal);
document.getElementById("modal-producto")?.addEventListener("click", (e) => {
  if (e.target === e.currentTarget) cerrarModal();
});
document.getElementById("form-producto")?.addEventListener("submit", (e) => {
  e.preventDefault();
  guardarProducto();
});

actualizarHeader();
renderizarTabla();
