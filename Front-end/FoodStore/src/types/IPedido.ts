import type { IUser } from "./IUser";
import type { IProducto } from "./IProducto";

export interface IDetalle {
  cantidad: number;
  subtotal: number;
  producto: IProducto;
}

export interface IPedido {
  id: number;
  fecha: string;
  estado: string;
  total: number;
  formaPago: string;
  detalles: IDetalle[];
  usuarioDto: IUser;
}