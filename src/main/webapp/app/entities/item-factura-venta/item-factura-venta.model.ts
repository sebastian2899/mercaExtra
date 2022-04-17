export interface IItemFacturaVenta {
  id?: number;
  idFactura?: number | null;
  idProducto?: number | null;
  cantidad?: number | null;
  precio?: number | null;
  nombreProducto?: string | null;
  precioOriginal?: number | null;
}

export class ItemFacturaVenta implements IItemFacturaVenta {
  constructor(
    public id?: number,
    public idFactura?: number | null,
    public idProducto?: number | null,
    public cantidad?: number | null,
    public precio?: number | null,
    public nombreProducto?: string | null,
    public precioOriginal?: number | null
  ) {}
}

export function getItemFacturaVentaIdentifier(itemFacturaVenta: IItemFacturaVenta): number | undefined {
  return itemFacturaVenta.id;
}
