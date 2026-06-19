import type { ICategoria } from "./ICategoria";

export interface IProducto {
  id: number;
  nombre: string;
  precio: number;
  descripcion: string;
  stock: number;
  imagen: string;
  disponible: boolean;
  categoria: ICategoria;
}