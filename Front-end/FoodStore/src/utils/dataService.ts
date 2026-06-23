import type { IProducto } from "../types/IProducto";
import type { ICategoria } from "../types/ICategoria";
import type { IPedido } from "../types/IPedido";
import type { IUser } from "../types/IUser";

export const obtenerUsuarios = async (): Promise<IUser[]> => {
  const res = await fetch("/data/usuarios.json");
  return res.json();
};

export const obtenerProductos = async (): Promise<IProducto[]> => {
  const res = await fetch("/data/productos.json");
  return res.json();
};

export const obtenerCategorias = async (): Promise<ICategoria[]> => {
  const res = await fetch("/data/categorias.json");
  return res.json();
};

export const obtenerPedidos = async (): Promise<IPedido[]> => {
  const res = await fetch("/data/pedidos.json");
  return res.json();
};
