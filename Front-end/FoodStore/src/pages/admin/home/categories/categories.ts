import type { ICategoria } from "../../../../types/ICategoria";
import type { IUser } from "../../../../types/IUser";
import { cerrarSesion, checkAuhtUser } from "../../../../utils/auth";
import { obtenerCategorias } from "../../../../utils/dataService";
const categoriasData = await obtenerCategorias();
checkAuhtUser("/src/pages/auth/login/login.html", "/src/pages/store/home/home.html", "ADMIN");

const OVERRIDES_KEY = "categoriasOverrides";
let categoriaEditandoId: number | null = null;

// Cambios de nombre/descripción/disponibilidad guardados en localStorage (no se pisa el JSON base)
const obtenerOverrides = (): Record<number, Partial<ICategoria>> => {
  return JSON.parse(localStorage.getItem(OVERRIDES_KEY) || "{}");
};

const guardarOverrides = (overrides: Record<number, Partial<ICategoria>>) => {
  localStorage.setItem(OVERRIDES_KEY, JSON.stringify(overrides));
};

// Combina las categorías base con los cambios guardados en localStorage
const obtenerCategoriasMerged = (): ICategoria[] => {
  const overrides = obtenerOverrides();
  return categoriasData.map((categoria: ICategoria) => ({
    ...categoria,
    disponible: categoria.disponible ?? true,
    ...overrides[categoria.id],
  }));
};

// Actualizar nombre usuario en header
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
  const tbody = document.getElementById("tbody-categorias");
  if (!tbody) return;

  tbody.innerHTML = "";

  obtenerCategoriasMerged().forEach((categoria: ICategoria) => {
    const tr = document.createElement("tr");
    const estado = categoria.disponible ? "Disponible" : "No disponible";
    tr.innerHTML = `
      <td>${categoria.id}</td>
      <td>${categoria.nombre}</td>
      <td>${categoria.descripcion}</td>
      <td><span class="${categoria.disponible ? "disponible" : "no-disponible"}">${estado}</span></td>
      <td>
        <div class="acciones-tabla">
          <button class="btn-tabla-editar" data-id="${categoria.id}">Editar</button>
          <button class="${categoria.disponible ? "btn-tabla-eliminar" : "btn-tabla-activar"}" data-id="${categoria.id}">
            ${categoria.disponible ? "Eliminar" : "Activar"}
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

// Abre el modal de edición con los datos actuales de la categoría
const abrirModalEditar = (id: number) => {
  const categoria = obtenerCategoriasMerged().find((c) => c.id === id);
  if (!categoria) return;

  categoriaEditandoId = id;

  const modal = document.getElementById("modal-categoria") as HTMLDivElement;
  const nombreEl = document.getElementById("categoria-nombre") as HTMLInputElement;
  const descripcionEl = document.getElementById("categoria-descripcion") as HTMLTextAreaElement;

  nombreEl.value = categoria.nombre;
  descripcionEl.value = categoria.descripcion;

  modal.style.display = "flex";
};

const cerrarModal = () => {
  const modal = document.getElementById("modal-categoria") as HTMLDivElement;
  modal.style.display = "none";
  categoriaEditandoId = null;
};

// Marca/desmarca la categoría como no disponible (baja lógica)
const toggleDisponibilidad = (id: number) => {
  const categoria = obtenerCategoriasMerged().find((c) => c.id === id);
  if (!categoria) return;

  const overrides = obtenerOverrides();
  overrides[id] = { ...overrides[id], disponible: !categoria.disponible };
  guardarOverrides(overrides);
  renderizarTabla();
};

// Valida y guarda los cambios del formulario de edición
const guardarCategoria = () => {
  if (categoriaEditandoId === null) return;

  const nombreEl = document.getElementById("categoria-nombre") as HTMLInputElement;
  const descripcionEl = document.getElementById("categoria-descripcion") as HTMLTextAreaElement;

  const nombre = nombreEl.value.trim();
  if (!nombre) {
    alert("El nombre es obligatorio");
    return;
  }

  const overrides = obtenerOverrides();
  overrides[categoriaEditandoId] = {
    ...overrides[categoriaEditandoId],
    nombre,
    descripcion: descripcionEl.value.trim(),
  };
  guardarOverrides(overrides);

  cerrarModal();
  renderizarTabla();
};

// Event listeners
document.getElementById("logoutButton")?.addEventListener("click", cerrarSesion);
document.getElementById("cerrar-modal-categoria")?.addEventListener("click", cerrarModal);
document.getElementById("modal-categoria")?.addEventListener("click", (e) => {
  if (e.target === e.currentTarget) cerrarModal();
});
document.getElementById("form-categoria")?.addEventListener("submit", (e) => {
  e.preventDefault();
  guardarCategoria();
});

// Inicializar
actualizarHeader();
renderizarTabla();
