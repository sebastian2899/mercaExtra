export interface ICategoriaProducto {
  id?: number;
  nombreCategoria?: string | null;
  descripcion?: string | null;
}

export class CategoriaProducto implements ICategoriaProducto {
  constructor(public id?: number, public nombreCategoria?: string | null, public descripcion?: string | null) {}
}

export function getCategoriaProductoIdentifier(categoriaProducto: ICategoriaProducto): number | undefined {
  return categoriaProducto.id;
}
