export interface IProducto {
  id?: number;
  nombre?: string | null;
  cantidad?: number | null;
  precio?: number | null;
  descripcion?: string | null;
  categoria?: string | null;
  imagenContentType?: string | null;
  imagen?: string | null;
  valorDescuento?: number | null;
}

export class Producto implements IProducto {
  constructor(
    public id?: number,
    public nombre?: string | null,
    public cantidad?: number | null,
    public precio?: number | null,
    public descripcion?: string | null,
    public categoria?: string | null,
    public imagenContentType?: string | null,
    public imagen?: string | null,
    public valorDescuento?: number | null
  ) {}
}

export function getProductoIdentifier(producto: IProducto): number | undefined {
  return producto.id;
}
