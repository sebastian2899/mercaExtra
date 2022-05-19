export interface IItemFacturaCompra {
  id?: number | null;
  idFactura?: number | null;
  idProducto?: number | null;
  cantidad?: number | null;
  precio?: number | null;
  nombreProducto?: string | null;
}

export class ItemFacturaCompra implements IItemFacturaCompra {
  constructor(
    public id?: number | null,
    public idFactura?: number | null,
    public idProducto?: number | null,
    public cantidad?: null | null,
    public precio?: number | null,
    public nombreProducto?: string | null
  ) {}
}
