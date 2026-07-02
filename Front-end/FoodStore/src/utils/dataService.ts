import type { IProducto } from "../types/IProducto";
import type { ICategoria } from "../types/ICategoria";
import type { IPedido } from "../types/IPedido";
import type { IUser } from "../types/IUser";

export const obtenerUsuarios = async (): Promise<IUser[]> => {
  const res = await fetch("/data/usuarios.json");
  return res.json();
};

const PRODUCTOS_OVERRIDES_KEY = "productosOverrides";

// Cambios de producto (edición, baja lógica) guardados en localStorage por el admin
export const obtenerProductosOverrides = (): Record<number, Partial<IProducto>> => {
  return JSON.parse(localStorage.getItem(PRODUCTOS_OVERRIDES_KEY) || "{}");
};

export const guardarProductosOverrides = (overrides: Record<number, Partial<IProducto>>) => {
  localStorage.setItem(PRODUCTOS_OVERRIDES_KEY, JSON.stringify(overrides));
};

export const obtenerProductos = async (): Promise<IProducto[]> => {
  const res = await fetch("/data/productos.json");
  const productos: IProducto[] = await res.json();
  const overrides = obtenerProductosOverrides();
  return productos.map((producto) => ({ ...producto, ...overrides[producto.id] }));
};

export const obtenerCategorias = async (): Promise<ICategoria[]> => {
  const res = await fetch("/data/categorias.json");
  return res.json();
};

export const obtenerPedidos = async (): Promise<IPedido[]> => {
  const res = await fetch("/data/pedidos.json");
  return res.json();
};
